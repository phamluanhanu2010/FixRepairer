package com.strategy.intecom.vtc.fixrepairer.view.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.strategy.intecom.vtc.fixrepairer.BuildConfig;
import com.strategy.intecom.vtc.fixrepairer.MainScreen;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelCity;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelJob;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelTools;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMServiceList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;

/**
 * Created by Mr. Ha on 6/29/16.
 */
public class AppCoreAPI extends AppCoreBase implements RequestListener {

    protected void initGetCommonInfo(RequestListener requestListener) {
        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_LIST_COMMONINFO, RequestListener.API_CONNECTION_GET_COMMONINFO, false);
    }

    protected void initGetLogin(RequestListener requestListener, String phoneNum, String password, double log, double lat) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ParserJson.API_PARAMETER_DEVICE_ID, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.DEVICE_ID));

            jsonObject.put(ParserJson.API_PARAMETER_USERNAME, phoneNum);

            jsonObject.put(ParserJson.API_PARAMETER_PASSWORD, password);

            jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_LONG, log);

            jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_LAT, lat);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_LOGIN, RequestListener.API_CONNECTION_LOGIN, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initFogotPassword(RequestListener requestListener, String phoneNum) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_FORGOT_PASSWORD, RequestListener.API_CONNECTION_FORGOT_PASSWORD, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initChangePassword(RequestListener requestListener, String userID, String oldPassword, String newPassword) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_OLD_PASSWORD, oldPassword);
            jsonObject.put(ParserJson.API_PARAMETER_NEW_PASSWORD, newPassword);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CHANGE_PASSWORD, RequestListener.API_CONNECTION_AGENCY_ORDER + userID + RequestListener.API_CONNECTION_CHANGE_PASSWORD + VtcNWConnection.urlEncodeUTF8(map), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initFogotPasswordConfirmCode(RequestListener requestListener, String phoneNum, String confirmCode, String password) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_PASSCODE, confirmCode);
            jsonObject.put(ParserJson.API_PARAMETER_PASSWORD, password);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CONFIRM_CODE, RequestListener.API_CONNECTION_PASSCODE, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initCheckPhoneNum(RequestListener requestListener, String phoneNum) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CHECK_PHONE_NUM, RequestListener.API_CONNECTION_CHECK_PHONE_NUM, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initGetFieldLst(RequestListener requestListener) {
        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_LIST_FIELD, RequestListener.API_CONNECTION_GET_FIELD_LIST + VtcNWConnection.urlEncodeUTF8(map), null, false, false);
    }

    public static void initUpLoadAvatar(RequestListener requestListener, String avatarLink) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_UPLOAD_AVATAR, RequestListener.API_CONNECTION_AGENCY_ORDER + getVtcModelUser().getId() + RequestListener.API_CONNECTION_UPLOAD_AVATAR + VtcNWConnection.urlEncodeUTF8(map), avatarLink, false);
    }

    protected void initRegister(RequestListener requestListener, VtcModelUser vtcModelUser) {
        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_DEVICE_ID, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.DEVICE_ID));
            jsonObject.put(ParserJson.API_PARAMETER_NAME, vtcModelUser.getUser_full_name());
            jsonObject.put(ParserJson.API_PARAMETER_PHONE, vtcModelUser.getUser_Phone_Num());
            jsonObject.put(ParserJson.API_PARAMETER_EMAIL, vtcModelUser.getUser_Email());
            jsonObject.put(ParserJson.API_PARAMETER_CMTND, vtcModelUser.getUser_CMTND());
            jsonObject.put(ParserJson.API_PARAMETER_PASSWORD, vtcModelUser.getPassword());
//            jsonObject.put(ParserJson.API_PARAMETER_DISTRICT, vtcModelUser.getUser_District());
//            jsonObject.put(ParserJson.API_PARAMETER_CITY, vtcModelUser.getUser_City());

            VtcModelUser.Address address = vtcModelUser.getAddress();

            if(address != null) {
                jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_NAME, address.getName());

                jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_LONG, address.getLongt());
                jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_LAT, address.getLatt());
            }

            if(vtcModelUser.getSkills() != null && vtcModelUser.getSkills().size() > 0){

                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < vtcModelUser.getSkills().size(); i++){

                    VtcModelUser.Skills skills = vtcModelUser.getSkills().get(i);

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put(ParserJson.API_PARAMETER_NAME, skills.getName());
                    jsonObject1.put(ParserJson.API_PARAMETER_ID, skills.get_id());

                    jsonArray.put(jsonObject1);
                }

                jsonObject.put(ParserJson.API_PARAMETER_SKILLS, jsonArray);

            }

            jsonObject.put(ParserJson.API_PARAMETER_TYPE_OS, Const.OS_TYPE);
            jsonObject.put(ParserJson.API_PARAMETER_DEVICE_ID, AppCore.getPreferenceUtil(getActivity()).getValueString(PreferenceUtil.DEVICE_ID));

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_REGISTER, RequestListener.API_CONNECTION_REGISTER, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initConfirmPassCode(RequestListener requestListener, String phoneNum, String passCode) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_PASSCODE, passCode);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_CONFIRM_PASSCODE, RequestListener.API_CONNECTION_PASSCODE, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initGetPassCode(RequestListener requestListener, String phoneNum, String strtyle) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ParserJson.API_PARAMETER_PHONE, phoneNum);
            jsonObject.put(ParserJson.API_PARAMETER_ACCOUNT_TYPE, Const.ACCOUNT_TYPE);
            jsonObject.put(ParserJson.API_PARAMETER_TYPE, strtyle);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_PASSCODE, RequestListener.API_CONNECTION_GET_PASSCODE, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * Get list Order
     *
     * */
    protected void initGetOrderLst(RequestListener requestListener, String offset, String type, String fieldId, StatusBookingJob statusBookingJob, String sortBy, boolean isShowProcess) {
        initGetOrderLst(requestListener, "", "20", offset, "", type, statusBookingJob.getValuesStatus(), "", "", fieldId, sortBy, isShowProcess);
    }

    /**
     *
     * Get list Order
     *
     * */
    protected void initGetOrderLst(RequestListener requestListener, StatusBookingJob statusBookingJob, String sortBy, boolean isShowProcess) {
        initGetOrderLst(requestListener, "", "20", "", "", "", statusBookingJob.getValuesStatus(), "", "", "", sortBy, isShowProcess);
    }

    /**
     *
     * Get list Order
     *
     * */
    protected void initGetOrderLst(RequestListener requestListener, String type, String fieldId, StatusBookingJob statusBookingJob, String sortBy) {
        initGetOrderLst(requestListener, "", "20", "", "", type, statusBookingJob.getValuesStatus(), "", "", fieldId, sortBy, true);
    }

    /**
     *
     * Get list Order
     *
     * */
    protected void initGetOrderLst(RequestListener requestListener, String booking, boolean isShowProcess) {
        initGetOrderLst(requestListener, "", "20", "", "", "", booking, "", "", "", "", isShowProcess);
    }


    /**
     *
     * Get list Order
     *
     * */

    protected void initGetOrderLst(RequestListener requestListener, String select, String limit, String offset, String order, String type, String status, String payment_method, String username, String fieldId, String sortBy, boolean isShowProcess) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        if(!select.equals("")) {
            map.put(ParserJson.API_PARAMETER_SELECT, select);
        }
        if(!limit.equals("")) {
            map.put(ParserJson.API_PARAMETER_LIMIT, limit);
        }
        if(!offset.equals("")) {
            map.put(ParserJson.API_PARAMETER_OFFSET, offset);
        }
        if(!order.equals("")) {
            map.put(ParserJson.API_PARAMETER_ORDER, order);
        }
        if(!type.equals("")) {
            map.put(ParserJson.API_PARAMETER_TYPE, type);
        }
        if(!status.equals("")) {
            map.put(ParserJson.API_PARAMETER_STATUS, status);
        }
        if(!payment_method.equals("")) {
            map.put(ParserJson.API_PARAMETER_PAYMENT_METHOD, payment_method);
        }
        if(!username.equals("")) {
            map.put(ParserJson.API_PARAMETER_USERNAME, username);
        }
        if(!fieldId.equals("")) {
            map.put(ParserJson.API_PARAMETER_FIELD, fieldId);
        }
        if(!sortBy.equals("")) {
            map.put(ParserJson.API_PARAMETER_SORT, sortBy);
        }

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_ORDER_LIST, RequestListener.API_CONNECTION_GET_ORDER_LIST + VtcNWConnection.urlEncodeUTF8(map), null, false, isShowProcess);
    }

    protected void initAcceptOrder(RequestListener requestListener, String idOrder, String idAgency) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_ORDER_ACCEPT, RequestListener.API_CONNECTION_AGENCY_ORDER + idAgency + RequestListener.API_CONNECTION_AGENCY_ORDER_APPLY + idOrder + VtcNWConnection.urlEncodeUTF8(map));

    }

    protected void initUpdateOrdeFinished(RequestListener requestListener, String idOrder, String idAgency) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_FINISHED, RequestListener.API_CONNECTION_AGENCY_ORDER + idAgency + RequestListener.API_CONNECTION_AGENCY_ORDER_FINISHED + idOrder + VtcNWConnection.urlEncodeUTF8(map));

    }

    protected void initUpdateOrdeCancel(RequestListener requestListener, String idOrder, String idAgency) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_CANCEL, RequestListener.API_CONNECTION_AGENCY_ORDER + idAgency + RequestListener.API_CONNECTION_AGENCY_ORDER_CANCEL + idOrder + VtcNWConnection.urlEncodeUTF8(map));

    }

    protected void initUpdateOrdeWorking(RequestListener requestListener, String idOrder, String idAgency) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_WORKING, RequestListener.API_CONNECTION_AGENCY_ORDER + idAgency + RequestListener.API_CONNECTION_AGENCY_ORDER_WORKING + idOrder + VtcNWConnection.urlEncodeUTF8(map));

    }

    protected void initUpdateOrdeComing(RequestListener requestListener, String idOrder, String idAgency) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_COMING, RequestListener.API_CONNECTION_AGENCY_ORDER + idAgency + RequestListener.API_CONNECTION_AGENCY_ORDER_COMING + idOrder + VtcNWConnection.urlEncodeUTF8(map));

    }

    protected void initUpdateIsWorking(RequestListener requestListener, String userID, boolean isWorking) {

        try {

            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ParserJson.API_PARAMETER_AVAILABLE, isWorking);

            getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_UPDATE_IS_WORKING, RequestListener.API_CONNECTION_AGENCY_ORDER + userID + RequestListener.API_CONNECTION_ORDER_AGENCY_UPDATE_STATUS + VtcNWConnection.urlEncodeUTF8(map), jsonObject, true, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initGetOrderCount(RequestListener requestListener) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_COUNT_SKILL, RequestListener.API_CONNECTION_GET_COUNT_SKILL + VtcNWConnection.urlEncodeUTF8(map), false);
    }


    protected void initSearchJob(RequestListener requestListener, String str, String limit, String offset, String status, boolean isShowProcess) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));
        map.put(ParserJson.API_PARAMETER_ADDRESS, str);
        map.put(ParserJson.API_PARAMETER_LIMIT, limit);
        map.put(ParserJson.API_PARAMETER_OFFSET, offset);
        map.put(ParserJson.API_PARAMETER_STATUS, status);

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_SEARCH, RequestListener.API_CONNECTION_ORDER_AGENCY_SEARCH + VtcNWConnection.urlEncodeUTF8(map), null, false, isShowProcess);

    }


    protected void initUpdateInfo(RequestListener requestListener, VtcModelUser vtcModelUserNew, VtcModelUser vtcModelUser, boolean isChangeLocation) {
        if (vtcModelUserNew != null && vtcModelUser != null) {

            Map<String, String> map = new HashMap<>();
            map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

            JSONObject jsonObject = new JSONObject();

            try {
                if (!vtcModelUserNew.getUser_full_name().equals(vtcModelUser.getUser_full_name())) {
                    jsonObject.put(ParserJson.API_PARAMETER_NAME, vtcModelUserNew.getUser_full_name());
                }
                if (!vtcModelUserNew.getUser_Phone_Num().equals(vtcModelUser.getUser_Phone_Num())) {
                    jsonObject.put(ParserJson.API_PARAMETER_PHONE, vtcModelUserNew.getUser_Phone_Num());
                }
                if (!vtcModelUserNew.getUser_Email().equals(vtcModelUser.getUser_Email())) {
                    jsonObject.put(ParserJson.API_PARAMETER_EMAIL, vtcModelUserNew.getUser_Email());
                }
                if (!vtcModelUserNew.getUser_CMTND().equals(vtcModelUser.getUser_CMTND())) {
                    jsonObject.put(ParserJson.API_PARAMETER_CMTND, vtcModelUserNew.getUser_CMTND());
                }
                if (!vtcModelUserNew.getUser_District().equals(vtcModelUser.getUser_District())) {
                    jsonObject.put(ParserJson.API_PARAMETER_DISTRICT, vtcModelUserNew.getUser_District());
                }
                if (!vtcModelUserNew.getUser_City().equals(vtcModelUser.getUser_City())) {
                    jsonObject.put(ParserJson.API_PARAMETER_CITY, vtcModelUserNew.getUser_City());
                }

                VtcModelUser.Address addressNew = vtcModelUserNew.getAddress();
                VtcModelUser.Address address = vtcModelUser.getAddress();

                if(address != null && addressNew != null) {

                    if (!addressNew.getName().equals(address.getName())) {
                        jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_NAME, address.getName());
                    }

                    if (isChangeLocation) {
                        jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_LONG, address.getLongt());
                        jsonObject.put(ParserJson.API_PARAMETER_ADDRESS_LAT, address.getLatt());
                    }
                }

               List<VtcModelUser.Skills> lstSkillNew = vtcModelUserNew.getSkills();

                if(lstSkillNew != null && lstSkillNew.size() > 0){

                    JSONArray jsonArray = new JSONArray();

                    for (int i = 0; i < lstSkillNew.size(); i++){

                        VtcModelUser.Skills skills = lstSkillNew.get(i);

                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put(ParserJson.API_PARAMETER_NAME, skills.getName());
                        jsonObject1.put(ParserJson.API_PARAMETER_ID, skills.get_id());

                        jsonArray.put(jsonObject1);
                    }

                    jsonObject.put(ParserJson.API_PARAMETER_SKILLS, jsonArray);

                }

                jsonObject.put(ParserJson.API_PARAMETER_TYPE_OS, Const.OS_TYPE);

                getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_UPDATE_AGENCY, RequestListener.API_CONNECTION_AGENCY_ORDER + vtcModelUserNew.getId() + RequestListener.API_CONNECTION_ORDER_AGENCY_UPDATE + VtcNWConnection.urlEncodeUTF8(map), jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    protected void initGetInfoJob(RequestListener requestListener, String orderId) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(getmActivity()).getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess(TypeActionConnection.TYPE_ACTION_ORDER_GET_DETAIL, RequestListener.API_CONNECTION_ORDER + orderId + RequestListener.API_CONNECTION_ORDER_AGENCY_GET + VtcNWConnection.urlEncodeUTF8(map), false);

    }

    protected static void initSocketOrderOffer(RequestListener requestListener) {

        getConnection(requestListener).onExcuteConnectSocket();

    }

    protected static void initSocketDisconnect(RequestListener requestListener) {

        getConnection(requestListener).onExcuteDisconnectSocket();

    }

    protected static void initSocketOffEvent(RequestListener requestListener) {

        getConnection(requestListener).onExcuteOffEventSocket();

    }

    protected static void initSocketOnEvent(RequestListener requestListener) {

        getConnection(requestListener).onExcuteOnEventSocket();

    }

    protected static void initSendRequestAccept(Socket socket, String id){
        try {
            JSONObject obj = new JSONObject();
            obj.put(ParserJson.API_PARAMETER_ORDER_ID, id);
            if (socket != null) {
                socket.emit(RequestListener.SOCKET_EVENT_ACCEPT_OFFER, obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initSendLocation(Socket socket, String agencyID, double lat, double longi){
        try {
            JSONObject obj = new JSONObject();
            obj.put(ParserJson.API_PARAMETER_LATITUDE, lat);
            obj.put(ParserJson.API_PARAMETER_LONGITUDE, longi);
            obj.put(ParserJson.API_PARAMETER_USER_ID, agencyID);
            if (socket != null) {
                socket.emit(RequestListener.SOCKET_EVENT_SEND_LOCATION, obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initGetNotificationList(RequestListener requestListener, String userID) {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN,
                AppCore.getPreferenceUtil(getmActivity())
                        .getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess
                (TypeActionConnection.TYPE_ACTION_NOTIFICATION_LIST,
                        RequestListener.API_CONNECTION_NOTIFICATION_LIST
                                + userID + VtcNWConnection.urlEncodeUTF8(map),
                        false);
    }

    public static void initSendRequestLogout(RequestListener requestListener) {
        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN,
                AppCore.getPreferenceUtil(getmActivity())
                        .getValueString(PreferenceUtil.AUTH_TOKEN));

        getConnection(requestListener).onExcuteProcess
                (TypeActionConnection.TYPE_ACTION_LOGOUT,
                        RequestListener.API_CONNECTION_LOGOUT
                                + VtcNWConnection.urlEncodeUTF8(map),
                        null, false, false);
    }


    @Override
    public void onPostExecuteError(TypeErrorConnection keyType, String msg) {

        switch (keyType) {

            case TYPE_CONNECTION_NOT_CONNECT_SERVER:
                initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "",
                        getmActivity().getResources().getString(R.string.confirm_server_not_found));
                break;
            case TYPE_CONNECTION_NO_INTERNET:
                initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_ENABLE_NETWORK);
                break;
            case TYPE_CONNECTION_TIMEOUT:
                initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "",
                        getmActivity().getResources().getString(R.string.confirm_server_timeout));
                break;
            case TYPE_CONNECTION_ERROR:
                initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "",
                        getmActivity().getResources().getString(R.string.confirm_server_error_connect));
                break;
            case TYPE_CONNECTION_ERROR_FROM_SERVER:
                if (!msg.equals("")) {
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", msg);
                }
                break;
            default:
                if (!msg.equals("")) {
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", msg);
                }
                break;
        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {

        switch (keyType) {
            case TYPE_ACTION_GET_LIST_FIELD:
                try {
                    JSONArray jsonArrayFields = new JSONArray(response);

                    parserResponseField(jsonArrayFields);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_ACTION_GET_LIST_COMMONINFO:
                parserResponseComoninfo(response);
                break;

            case TYPE_ACTION_LOGOUT:
                AppCore.getPreferenceUtil(getmActivity()).RemoveDataWhenLogOut();
                Intent i = new Intent(getmActivity().getApplicationContext(), MainScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getmActivity().getApplicationContext().startActivity(i);
                break;

            default:
                break;

        }
    }

    @Override
    public void onResultSocket(TypeActionConnection keyType, Socket socket, final String response) {
        final Activity activity = getmActivity();
        switch (keyType) {
            case TYPE_ACTION_SOCKET_ORDER_OFFER:
                if(activity != null) {
                    strResponseOrderFast = response;
                    initShowDialogOption(activity, TypeShowDialog.TYPE_SHOW_MESSAGE_NEW_JOB_FAST, socket, "", response);
                }
                break;
            case TYPE_ACTION_SOCKET_ORDER_ACCEPT:

                if(activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.optString(ParserJson.API_PARAMETER_MESSAGE);

                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(strResponseOrderFast != null && !strResponseOrderFast.isEmpty()) {
                                AppCore.getPreferenceUtil(activity).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, strResponseOrderFast);
                            }
                            AppCore.getPreferenceUtil(activity).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, false);

                            strResponseOrderFast = AppCore.getPreferenceUtil(activity).getValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST);

                            VtcModelJob vtcModelJob = VtcModelJob.initGetDataJson(strResponseOrderFast);

                            if(vtcModelJob != null) {
                                Bundle bundle = new Bundle();

                                bundle.putString(Const.KEY_BUNDLE_ACTION_ID, vtcModelJob.getId());

                                AppCore.CallFragmentSection(Const.UI_JOB_DETAIL, bundle);
                            }
                        }
                    });
                }

                break;

            case TYPE_ACTION_SOCKET_RECEIVE_LOCATION:

                if(activity != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String agencyID = jsonObject.optString(ParserJson.API_PARAMETER_USER_ID);

                        GPSTracker gpsTracker = getGpsTracker(activity);

                        initSendLocation(socket, agencyID, gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

            default:
                break;

        }
    }

    private void parserResponseComoninfo(String response) {
        try {
            if (!response.equals("")) {
                JSONArray jsonArrayTools = new JSONObject(response).optJSONArray(ParserJson.API_PARAMETER_TOOLS);
                parserResponseTools(jsonArrayTools);

                JSONArray jsonArrayFields = new JSONObject(response).optJSONArray(ParserJson.API_PARAMETER_FIELDS);
                parserResponseField(jsonArrayFields);

                JSONArray jsonArrayCity = new JSONObject(response).optJSONArray(ParserJson.API_PARAMETER_CITIES);
                parserResponseCities(jsonArrayCity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parserResponseTools(JSONArray jsonArrayTools) {
        if (jsonArrayTools != null && jsonArrayTools.length() > 0) {
            setVtcModelTools(VtcModelTools.getLstDataTools(jsonArrayTools));
        }
    }

    private void parserResponseField(JSONArray jsonArrayFields) {
        if (jsonArrayFields != null && jsonArrayFields.length() > 0) {

            setVtcModelFields(VtcModelFields.getLstDataFields(jsonArrayFields));
        }
    }

    private void parserResponseCities(JSONArray jsonArrayCity) {
        if (jsonArrayCity != null && jsonArrayCity.length() > 0) {

            setVtcModelCity(VtcModelCity.getLstDataCity(jsonArrayCity));
        }
    }
}
