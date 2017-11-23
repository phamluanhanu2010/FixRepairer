package com.strategy.intecom.vtc.fixrepairer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.strategy.intecom.vtc.fixrepairer.dialog.DlChoicePhoto;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelJob;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelNoti;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.UtilsBitmap;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCoreBase;
import com.strategy.intecom.vtc.fixrepairer.view.UIInvite;
import com.strategy.intecom.vtc.fixrepairer.view.UISupport;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ultilLoad.MediaAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;


/**
 * Created by Mr. Ha on 5/16/16.
 */

public class MainScreen extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private NavigationView navigationView;
    private static ImageView img_avatar;
    private static TextView tv_profile_name;
    private static TextView tv_profile_des;
    private static ImageView img_rating;
    private ImageView img_edit_profile;

    private LinearLayout nav_job_history;
    private LinearLayout nav_job_list;
    private LinearLayout nav_message;
    private LinearLayout nav_invite;
    private LinearLayout nav_support;
    private LinearLayout nav_change_password;
    private LinearLayout nav_logout;

    private String FILE_PATH = "";
    private String FILE_NAME = "";

    private static DrawerLayout drawer;

    private List<String> items;

    boolean isStartChangeColor = false;
    boolean isStartUnChangeColor = false;
    boolean isChangeColor = true;

    public Bundle bundle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;

        AppCore.setmActivity(this);

        setContentView(R.layout.ui_main_activity);


        // Create APP_PATH folder
        if (isExternalStorageWritable()) {
            AppCoreBase.APP_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            AppCoreBase.APP_PATH = MainScreen.this.getCacheDir().getAbsolutePath();
        }

        File file = new File(AppCoreBase.APP_PATH, getAlbumName());
        if (!file.exists()) {
            file.mkdirs();
        }

        AppCoreBase.APP_PATH = file.getAbsolutePath();

        Utils.getHashKey(this);

        AppCore.getGpsTracker(this);

        initController();

        items = new ArrayList<>();

        String infoUser = AppCore.getPreferenceUtil(this).getValueString(PreferenceUtil.USER_INFO);

        if (infoUser.isEmpty()) {
            AppCore.CallFragmentSection(Const.UI_MY_LOGIN_CONFIRM, false, false);
        } else {
            try {

                JSONObject jsonUser = new JSONObject(infoUser);
                VtcModelUser user = VtcModelUser.getDataPaserUser(jsonUser);
                if (user != null) {

                    AppCore.setVtcModelUser(user);

                    MainScreen.initShowInfo(MainScreen.this, AppCore.getVtcModelUser());

                    AppCore.CallFragmentSection(Const.UI_SERVICE_LIST, false, true);

                } else {
                    AppCore.showLog("Login Parser User Error.....");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                AppCore.showLog("Login Json User Error..... : " + e.getMessage());
            }
        }

        int screen[] = Utils.getSizeScreen(this);

        AppCore.showLog("Width---------1------ : " + screen[0]);
        AppCore.showLog("Height--------1------- : " + screen[1]);

    }

    public void initController() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main_screen);
        img_avatar = (ImageView) headerLayout.findViewById(R.id.img_avatar);
        tv_profile_name = (TextView) headerLayout.findViewById(R.id.tv_profile_name);
        img_edit_profile = (ImageView) headerLayout.findViewById(R.id.img_edit_profile);
        tv_profile_des = (TextView) headerLayout.findViewById(R.id.tv_profile_des);
        img_rating = (ImageView) headerLayout.findViewById(R.id.img_rating);

        View contentLayout = navigationView.inflateHeaderView(R.layout.nav_content_main_screen);
        nav_job_history = (LinearLayout) contentLayout.findViewById(R.id.nav_job_history);
        nav_job_list = (LinearLayout) contentLayout.findViewById(R.id.nav_job_list);
        nav_message = (LinearLayout) contentLayout.findViewById(R.id.nav_message);
        nav_invite = (LinearLayout) contentLayout.findViewById(R.id.nav_invite);
        nav_support = (LinearLayout) contentLayout.findViewById(R.id.nav_support);
        nav_change_password = (LinearLayout) contentLayout.findViewById(R.id.nav_change_password);
        nav_logout = (LinearLayout) contentLayout.findViewById(R.id.nav_logout);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        initEventAction();
    }

    private void initEventAction() {

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST).isEmpty()) {
                    if (isChangeColor) {
                        if (slideOffset < 0.3f) {
                            isStartChangeColor = true;
                        } else if (slideOffset > 0.7f) {
                            isStartUnChangeColor = true;
                        }

                        if (isStartChangeColor) {
                            isStartChangeColor = false;
                            AppCore.initToolsBar(MainScreen.this, true);
                        } else if (isStartUnChangeColor) {
                            AppCore.initToolsBar(MainScreen.this, false);
                            isStartUnChangeColor = false;
                        }
                    }
                } else {
                    initCloseMenu();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isChangeColor = true;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        img_avatar.setOnClickListener(this);
        tv_profile_name.setOnClickListener(this);
        img_edit_profile.setOnClickListener(this);
        tv_profile_des.setOnClickListener(this);
        img_rating.setOnClickListener(this);

        nav_job_history.setOnClickListener(this);
        nav_job_list.setOnClickListener(this);
        nav_message.setOnClickListener(this);
        nav_invite.setOnClickListener(this);
        nav_support.setOnClickListener(this);
        nav_logout.setOnClickListener(this);
        nav_change_password.setOnClickListener(this);

        nav_job_history.setOnTouchListener(this);
        nav_job_list.setOnTouchListener(this);
        nav_message.setOnTouchListener(this);
        nav_invite.setOnTouchListener(this);
        nav_support.setOnTouchListener(this);
        nav_logout.setOnTouchListener(this);
        nav_change_password.setOnTouchListener(this);

        nav_job_history.setFocusableInTouchMode(true);
        nav_job_list.setFocusableInTouchMode(true);
        nav_message.setFocusableInTouchMode(true);
        nav_invite.setFocusableInTouchMode(true);
        nav_support.setFocusableInTouchMode(true);
        nav_logout.setFocusableInTouchMode(true);
        nav_change_password.setFocusableInTouchMode(true);
    }

    @Override
    public void onBackPressed() {
        if (!AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST).isEmpty() &&
                Const.UI_CURRENT_CONTEXT == Const.UI_JOB_DETAIL) {
            AppCore.initReceived(Const.UI_MY_JOB_FAST, this.getResources().getString(R.string.validate_check_exist_job));
        } else {
            if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                initCloseMenu();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    break;
//                case KeyEvent.KEYCODE_BACK:
//                    AppCore.getGpsTracker(MainScreen.this);
//                    break;
            }
        }

//        if (!AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST).isEmpty()) {
//            event = KeyEvent.changeFlags(event, 0);
//        }
        return super.onKeyUp(keyCode, event);
    }

    private void initCloseMenu() {
        if (drawer != null) {
            AppCore.initToolsBar(MainScreen.this, true);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void initOpenMenu() {
        if (drawer != null) {
            AppCore.initToolsBar(MainScreen.this, false);
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public static void initMenu(Activity activity) {
        if (drawer != null) {
            if (drawer.isActivated()) {
                AppCore.initToolsBar(activity, true);
                drawer.closeDrawer(GravityCompat.START);
            } else {
                AppCore.initToolsBar(activity, false);
                drawer.openDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_home:
                initOpenMenu();
                break;
            case R.id.img_avatar:

                Utils.hideKeyboard(MainScreen.this);
                initShowSelectPhoto(MainScreen.this);
                break;
            case R.id.tv_profile_name:

                initCloseMenu();
                AppCore.CallFragmentSection(Const.UI_USER_PROFILE_UPGRADE_LEVEL);
                break;

            case R.id.img_edit_profile:

                initCloseMenu();
//
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_USER, AppCore.getVtcModelUser());
//                AppCore.CallFragmentSection(Const.UI_MY_REGISTER, bundle, true, false);

                AppCore.CallFragmentSection(Const.UI_USER_PROFILE);
                break;

            case R.id.tv_profile_des:
                initCloseMenu();
                AppCore.CallFragmentSection(Const.UI_USER_PROFILE_UPGRADE_LEVEL);
                break;

            case R.id.img_rating:
                initCloseMenu();
                AppCore.CallFragmentSection(Const.UI_USER_PROFILE_UPGRADE_LEVEL);
                break;
            default:
                return;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            switch (v.getId()) {
                case R.id.nav_job_history:
                    isChangeColor = false;
                    AppCore.CallFragmentSection(Const.UI_MY_JOB_HISTORY);
                    break;
                case R.id.nav_job_list:
                    AppCore.CallFragmentSection(Const.UI_MY_JOB_LIST);
                    break;
                case R.id.nav_message:
                    AppCore.CallFragmentSection(Const.UI_NOTIFICATION);
                    break;
                case R.id.nav_invite:
                    callUIActivity(UIInvite.class);
                    break;
                case R.id.nav_support:
                    AppCore.CallFragmentSection(Const.UI_MY_SUPPORT);
                    break;
                case R.id.nav_change_password:
                    AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_CHANGE_PASSWORD);
                    break;
                case R.id.nav_logout:
                    AppCore.initReceived(Const.UI_LOGOUT);
                    this.finish();

                    break;
            }

            initCloseMenu();
        }
        return false;
    }

    private void callUIActivity(Class aClass) {
        startActivity(new Intent(this, aClass));
    }

    public static void initShowInfo(final Activity activity, final VtcModelUser vtcModelUser) {
        if(activity == null){
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (vtcModelUser != null) {

                    int sizeAvatar = (int) AppCore.getmActivity().getResources().getDimension(R.dimen.confirm_ui_button_option);

                    String avatar = AppCore.getPreferenceUtil(activity).getValueString(PreferenceUtil.USER_INFO_AVATAR);

                    ImageLoadAsync loadAsync = new ImageLoadAsync(activity, img_avatar, sizeAvatar, true);
                    loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, avatar);

                    tv_profile_name.setText(vtcModelUser.getUser_full_name());

                    initRate(vtcModelUser);
                }
            }
        });
    }

    private static void initRate(VtcModelUser vtcModelUser) {

        int level = vtcModelUser.getLevel();

        String sLV = "";
        switch (level) {
            case 1:
                // Thợ cấp 1
                sLV = AppCore.getmActivity().getResources().getString(R.string.title_rating_level_1);
                level = R.drawable.ic_level_1;
                break;

            case 2:
                // Thợ cấp 2
                sLV = AppCore.getmActivity().getResources().getString(R.string.title_rating_level_2);
                level = R.drawable.ic_level_2;
                break;

            case 3:
                // Thợ cấp 3
                sLV = AppCore.getmActivity().getResources().getString(R.string.title_rating_level_3);
                level = R.drawable.ic_level_3;
                break;

            case 4:
                // Thợ cấp 4
                sLV = AppCore.getmActivity().getResources().getString(R.string.title_rating_level_4);
                level = R.drawable.ic_level_4;
                break;

            case 5:
                // Thợ cấp 5
                sLV = AppCore.getmActivity().getResources().getString(R.string.title_rating_level_5);
                level = R.drawable.ic_level_5;
                break;
        }

        tv_profile_des.setText(sLV);
        img_rating.setBackgroundResource(level);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (bundle == null && this.getIntent() != null) {
            bundle = this.getIntent().getExtras();
        }
        String strData = AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST);
        if (strData.isEmpty()) {
            initGetDataBundle();
        } else {
            VtcModelJob vtcModelJob = VtcModelJob.initGetDataJson(strData);
            if (vtcModelJob != null) {
                AppCore.initReceived(Const.UI_NOTIFICATION_EXIST_JOB, String.valueOf(vtcModelJob.getId()));
            }else {
                initGetDataBundle();
            }
        }

        bundle = null;

        setIntent(null);
    }

    private void initGetDataBundle() {
        if (bundle != null) {
            int type = bundle.getInt("type");
            String id_order = bundle.getString("id_order");
            String message = bundle.getString("message");
            String responseData = bundle.getString("responseData");

            switch (type) {
                case -1001:
                    if (id_order != null && !id_order.isEmpty() && responseData != null && !responseData.isEmpty()) {
                        AppCore.getPreferenceUtil(MainScreen.this).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, responseData);
                        AppCore.initReceived(Const.UI_NOTIFICATION_ACCEPT_JOB, id_order);
                    }
                    break;
                default:

                    VtcModelNoti vtcModelNoti = new VtcModelNoti();
                    vtcModelNoti.setType(type);
                    vtcModelNoti.setMessage(message);
                    vtcModelNoti.setId_order(id_order);
                    vtcModelNoti.setResponseData(responseData);
                    vtcModelNoti.setTypeGoto(1);

                    AppCore.initReceived(Const.UI_ACTION_NOTIFICATION, vtcModelNoti);
                    break;
            }
        }
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
                dispatchPictureLibraryIntent(Const.REQUEST_CODE_LOAD_IMAGE_AVATAR);
            }

            @Override
            public void onClickGetCamera() {
                initGetCamera(Const.REQUEST_CODE_CAM_AVATAR, "avatar");
            }
        });
        dlTime.show();
    }

    protected void dispatchPictureLibraryIntent(int requestCode) {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), requestCode);

    }

    protected void initGetCamera(int requestCode, String path) {
        final File filetmp = new File(AppCoreBase.APP_PATH, path);
        if (!filetmp.exists()) {
            filetmp.mkdirs();
        }
        FILE_PATH = filetmp.getAbsolutePath();
        FILE_NAME = String.valueOf(System.currentTimeMillis()) + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(FILE_PATH, FILE_NAME);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, requestCode);
    }

    private boolean performCropImage(Uri mFinalImageUri, int hight, int width) {
        try {
            if (mFinalImageUri != null) {
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                cropIntent.setDataAndType(mFinalImageUri, "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", width);
                cropIntent.putExtra("aspectY", hight);
                cropIntent.putExtra("scaleUpIfNeeded", false);
                cropIntent.putExtra("outputX", width * 100);
                cropIntent.putExtra("outputY", hight * 100);
                cropIntent.putExtra("return-data", false);

                FILE_NAME = "CROP_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                File f = new File(FILE_PATH, FILE_NAME);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(cropIntent, Const.REQUEST_CODE_CROP_IMAGE);
                return true;
            }
        } catch (ActivityNotFoundException anfe) {
            return false;
        }
        return false;
    }

    private String getAlbumName() {
        Activity activity = MainScreen.this;
        if (activity == null) {
            return "";
        }
        return activity.getResources().getString(R.string.app_name);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case Const.REQUEST_CODE_CAM_AVATAR:
                    File fRequestCamAvatar = UtilsBitmap.isFileOnList(FILE_PATH, FILE_NAME);
                    if (fRequestCamAvatar.exists()) {
                        Uri picUri = Uri.fromFile(fRequestCamAvatar);
                        performCropImage(picUri, 3, 3);
                    }
                    break;
                case Const.REQUEST_CODE_LOAD_IMAGE_AVATAR:
                    if (data != null) {

                        FILE_PATH = UtilsBitmap.getRealPathFromURI(MainScreen.this, data.getData());

                        String[] strPath = FILE_PATH.split(File.separator);

                        FILE_NAME = strPath[strPath.length - 1];

                        FILE_PATH = FILE_PATH.replace(String.valueOf(File.separator + FILE_NAME), "");

                        if (FILE_PATH != null && !FILE_PATH.isEmpty()) {

                            File fLoadImageAvatar = UtilsBitmap.isFileOnList(FILE_PATH, FILE_NAME);
                            if (fLoadImageAvatar.exists()) {
                                Uri picUri = Uri.fromFile(fLoadImageAvatar);
                                performCropImage(picUri, 3, 3);
                            }
                        }
                    }

                    break;

                case Const.REQUEST_CODE_CROP_IMAGE:
                    try {

                        AppCore.initUpLoadAvatar(new RequestListener() {
                            @Override
                            public void onPostExecuteError(TypeErrorConnection keyType, String msg) {
                                Activity activity = MainScreen.this;

                                Toast.makeText(activity, activity.getResources().getString(R.string.update_profile_avatar_error), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {

                                Activity activity = MainScreen.this;
                                    String avatar = "";
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        avatar = jsonObject.optString(ParserJson.API_PARAMETER_AVATAR);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(!avatar.isEmpty()) {
                                        AppCore.getPreferenceUtil(activity).setValueString(PreferenceUtil.USER_INFO_AVATAR, avatar + AppCore.getExtraPathAvatar());

                                        MainScreen.initShowInfo(MainScreen.this, AppCore.getVtcModelUser());
                                    }

                                    Toast.makeText(activity, activity.getResources().getString(R.string.update_profile_complete), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResultSocket(TypeActionConnection keyType, Socket socket, String response) {

                            }
                        }, FILE_PATH + File.separator + FILE_NAME);
                    } catch (Exception e) {
                        AppCore.showLog("---------FILE_PATH----------" + e.getMessage());
                    }
                    break;
            }
        }
    }
}