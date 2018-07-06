package com.publisherwall.android.publisherwall.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.publisherwall.android.publisherwall.R;

import java.util.Map;

public class PreferenceUtils {


    public static void saveFcmRegistrationToken(String token, boolean isShared, Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.pref_fcm_registration_token), token);
        editor.putBoolean(context.getString(R.string.pref_is_fcm_token_shared), isShared);
        editor.apply();

    }

    public static String getUserEmail(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.pref_email_key),"");
    }

    public static String getUserName(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.pref_user_name_key),"");
    }

    public static String getProfilePicUrl(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.pref_profile_pic_url_key),"");
    }

    public static String getFcmRegistrationToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.pref_fcm_registration_token),"");
    }

    public static boolean isFcmTokenShared(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getString(R.string.pref_is_fcm_token_shared),false);
    }


    public static void saveCurrentVersion(Context context,String currentVersion,String dateTime,boolean isUpdateMustRequir){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.pref_app_current_version_on_server), currentVersion);
        editor.putString(context.getString(R.string.pref_current_version_update_time), dateTime);
        editor.putBoolean(context.getString(R.string.pref_is_update_must_required), isUpdateMustRequir);
        editor.apply();
    }

    public static String getCurrentVersionOnServer(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.pref_app_current_version_on_server),"1");
    }

    public static boolean isUpdateMustRequire(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_credential), context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getString(R.string.pref_is_update_must_required),false);
    }

}
