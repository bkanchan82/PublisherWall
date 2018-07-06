package com.publisherwall.android.publisherwall.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class SearchNetworkViewModel extends ViewModel {

    private LiveData<NetworkEntry> listLiveData;

    public SearchNetworkViewModel(AppDatabase appDatabase, String networkId){
        listLiveData = appDatabase.networkDao().getNetworksByNetworkId(networkId);
    }

    public LiveData<NetworkEntry> getNetworkLiveData() {
        return listLiveData;
    }
}
