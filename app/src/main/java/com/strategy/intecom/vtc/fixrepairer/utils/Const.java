package com.strategy.intecom.vtc.fixrepairer.utils;

/**
 * Created by Mr. Ha on 5/12/16.
 */
public class Const {

    public static int UI_CURRENT_CONTEXT = -1;
    public static final int UI_SERVICE_LIST = 1;
    public static final int UI_JOB_LIST = 2;
    public static final int UI_JOB_DETAIL = 3;
    public static final int UI_JOB_DETAIL_MAP = 4;
    public static final int UI_USER_PROFILE = 5;
    public static final int UI_USER_PROFILE_RATING_REVIEW = 6;
    public static final int UI_USER_PROFILE_UPGRADE_LEVEL = 7;
    public static final int UI_LEASE_NEW = 8;
    public static final int UI_TOOLS_LEASE = 9;
    public static final int UI_RECEIVE_NOTIFICATION = 10;
    public static final int UI_GUIDE_NEW_JOB = 11;
    public static final int UI_MY_JOB_LIST = 12;
    public static final int UI_MY_JOB_HISTORY = 13;
    public static final int UI_MY_LOGIN = 14;
    public static final int UI_MY_LOGIN_CONFIRM = 15;
    public static final int UI_MY_REGISTER = 16;
    public static final int UI_MY_REGISTER_CHOICE_SERVICE = 17;
    public static final int UI_MY_FORGOT_PASSWORD = 18;
    public static final int UI_MAP_NEARBY = 19;
    public static final int UI_MY_CONFIRM_CHANGE_FORGOT_PASSWORD = 20;
    public static final int UI_MY_CONFIRM_PASSCODE = 21;
    public static final int UI_RULES = 22;
    public static final int UI_MY_JOB_FAST = 23;
    public static final int UI_NOTIFICATION = 24;
    public static final int UI_NOTIFICATION_EXIST_JOB = 25;
    public static final int UI_NOTIFICATION_ACCEPT_JOB = 26;
    public static final int UI_LOGOUT = 27;
    public static final int UI_SEARCH_PIN_MAP = 28;
    public static final int UI_MY_CONFIRM_CHANGE_PASSWORD = 29;
    public static final int UI_MY_SUPPORT = 30;

    public static final int UI_ACTION_SHOW_DIALOG = 10001;
    public static final int UI_ACTION_NOTIFICATION = 10002;


    public static final String KEY_BUNDLE_ACTION_HISTORY = "is_history";
    public static final String KEY_BUNDLE_ACTION_UPDATE = "is_update";
    public static final String KEY_BUNDLE_ACTION_USER = "model_user";
    public static final String KEY_BUNDLE_ACTION_ORDER = "model_order";
    public static final String KEY_BUNDLE_ACTION_FIELDS = "action_fields";
    public static final String KEY_BUNDLE_ACTION_PHONE_NUM = "phone_num";
    public static final String KEY_BUNDLE_ACTION_TYPE_PASSCODE = "type_passcode";
    public static final String KEY_BUNDLE_ACTION_ID = "order_id";
    public static final String KEY_BUNDLE_ACTION_ADDRESS = "model_address";

    public static final String OS_TYPE = "android";
    public static final String ACCOUNT_TYPE = "agency";

    public static final String TYPE_PASSCODE_LOGIN = "login";
    public static final String TYPE_PASSCODE_REGISTER = "register";

    public static final String TYPE_SORT_BY_TIME = "time";
    public static final String TYPE_SORT_BY_DISTANCE = "distance";

    public static final int TYPE_DISPLAY_JOB_HISTORY = 1;
    public static final int TYPE_DISPLAY_JOB_RENTER = 2;
    public static final int TYPE_DISPLAY_JOB_HISTORY_HEADER = 3;

    public static final float ALPHA_ENABLE = 1.0f;
    public static final float ALPHA_DISABLE = 0.5f;

    public static final String KEY_EXTRACT_NOTIFICATION = "noti";
    public static final String KEY_EXTRACT_NOTI_ID = "noti_id";
    public static final String KEY_MESSAGE_OWNER_ADMIN = "Admin";

    // TODO link to share
    public static final String URL_TO_SHARE = "http://www.vtc.vn";
    public static final String SUPPORT_CALL_PHONE = "19001530";

    public static final int REQUEST_CODE_CAM_AVATAR = 1313;
    public static final int REQUEST_CODE_LOAD_IMAGE_AVATAR = 1314;
    public static final int REQUEST_CODE_CROP_IMAGE = 1315;

    public static final int VALIDATE_RESULT_NAME_OK = 0;
    public static final int VALIDATE_RESULT_NAME_NULL = 1;
    public static final int VALIDATE_RESULT_NAME_NOT_CORRECT = 2;
    public static final int VALIDATE_RESULT_NAME_WRONG_LENGTH = 3;
    public static final int VALIDATE_NAME_MAX_LENGTH = 32;
    public static final int VALIDATE_NAME_MIN_LENGTH = 3;
    public static final int VALIDATE_PASSWORD_MIN_LENGTH = 6;
    public static final int VALIDATE_PASSWORD_MAX_LENGTH = 32;
}
