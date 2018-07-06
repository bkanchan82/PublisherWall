package com.publisherwall.android.publisherwall.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.app.PreferenceUtils;
import com.publisherwall.android.publisherwall.app.PublisherWallAppController;
import com.publisherwall.android.publisherwall.data.AppDatabase;
import com.publisherwall.android.publisherwall.data.EventEntry;
import com.publisherwall.android.publisherwall.data.NetworkEntry;
import com.publisherwall.android.publisherwall.utilities.AppExecutor;
import com.publisherwall.android.publisherwall.utilities.EventData;
import com.publisherwall.android.publisherwall.utilities.NetworkUtils;
import com.publisherwall.android.publisherwall.utilities.TheNetworkJsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PublisherWallSyncTask {

    private static final String TAG = PublisherWallSyncTask.class.getSimpleName();

    synchronized public static void syncNetwork(final Context context) {

        try {
            String networkListUrlString = NetworkUtils.buildNetworksUrl().toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    networkListUrlString,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v(TAG, "Networks Response : " + response.toString());
                            try {
                                final NetworkEntry[] networkEntries = TheNetworkJsonUtils.getNetworksContentValuesFromJson(response);
                                if (networkEntries != null && networkEntries.length > 0) {
                                    AppExecutor.getAppExecutorInstance().getDiskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppDatabase appDatabase = AppDatabase.getAppDatabaseInstance(context.getApplicationContext());
                                            appDatabase.networkDao().nukeTable();

                                            long[] insertedRowCount = appDatabase.networkDao().insertAll(networkEntries);
                                            Log.v(TAG, "addedRowCount : " + insertedRowCount.length);
                                            if (insertedRowCount.length > 0) {
                                                SharedPreferences sharedPref = context.getSharedPreferences(
                                                        context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPref.edit();
                                                editor.putLong(context.getString(R.string.pref_last_sync_time), System.currentTimeMillis());
                                                editor.apply();
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            PublisherWallAppController.getInstance().addRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

    }



    synchronized public static void syncEvent(final Context context) {

        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
            final String emailAddress = sharedPref.getString(context.getString(R.string.pref_email_key), "");

            String urlString = NetworkUtils.buildEventsUrl(emailAddress).toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    urlString,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v(TAG, "Event Response : " + response.toString());
                            try {
                                EventEntry[] data = TheNetworkJsonUtils.getEventArrayListFromJson(response);
                                if (data != null && data.length > 0) {
                                    AppExecutor.getAppExecutorInstance().getDiskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppDatabase appDatabase = AppDatabase.getAppDatabaseInstance(context.getApplicationContext());
                                            appDatabase.eventDao().nukeTable();

                                            long[] insertedRowCount = appDatabase.eventDao().insertAll(data);
                                            Log.v(TAG, "addedEventRowCount : " + insertedRowCount.length);
                                            if (insertedRowCount.length > 0) {
                                                SharedPreferences sharedPref = context.getSharedPreferences(
                                                        context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPref.edit();
                                                editor.putLong(context.getString(R.string.pref_last_event_sync_time), System.currentTimeMillis());
                                                editor.apply();
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            PublisherWallAppController.getInstance().addRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

    }


    synchronized public static void sendRegistrationToServer(final Context context) {
        final String fcmRegistrationToken = PreferenceUtils.getFcmRegistrationToken(context);
        final String userEmail = PreferenceUtils.getUserEmail(context);
        final String userName = PreferenceUtils.getUserName(context);

        if (fcmRegistrationToken == null || fcmRegistrationToken.isEmpty()) {
            return;
        }
        if (userEmail == null || userEmail.isEmpty()) {
            return;
        }

        try {


            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    NetworkUtils.POST_FCM_REGISTRATION_TOKEN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v(TAG, "Registration to server response : " + response);

                            if (response.toString().contains("success")) {
                                String fcmToken = PreferenceUtils.getFcmRegistrationToken(context);
                                PreferenceUtils.saveFcmRegistrationToken(fcmToken, true, context);
                                PublisherWallSyncUtils.initialize(context);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.v(TAG, "Post user detail error : " + error.getMessage());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("email", userEmail);
                    params.put("fcm_token", fcmRegistrationToken);
                    params.put("name", userName);
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        Log.d(TAG, "PARAMS : " + entry.getKey() + "/" + entry.getValue());
                    }
                    return params;

                }
            };
            PublisherWallAppController.getInstance().addRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

    }

}
