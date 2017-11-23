package com.strategy.intecom.vtc.fixrepairer.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.BuildConfig;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtCityLst;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelCity;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/17/16.
 */
public class UIConfirmInfoProfile extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private ImageView img_back;
    private Button btn_register;

    private EditText edt_full_name;
    private EditText edt_cmtnd;
    private TextView edt_address;
    private EditText edt_phone_num;
    private EditText edt_email;
    private EditText edt_password;
    private EditText edt_re_password;

    private Callback callback;

    private VtcModelUser vtcModelUser;
    private VtcModelAddress vtcModelAddress;

    private boolean isUpdateProfile = Boolean.FALSE;

    @SuppressLint("ValidFragment")
    public UIConfirmInfoProfile(Callback callback) {
        this.callback = callback;
    }

    public UIConfirmInfoProfile() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        savedInstanceState = getArguments();

        if(savedInstanceState != null){

            vtcModelUser = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_USER);

            if(vtcModelUser != null) {

                isUpdateProfile = Boolean.TRUE;
            }
        }

        AppCore.initToolsBar(getActivity(), false);
        viewRoot = inflater.inflate(R.layout.ui_confirm_signin_info, container, false);

        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getVtcModelFields().getLstFieldsParent().size() <= 0 || getVtcModelTools().getLstToolsParent().size() <= 0 || getVtcModelCity().getLstCities().size() <= 0) {
            initGetCommonInfo(UIConfirmInfoProfile.this);
        }

        initController(view);
    }


    private void initController(View view) {

        init();

        img_back = (ImageView) view.findViewById(R.id.img_back);
        btn_register = (Button) view.findViewById(R.id.btn_register);

        edt_full_name = (EditText) view.findViewById(R.id.edt_full_name);
        edt_cmtnd = (EditText) view.findViewById(R.id.edt_cmtnd);
        edt_address = (TextView) view.findViewById(R.id.edt_address);
        edt_phone_num = (EditText) view.findViewById(R.id.edt_phone_num);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_password = (EditText) view.findViewById(R.id.edt_password);
        edt_re_password = (EditText) view.findViewById(R.id.edt_re_password);

        if(isUpdateProfile){
            edt_password.setVisibility(EditText.GONE);
            edt_re_password.setVisibility(EditText.GONE);

            if(AppCore.getVtcModelUser() != null && AppCore.getVtcModelUser().getAddress() != null){
                VtcModelUser.Address address = AppCore.getVtcModelUser().getAddress();

                double longt = 0.0f;
                double lat = 0.0f;

                try {
                    longt = Double.parseDouble(address.getLongt());
                    lat = Double.parseDouble(address.getLatt());
                }catch (NumberFormatException e){

                }
                if(vtcModelAddress == null){
                    vtcModelAddress = new VtcModelAddress();
                }
                vtcModelAddress.setLongitude(longt);
                vtcModelAddress.setLatitude(lat);
            }

        }else {
            edt_password.setVisibility(EditText.VISIBLE);
            edt_re_password.setVisibility(EditText.VISIBLE);
        }

        initEvent();
    }

//    @Override
//    protected void cmdOnRefreshLocation() {
//        super.cmdOnRefreshLocation();
//        init();
//    }

    private void init(){

        if(vtcModelUser == null) {
            vtcModelUser = new VtcModelUser();
        }

        GPSTracker gpsTracker = getGpsTracker(getActivity());

        vtcModelUser.getAddress().setLongt(String.valueOf(gpsTracker.getLongitude()));
        vtcModelUser.getAddress().setLatt(String.valueOf(gpsTracker.getLatitude()));
    }

    private void initEvent() {

        edt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ADDRESS, vtcModelAddress);
                AppCore.CallFragmentSectionWithCallback(Const.UI_SEARCH_PIN_MAP, bundle, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {
                        if(t != null && t.length > 0 && t[0] instanceof VtcModelAddress){
                            vtcModelAddress = (VtcModelAddress) t[0];
                            edt_address.setText(vtcModelAddress.getAddress());
                            VtcModelUser.Address address = new VtcModelUser.Address();
                            address.setName(vtcModelAddress.getAddress());
                            address.setLatt(String.valueOf(vtcModelAddress.getLatitude()));
                            address.setLongt(String.valueOf(vtcModelAddress.getLongitude()));

                            vtcModelUser.setAddress(address);
                        }
                    }
                });
            }
        });

        btn_register.setOnClickListener(this);

        img_back.setOnClickListener(this);

        initData();
    }

    private void initData() {

        if(isUpdateProfile && vtcModelUser != null){
            edt_full_name.setText(vtcModelUser.getUser_full_name());
            edt_cmtnd.setText(vtcModelUser.getUser_CMTND());
            edt_address.setText(vtcModelUser.getAddress() == null ? "" : vtcModelUser.getAddress().getName());
            edt_phone_num.setText(getEdt_phone_num(vtcModelUser.getUser_Phone_Num()));
            edt_email.setText(vtcModelUser.getUser_Email());
        }
    }

    public String getEdt_full_name() {
        if(edt_full_name == null){
            return "";
        }
        return edt_full_name.getText().toString().trim();
    }

    public String getEdt_cmtnd() {
        if(edt_cmtnd == null){
            return "";
        }
        return edt_cmtnd.getText().toString().trim();
    }

    public String getEdt_address() {
        if(edt_address == null){
            return "";
        }
        return edt_address.getText().toString().trim();
    }

    public String getTxtPhone() {
        if(edt_phone_num == null){
            return "";
        }
        return edt_phone_num.getText().toString().trim();
    }

    public String getEdt_phone_num() {
        return "+84" + (getTxtPhone().startsWith("0") ? getTxtPhone().substring(1, getTxtPhone().length()) : getTxtPhone());
    }

    public String getEdt_phone_num(String strNumber) {
        if(strNumber.startsWith("0")){
            return strNumber;
        }else if(strNumber.startsWith("+")){
            strNumber = "0" + strNumber.substring(3, strNumber.length());
        }
        return strNumber;
    }

    public String getEdt_email() {
        if(edt_email == null){
            return "";
        }
        return edt_email.getText().toString().trim();
    }

    public String getEdt_password() {
        if(edt_password == null){
            return "";
        }
        return edt_password.getText().toString().trim();
    }

    public String getEdt_re_password() {
        if(edt_re_password == null){
            return "";
        }
        return edt_re_password.getText().toString().trim();
    }

    private int validateData() {

        if (vtcModelUser == null) {
            vtcModelUser = new VtcModelUser();
        }
//        if(BuildConfig.DEBUG){
            // Chế độ Debug
            if(isUpdateProfile){

                int checkNameValid = Utils.isNameValid(getEdt_full_name());

                if (!getEdt_full_name().isEmpty() && checkNameValid != Const.VALIDATE_RESULT_NAME_OK) {
                    edt_full_name.requestFocus();
                    switch (checkNameValid) {
                        case Const.VALIDATE_RESULT_NAME_NULL:
                            return R.string.validate_full_name_null;
                        case Const.VALIDATE_RESULT_NAME_NOT_CORRECT:
                            return R.string.validate_full_name_not_valid;
                        case Const.VALIDATE_RESULT_NAME_WRONG_LENGTH:
                            return R.string.validate_full_name_wrong_length;
                    }
                } else if (!getEdt_cmtnd().isEmpty() && !Utils.validateCMTND(getEdt_cmtnd())) {
                    edt_cmtnd.requestFocus();
                    return R.string.validate_cmtnd;
                } else if (getEdt_address().isEmpty()) {
                    return R.string.validate_address;
                } else if (!getTxtPhone().isEmpty() && !Utils.validatePhoneNumber(getTxtPhone())) {
                    edt_phone_num.requestFocus();
                    return R.string.validate_phone_num;
                } else if (!getEdt_email().isEmpty() && !Utils.validateEmail(getEdt_email())) {
                    edt_email.requestFocus();
                    return R.string.validate_email;
                }

            }else {
                int checkNameValid = Utils.isNameValid(getEdt_full_name());

                if (checkNameValid != Const.VALIDATE_RESULT_NAME_OK) {
                    edt_full_name.requestFocus();
                    switch (checkNameValid) {
                        case Const.VALIDATE_RESULT_NAME_NULL:
                            return R.string.validate_full_name_null;
                        case Const.VALIDATE_RESULT_NAME_NOT_CORRECT:
                            return R.string.validate_full_name_not_valid;
                        case Const.VALIDATE_RESULT_NAME_WRONG_LENGTH:
                            return R.string.validate_full_name_wrong_length;
                    }
                } else if (!Utils.validateCMTND(getEdt_cmtnd())) {
                    edt_cmtnd.requestFocus();
                    return R.string.validate_cmtnd;
                } else if (getEdt_address().isEmpty()) {
                    return R.string.validate_address;
                } else if (!Utils.validatePhoneNumber(getTxtPhone())) {
                    edt_phone_num.requestFocus();
                    return R.string.validate_phone_num;
                } else if (!getEdt_email().isEmpty() && !Utils.validateEmail(getEdt_email())) {
                    edt_email.requestFocus();
                    return R.string.validate_email;
                } else if (getEdt_password().isEmpty() || getEdt_password().length() < Const.VALIDATE_PASSWORD_MIN_LENGTH || getEdt_password().length() > Const.VALIDATE_PASSWORD_MAX_LENGTH) {
                    edt_password.requestFocus();
                    return R.string.validate_password;
                } else if (!getEdt_password().equals(getEdt_re_password())) {
                    edt_re_password.requestFocus();
                    return R.string.validate_re_password;
                }
            }

//        }else {
//
//            int checkNameValid = Utils.isNameValid(getEdt_full_name());
//
//            if (checkNameValid != Const.VALIDATE_RESULT_NAME_OK) {
//                edt_full_name.requestFocus();
//                switch (checkNameValid) {
//                    case Const.VALIDATE_RESULT_NAME_NULL:
//                        return R.string.validate_full_name_null;
//                    case Const.VALIDATE_RESULT_NAME_NOT_CORRECT:
//                        return R.string.validate_full_name_not_valid;
//                    case Const.VALIDATE_RESULT_NAME_WRONG_LENGTH:
//                        return R.string.validate_full_name_wrong_length;
//                }
//            } else if (!Utils.validateCMTND(getEdt_cmtnd())) {
//                edt_cmtnd.requestFocus();
//                return R.string.validate_cmtnd;
//            } else if (getEdt_address().equals("")) {
//                return R.string.validate_address;
//            } else if (!Utils.validatePhoneNumber(getTxtPhone())) {
//                edt_phone_num.requestFocus();
//                return R.string.validate_phone_num;
//            } else if (!getEdt_email().isEmpty() && !Utils.validateEmail(getEdt_email())) {
//                edt_email.requestFocus();
//                return R.string.validate_email;
//            } else if (isUpdateProfile == Boolean.FALSE) {
//                if (getEdt_password().length() < Const.VALIDATE_PASSWORD_MIN_LENGTH || getEdt_password().length() > Const.VALIDATE_PASSWORD_MAX_LENGTH) {
//                    edt_password.requestFocus();
//                    return R.string.validate_password;
//                }
//            }
//            if (isUpdateProfile) {
//                if (!getEdt_password().isEmpty() && (getEdt_password().length() < Const.VALIDATE_PASSWORD_MIN_LENGTH || getEdt_password().length() > Const.VALIDATE_PASSWORD_MAX_LENGTH)) {
//                    edt_password.requestFocus();
//                    return R.string.validate_password;
//                }
//
//            } else if (isUpdateProfile == Boolean.FALSE && (getEdt_password().isEmpty() || getEdt_password().length() <
//                    Const.VALIDATE_PASSWORD_MIN_LENGTH || getEdt_password().length() > Const.VALIDATE_PASSWORD_MAX_LENGTH)) {
//                edt_password.requestFocus();
//                return R.string.validate_password;
//            }
//
//            if (!getEdt_password().isEmpty() && !getEdt_password().equals(getEdt_re_password())) {
//                edt_re_password.requestFocus();
//                return R.string.validate_re_password;
//            }
//        }

        vtcModelUser.setUser_full_name(getEdt_full_name());
        vtcModelUser.setUser_CMTND(getEdt_cmtnd());
        vtcModelUser.getAddress().setName(getEdt_address());
        vtcModelUser.setUser_Phone_Num(getEdt_phone_num());
        vtcModelUser.setUser_Email(getEdt_email());
        vtcModelUser.setPassword(getEdt_password());

        return 0;
    }

    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(getActivity());

        switch (v.getId()) {
            case R.id.btn_register:

                int message = validateData();

                if(message == 0){
                    if(isUpdateProfile){
                        Bundle bundle = new Bundle();

                        bundle.putParcelable(Const.KEY_BUNDLE_ACTION_USER, vtcModelUser);
                        bundle.putBoolean(Const.KEY_BUNDLE_ACTION_UPDATE, isUpdateProfile);

                        AppCore.CallFragmentSectionWithCallback(Const.UI_MY_REGISTER_CHOICE_SERVICE, bundle, new Callback() {
                            @Override
                            public <T> void onHandlerCallBack(int key, T... t) {
                                if (t != null && t.length > 0 && t[0] instanceof VtcModelUser) {
                                    vtcModelUser = (VtcModelUser) t[0];
                                }
                            }
                        }, true, false);
                    }else {
                        initCheckPhoneNum(UIConfirmInfoProfile.this, getEdt_phone_num());
                    }
                }else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(message));
                }

                break;

            case R.id.img_back:
                cmdBack();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        if(keyType == TypeActionConnection.TYPE_ACTION_CHECK_PHONE_NUM) {

            Bundle bundle = new Bundle();

            bundle.putParcelable(Const.KEY_BUNDLE_ACTION_USER, vtcModelUser);
            bundle.putBoolean(Const.KEY_BUNDLE_ACTION_UPDATE, isUpdateProfile);

            AppCore.CallFragmentSectionWithCallback(Const.UI_MY_REGISTER_CHOICE_SERVICE, bundle, new Callback() {
                @Override
                public <T> void onHandlerCallBack(int key, T... t) {
                    if (t != null && t.length > 0 && t[0] instanceof VtcModelUser) {
                        vtcModelUser = (VtcModelUser) t[0];
                    }
                }
            }, true, false);
        }
    }

    @Override
    protected void cmdOnClickChoiceCity(String city, String district) {
        super.cmdOnClickChoiceCity(city, district);
        if(vtcModelUser != null) {
            vtcModelUser.setUser_City(city);
            vtcModelUser.setUser_District(district);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(callback != null){
            callback.onHandlerCallBack(-1);
        }
    }
}
