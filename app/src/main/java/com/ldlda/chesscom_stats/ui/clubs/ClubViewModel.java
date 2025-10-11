package com.ldlda.chesscom_stats.ui.clubs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ldlda.chesscom_stats.java_api.ClubData;

import java.util.ArrayList;
import java.util.List;

class ClubViewModel extends AndroidViewModel {

    private final MutableLiveData<List<String>> clubUrls = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<ClubData>> clubList = new MutableLiveData<>(new ArrayList<>());

    public ClubViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getClubUrls() {
        return clubUrls;
    }

    public LiveData<List<ClubData>> getClubList() {
        return clubList;
    }

    public final void fromCountry(String iso) {
        // TODO: cuh
    }

    public final void loadMoar(int count) {
        // TODO: cuh
    }
}
