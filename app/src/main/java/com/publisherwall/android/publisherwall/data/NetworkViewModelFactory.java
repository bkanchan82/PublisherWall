package com.publisherwall.android.publisherwall.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class NetworkViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppDatabase mDb;
    private String mNetworkName;

    public NetworkViewModelFactory(AppDatabase db, String networkName){
        mDb = db;
        mNetworkName = networkName;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new NetworkViewModelTest(mDb,mNetworkName);
    }

}