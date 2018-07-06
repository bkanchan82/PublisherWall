package com.publisherwall.android.publisherwall.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import java.util.List;

public class NetworkPagedViewModel extends AndroidViewModel {


    private static final int INITIAL_LOAD_KEY = 0;
    private static final int PAGE_SIZE = 20;
    private static final int PREFETCH_DISTANCE = 5;

    private LiveData<PagedList<NetworkEntry>> listLiveData;

    public NetworkPagedViewModel(@NonNull Application application) {
        super(application);
        NetworkDao networkDao = AppDatabase.getAppDatabaseInstance(application).networkDao();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(PREFETCH_DISTANCE)
                        .setPageSize(PAGE_SIZE)
                        .build();

        listLiveData = (new LivePagedListBuilder(networkDao.allNetworksEntry(), pagedListConfig))
                .build();

    }

    public LiveData<PagedList<NetworkEntry>> getListLiveData() {
        return listLiveData;
    }
}
