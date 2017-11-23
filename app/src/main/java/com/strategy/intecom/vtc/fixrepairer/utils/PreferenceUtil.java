package com.strategy.intecom.vtc.fixrepairer.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    private SharedPreferences IShare = null;

    public static final String USER_INFO_AVATAR = "user_info_avatar";
    public static final String DEVICE_ID = "device_id";
    public static final String IS_RECEIVE_NOTI = "is_receive_noti";
    public static final String USER_INFO = "user_info";
    public static final String USER_ACCEPTED_JOB_FAST = "is_working_job";  // Trạng thái không cho back ra khỏi màn hình Job detail khi nhận order nhanh

    public static final String IS_GUIDE_NEW_JOB = "is_guide_new_job";

    public static final String AUTH_TOKEN = "auth_token";


    public PreferenceUtil(Context context) {
        if (context != null)
            IShare = context.getSharedPreferences(context.getApplicationInfo().packageName, Context.MODE_PRIVATE);
    }


    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Remove Share Preferences
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    public void PreferenceUtilRemove(Context context) {
        if (context != null)
            IShare = context.getSharedPreferences(context.getApplicationInfo().packageName, Context.MODE_PRIVATE);
        IShare.edit().clear().commit();
    }


    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Remove Data When Logout User
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    public void RemoveDataWhenLogOut() {
        removeValue(AUTH_TOKEN);
        removeValue(DEVICE_ID);
        removeValue(USER_INFO);
        removeValue(IS_GUIDE_NEW_JOB);
        removeValue(IS_RECEIVE_NOTI);
        removeValue(USER_INFO_AVATAR);
        removeValue(USER_ACCEPTED_JOB_FAST);
    }

    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Get Set By Other Key
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    public void setValueLong(String key, long val) {
        IShare.edit().putLong(key, val).commit();
    }

    public void setValueString(String key, String val) {
        IShare.edit().putString(key, val).commit();
    }

    public void setValueBoolean(String key, boolean val) {
        IShare.edit().putBoolean(key, val).commit();
    }

    public void setValueInteger(String key, int val) {
        IShare.edit().putInt(key, val).commit();
    }

    public int getValueInteger(String key) {
        return IShare.getInt(key, -1);
    }

    public long getValueLong(String key) {
        return IShare.getLong(key, -1);
    }

    public boolean getValueBoolean(String key) {
        return IShare.getBoolean(key, false);
    }

    public String getValueString(String key) {
        return IShare.getString(key, "").trim();
    }

    public void removeValue(String key) {
        IShare.edit().remove(key).commit();
    }

}
