package com.strategy.intecom.vtc.fixrepairer.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Sup on 6/20/16.
 */
public class ParserJson {

//    public static final String API_PARAMETER_SUCCESS = "success";
    public static final String API_PARAMETER_ERRORCODE = "errorCode";
    public static final String API_PARAMETER_RESSPONSE_DATA = "responseData";
    public static final String API_PARAMETER_MESSAGE = "message";
    public static final String API_PARAMETER_CODE = "code";

    public static final String API_PARAMETER_PHONE = "phone";
    public static final String API_PARAMETER_PASSWORD = "password";
    public static final String API_PARAMETER_OLD_PASSWORD = "old_password";
    public static final String API_PARAMETER_NEW_PASSWORD = "new_password";
    public static final String API_PARAMETER_USERNAME = "username";
    public static final String API_PARAMETER_NAME = "name";
    public static final String API_PARAMETER_EMAIL = "email";
    public static final String API_PARAMETER_CMTND = "social_id";
    public static final String API_PARAMETER_ADDRESS = "address";
    public static final String API_PARAMETER_DISTRICT = "district";
    public static final String API_PARAMETER_CITY = "city";
    public static final String API_PARAMETER_TYPE_OS = "os_type";
    public static final String API_PARAMETER_DEVICE_ID = "device_id";
    public static final String API_PARAMETER_TOKEN = "token";
    public static final String API_PARAMETER_AUTH_TOKEN = "auth_token";
    public static final String API_PARAMETER_AVATAR = "avatar";
    public static final String API_PARAMETER_POINT = "point";
    public static final String API_PARAMETER_SYSTEM_ID = "system_id";
    public static final String API_PARAMETER_DESCRIPTION = "description";
    public static final String API_PARAMETER_DISTANCE = "distance";
    public static final String API_PARAMETER_PRICE = "price";
    public static final String API_PARAMETER_THUMB = "image";
    public static final String API_PARAMETER_PARENT = "parent";
    public static final String API_PARAMETER_ID = "id";
    public static final String API_PARAMETER_ID_ = "_id";
    public static final String API_PARAMETER_VERSION = "__v";
    public static final String API_PARAMETER_CREATE_AT = "created_at";
    public static final String API_PARAMETER_LEVEL = "level";
    public static final String API_PARAMETER_RATE_TOTAL = "total_rate";
    public static final String API_PARAMETER_RATE_AVG = "avg_rate";
    public static final String API_PARAMETER_RATE_START = "stars";
    public static final String API_PARAMETER_RATE_START_ONE = "one_star";
    public static final String API_PARAMETER_RATE_START_TWO = "two_stars";
    public static final String API_PARAMETER_RATE_START_THREE = "three_stars";
    public static final String API_PARAMETER_RATE_START_FOUR = "four_stars";
    public static final String API_PARAMETER_RATE_START_FIVE = "five_stars";
    public static final String API_PARAMETER_SELECT = "select";
    public static final String API_PARAMETER_LIMIT = "limit";
    public static final String API_PARAMETER_OFFSET = "offset";
    public static final String API_PARAMETER_ORDER = "order";
    public static final String API_PARAMETER_TYPE = "type";
    public static final String API_PARAMETER_STATUS = "status";
    public static final String API_PARAMETER_PAYMENT_METHOD = "payment_method";
    public static final String API_PARAMETER_CATEGORY_NAME = "category_name";
    public static final String API_PARAMETER_LONGITUDE = "long";
    public static final String API_PARAMETER_LATITUDE = "lat";
    public static final String API_PARAMETER_VALUE = "value";
    public static final String API_PARAMETER_ORDER_TIME = "order_time";
    public static final String API_PARAMETER_COMMENT = "comment";
    public static final String API_PARAMETER_IMAGES = "images";
    public static final String API_PARAMETER_COUPON_CODE = "coupon_code";
    public static final String API_PARAMETER_AGENCY_ID = "agencyId";
    public static final String API_PARAMETER_NOTIFI_TYPE = "notifi_type";
    public static final String API_PARAMETER_FIELD_NAME = "field_name";
    public static final String API_PARAMETER_ADDRESS_NAME = "address_name";
    public static final String API_PARAMETER_LINK = "link";
    public static final String API_PARAMETER_CUSTOMER_NAME = "customer_name";
    public static final String API_PARAMETER_AVAILABLE = "available";
    public static final String API_PARAMETER_IS_WORKING = "is_working";
    public static final String API_PARAMETER_ADMIN_VERIFY = "admin_verify";
    public static final String API_PARAMETER_ADDRESS_LONG = "address_long";
    public static final String API_PARAMETER_ADDRESS_LAT = "address_lat";
    public static final String API_PARAMETER_PASSCODE = "verify_code";
    public static final String API_PARAMETER_ACCOUNT_TYPE = "account_type";
    public static final String API_PARAMETER_SORT = "sort";
    public static final String API_PARAMETER_ORDER_ID = "orderId";
    public static final String API_PARAMETER_USER_ID = "userId";
    public static final String API_PARAMETER_TITLE= "title";
    public static final String API_PARAMETER_FROM= "from";
    public static final String API_PARAMETER_IS_READED = "is_readed";
    public static final String API_PARAMETER_COUNT = "count";


    public static final String API_PARAMETER_TOOLS = "tools";
    public static final String API_PARAMETER_FIELDS = "fields";
    public static final String API_PARAMETER_FIELD = "field";
    public static final String API_PARAMETER_CITIES = "cities";
    public static final String API_PARAMETER_CHILD = "children";
    public static final String API_PARAMETER_SKILLS = "skills";
    public static final String API_PARAMETER_INFO = "info";
    public static final String API_PARAMETER_COMMENTS = "comments";
    public static final String API_PARAMETER_RATE = "rate";
    public static final String API_PARAMETER_USER = "user";
    public static final String API_PARAMETER_AGENCY = "agency";




    public static boolean getStatusSuccess(String s) {

        if (getErrorCode(s) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int getErrorCode(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            return jStatus.optInt(API_PARAMETER_ERRORCODE);
        } catch (JSONException e) {
            return 0;
        }
    }

    public static String getStatusMsg(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            return jStatus.optString(API_PARAMETER_MESSAGE, "");
        } catch (JSONException e) {
            return "";
        }
    }

    public static String getAuthToken(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            return jStatus.optString(API_PARAMETER_TOKEN, "");
        } catch (JSONException e) {
            return "";
        }
    }

    public static String getResponseData(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            return jStatus.optString(API_PARAMETER_RESSPONSE_DATA, "");
        } catch (JSONException e) {
            return "";
        }
    }
}
