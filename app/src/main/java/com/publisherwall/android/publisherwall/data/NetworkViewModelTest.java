package com.publisherwall.android.publisherwall.data;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import android.support.annotation.Nullable;

public class NetworkViewModelTest extends ViewModel {


    private static final int INITIAL_LOAD_KEY = 0;
    private static final int PAGE_SIZE = 20;
    private static final int PREFETCH_DISTANCE = 5;

    public LiveData<PagedList<NetworkEntry>> getListLiveData() {
        return listLiveData;
    }

    private LiveData<PagedList<NetworkEntry>> listLiveData;

    private String networkName;

    private LiveData<PagedList<NetworkEntry>> createFilteredUsers(NetworkDao networkDao,String networkName) {
        // TODO: handle if `null` and load all data instead

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(PREFETCH_DISTANCE)
                        .setPageSize(PAGE_SIZE)
                        .build();
        if(networkName!=null && networkName.length()>0) {
            return new LivePagedListBuilder<>(networkDao.getNetworksByName(networkName),
                    pagedListConfig)
                    .setInitialLoadKey(0)
                    .build();
        }else {
            return new LivePagedListBuilder<>(networkDao.allNetworksEntry(),
                    pagedListConfig)
                    .setInitialLoadKey(0)
                    .build();
        }

        /*return new LivePagedListBuilder<>(networkDao.getNetworksByName(networkName),
                new PagedList.Config.Builder() //
                        .setPageSize(20) //
                        .setPrefetchDistance(20) //
                        .setEnablePlaceholders(true) //
                        .build())
                .setInitialLoadKey(0)
                .build();*/
    }

    public NetworkViewModelTest(AppDatabase appDatabase,@Nullable String networkName) { // null or restored, from ViewModelProviders.of(Factory)
        listLiveData = createFilteredUsers(appDatabase.networkDao(),networkName);
    }

    public void replaceSubscription(AppDatabase appDatabase,LifecycleOwner lifecycleOwner, String networkName) {
        this.networkName = networkName;
        listLiveData.removeObservers(lifecycleOwner);
        listLiveData = createFilteredUsers(appDatabase.networkDao(),networkName);
    }

    /*private LiveData<NetworkEntry> listLiveData;

    public NetworkViewModelTest(AppDatabase appDatabase, String networkId){
        listLiveData = appDatabase.networkDao().getNetworksByNetworkId(networkId);
    }

    public LiveData<NetworkEntry> getNetworkLiveData() {
        return listLiveData;
    }*/
}
