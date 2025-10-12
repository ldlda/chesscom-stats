package com.ldlda.chesscom_stats.ui.clubs;

import static androidx.lifecycle.ViewModelKt.getViewModelScope;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ldlda.chesscom_stats.api.data.club.Club;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache;
import com.ldlda.chesscom_stats.api.repository.JavaChessRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;

class ClubViewModel extends AndroidViewModel {
    final static String TAG = "ClubViewModel";
    private final JavaChessRepository repo = ChessRepoAdapterJava.getAdapterJava(
            new ChessRepositoryTimedCache(),
            getViewModelScope(this)
    ); // just call kotlin methods from java who cares

    private final @NonNull MutableLiveData<List<HttpUrl>> clubUrls = new MutableLiveData<>(Collections.emptyList());
    private final @NonNull MutableLiveData<List<Club>> clubList = new MutableLiveData<>(Collections.emptyList());
    private final @NonNull MutableLiveData<Boolean> loadingUrls = new MutableLiveData<>(false);
    private final @NonNull MutableLiveData<Boolean> loadingList = new MutableLiveData<>(false);

    public ClubViewModel(@NonNull Application application) {
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

    public final void fromCountry(@NonNull String iso) {
        loadingUrls.postValue(true);
        repo.getCountryClubsAsync(iso)
                .thenApply(
                        httpUrls -> {
                            clubUrls.setValue(httpUrls);
                            clubUrls.setValue(Collections.emptyList());
                            loadMoar(20);
                            return httpUrls;
                        })
                .exceptionally(throwable -> {
                    Log.e(TAG, "fromCountry: fuhhed up", throwable);
                    return null;
                }).handle((httpUrls, throwable) ->
                {
                    loadingUrls.setValue(false);
                    return null;
                });
    }

    public final void loadMoar(int count) {
        if (!clubUrls.isInitialized() || !clubList.isInitialized()) return;
        Objects.requireNonNull(clubUrls.getValue());
        Objects.requireNonNull(clubList.getValue());
        clubUrls.getValue().stream().skip(clubList.getValue().size()).limit(count).parallel().forEach(
                httpUrl -> {
                    loadingList.setValue(true);
                    repo.getClubAsync(httpUrl).thenApply(club -> {
                                List<Club> clublist = clubList.getValue() == null ? new ArrayList<>() : clubList.getValue();
                                clublist.add(club);
                                clubList.setValue(clublist);
                                return club;
                            })
                            .exceptionally(
                                    throwable -> {
                                        Log.e(TAG, "loadMoar: fuhhed up cuh", throwable);
                                        return null;
                                    }
                            ).handle(
                                    (club, throwable) -> {
                                        loadingList.setValue(false);
                                        return null;
                                    }
                            );
                }
        );
    }
}
