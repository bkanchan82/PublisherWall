package com.publisherwall.android.publisherwall.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private LiveData<List<EventEntry>> listLiveData;

    public EventViewModel(@NonNull Application application) {
        super(application);
        listLiveData = AppDatabase.getAppDatabaseInstance(application).eventDao().loadEventEntry();
    }

    public LiveData<List<EventEntry>> getListLiveData() {
        return listLiveData;
    }
}
