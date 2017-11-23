package com.strategy.intecom.vtc.fixrepairer.view.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;
import com.strategy.intecom.vtc.fixrepairer.BuildConfig;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixrepairer.dialog.DLChoiceDistrictCity;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlChoicePhoto;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlListSkill;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlMessage;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlNewJob;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlSortedList;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelCity;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelTools;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;

import java.util.HashMap;
import java.util.List;

import io.socket.client.Socket;

/**
 * Created by Mr. Ha on 5/16/16.
 */
public class AppCoreBase extends Fragment {

    private static final String TAG = "AppCoreBase";

//    protected static VtcNWConnection connection;
public static Handler handlerReceiveMessageAction;

    public static String APP_PATH;

    public String strResponseOrderFast;

    protected AlertDialog alertDialogMesage;
    protected AlertDialog alertDialog;
    protected DlMessage alertDialogConfirmNetWork;

    private static FragmentActivity mActivity;

    private static VtcModelFields vtcModelFields;
    private static VtcModelTools vtcModelTools;
    private static VtcModelCity vtcModelCity;

    private static VtcModelUser vtcModelUser;

    private static PreferenceUtil preferenceUtil;

    public static PreferenceUtil getPreferenceUtil(Context context) {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtil(context);
        }
        return preferenceUtil;
    }

    public static void setmActivity(FragmentActivity mActivity) {
        AppCoreBase.mActivity = mActivity;
    }

    public static FragmentActivity getmActivity() {
        return mActivity;
    }


    public static VtcNWConnection getConnection(RequestListener requestListener) {
        return new VtcNWConnection(getmActivity(), requestListener);
    }


    public static GPSTracker getGpsTracker(Activity context) {
        return new GPSTracker(context);
    }

    public static VtcModelFields getVtcModelFields() {
        if(vtcModelFields == null){
            vtcModelFields = new VtcModelFields();
        }
        return vtcModelFields;
    }

    public static void setVtcModelFields(VtcModelFields vtcModelFields) {
        AppCoreBase.vtcModelFields = vtcModelFields;
    }

    public static List<VtcModelFields> getVtcFieldsParentList() {
        if(vtcModelFields != null && vtcModelFields.getLstFieldsParent() !=  null){
            return vtcModelFields.getLstFieldsParent();
        }
        return null;
    }

    public static HashMap<String, List<VtcModelFields>> getVtcFieldsChildList() {
        if(vtcModelFields != null && vtcModelFields.getLstFieldsChild() !=  null){
            return vtcModelFields.getLstFieldsChild();
        }
        return null;
    }

    public static VtcModelTools getVtcModelTools() {
        if(vtcModelTools == null){
            vtcModelTools = new VtcModelTools();
        }
        return vtcModelTools;
    }

    public static void setVtcModelTools(VtcModelTools vtcModelTools) {
        AppCoreBase.vtcModelTools = vtcModelTools;
    }

    public static List<VtcModelTools> getVtcToolsParentList() {
        if(vtcModelTools != null && vtcModelTools.getLstToolsParent() !=  null){
            return vtcModelTools.getLstToolsParent();
        }
        return null;
    }

    public static HashMap<String, List<VtcModelTools>> getVtcToolsChildList() {
        if(vtcModelTools != null && vtcModelTools.getLstToolsChild() !=  null){
            return vtcModelTools.getLstToolsChild();
        }
        return null;
    }


    public static VtcModelCity getVtcModelCity() {
        if(vtcModelCity == null){
            vtcModelCity = new VtcModelCity();
        }
        return vtcModelCity;
    }

    public static void setVtcModelCity(VtcModelCity vtcModelCity) {
        AppCoreBase.vtcModelCity = vtcModelCity;
    }

    public static List<VtcModelCity> getVtcCitiesList() {
        if(vtcModelCity != null && vtcModelCity.getLstCities() !=  null){
            return vtcModelCity.getLstCities();
        }
        return null;
    }

    public static HashMap<String, List<VtcModelCity>> getVtcDistrictLst() {
        if(vtcModelCity != null && vtcModelCity.getLstDistrict() !=  null){
            return vtcModelCity.getLstDistrict();
        }
        return null;
    }

    public static VtcModelUser getVtcModelUser() {
        if(vtcModelUser == null){
            vtcModelUser = new VtcModelUser();
        }
        return vtcModelUser;
    }

    public static void setVtcModelUser(VtcModelUser vtcModelUser) {
        AppCoreBase.vtcModelUser = vtcModelUser;
        getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_INFO_AVATAR, vtcModelUser.getUser_Avatar() + getExtraPathAvatar());
    }

    public static String getExtraPathAvatar(){
        return String.valueOf("?avatar_agency_time=" + String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bundle the send to data
     *
     */
    public static void CallFragmentSection(int KEY, Bundle bundle) {
        CallFragmentSection(KEY, bundle, true, true);
    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     *
     */
    public static void CallFragmentSection(int KEY) {
        CallFragmentSection(KEY, true);
    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param isAddBackStack add back stack yes or no
     *
     */
    public static void CallFragmentSection(int KEY, boolean isAddBackStack) {
        CallFragmentSection(KEY, null, isAddBackStack, true);
    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSection(int KEY, boolean isAddBackStack, boolean isAddFrag) {
        CallFragmentSection(KEY, null, isAddBackStack, isAddFrag);
    }


    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bunds put data using bunder
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSection(final int KEY, final Bundle bunds, final boolean isAddBackStack, final boolean isAddFrag) {
        try {
            Const.UI_CURRENT_CONTEXT = KEY;
            System.gc();
            FragmentTransaction fragmentTS = getmActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = VtcFragmentFactory.getFragmentByKey(KEY);
            if (fragment != null) {
                fragment.setArguments(bunds);
            }
            if (isAddFrag) {
                fragmentTS.add(R.id.fragcontent, fragment);
            } else {
                fragmentTS.add(R.id.fragcontent, fragment);
//                fragmentTS.add(android.R.id.content, fragment);
            }

            if (isAddBackStack) {
                if (fragment != null) {
                    fragmentTS.addToBackStack(fragment.getClass().getName());
                }
            }

            fragmentTS.commit();

        } catch (Exception e) {
            AppCoreBase.showLog(TAG + " Exception : " + e.getMessage());
        }
    }
    public static void CallFragmentSectionRemove(int KEY) {

        Fragment fragment = VtcFragmentFactory.getFragmentByKey(KEY);
        if(fragment != null)
            getmActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

    }

    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bundle put data using bunder
     * @param callback return data
     *
     */
    public static void CallFragmentSectionWithCallback(int KEY, Bundle bundle, Callback callback) {
        CallFragmentSectionWithCallback(KEY, bundle, callback, true, true);
    }

    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param callback return data
     *
     */
    public static void CallFragmentSectionWithCallback(int KEY, Callback callback) {
        CallFragmentSectionWithCallback(KEY, null, callback, true, true);
    }
    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param callback return data
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSectionWithCallback(int KEY, Callback callback, boolean isAddBackStack, boolean isAddFrag) {
        CallFragmentSectionWithCallback(KEY, null, callback, isAddBackStack, isAddFrag);
    }

    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bund put data using bunder
     * @param callback return data
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSectionWithCallback(final int KEY, final Bundle bund, final Callback callback, final boolean isAddBackStack, final boolean isAddFrag) {
        try {
            Const.UI_CURRENT_CONTEXT = KEY;
            System.gc();
            FragmentTransaction fragmentTS = getmActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = VtcFragmentFactory.getFragmentWithCallbackByKey(KEY, callback);
            if (fragment != null) {
                fragment.setArguments(bund);
            }
            if (isAddFrag) {
                fragmentTS.add(R.id.fragcontent, fragment);
            } else {
                fragmentTS.add(R.id.fragcontent, fragment);
//                fragmentTS.add(android.R.id.content, fragment);
            }

            if (isAddBackStack) {
                if (fragment != null) {
                    fragmentTS.addToBackStack(fragment.getClass().getName());
                }
            }
            fragmentTS.commit();

        } catch (Exception e) {
            AppCoreBase.showLog(TAG + " Exception : " + e.getMessage());
        }
    }

    /**
     * <p>Call when wan using dialog message, confirm, option..</p>
     *
     * <p>Type call Dialog</p>
     *
     * <p>TYPE_SHOW_MESSAGE_INFO</p>
     * <p>TYPE_SHOW_MESSAGE_CONFIRM</p>
     * <p>TYPE_SHOW_MESSAGE_INPUT</p>
     * <p>TYPE_SHOW_MESSAGE_NEW_JOB</p>
     * <p>TYPE_SHOW_CHOICE_CITY_DISTRICT</p>
     * <p>TYPE_SHOW_SHORTED_BY</p>
     * <p>TYPE_SHOW_CHOICE_IMAGE</p>
     * <p>TYPE_SHOW_CHOICE_SKILL</p>
     *
     * @param context context
     * @param typekey Key type call dialog
     *
     */
    public void initShowDialogOption(Context context, TypeShowDialog typekey){
        initShowDialogOption(context, typekey, "", "");
    }

    /**
     * <p>Call when wan using dialog message, confirm, option..</p>
     *
     * <p>Type call Dialog</p>
     *
     * <p>TYPE_SHOW_MESSAGE_INFO</p>
     * <p>TYPE_SHOW_MESSAGE_CONFIRM</p>
     * <p>TYPE_SHOW_MESSAGE_INPUT</p>
     * <p>TYPE_SHOW_MESSAGE_NEW_JOB</p>
     * <p>TYPE_SHOW_CHOICE_CITY_DISTRICT</p>
     * <p>TYPE_SHOW_SHORTED_BY</p>
     * <p>TYPE_SHOW_CHOICE_IMAGE</p>
     * <p>TYPE_SHOW_CHOICE_SKILL</p>
     *
     * @param context context
     * @param typekey Key type call dialog
     * @param title title dialog
     * @param msg content dialog
     *
     */

    /**
     * <p>Call when wan using dialog message, confirm, option..</p>
     *
     * <p>Type call Dialog</p>
     *
     * <p>TYPE_SHOW_MESSAGE_INFO</p>
     * <p>TYPE_SHOW_MESSAGE_CONFIRM</p>
     * <p>TYPE_SHOW_MESSAGE_INPUT</p>
     * <p>TYPE_SHOW_MESSAGE_NEW_JOB</p>
     * <p>TYPE_SHOW_CHOICE_CITY_DISTRICT</p>
     * <p>TYPE_SHOW_SHORTED_BY</p>
     * <p>TYPE_SHOW_CHOICE_IMAGE</p>
     * <p>TYPE_SHOW_CHOICE_SKILL</p>
     *
     * @param context context
     * @param typekey Key type call dialog
     * @param title title dialog
     * @param msg content dialog
     *
     */
    public void initShowDialogOption(Context context, TypeShowDialog typekey, String title, String msg) {
        initShowDialogOption(context, typekey, null, title, msg);
    }


    /**
     * <p>Call when wan using dialog message, confirm, option..</p>
     *
     * <p>Type call Dialog</p>
     *
     * <p>TYPE_SHOW_MESSAGE_INFO</p>
     * <p>TYPE_SHOW_MESSAGE_CONFIRM</p>
     * <p>TYPE_SHOW_MESSAGE_INPUT</p>
     * <p>TYPE_SHOW_MESSAGE_NEW_JOB</p>
     * <p>TYPE_SHOW_CHOICE_CITY_DISTRICT</p>
     * <p>TYPE_SHOW_SHORTED_BY</p>
     * <p>TYPE_SHOW_CHOICE_IMAGE</p>
     * <p>TYPE_SHOW_CHOICE_SKILL</p>
     *
     * @param context context
     * @param typekey Key type call dialog
     * @param socket socket
     * @param title title dialog
     * @param msg content dialog
     *
     */
    public void initShowDialogOption(Context context, TypeShowDialog typekey, Socket socket, String title, String msg){
        initShowDialogOption(context, typekey, socket, title, msg, null, null);
    }

    /**
     * <p>Call when wan using dialog message, confirm, option..</p>
     *
     * <p>Type call Dialog</p>
     *
     * <p>TYPE_SHOW_MESSAGE_INFO</p>
     * <p>TYPE_SHOW_MESSAGE_CONFIRM</p>
     * <p>TYPE_SHOW_MESSAGE_INPUT</p>
     * <p>TYPE_SHOW_MESSAGE_NEW_JOB</p>
     * <p>TYPE_SHOW_CHOICE_CITY_DISTRICT</p>
     * <p>TYPE_SHOW_SHORTED_BY</p>
     * <p>TYPE_SHOW_CHOICE_IMAGE</p>
     * <p>TYPE_SHOW_CHOICE_SKILL</p>
     *
     * @param context context
     * @param typekey Key type call dialog
     * @param socket socket
     * @param title title dialog
     * @param msg content dialog
     * @param listChoice List skill choice
     *
     */
    public void initShowDialogOption(Context context, TypeShowDialog typekey, Socket socket, String title, String msg, List<VtcModelFields> listChoice, HashMap<String, List<VtcModelFields>> listDataChild){

        int key = typekey.getValuesTypeDialog();

        switch (TypeShowDialog.forValue(key)){
            case TYPE_SHOW_MESSAGE_INFO:
                initShowMessageInfo(context, title, msg);
                break;

            case TYPE_SHOW_MESSAGE_CONFIRM:
                initShowMessageAlert(context, title, msg);
                break;

            case TYPE_SHOW_MESSAGE_INPUT:

                break;

            case TYPE_SHOW_MESSAGE_NEW_JOB_FAST:
                initShowNewJobDiaLog(context, socket, title, msg);
                break;

            case TYPE_SHOW_CHOICE_CITY_DISTRICT:
                initShowChoiceCityDistrict(context);
                break;

            case TYPE_SHOW_SHORTED_BY:
                initShowShorted(context);
                break;

            case TYPE_SHOW_CHOICE_IMAGE:
                initShowSelectPhoto(context);
                break;

            case TYPE_SHOW_CHOICE_SKILL:
                initShowChoiceSkill((Activity) context, listChoice, listDataChild);
                break;

            case TYPE_SHOW_ENABLE_NETWORK:
                initShowMessageConfirmNetwork((Activity) context);
                break;

            case TYPE_SHOW_MESSAGE_NEW_JOB_NORMAL:
                initShowMessageNewJobNomal(context, msg, title);
                break;
        }
    }

    /**
     *
     * Called when Choice skill
     * Show Dialog choice choice kill
     *
     */

    private void initShowChoiceSkill(final Activity context, List<VtcModelFields> listChoice, HashMap<String, List<VtcModelFields>> listDataChild) {

        int screen[] = Utils.getSizeScreen(context);

        DlListSkill dlListSkill = new DlListSkill(context, listChoice, listDataChild, screen[0]);

        dlListSkill.setOnClickButton(new DlListSkill.onClickButton() {

            @Override
            public void onClickButton(List<VtcModelFields> fieldsList) {
                cmdOnSetSkill(fieldsList);
            }

            @Override
            public void onClickButtonCancel() {

            }
        });

        dlListSkill.show();
    }


    /**
     * Called when message is received.
     * Show Dialog choice City, District
     *
     */
    private void initShowChoiceCityDistrict(final Context context) {

        DLChoiceDistrictCity districtCity = new DLChoiceDistrictCity(context, getVtcCitiesList(), getVtcDistrictLst());

        districtCity.setOnClickChoiceSelect(new DLChoiceDistrictCity.onClickChoiceSelect() {
            @Override
            public void onClickChoice(String cities, String district) {
                cmdOnClickChoiceCity(cities, district);
            }
        });

        districtCity.show();
    }

    /**
     * Called when message is received.
     * Show Dialog choice Shorted by
     *
     */
    private void initShowShorted(final Context context) {

        int screen[] = Utils.getSizeScreen(getmActivity());

        DlSortedList dlSortedList = new DlSortedList(context, screen[0]);

        dlSortedList.setOnClickDialogItem(new DlSortedList.onClickDialogItem() {
            @Override
            public void onClickDialogItem(String charSort) {
                cmdOnClickSort(charSort);
            }
        });


        dlSortedList.show();
    }

    /**
     *
     *  Call dialog when select photo
     *
     * @param context
     *
     */
    public void initShowSelectPhoto(Context context){

        int []size = Utils.getSizeScreen((Activity) context);
        DlChoicePhoto dlTime = new DlChoicePhoto(context, size[0]);
        dlTime.setOnClickDialogPhoto(new DlChoicePhoto.IActionDialogPhoto() {
            @Override
            public void onClickGetPictureLibrary() {
                cmdOnLibrary();
            }

            @Override
            public void onClickGetCamera() {
                cmdOnCapture();
            }
        });
        dlTime.show();
    }


    /**
     * Called when message is received.
     * Show alert message
     *
     * @param msContent content alert.
     * @param sTitle title of dialog.
     *
     */

    private Handler mHandler;
    private static DlNewJob dlNewJob;

    private void initShowNewJobDiaLog(final Context context, final Socket socket, final String sTitle, final String msContent) {

        if(context == null){
            return;
        }
        if(mHandler != null){
            mHandler = null;
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (dlNewJob != null) {

                    if (dlNewJob.isShowing()) {
                        dlNewJob.cancel();
                    }
                    dlNewJob = null;
                }
                dlNewJob = new DlNewJob(context, msContent);

                dlNewJob.setOnClickButton(new DlNewJob.onClickButton() {
                    @Override
                    public void onClickButton(String id) {

                        AppCoreAPI.initSendRequestAccept(socket, id);
                    }

                    @Override
                    public void onClickButtonCancel() {

                    }
                });

                dlNewJob.show();

                mHandler = null;
            }

        };

        if (mHandler != null) {
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
        }
    }

    /**
     * Call dialog when show dialog message info
     *
     * @param context context
     * @param smg content message
     * @return Dialog
     *
     */

    private void initShowMessageInfo(Context context, String title, String smg){

        if(context == null){
            return;
        }
        if(alertDialogMesage != null && alertDialogMesage.isShowing()){
            alertDialogMesage.dismiss();
            alertDialogMesage = null;
        }

        alertDialogMesage = new AlertDialog.Builder(context)
                .setTitle(title.equals("") ? context.getResources().getString(R.string.title_dialog_message) : title)
                .setMessage(smg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogYesInfo();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    /**
     * Call dialog when show dialog message Alert
     *
     * @param context
     * @param smg
     * @return Dialog
     *
     */
    private void initShowMessageAlert(Context context, String title, String smg){

        if(context == null){
            return;
        }
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
            alertDialog = null;
        }

        alertDialog = new AlertDialog.Builder(context)
                .setTitle(title.equals("") ? context.getResources().getString(R.string.title_dialog_confirm) : title)
                .setMessage(smg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogYes("");
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogNo();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    /**
     * Call dialog when show dialog message Alert
     *
     * @param context
     * @param smg
     * @return Dialog
     *
     */
    private void initShowMessageNewJobNomal(Context context, String smg, final String response){

        if(context == null){
            return;
        }
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
            alertDialog = null;
        }


        alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.title_dialog_confirm))
                .setMessage(smg)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_job_detail, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressViewDetail(response);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    /**
     * Call dialog when show dialog message Alert setting network and GPS
     *
     * @param context
     * @return Dialog
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("NewApi")
    public void initShowMessageConfirmNetwork(final Activity context) {
        if(context == null){
            return;
        }
        if (!getGpsTracker(context).isConnection()) {

            if (alertDialogConfirmNetWork != null && alertDialogConfirmNetWork.isShowing()) {
                alertDialogConfirmNetWork.dismiss();
                alertDialogConfirmNetWork = null;
            }

            int screen[] = Utils.getSizeScreen(context);

            alertDialogConfirmNetWork = new DlMessage(context, context.getResources().getString(R.string.gps_network_not_enabled), screen[0]);

            alertDialogConfirmNetWork.setOnClickDialogItem(new DlMessage.onClickDialogItem() {
                @Override
                public void onClickDialogItemCancel() {
                    alertDialogConfirmNetWork.dismiss();
                }

                @Override
                public void onClickDialogItemEnableWifi() {
                    alertDialogConfirmNetWork.dismiss();
                    Intent myIntent = new Intent(Settings.ACTION_SETTINGS);
                    getmActivity().startActivity(myIntent);
                }

                @Override
                public void onClickDialogItemEnableGPS() {
                    alertDialogConfirmNetWork.dismiss();
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getmActivity().startActivity(myIntent);
                }
            });

            alertDialogConfirmNetWork.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (!AppCore.getGpsTracker(getmActivity()).isConnection()) {
                        getmActivity().finish();
                    }else {
                        cmdOnRefreshLocation();
                    }
                }
            });
            alertDialogConfirmNetWork.show();
        }
    }


    public static boolean initReceived(int key) {
       return initReceived(key, -1, null, -1);
    }

    public static boolean initReceived(int key, int arg1) {
        return initReceived(key, arg1, null, -1);
    }

    public static boolean initReceived(int key, Object object) {
       return initReceived(key, -1, object, -1);
    }

    public static boolean initReceived(int key, int arg1, Object object) {
       return initReceived(key, arg1, object, -1);
    }

    public static boolean initReceived(int key, int arg1, int arg2) {
        return initReceived(key, arg1, null, arg2);
    }

    public static boolean initReceived(int key, int arg1, Object object, int arg2) {
        if(AppCore.handlerReceiveMessageAction != null){
            android.os.Message msg = AppCore.handlerReceiveMessageAction.obtainMessage();
            msg.what = key;
            msg.obj = object;
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            AppCore.handlerReceiveMessageAction.sendMessage(msg);
            return true;
        }
        return false;
    }

    /**
     *
     * Show Log toast
     * Enable when DEBUG is true
     *
     * @param view
     * @param msg content message
     *
     */
    public static void showLogToast(View view, String msg){
        if(BuildConfig.DEBUG) {
//            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }


    /**
     *
     * Show toast Long for message
     *
     * @param view
     * @param msg content message
     *
     */
    public static void showToastLong(View view, String msg){
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    /**
     *
     * Show toast Short for message
     *
     * @param view
     * @param msg content message
     *
     */
    public static void showToastShort(View view, String msg){
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


    /**
     *
     * Show Log Bug
     * Enable when DEBUG is true
     *
     * @param msg content log
     *
     */
    public static void showLog(String msg){
        if(BuildConfig.DEBUG){
            Log.i("Ha : ", "----------------- : " + msg);
        }
    }

    /**
     *
     * Show Log Bug for Tag
     * Enable when DEBUG is true
     *
     * @param tag Tag Name
     * @param msg content log
     *
     */
    public static void showLog(String tag, String msg){
        if(BuildConfig.DEBUG){
            Log.i(tag, "----------------- : " + msg);
        }
    }


    /**
     *
     * Call when action on message info yes
     *
     * @param str return data
     *
     */
    public void cmdPressDialogYes(TypeShowDialog typeShowDialog, String str){

    }

    /**
     *
     * Call when action on message info yes
     *
     */
    public void cmdPressDialogYesInfo(){

    }

    /**
     *
     * Call when action on message info yes
     *
     */
    public void cmdPressDialogYes(String response){

    }

    /**
     *
     * Call when action View Detail job
     *
     */
    public void cmdPressViewDetail(String response){

    }

    /**
     *
     * Call when action on message info No
     *
     */
    public void cmdPressDialogNo(){

    }

    /**
     *
     * Call when Reload data list Feilds
     *
     */
    protected void cmdReloadField(){

    }

    /**
     *
     * Call when action Set Skill
     *
     */
    protected void cmdOnSetSkill(List<VtcModelFields> fieldsList){

    }

    /**
     *
     * Call when action Click Marker Map
     *
     */
    protected void cmdOnClickMarkerMap(VtcModelOrder vtcModelOrder){

    }

    /**
     *
     * Call when action Click Choice City, District
     *
     */
    protected void cmdOnClickChoiceCity(String city, String district){

    }

    /**
     *
     * Call when action Click Sort
     *
     */
    protected void cmdOnClickSort(String charSort){

    }

    /**
     *
     * Call when action Choice Image
     *
     */
    protected void cmdClickDialogItemImage(String url){

    }

    /**
     *
     * Call when action Get location
     *
     */
    protected void cmdOnGetLocation(LatLng destPosition){

    }

    /**
     *
     * Call when action User Cancel status Job
     *
     */
    protected void cmdOnUserCancelJob(){

    }

    /**
     *
     * Call when action Refresh Location
     *
     */
    protected void cmdOnRefreshLocation(){

    }

    /**
     *
     * Call when action Click Map
     *
     */
    protected void cmdOnClickMap(boolean isClick){

    }

    /**
     *
     * Call when action Click View Detail map
     *
     */
    protected void cmdOnClickViewDetail(){

    }

    /**
     *
     * Call when action Capture
     *
     */
    protected void cmdOnCapture(){

    }
    /**
     *
     * Call when action Choice Library
     *
     */
    protected void cmdOnLibrary(){

    }



    /**
     *
     * Call when back screen
     *
     */
    public void cmdBack(){
        getmActivity().getSupportFragmentManager().popBackStack();
    }

    public static void initToolsBar(final Activity context, final boolean isTrue){
        if(context == null){
            return;
        }

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Window window = context.getWindow();

                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    // finally change the color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if(isTrue) {
                            window.setStatusBarColor(context.getResources().getColor(R.color.color_yellow));
                        }else {
                            window.setStatusBarColor(context.getResources().getColor(android.R.color.transparent));
                        }
                    }
                }
            }
        });
    }
}
