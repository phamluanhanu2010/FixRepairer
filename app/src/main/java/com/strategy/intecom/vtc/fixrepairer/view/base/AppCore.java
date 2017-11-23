package com.strategy.intecom.vtc.fixrepairer.view.base;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.MainScreen;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelNoti;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.service.GcmListenerServices;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public abstract class AppCore extends AppCoreMap {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handlerReceiveMessageAction = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                initReceiveMessage(msg);
            }
        };
    }


    /**
     * Handle When receive message action
     * Push action to UI Similar
     *
     * @param handlerMessage
     */
    protected synchronized void initReceiveMessage(Message handlerMessage) {
        synchronized (this) {
            switch (handlerMessage.what) {

                case Const.UI_RECEIVE_NOTIFICATION:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkDrawOverlayPermission(getmActivity())) {
                            initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_NEW_JOB_FAST);
                        }
                    }
                    break;

                case Const.UI_SERVICE_LIST:
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_CHOICE_IMAGE);
                    break;
                case Const.UI_MY_JOB_FAST:
                    if(handlerMessage.obj instanceof String) {

                        initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", String.valueOf(handlerMessage.obj));
                    }
                    break;
                case Const.UI_NOTIFICATION_EXIST_JOB:
                    if(handlerMessage.obj instanceof String) {
                        Bundle bundle = new Bundle();

                        bundle.putString(Const.KEY_BUNDLE_ACTION_ID, String.valueOf(handlerMessage.obj));

                        AppCore.CallFragmentSection(Const.UI_JOB_DETAIL, bundle);
                    }
                    break;
                case Const.UI_NOTIFICATION_ACCEPT_JOB:
                    if (handlerMessage.obj instanceof String) {

                        AppCoreAPI.initSendRequestAccept(getConnection(this).getSocket(), String.valueOf(handlerMessage.obj));
                    }
                    break;
                case Const.UI_LOGOUT:
                    AppCoreAPI.initSendRequestLogout(AppCore.this);
                    break;

                case Const.UI_ACTION_NOTIFICATION:
                    if (handlerMessage.obj instanceof VtcModelNoti) {
                        initGotoFragmentFromNoti((VtcModelNoti) handlerMessage.obj);
                    }
                    break;
            }
        }
    }

    /**
     * Handle When receive message action notification goto from Noti
     *
     * @param vtcModelNoti obj noti data
     */
    public void initGotoFragmentFromNoti(VtcModelNoti vtcModelNoti) {

        if (vtcModelNoti != null) {

            int type = vtcModelNoti.getType();

            switch (type) {
                case GcmListenerServices.NOTI_REMIND_30:
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", vtcModelNoti.getMessage());
                    break;
                case GcmListenerServices.NOTI_NEW_JOB_FAST:
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", vtcModelNoti.getMessage());
                    break;
                case GcmListenerServices.NOTI_JOB_CENCEL:
                    AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");
                    cmdOnUserCancelJob();
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", vtcModelNoti.getMessage());
                    break;
                case GcmListenerServices.NOTI_FEEDBACK_CONFRIM:
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", vtcModelNoti.getMessage());
                    break;
                case GcmListenerServices.NOTI_MESSAGE_SYSTEM:
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", vtcModelNoti.getMessage());
                    break;
                case GcmListenerServices.NOTI_NEW_JOB_NOMAL:
                    initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_NEW_JOB_NORMAL, vtcModelNoti.getResponseData(), vtcModelNoti.getMessage());
                    break;
            }
        }
    }

    @Override
    public void cmdPressViewDetail(String response) {
        super.cmdPressViewDetail(response);

        try {
            VtcModelOrder vtcModelOrder = VtcModelOrder.getOrderData(new JSONObject(response));

            if(vtcModelOrder != null) {

                Bundle bundle = new Bundle();

                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ORDER, vtcModelOrder);

                AppCore.CallFragmentSection(Const.UI_JOB_DETAIL, bundle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Call when using set Format currency
     *
     * @param textView
     * @param strPrice
     */
    public static void setFormatCurrency(TextView textView, String strPrice){

        try {
            float price = Float.parseFloat(strPrice);

            textView.setText(Html.fromHtml(getmActivity().getResources().getString(R.string.title_currency, Utils.formatDecimal(price))));
        }catch (NullPointerException e){

            textView.setText(Html.fromHtml(getmActivity().getResources().getString(R.string.title_currency, strPrice)));
        }
    }

    /**
     * Call when using service call
     *
     * @param num Phone number
     */
    public void initCallIntentPhone(String num) {

        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
            startActivity(intent);
        } catch (SecurityException ex) {
            AppCore.showLog("initCallIntentPhone ----- : " + ex.getMessage());
        }
    }


    /**
     * Call when using service send message
     *
     * @param num  Phone number
     * @param body Body message
     */
    public void initCallIntentMessage(String num, String body) {

        try {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", num);
            smsIntent.putExtra("sms_body", body);
            startActivity(smsIntent);
        } catch (ActivityNotFoundException ex) {
            AppCore.showLog("initCallIntentMessage ----- : " + ex.getMessage());
            try {
                PendingIntent sentIntent = PendingIntent.getActivity(getmActivity(), 0, new Intent(getmActivity(), MainScreen.class), 0);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(num, null, body, sentIntent, null);
            } catch (SecurityException e) {
                AppCore.showLog("initCallIntentMessage ActivityNotFoundException ----- : " + e.getMessage());
            }
        }
    }

    /**
     * Feedback for Choose any app
     */
    public void CallSendEmailForChoose(String[] emailaddress, String content) {
        try {
            Intent sendmail = new Intent(Intent.ACTION_SEND);
            sendmail.setType("text/plain");
            sendmail.putExtra(Intent.EXTRA_EMAIL, emailaddress);
            sendmail.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.app_name));
            sendmail.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(sendmail, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
            AppCore.showLog(" CallSendEmailForChoose ActivityNotFoundException : " + ex.getMessage());
        }
    }

    /**
     * Feedback for Gmail
     */
    public void CallSendEmailForGmail(String emailaddress, String content) {
        try {
            Intent gmail = new Intent(Intent.ACTION_VIEW);
            gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            if (!emailaddress.equals(""))
                gmail.putExtra(Intent.EXTRA_EMAIL, emailaddress);
            gmail.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.app_name));
            gmail.setType("plain/text");
            gmail.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(gmail);
        } catch (ActivityNotFoundException ex) {
            AppCore.showLog(" CallSendEmailForGmail ActivityNotFoundException : " + ex.getMessage());
        }

//        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
//        intent.setType("message/rfc822");
//        intent.putExtra(Intent.EXTRA_EMAIL, emailaddress);
//        intent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.app_name));
//        intent.putExtra(Intent.EXTRA_TEXT, content);
//
//        startActivity(Intent.createChooser(intent, "Send Email"));
    }


    /**
     * code to post/handler request for permission
     */
    public final static int REQUEST_CODE = 101;

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkDrawOverlayPermission(Context context) {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(context)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(getmActivity())) {
                // continue here - permission was granted

                initShowDialogOption(getmActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_NEW_JOB_FAST);
            }
        }
    }
}
