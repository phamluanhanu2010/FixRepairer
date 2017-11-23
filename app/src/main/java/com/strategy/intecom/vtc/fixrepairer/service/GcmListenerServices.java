package com.strategy.intecom.vtc.fixrepairer.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmListenerService;
import com.strategy.intecom.vtc.fixrepairer.MainScreen;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlNewJob;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelNoti;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCoreBase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Ha on 5/17/16.
 */
public class GcmListenerServices extends GcmListenerService {

    public final static int NOTI_REMIND_30 = 1001;               // Thông báo khi sắp đến thời gian làm việc
    public final static int NOTI_NEW_JOB_FAST = 1002;            // Nhận được thông báo khi có job mới
    public final static int NOTI_FEEDBACK_CONFRIM = 2002;        // Thông báo hệ thống đã nhận được feedback
    public final static int NOTI_MESSAGE_SYSTEM = 3001;          // Các thông báo khác của System
    public final static int NOTI_JOB_CENCEL = 3002;              // Thông báo Job bị User hủy
    public final static int NOTI_NEW_JOB_NOMAL = 1004;           // Nhận được thông báo khi có việc đặt lịch mới

    public GcmListenerServices(){

    }

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String notification_type = data.getString("notification_type");
        String title = data.getString("gcm.notification.title");
        String body = data.getString("gcm.notification.body");
        String responseData = data.getString("responseData");
        String badge = data.getString("gcm.notification.badge");

        AppCoreBase.showLog(" onMessageReceived -----from--- : " + from + ":--body---:" + body + ":--title---:" + title + " --------- : " + responseData);

        wakeUpScreenDevice();

        String sID = "";
        int type = 0;

        if (notification_type != null && !notification_type.isEmpty()) {

            try {

                type = Integer.parseInt(notification_type);

            } catch (NumberFormatException e) {

            }
            try {
                JSONObject jsonObject = new JSONObject(responseData);

                JSONObject orderObj = jsonObject.optJSONObject(ParserJson.API_PARAMETER_RESSPONSE_DATA);

                if (orderObj == null) {
                    orderObj = jsonObject;
                }

                sID = orderObj.optString(ParserJson.API_PARAMETER_ID_);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (type) {

                case NOTI_NEW_JOB_FAST:
                    initShowNewJobDiaLog(type, title, body, sID, responseData);
                    break;
                case NOTI_JOB_CENCEL:
                case NOTI_REMIND_30:
                case NOTI_FEEDBACK_CONFRIM:
                case NOTI_MESSAGE_SYSTEM:
                case NOTI_NEW_JOB_NOMAL:
                default:

                    try {
                        VtcModelNoti vtcModelNoti = new VtcModelNoti();
                        vtcModelNoti.setType(type);
                        vtcModelNoti.setMessage(body);
                        vtcModelNoti.setId_order(sID);
                        vtcModelNoti.setResponseData(responseData);
                        vtcModelNoti.setTypeGoto(1);
                        AppCore.initReceived(Const.UI_ACTION_NOTIFICATION, vtcModelNoti);

                    } catch (ExceptionInInitializerError error) {
                        error.printStackTrace();
                    } catch (NoClassDefFoundError e) {
                        e.printStackTrace();
                    }
                    initShowNotification(type, title, body, sID, responseData);
                    break;
            }
        }
    }

    /**
     *
     *  Show Big content Notification
     * @param msg -->> Message
     *
     **/
    public void initShowNotification(int type, String title, String msg, String idOrder, String responseData){

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext()).setAutoCancel(true)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon1).setContentText(msg);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(msg);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText("Por: " + msg);
        mBuilder.setStyle(bigText);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getApplicationContext(),
                MainScreen.class);
        resultIntent.putExtra("type", type);
        resultIntent.putExtra("message", msg);
        resultIntent.putExtra("responseData", responseData);
        resultIntent.putExtra("id_order", idOrder);

        // The stack builder object will contain an artificial back
        // stack for
        // the
        // started Activity.
        // getApplicationContext() ensures that navigating backward from
        // the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder
                .create(getApplicationContext());

        // Adds the back stack for the Intent (but not the Intent
        // itself)
        stackBuilder.addParentStack(MainScreen.class);

        // Adds the Intent that starts the Activity to the top of the
        // stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(100, mBuilder.build());
    }

    /**
     *
     * Call when received message priority
     *
     * Wake up screen display message
     *
     */
    private void wakeUpScreenDevice(){

        PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        screenLock.release();
    }

    private Handler mHandler;
    private static DlNewJob dlNewJob;

    /**
     * Called when message is received.
     * Show alert message
     *
     * @param msg Obj New job.
     *
     */
    private void initShowNewJobDiaLog(final int type, final String title, final String msg, final String idOrder, final String responseData) {
        try {

            final Context context = getApplication();

            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    if (dlNewJob == null) {
                        dlNewJob = new DlNewJob(context, responseData);

                        dlNewJob.setOnClickButton(new DlNewJob.onClickButton() {
                            @Override
                            public void onClickButton(String id) {

                                Intent resultIntent = new Intent(getApplicationContext(),
                                        MainScreen.class);
                                resultIntent.putExtra("type", -1001);
                                resultIntent.putExtra("message", msg);
                                resultIntent.putExtra("responseData", responseData);
                                resultIntent.putExtra("id_order", id);

                                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(resultIntent);
                            }

                            @Override
                            public void onClickButtonCancel() {

//                                initShowNotification(type, title, msg, idOrder, responseData);
                            }
                        });
                    } else {
                        if (dlNewJob.isShowing()) {
                            dlNewJob.cancel();
                        }
                    }

                    dlNewJob.show();

                    mHandler = null;

                }

            };

            if (mHandler != null) {
                Message message = mHandler.obtainMessage();
                message.sendToTarget();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onDeletedMessages() {
    }

    @Override
    public void onMessageSent(String msgId) {
        AppCoreBase.showLog(" onMessageSent -------- : " + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
    }
}
