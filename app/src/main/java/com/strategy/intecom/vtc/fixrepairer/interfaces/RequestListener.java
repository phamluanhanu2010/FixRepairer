package com.strategy.intecom.vtc.fixrepairer.interfaces;

import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;

import io.socket.client.Socket;

/**
 * Created by Mr. Ha on 5/19/16.
 */
public interface RequestListener {

//    void onPostExecuteStart(TypeActionConnection keyType, String sApi);

    void onPostExecuteError(TypeErrorConnection keyType, String msg);

    void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message);

    void onResultSocket(TypeActionConnection keyType, Socket socket, String response);

    String SOCKET_EVENT_ORDER_OFFER = "server:send_order_offer";
    String SOCKET_EVENT_ACCEPT_RESULT = "server:accept_result";
    String SOCKET_EVENT_ACCEPT_OFFER = "agency:accept_offer";
    String SOCKET_EVENT_RECEIVE_LOCATION = "server:get_location";
    String SOCKET_EVENT_SEND_LOCATION = "agency:send_location";



    String API_CONNECTION_LOGIN = "/agency/login";
    String API_CONNECTION_REGISTER = "/agency/register";
    String API_CONNECTION_FORGOT_PASSWORD = "/forgotPassword";
    String API_CONNECTION_GET_COMMONINFO = "/getCommonInfo";
    String API_CONNECTION_GET_TOOLS_LIST = "/api/tool/getList";
    String API_CONNECTION_GET_FIELD_LIST = "/api/field/getList";
    String API_CONNECTION_GET_ORDER_LIST = "/api/order/listByAgency";
    String API_CONNECTION_GET_HIRE_LIST = "/api/hire/list";
    String API_CONNECTION_CREATE_HIRE = "/api/hire/create";
    String API_CONNECTION_PASSCODE = "/verify";
    String API_CONNECTION_GET_PASSCODE = "/getVerifyCode";
    String API_CONNECTION_FEEDBACK = "/api/feedback/";
    String API_CONNECTION_SEND_FEEDBACK = "/api/feedback/addFeedback";
    String API_CONNECTION_ORDER_AGENCY_SEARCH = "/api/order/searchByAddressName";
    String API_CONNECTION_CHANGE_PASSWORD = "/changePassword";
    String API_CONNECTION_GET_COUNT_SKILL = "/api/agency/getOrderCount";
    String API_CONNECTION_CHECK_PHONE_NUM = "/checkPhoneNumber";



//    "/api/agency/\(accountID)/updateStatus?auth_token=\(token)"
//    /api/order/:id/get
//    /api/agency/:id/update
//    /api/agency/:id/applyOrder/:orderId
    String API_CONNECTION_ORDER = "/api/order/";
    String API_CONNECTION_ORDER_AGENCY_APPLY = "/apply";
    String API_CONNECTION_ORDER_AGENCY_UPDATE_STATUS = "/updateStatus";
    String API_CONNECTION_ORDER_AGENCY_DELETE = "/delete";
    String API_CONNECTION_ORDER_AGENCY_UPDATE = "/update";
    String API_CONNECTION_ORDER_AGENCY_GET = "/get";

    String API_CONNECTION_AGENCY_ORDER_COMING = "/markAsComing/";
    String API_CONNECTION_AGENCY_ORDER_WORKING = "/markAsWorking/";
    String API_CONNECTION_AGENCY_ORDER_CANCEL = "/markAsCancel/";
    String API_CONNECTION_AGENCY_ORDER_FINISHED = "/markAsFinished/";
    String API_CONNECTION_AGENCY_ORDER_APPLY = "/applyOrder/";
    String API_CONNECTION_AGENCY_ORDER = "/api/agency/";
    String API_CONNECTION_UPLOAD_AVATAR = "/changeAvatar";

    String API_CONNECTION_FEEDBACK_MARK_READ = "/markAsReaded";
    String API_CONNECTION_NOTIFICATION_LIST = "/api/feedback/get/";
    String API_CONNECTION_LOGOUT = "/api/agency/logout";
}
