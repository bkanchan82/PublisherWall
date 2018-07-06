package com.publisherwall.android.publisherwall.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.publisherwall.android.publisherwall.app.PreferenceUtils;
import com.publisherwall.android.publisherwall.utilities.NotificationUtilities;

import java.util.Map;

public class PbFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = PbFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> pushMessageResult = remoteMessage.getData();

        String dataString = "";
        for (Map.Entry<String, String> entrySet : pushMessageResult.entrySet()) {
            Log.d(TAG, "Received Data " + entrySet.getKey() + " : " + entrySet.getValue());
        }
        if(pushMessageResult.containsKey("version")){
            String versionString = pushMessageResult.get("version");
            String updateRequiresString = pushMessageResult.get("isUpdateMustRequire");
            boolean isUpdateMustRequire = updateRequiresString.equals("1") ? true : false;
            String currentTimeMillis = pushMessageResult.get("time");

            PreferenceUtils.saveCurrentVersion(this,versionString,currentTimeMillis,isUpdateMustRequire);
        }else {
            NotificationUtilities.processMessage(pushMessageResult, this);
        }
    }



}
