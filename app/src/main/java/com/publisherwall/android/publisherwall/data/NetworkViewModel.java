package com.publisherwall.android.publisherwall.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NetworkViewModel extends AndroidViewModel {

    private LiveData<List<NetworkEntry>> listLiveData;

    public NetworkViewModel(@NonNull Application application) {
        super(application);
        listLiveData = AppDatabase.getAppDatabaseInstance(application).networkDao().loadNetworkEntry();
    }

    public LiveData<List<NetworkEntry>> getListLiveData() {
        return listLiveData;
    }
}
