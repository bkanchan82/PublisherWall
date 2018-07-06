package com.publisherwall.android.publisherwall.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.app.PreferenceUtils;
import com.publisherwall.android.publisherwall.data.AppDatabase;
import com.publisherwall.android.publisherwall.data.EventEntry;
import com.publisherwall.android.publisherwall.data.NetworkEntry;
import com.publisherwall.android.publisherwall.utilities.AppExecutor;

import java.util.List;

public class PublisherWallSyncUtils {

    private static boolean sInitialized;
    private static boolean sEventInitialized;


    private static final int SYNC_DURATION_IN_MILIS = 3600000;
//    private static final int SYNC_DURATION_IN_MILIS = 60000;

    synchronized public static void initialize(@NonNull final Context context) {

        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if(!PreferenceUtils.isFcmTokenShared(context)){
            shareFcmToken(context);
        }

        if (sInitialized) {
            Log.v(TAG,"Is needs to sync is : "+isNeedsToSync(context));
            if (isNeedsToSync(context)) {
                startImmediateSync(context);
                return;
            }
        }

        sInitialized = true;

        AppExecutor.getAppExecutorInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                /* URI for every row of weather data in our weather table*/
                List<NetworkEntry> networkEntries = AppDatabase.getAppDatabaseInstance(context.getApplicationContext()).networkDao().checkNetworkEntry();
                if(networkEntries==null || networkEntries.size()==0){
                    startImmediateSync(context);
                }else{
                    if (isNeedsToSync(context)) {
                        startImmediateSync(context);
                        return;
                    }
                }
            }
        });

    }



    synchronized public static void initializeEvent(@NonNull final Context context) {

        if (sEventInitialized) {
            Log.v(TAG,"Is needs to sync is : "+isNeedsToSyncEvent(context));
            if (isNeedsToSyncEvent(context)) {
                startImmediateEventSync(context);
                return;
            }
        }

        sEventInitialized = true;

        AppExecutor.getAppExecutorInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                /* URI for every row of weather data in our weather table*/
                List<EventEntry> eventEntries = AppDatabase.getAppDatabaseInstance(context.getApplicationContext()).eventDao().checkEventEntry();
                if(eventEntries==null || eventEntries.size()==0){
                    startImmediateEventSync(context);
                }else{
                    if (isNeedsToSyncEvent(context)) {
                        startImmediateEventSync(context);
                        return;
                    }
                }
            }
        });

    }


    private static boolean isNeedsToSync(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        long savedTime = sharedPref.getLong(context.getString(R.string.pref_last_sync_time), System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - savedTime;


        Log.v(TAG,"Sync before im milis : "+diff);

        return (diff >= SYNC_DURATION_IN_MILIS);
    }

    private static boolean isNeedsToSyncEvent(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        long savedTime = sharedPref.getLong(context.getString(R.string.pref_last_event_sync_time), System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - savedTime;


        Log.v(TAG, "Sync before im milis : " + diff);

        return (diff >= SYNC_DURATION_IN_MILIS);
    }


    private static void startImmediateSync(Context context) {
        Log.v(TAG, "startImmediateSync");
        Intent intentToSyncImmediately = new Intent(context, PublisherWallIntentService.class);
        intentToSyncImmediately.setAction(PublisherWallIntentService.ACTION_SYNC_NETWORK_LIST);
        context.startService(intentToSyncImmediately);
    }

    private static void startImmediateEventSync(Context context) {
        Log.v(TAG, "startImmediateEventSync");
        Intent intentToSyncImmediately = new Intent(context, PublisherWallIntentService.class);
        intentToSyncImmediately.setAction(PublisherWallIntentService.ACTION_SYNC_EVENT_LIST);
        context.startService(intentToSyncImmediately);
    }

    private static void shareFcmToken(Context context) {
        Log.v(TAG, "startImmediateSync");
        Intent intentToSyncImmediately = new Intent(context, PublisherWallIntentService.class);
        intentToSyncImmediately.setAction(PublisherWallIntentService.ACTION_SEND_FCM_REGISTRATION);
        context.startService(intentToSyncImmediately);
    }

    private static final String TAG = PublisherWallSyncUtils.class.getSimpleName();
}
