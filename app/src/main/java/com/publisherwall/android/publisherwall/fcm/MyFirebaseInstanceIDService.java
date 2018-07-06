package com.publisherwall.android.publisherwall.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.app.PreferenceUtils;
import com.publisherwall.android.publisherwall.app.PublisherWallAppController;
import com.publisherwall.android.publisherwall.sync.PublisherWallSyncTask;
import com.publisherwall.android.publisherwall.utilities.NetworkUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        PreferenceUtils.saveFcmRegistrationToken(refreshedToken,false,this);
//        PublisherWallSyncTask.sendRegistrationToServer(this);
    }

}
