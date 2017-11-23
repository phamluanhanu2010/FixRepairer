package com.strategy.intecom.vtc.fixrepairer.view.base;

import android.support.v4.app.Fragment;

import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.view.UIConfirmChangePass;
import com.strategy.intecom.vtc.fixrepairer.view.UIConfirmChooseService;
import com.strategy.intecom.vtc.fixrepairer.view.UIConfirmInfoProfile;
import com.strategy.intecom.vtc.fixrepairer.view.UIConfirmPassCode;
import com.strategy.intecom.vtc.fixrepairer.view.UIConfirmReChangePass;
import com.strategy.intecom.vtc.fixrepairer.view.UIConfirmSignIn;
import com.strategy.intecom.vtc.fixrepairer.view.UIForgotPassword;
import com.strategy.intecom.vtc.fixrepairer.view.UILogin;
import com.strategy.intecom.vtc.fixrepairer.view.UISupport;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMGuideNewJob;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMJobDetail;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMJobDetailMap;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMMapNearby;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMNotification;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMRules;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMSearchPinMap;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMToolsLeaseNew;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMServiceList;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMJobList;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMToolsLeaseList;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMUserJobHistory;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMUserJobList;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMUserProfile;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMUserProfileRatingReview;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMUserProfileUpgradeLevel;

public class VtcFragmentFactory {

    /**
     * All class in app will int below with key_value and return Fragment by
     * name class fragment and CallBack
     **/
    public static Fragment getFragmentWithCallbackByKey(final int key, Callback callback) {

        switch (key) {

            case Const.UI_SERVICE_LIST:
                return new FMServiceList(callback);
            case Const.UI_JOB_LIST:
                return new FMJobList(callback);
            case Const.UI_JOB_DETAIL:
                return new FMJobDetail(callback);
            case Const.UI_JOB_DETAIL_MAP:
                return new FMJobDetailMap(callback);
            case Const.UI_MAP_NEARBY:
                return new FMMapNearby(callback);
            case Const.UI_USER_PROFILE:
                return new FMUserProfile(callback);
//            case Const.UI_USER_PROFILE_RATING_REVIEW:
//                return new FMUserProfileRatingReview(callback);
//            case Const.UI_USER_PROFILE_UPGRADE_LEVEL:
//                return new FMUserProfileUpgradeLevel(callback);
//            case Const.UI_LEASE_NEW:
//                return new FMToolsLeaseNew(callback);
            case Const.UI_TOOLS_LEASE:
                return new FMToolsLeaseList(callback);
            case Const.UI_GUIDE_NEW_JOB:
                return new FMGuideNewJob(callback);
            case Const.UI_MY_JOB_LIST:
                return new FMUserJobList(callback);
            case Const.UI_MY_JOB_HISTORY:
                return new FMUserJobHistory(callback);
//            case Const.UI_MY_LOGIN:
//                return new UILogin();
//            case Const.UI_MY_LOGIN_CONFIRM:
//                return new UIConfirmSignIn();
            case Const.UI_MY_REGISTER:
                return new UIConfirmInfoProfile(callback);
            case Const.UI_MY_REGISTER_CHOICE_SERVICE:
                return new UIConfirmChooseService(callback);
//            case Const.UI_MY_FORGOT_PASSWORD:
//                return new UIForgotPassword(callback);
            case Const.UI_NOTIFICATION:
                return new FMNotification(callback);
            case Const.UI_SEARCH_PIN_MAP:
                return new FMSearchPinMap(callback);

            default:
                return null;
        }

    }

    /**
     * All class in app will int below with key_value and return Fragment by
     * name class fragment
     */
    public static Fragment getFragmentByKey(final int key) {

        switch (key) {

            case Const.UI_SERVICE_LIST:
                return new FMServiceList();
            case Const.UI_JOB_LIST:
                return new FMJobList();
            case Const.UI_JOB_DETAIL:
                return new FMJobDetail();
            case Const.UI_JOB_DETAIL_MAP:
                return new FMJobDetailMap();
            case Const.UI_MAP_NEARBY:
                return new FMMapNearby();
            case Const.UI_USER_PROFILE:
                return new FMUserProfile();
            case Const.UI_USER_PROFILE_RATING_REVIEW:
                return new FMUserProfileRatingReview();
            case Const.UI_USER_PROFILE_UPGRADE_LEVEL:
                return new FMUserProfileUpgradeLevel();
            case Const.UI_LEASE_NEW:
                return new FMToolsLeaseNew();
            case Const.UI_TOOLS_LEASE:
                return new FMToolsLeaseList();
            case Const.UI_GUIDE_NEW_JOB:
                return new FMGuideNewJob();
            case Const.UI_MY_JOB_LIST:
                return new FMUserJobList();
            case Const.UI_MY_JOB_HISTORY:
                return new FMUserJobHistory();
            case Const.UI_MY_LOGIN:
                return new UILogin();
            case Const.UI_MY_LOGIN_CONFIRM:
                return new UIConfirmSignIn();
            case Const.UI_MY_REGISTER:
                return new UIConfirmInfoProfile();
            case Const.UI_MY_REGISTER_CHOICE_SERVICE:
                return new UIConfirmChooseService();
            case Const.UI_MY_FORGOT_PASSWORD:
                return new UIForgotPassword();
            case Const.UI_MY_CONFIRM_CHANGE_FORGOT_PASSWORD:
                return new UIConfirmReChangePass();
            case Const.UI_MY_CONFIRM_PASSCODE:
                return new UIConfirmPassCode();
            case Const.UI_RULES:
                return new FMRules();
            case Const.UI_NOTIFICATION:
                return new FMNotification();
            case Const.UI_SEARCH_PIN_MAP:
                return new FMSearchPinMap();
            case Const.UI_MY_CONFIRM_CHANGE_PASSWORD:
                return new UIConfirmChangePass();
            case Const.UI_MY_SUPPORT:
                return new UISupport();
            default:
                return null;
        }
    }
}
