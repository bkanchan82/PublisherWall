package com.publisherwall.android.publisherwall.utilities;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    public static final Object LOCK = new Object();
    private static AppExecutor sExecutor;

    private final Executor diskIO;
    private final Executor mainThreadExecutor;
    private final Executor networkExecutor;

    private AppExecutor(Executor diskIO, Executor mainThreadExecutor, Executor networkExecutor){
        this.diskIO = diskIO;
        this.mainThreadExecutor = mainThreadExecutor;
        this.networkExecutor = networkExecutor;
    }

    public static AppExecutor getAppExecutorInstance(){
        if(sExecutor == null){
            synchronized (LOCK){
                sExecutor = new AppExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sExecutor;
    }

    public Executor getDiskIO(){
        return diskIO;
    }

    public Executor getNetworkExecutor(){
        return networkExecutor;
    }

    public Executor getMainThreadExecutor(){
        return mainThreadExecutor;
    }

    public static class MainThreadExecutor implements Executor{

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }


}
