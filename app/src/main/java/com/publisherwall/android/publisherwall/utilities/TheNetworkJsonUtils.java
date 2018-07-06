package com.publisherwall.android.publisherwall.utilities;

import android.util.Log;

import com.publisherwall.android.publisherwall.data.EventEntry;
import com.publisherwall.android.publisherwall.data.NetworkEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created on 20-12-2017.
 */

public class TheNetworkJsonUtils {

    public static final String TAG = TheNetworkJsonUtils.class.getSimpleName();

    private static final String PB_RESULTS = "data";

    private static final String PB_SNO = "SNO";

    private static final String PB_NETWORK_NAME = "NetworkName";

    private static final String PB_NETWORK_IMAGE_URL = "NetworkImageURL";

    private static final String PB_NETWORK_JOIN_URL = "NetworkJoinURL";

    private static final String PB_IMAGE_NAME = "Img";
    private static final String PB_COMMISSION = "comm";
    private static final String PB_MIN_PAY = "minpay";
    private static final String PB_PAY_FREQUENCY = "payfrq";
    private static final String PB_OFFERS = "offers";
    private static final String PB_SHARE_URL = "share_url";
    private static final String PB_FAVORITE_COUNT = "fav_count";


    //this variable holds the error code if there is any problem in fulfilling the request
    private static final String TMD_MESSAGE_CODE = "status_message";

    public static final String LOGO_BASE_URL = "http://publisherwall.com/netimg/";
    public static final String NETWORK_BIG_IMAGE_BASE_URL = "http://publisherwall.com/netBIGimg/";


    public static NetworkEntry[] getNetworksContentValuesFromJson(JSONObject networkDataJson)
            throws JSONException {

        /* String array list to hold each movie String */
        NetworkEntry[] parsedNetworkData;

        /* Is there an error? */
        if (networkDataJson.has(TMD_MESSAGE_CODE)) {
            int errorCode = networkDataJson.getInt(TMD_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray networkArray = networkDataJson.getJSONArray(PB_RESULTS);

        parsedNetworkData = new NetworkEntry[networkArray.length()];

        for (int i = 0; i < networkArray.length(); i++) {

            /* These are the values that will be collected */
            String networkName;
            String imageUrl;
            String sno;
            String networkImageUrl;
            String networkJoinUrl;
            String commission;
            String minPay;
            String payFrequency;
            String offers;
            String shareUrl;
            String favoriteCOunt;

            /* Get the JSON object representing the movie */
            JSONObject networkDetail = networkArray.getJSONObject(i);

            networkName = networkDetail.getString(PB_NETWORK_NAME);
            imageUrl = LOGO_BASE_URL + networkDetail.getString(PB_IMAGE_NAME);
            sno = networkDetail.getString(PB_SNO);
            networkImageUrl = networkDetail.getString(PB_NETWORK_IMAGE_URL);
            networkJoinUrl = networkDetail.getString(PB_NETWORK_JOIN_URL);
            commission = networkDetail.getString(PB_COMMISSION);
            minPay = networkDetail.getString(PB_MIN_PAY);
            payFrequency = networkDetail.getString(PB_PAY_FREQUENCY);
            offers = networkDetail.getString(PB_OFFERS);
            shareUrl = networkDetail.getString(PB_SHARE_URL);
            favoriteCOunt = networkDetail.getString(PB_FAVORITE_COUNT);
            Log.d(TAG,"Favorite Count : "+favoriteCOunt);

            NetworkEntry networkEntry = new NetworkEntry(sno,networkName,imageUrl,networkJoinUrl,networkImageUrl,commission,minPay,payFrequency,offers,shareUrl,"0",favoriteCOunt);
            parsedNetworkData[i] = networkEntry;
        }

        return parsedNetworkData;
    }


    private static final String PB_EVENT_RESULT = "data";
    private static final String PB_EVENT_ID = "eid";
    private static final String PB_EVENT_TITLE = "title";
    private static final String PB_EVENT_URL = "url";
    private static final String PB_EVENT_DATE = "date";
    private static final String PB_EVENT_LOCATION = "location";
    private static final String PB_EVENT_DESCRIPTION = "detail";
    private static final String PB_EVENT_FAVORITE_COUNT = "event_fav_count";
    private static final String PB_EVENT_POSTER_URL = "event_img";
    private static final String PB_EVENT_IS_FAVORITE = "is_fav";

    public static EventEntry[] getEventArrayListFromJson(JSONObject eventDataJson)
            throws JSONException {

        EventEntry[] parsedEventData;

        JSONArray eventArray = eventDataJson.getJSONArray(PB_EVENT_RESULT);

        parsedEventData = new EventEntry[eventArray.length()];

        for (int i = 0; i < eventArray.length(); i++) {

            /* These are the values that will be collected */
            String edi;
            String title;
            String url;
            String date;
            String location;
            String posterUrl;
            String isFavorite;
            String detail;
            String favoriteCount;


            /* Get the JSON object representing the movie */
            JSONObject eventDetail = eventArray.getJSONObject(i);

            edi = eventDetail.getString(PB_EVENT_ID);
            title = eventDetail.getString(PB_EVENT_TITLE);
            url = eventDetail.getString(PB_EVENT_URL);
            date = eventDetail.getString(PB_EVENT_DATE);
            location = eventDetail.getString(PB_EVENT_LOCATION);
            posterUrl = eventDetail.getString(PB_EVENT_POSTER_URL);
            isFavorite = eventDetail.getString(PB_EVENT_IS_FAVORITE);
            detail = eventDetail.getString(PB_EVENT_DESCRIPTION);
            favoriteCount = eventDetail.getString(PB_EVENT_FAVORITE_COUNT);
//            favoriteCount = "100";

            parsedEventData[i] = new EventEntry(edi, title, url, date, location,detail,posterUrl,isFavorite,favoriteCount);
        }

        return parsedEventData;
    }

    private static final String CND_SNO = "SNO";
    private static final String CND_NETWORK_NAME = "NetworkName";
    private static final String CND_AFF_PAYING_URL = "AffpayingURL";
    private static final String CND_NETWORK_IMAGE_URL = "NetworkImageURL";
    private static final String CND_NETWORK_JOIN_URL = "NetworkJoinURL";
    private static final String CND_IMG = "Img";
    private static final String CND_BIG_IMG = "bigImg";
    private static final String CND_PHONE = "phone";
    private static final String CND_EMAIL = "email";
    private static final String CND_OFFERS = "offers";
    private static final String CND_COMM = "comm";
    private static final String CND_MIN_PAY = "minpay";
    private static final String CND_PAY_FRQ = "payfrq";
    private static final String CND_PAY_METH = "paymeth";
    private static final String CND_REF_CO = "refcomm";
    private static final String CND_TRACK_SOFT = "tracksoft";
    private static final String CND_TRACK_LINE = "tracklink";
    private static final String CND_TWITTER = "twitter";
    private static final String CND_FACEBOOK = "facebook";
    private static final String CND_DESCRIPTION = "Description";
    private static final String CND_LIKES = "likes";
    private static final String CND_STATUS = "status";
    private static final String CND_POSITION = "position";
    private static final String CND_JOIN_DATE = "joindate";
    private static final String CND_SHARE_URL = "share_url";


    public static CompleteNetworkData getCompleteNetworkDataFromJsonString(JSONObject jsonObject)
            throws JSONException {

        /* Is there an error? */
        if (jsonObject.has(TMD_MESSAGE_CODE)) {
            int errorCode = jsonObject.getInt(TMD_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        String sno = jsonObject.has(CND_SNO) ? jsonObject.getString(CND_SNO) : "";
        String networkName = jsonObject.has(CND_NETWORK_NAME) ? jsonObject.getString(CND_NETWORK_NAME) : "";
        String affpayingURL = jsonObject.has(CND_AFF_PAYING_URL) ? jsonObject.getString(CND_AFF_PAYING_URL) : "";
        String networkImageURL = jsonObject.has(CND_NETWORK_IMAGE_URL) ? jsonObject.getString(CND_NETWORK_IMAGE_URL) : "";
        String networkJoinURL = jsonObject.has(CND_NETWORK_JOIN_URL) ? jsonObject.getString(CND_NETWORK_JOIN_URL) : "";
        String img = jsonObject.has(CND_IMG) ? jsonObject.getString(CND_IMG) : "";
        String bigImg = jsonObject.has(CND_BIG_IMG) ? NETWORK_BIG_IMAGE_BASE_URL + jsonObject.getString(CND_BIG_IMG) : "";
        String phone = jsonObject.has(CND_PHONE) ? jsonObject.getString(CND_PHONE) : "";
        String email = jsonObject.has(CND_EMAIL) ? jsonObject.getString(CND_EMAIL) : "";
        String offers = jsonObject.has(CND_OFFERS) ? jsonObject.getString(CND_OFFERS) : "";
        String comm = jsonObject.has(CND_COMM) ? jsonObject.getString(CND_COMM) : "";
        String minpay = jsonObject.has(CND_MIN_PAY) ? jsonObject.getString(CND_MIN_PAY) : "";
        String payfrq = jsonObject.has(CND_PAY_FRQ) ? jsonObject.getString(CND_PAY_FRQ) : "";
        String paymeth = jsonObject.has(CND_PAY_METH) ? jsonObject.getString(CND_PAY_METH) : "";
        String refcomm = jsonObject.has(CND_REF_CO) ? jsonObject.getString(CND_REF_CO) : "";
        String tracksoft = jsonObject.has(CND_TRACK_SOFT) ? jsonObject.getString(CND_TRACK_SOFT) : "";
        String tracklink = jsonObject.has(CND_TRACK_LINE) ? jsonObject.getString(CND_TRACK_LINE) : "";
        String twitter = jsonObject.has(CND_TWITTER) ? jsonObject.getString(CND_TWITTER) : "";
        String facebook = jsonObject.has(CND_FACEBOOK) ? jsonObject.getString(CND_FACEBOOK) : "";
        String description = jsonObject.has(CND_DESCRIPTION) ? jsonObject.getString(CND_DESCRIPTION) : "";
        String likes = jsonObject.has(CND_LIKES) ? jsonObject.getString(CND_LIKES) : "";
        String status = jsonObject.has(CND_STATUS) ? jsonObject.getString(CND_STATUS) : "";
        String position = jsonObject.has(CND_POSITION) ? jsonObject.getString(CND_POSITION) : "";
        String joindate = jsonObject.has(CND_JOIN_DATE) ? jsonObject.getString(CND_JOIN_DATE) : "";

        CompleteNetworkData completeNetworkData = new CompleteNetworkData(sno,
                networkName,
                affpayingURL,
                networkImageURL,
                networkJoinURL,
                img,
                bigImg,
                phone,
                email,
                offers,
                comm,
                minpay,
                payfrq,
                paymeth,
                refcomm,
                tracksoft,
                tracklink,
                twitter,
                facebook,
                description,
                likes,
                status,
                position,
                joindate);
        return completeNetworkData;
    }


    private static final String CND_AFFILIATE_MANAGER_DATA = "am_data";
    private static final String CND_AFFILIATE_MANAGER_NAME = "AffiliateManager";
    private static final String CND_AIM = "AIM";
    private static final String CND_AFF_EMAIL = "Email";
    private static final String CND_AFF_PHONE = "phone";
    private static final String CND_SKYPE = "skype";

    public static ArrayList<ManagerData> getManagersDetailFromNetworkDetail(JSONObject jsonObject) throws JSONException {
        ArrayList<ManagerData> managerDatas = new ArrayList<>();

        if(jsonObject.has(CND_AFFILIATE_MANAGER_DATA)){
            JSONArray managerJsonArray = jsonObject.getJSONArray(CND_AFFILIATE_MANAGER_DATA);

            for(int i = 0 ; i < managerJsonArray.length() ; i++){
                JSONObject managerJson = managerJsonArray.getJSONObject(i);

                String name = managerJson.getString(CND_AFFILIATE_MANAGER_NAME);
                String aim = managerJson.getString(CND_AIM);
                String email = managerJson.getString(CND_AFF_EMAIL);
                String phone = managerJson.getString(CND_AFF_PHONE);
                String skype = managerJson.getString(CND_SKYPE);
                managerDatas.add(new ManagerData(name,email,phone,skype,aim));
            }

        }

        return managerDatas;
    }

}
