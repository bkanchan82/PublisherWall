package com.publisherwall.android.publisherwall.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class SearchNetworkViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppDatabase mDb;
    private String mNetworkSno;

    public SearchNetworkViewModelFactory(AppDatabase db, String networkSno){
        mDb = db;
        mNetworkSno = networkSno;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SearchNetworkViewModel(mDb,mNetworkSno);
    }

}
