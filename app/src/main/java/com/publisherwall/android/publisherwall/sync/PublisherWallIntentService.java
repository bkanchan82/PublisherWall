package com.publisherwall.android.publisherwall.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class PublisherWallIntentService extends IntentService {

    public static final String ACTION_SYNC_NETWORK_LIST = "sync_network_list";
    public static final String ACTION_SYNC_EVENT_LIST = "sync_event_list";
    public static final String ACTION_SEND_FCM_REGISTRATION = "send_fcm_registration";




    public PublisherWallIntentService(){
        super(PublisherWallIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String intentAction = intent.getAction();
        if(intentAction != null){
            if (intentAction.equals(ACTION_SYNC_NETWORK_LIST)){
                PublisherWallSyncTask.syncNetwork(this);
            }else if(intentAction.equals(ACTION_SYNC_EVENT_LIST)){
                PublisherWallSyncTask.syncEvent(this);
            }else if(intentAction.equals(ACTION_SEND_FCM_REGISTRATION)){
                PublisherWallSyncTask.sendRegistrationToServer(this);
            }

        }

    }
}
