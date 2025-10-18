package com.ldlda.chesscom_stats.ui.clubs;

import static androidx.lifecycle.ViewModelKt.getViewModelScope;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ldlda.chesscom_stats.api.data.club.Club;
import com.ldlda.chesscom_stats.api.repository.JavaChessRepository;
import com.ldlda.chesscom_stats.di.RepoProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.HttpUrl;

public class ClubsViewModel extends AndroidViewModel {
    final static String TAG = "ClubViewModel";

    // Keep ViewModelScope for lifecycle-aware cancellation
    private final JavaChessRepository repo = RepoProvider.defaultRepository()
            .buildJavaAdapter(getViewModelScope(this));
    private final @NonNull MutableLiveData<List<HttpUrl>> clubUrls = new MutableLiveData<>(Collections.emptyList());
    private final @NonNull MutableLiveData<List<Club>> clubList = new MutableLiveData<>(Collections.emptyList());
    private final @NonNull MutableLiveData<Boolean> loadingUrls = new MutableLiveData<>(false);
    private final @NonNull MutableLiveData<Boolean> loadingList = new MutableLiveData<>(false);
    private final @NonNull MutableLiveData<String> error = new MutableLiveData<>();
    private final @NonNull MutableLiveData<Integer> errorClubs = new MutableLiveData<>(0);

    public ClubsViewModel(@NonNull Application application) {
        super(application);
    }

    public @NonNull LiveData<List<HttpUrl>> getClubUrls() {
        return clubUrls;
    }

    public @NonNull LiveData<List<Club>> getClubList() {
        return clubList;
    }

    public LiveData<Boolean> isLoadingList() {
        return loadingList;
    }

    public LiveData<Boolean> isLoadingUrls() {
        return loadingUrls;
    }

    @NonNull
    public LiveData<String> getErrorMessage() {
        return error;
    }

    @NonNull
    public LiveData<Integer> getErrorClubs() {
        return errorClubs;
    }

    public final void fromCountry(@NonNull String iso) {
        loadingUrls.setValue(true); // this line is the problem
        repo.getCountryClubsAsync(iso)
                .thenApply(httpUrls -> {
                    clubUrls.setValue(httpUrls);
                    clubList.setValue(Collections.emptyList()); // Clear old clubs
                    loadMoar(20);
                    return httpUrls;
                })
                .exceptionally(throwable -> {
                    Log.e(TAG, "fromCountry: fuhhed up", throwable);

                    String er;
                    if (throwable instanceof CompletionException c && c.getCause() != null)
                        er = c.getCause().getMessage();
                    else
                        er = throwable.getMessage();

                    clubUrls.setValue(Collections.emptyList());
                    clubList.setValue(Collections.emptyList());
                    error.setValue("Failed to load clubs: " + er);
                    return null;
                })
                .whenComplete((httpUrls, throwable) -> {
                    loadingUrls.setValue(false);
                    errorClubs.setValue(0);
                });
    }

    public final void loadMoar(int count) {
        if (Boolean.TRUE.equals(loadingList.getValue())) return;
        List<HttpUrl> urls = clubUrls.getValue();
        List<Club> clubs = clubList.getValue();

        if (urls == null || clubs == null) return;

        if (urls.isEmpty() || clubs.size() >= urls.size()) return;

        // Take next batch of URLs to load
        List<HttpUrl> batch = urls.stream()
                .skip(clubs.size())
                .limit(count)
                .toList();

        Log.d(TAG, "loadMoar: requesting " + count + ", got batch size: " + batch.size() + ", current clubs: " + clubs.size() + ", total urls: " + urls.size());

        if (batch.isEmpty()) return;

        loadingList.setValue(true);

        // Track how many completed
        AtomicInteger completed = new AtomicInteger(0);
        int total = batch.size();

        // Fire all requests, update list as each completes
        CompletableFuture<?>[] c = batch.stream().map(url -> {
            Log.d(TAG, "loadMoar: fetching " + url);
            return repo.getClubAsync(url)
                    .thenAccept(club -> {
//                        Log.d(TAG, "loadMoar: received " + club.getName() + " for " + url);
                        // Add club immediately when it arrives
                        List<Club> current = clubList.getValue();
                        if (current != null) {
                            List<Club> updated = new ArrayList<>(current);
                            updated.add(club);
                            clubList.setValue(updated);
                        }
                    })
                    .exceptionally(throwable -> {
                        Log.e(TAG, "loadMoar: fuhhed up cuh", throwable);
                        if (errorClubs.getValue() != null)
                            errorClubs.setValue(errorClubs.getValue() + 1);
                        return null;
                    })
                    .whenComplete((v, ex) -> {
                        // After LAST one completes, clear loading flag
                        if (completed.incrementAndGet() == total) {
                            Log.d(TAG, "loadMoar: falsing");
                            loadingList.setValue(false);
                        }
                    });
        }).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(c).handle((unused, throwable) -> {
            Log.d(TAG, "loadMoar: falsingAGAIN");
            loadingList.setValue(false);
            return null;
        });
    }

}