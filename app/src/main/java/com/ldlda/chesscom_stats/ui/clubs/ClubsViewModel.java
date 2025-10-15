package com.ldlda.chesscom_stats.ui.clubs;

import static androidx.lifecycle.ViewModelKt.getViewModelScope;
import static java.util.Objects.requireNonNull;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ldlda.chesscom_stats.api.data.PubApiError;
import com.ldlda.chesscom_stats.api.data.club.Club;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache;
import com.ldlda.chesscom_stats.api.repository.JavaChessRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.HttpUrl;
import retrofit2.HttpException;

public class ClubsViewModel extends AndroidViewModel {
    final static String TAG = "ClubViewModel";

    // Keep ViewModelScope for lifecycle-aware cancellation
    private final JavaChessRepository repo = ChessRepoAdapterJava.getAdapterJava(
            new ChessRepositoryTimedCache(),
            getViewModelScope(this)
    );
    private final @NonNull MutableLiveData<List<HttpUrl>> clubUrls = new MutableLiveData<>(Collections.emptyList());
    private final @NonNull MutableLiveData<List<Club>> clubList = new MutableLiveData<>(Collections.emptyList());
    private final @NonNull MutableLiveData<Boolean> loadingUrls = new MutableLiveData<>(false);
    private final @NonNull MutableLiveData<Boolean> loadingList = new MutableLiveData<>(false);
    private final @NonNull MutableLiveData<String> error = new MutableLiveData<>();

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

    public final void fromCountry(@NonNull String iso) {
        loadingUrls.postValue(true);
        repo.getCountryClubsAsync(iso)
                .thenApply(httpUrls -> {
                    clubUrls.setValue(httpUrls);
                    clubList.setValue(Collections.emptyList()); // Clear old clubs
                    loadMoar(20);
                    return httpUrls;
                })
                .exceptionally(throwable -> {
                    Log.e(TAG, "fromCountry: fuhhed up", throwable);

                    String er = null;

                    if ((throwable.getCause() instanceof HttpException ex)) {
                        var pae = PubApiError.message(ex);
                        if (pae != null)
                            er = pae.getMessage();
                    }
                    if (er == null) er = throwable.getMessage();

                    error.setValue("Failed to load clubs: " + er);
                    return null;
                })
                .whenComplete((httpUrls, throwable) -> loadingUrls.setValue(false));
    }

    public final void loadMoar(int count) {
        if (!clubUrls.isInitialized() || !clubList.isInitialized()) return;
        requireNonNull(clubUrls.getValue());
        requireNonNull(clubList.getValue());

        List<HttpUrl> urls = clubUrls.getValue();
        List<Club> clubs = clubList.getValue();

        if (urls.isEmpty() || clubs.size() >= urls.size()) return;

        // Take next batch of URLs to load
        List<HttpUrl> batch = urls.stream()
                .skip(clubs.size())
                .limit(count)
                .toList();

        if (batch.isEmpty()) return;

        loadingList.setValue(true);

        // Track how many completed
        AtomicInteger completed = new AtomicInteger(0);
        int total = batch.size();

        // Fire all requests, update list as each completes
        batch.forEach(url -> repo.getClubAsync(url)
                .thenAccept(club -> {
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
                    return null;
                })
                .whenComplete((v, ex) -> {
                    // After LAST one completes, clear loading flag
                    if (completed.incrementAndGet() == total) {
                        loadingList.setValue(false);
                    }
                }));
    }
}