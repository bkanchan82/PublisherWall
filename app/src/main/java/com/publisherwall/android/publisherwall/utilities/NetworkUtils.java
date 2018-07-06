package com.publisherwall.android.publisherwall.utilities;

import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String TAG = "PublisherWall"+ NetworkUtils.class.getSimpleName();
    public static final String POST_USER_DETAIL_URL = "http://publisherwall.com/api/networks/reg_user.php";
    public static final String POST_LIKED_NETWORK_URL = "http://publisherwall.com/api/networks/fav_net.php";
    public static final String POST_LIKED_EVENT_URL = "http://publisherwall.com/api/networks/event_reminder.php";
    private static final String BASE_URL = "http://publisherwall.com/api";
    private static final String NETWORK_DETAIL_BASE_URL = "http://publisherwall.com/api/networks/net_details.php";
    public static final String EVENT_LIST_URL = "http://publisherwall.com/api/networks/events.php";
    public static final String BUY_EMAIL_DATA_URL = "http://publisherwall.com/usadata/index.php";
    public static final String PUBLISHER_TOOLS_URL = "https://publisherwall.net";
    public static final String POST_FCM_REGISTRATION_TOKEN_URL = "https://publisherwall.com/api/networks/client_key.php";

    private static final String EVENT_DESCRIPTION_BASE_URL = "http://publisherwall.com/api/networks/event_detail.php";


    private static final String NETWORKS_PATH = "networks";
    private static final String QUERY_PARAM_SNO = "sno";
    private static final String QUERY_PARAM_EMAIL = "email";
    private static final String IMAGE_EXTENSION = ".jpg";

    public static URL buildNetworksUrl(){
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(NETWORKS_PATH)
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }

        Log.v(TAG,"Build url : "+ (url != null ? url.toString() : null));
        return url;
    }

    public static URL buildEventsUrl(String email){
        Uri buildUri = Uri.parse(EVENT_LIST_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_EMAIL,email)
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }

        Log.v(TAG,"Event list build url : "+ (url != null ? url.toString() : null));
        return url;
    }

    public static URL eventDescriptionUrl(String eid) {
        Uri buildUri = Uri.parse(EVENT_DESCRIPTION_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_SNO,eid)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        Log.v(TAG, "Build url : " + (url != null ? url.toString() : null));
        return url;
    }

    public static URL buildNetworkDetailUrl(String sno){
        Uri buildUri = Uri.parse(NETWORK_DETAIL_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_SNO,sno)
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }

        Log.v(TAG,"Build url : "+ (url != null ? url.toString() : null));
        return url;
    }



}
