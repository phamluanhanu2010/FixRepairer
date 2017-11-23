package com.strategy.intecom.vtc.fixrepairer.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.BuildConfig;
import com.strategy.intecom.vtc.fixrepairer.MainScreen;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMServiceList;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Mr. Ha on 5/23/16.
 */
@SuppressLint("ValidFragment")
public class UILogin extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private EditText edt_phone_num;
    private EditText edt_password;

    private TextView tv_register_now;
    private TextView tv_forgot_password;

    private ImageView img_back;

    private Button btn_login;

    public UILogin() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.ui_login, container, false);

        AppCore.initToolsBar(getActivity(), false);

        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControler(view);
    }


    private void initControler(View view) {

        edt_phone_num = (EditText) view.findViewById(R.id.edt_phone_num);
        edt_password = (EditText) view.findViewById(R.id.edt_password);

        tv_register_now = (TextView) view.findViewById(R.id.tv_register_now);
        tv_forgot_password = (TextView) view.findViewById(R.id.tv_forgot_password);

        img_back = (ImageView) view.findViewById(R.id.img_back);

        btn_login = (Button) view.findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
        img_back.setOnClickListener(this);

        tv_register_now.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
    }

    public String getEdt_phone_num() {
        return "+84" + (getTxtPhone().startsWith("0") ? getTxtPhone().substring(1, getTxtPhone().length()) : getTxtPhone());
    }

    public String getTxtPhone() {
        if (edt_phone_num == null) {
            return "";
        }
        return edt_phone_num.getText().toString().trim();
    }

    public String getEdt_password() {
        if (edt_password == null) {
            return "";
        }
        return edt_password.getText().toString() ;
    }

    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(getActivity());

        switch (v.getId()) {
            case R.id.btn_login:

                int idString = inValidateData();
                if (idString == 0) {
                    GPSTracker gpsTracker = getGpsTracker(getmActivity());
                    initGetLogin(UILogin.this, getEdt_phone_num(), getEdt_password(), gpsTracker.getLongitude(), gpsTracker.getLatitude());
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(idString));
                }

                break;

            case R.id.img_back:
                cmdBack();
                break;

            case R.id.tv_register_now:
                AppCore.CallFragmentSection(Const.UI_MY_REGISTER, true, false);
                break;

            case R.id.tv_forgot_password:
                AppCore.CallFragmentSection(Const.UI_MY_FORGOT_PASSWORD, true, false);
                break;

            default:
                return;
        }
    }

    private int inValidateData() {

        if (!Utils.validatePhoneNumber(getTxtPhone())) {
            edt_phone_num.requestFocus();
            return R.string.validate_phone_num;
        } else if (getEdt_password().equals("") || getEdt_password().length() < 6 || getEdt_password().length() > 32) {
            edt_password.requestFocus();
            return R.string.validate_password;
        }
        return 0;
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType){
            case TYPE_ACTION_LOGIN:

                try {
                    AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.AUTH_TOKEN, ParserJson.getAuthToken(response));

                    JSONObject jsonUser = new JSONObject(response);
                    VtcModelUser user = VtcModelUser.getDataPaserUser(jsonUser);
                    if (user != null) {

                        AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.USER_INFO, response);

                        AppCore.getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, user.isAvailable());

                        setVtcModelUser(user);

                        MainScreen.initShowInfo(getmActivity(), getVtcModelUser());

                        AppCore.CallFragmentSection(Const.UI_SERVICE_LIST, false, true);

                    } else {
                        AppCore.showLog("Login Parser User Error.....");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppCore.showLog("Login Json User Error..... : " + e.getMessage());
                }

                getmActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }

    @Override
    public void onPostExecuteError(TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyType, msg);
        switch (keyType){
            case TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE:

                initGetPassCode(UILogin.this, getEdt_phone_num(), Const.TYPE_PASSCODE_LOGIN);

                Bundle bundle = new Bundle();

                bundle.putString(Const.KEY_BUNDLE_ACTION_PHONE_NUM, getEdt_phone_num());
                bundle.putString(Const.KEY_BUNDLE_ACTION_TYPE_PASSCODE, Const.TYPE_PASSCODE_LOGIN);

                AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_PASSCODE, bundle, true, false);
                break;
        }
    }
}
