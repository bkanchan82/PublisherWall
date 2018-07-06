package com.publisherwall.android.publisherwall.app;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class PublisherWallAppController extends Application {


    private static final String TAG = PublisherWallAppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static PublisherWallAppController mInstance;

    public static synchronized PublisherWallAppController getInstance(){
        return mInstance;
    }

    private RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

}
