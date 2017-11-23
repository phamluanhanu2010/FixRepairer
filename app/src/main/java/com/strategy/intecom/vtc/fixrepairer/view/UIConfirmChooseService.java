package com.strategy.intecom.vtc.fixrepairer.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.MainScreen;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/23/16.
 */
public class UIConfirmChooseService extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private TextView tv_field;

    private ImageView img_back;
    private Button btn_register;

    private EditText edt_description;

    private CheckBox cb_dieukhoan;

    private TextView tv_dieukhoan;

    private ImageView img_avatar;

    private TextView tv_title;

    private RelativeLayout lout_container;

    private Callback callback;

    private VtcModelUser vtcModelUser;

    private List<VtcModelFields> lstFields;

    private boolean isUpdateProfile = Boolean.FALSE;


    @SuppressLint("ValidFragment")
    public UIConfirmChooseService(Callback callback) {
        this.callback = callback;
    }

    public UIConfirmChooseService() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AppCore.initToolsBar(getActivity(), false);

        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            vtcModelUser = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_USER);
            isUpdateProfile = savedInstanceState.getBoolean(Const.KEY_BUNDLE_ACTION_UPDATE);
        }

        viewRoot = inflater.inflate(R.layout.ui_confirm_signin_choose_service, container, false);

        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initController(view);
    }


    private void initController(View view) {

        lout_container = (RelativeLayout) view.findViewById(R.id.lout_container);

        img_avatar =(ImageView) view.findViewById(R.id.img_avatar);

        tv_title = (TextView) view.findViewById(R.id.tv_title);

        tv_field = (TextView) view.findViewById(R.id.tv_field);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        btn_register = (Button) view.findViewById(R.id.btn_register);

        edt_description = (EditText) view.findViewById(R.id.edt_description);

        cb_dieukhoan = (CheckBox) view.findViewById(R.id.cb_dieukhoan);
        tv_dieukhoan = (TextView) view.findViewById(R.id.tv_dieukhoan);
        if (isUpdateProfile) {

            cb_dieukhoan.setVisibility(CheckBox.GONE);
            tv_dieukhoan.setVisibility(CheckBox.GONE);
            btn_register.setText(getmActivity().getResources().getString(R.string.confirm_signin_button_next));
        } else {

            cb_dieukhoan.setVisibility(CheckBox.VISIBLE);
            tv_dieukhoan.setVisibility(CheckBox.VISIBLE);

            tv_dieukhoan.setOnClickListener(this);
            btn_register.setText(getmActivity().getResources().getString(R.string.confirm_info_profile_btn_register));
        }
        tv_field.setSelected(true);
        tv_field.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        initData();


    }

    private void initData() {
        String strSkill = initGetListSkill();

        if (vtcModelUser != null) {
            if (vtcModelUser.getSkills() != null && vtcModelUser.getSkills().size() > 0 && strSkill.isEmpty()) {
                strSkill = initOutDataSkill(vtcModelUser.getSkills());
            }

            edt_description.setText(vtcModelUser.getUser_Description());
        }

        if (!strSkill.isEmpty()) {
            tv_field.setText(strSkill);
        } else {
            tv_field.setText(getActivity().getResources().getString(R.string.title_user_info_choose_service));
        }

        viewRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
            }
        });

        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
            }
        });

        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
            }
        });

        lout_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
            }
        });

    }

    private String initGetListSkill(){

        String strSkill = "";
        if (AppCore.getVtcModelUser() != null && AppCore.getVtcModelUser().getSkills() != null && AppCore.getVtcModelUser().getSkills().size() > 0) {

            if (lstFields != null) {
                lstFields = null;
            }
            lstFields = new ArrayList<>();
            List<VtcModelUser.Skills> skillsList = AppCore.getVtcModelUser().getSkills();

            for (VtcModelUser.Skills skill : skillsList) {
                VtcModelFields fields = new VtcModelFields();
                fields.setName(skill.getName());
                fields.setId(skill.get_id());
                fields.setChoice(true);

                lstFields.add(fields);
            }

            strSkill = initOutDataSkill(skillsList);

            skillsList = null;
        }

        return strSkill;
    }

    public String getEdt_description() {
        if (edt_description == null) {
            return "";
        }
        return edt_description.getText().toString().trim();
    }

    private int validateData() {

        if (vtcModelUser == null) {
            vtcModelUser = new VtcModelUser();
        }

//        if (!isUpdateProfile && getEdt_description().equals("")) {
//            return R.string.validate_description;
//        } else
        if (!isUpdateProfile && (vtcModelUser.getSkills() == null || vtcModelUser.getSkills().size() <= 0)) {
            return R.string.validate_service;
        } else if (!isUpdateProfile && !cb_dieukhoan.isChecked()) {
            return R.string.validate_check_dk;
        }

        vtcModelUser.setUser_Description(getEdt_description());

        return 0;
    }

    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(getActivity());

        switch (v.getId()) {
            case R.id.btn_register:

                int message = validateData();

                if (message == 0) {
                    if (vtcModelUser != null) {
                        vtcModelUser.setUser_Phone_Num(vtcModelUser.getUser_Phone_Num());
                        if (isUpdateProfile) {

                            initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", getResources().getString(R.string.title_confirm_change_location));

                        } else {
                            initRegister(UIConfirmChooseService.this, vtcModelUser);
                        }
                    }
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(message));
                }

                break;
            case R.id.img_back:
                if (vtcModelUser == null) {
                    vtcModelUser = new VtcModelUser();
                }

                vtcModelUser.setUser_Description(getEdt_description());

                cmdBack();
                break;
            case R.id.tv_field:
                initGetListSkill();

                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_CHOICE_SKILL, null, "", "", lstFields, AppCore.getVtcFieldsChildList());
                break;
            case R.id.tv_dieukhoan:
                AppCore.CallFragmentSection(Const.UI_RULES, true, false);
                break;
        }
    }

    @Override
    public void cmdPressDialogYes(String response) {
        super.cmdPressDialogYes(response);
        if(vtcModelUser == null){
            vtcModelUser = new VtcModelUser();
        }

        GPSTracker gpsTracker = getGpsTracker(getmActivity());

        vtcModelUser.getAddress().setLongt(String.valueOf(gpsTracker.getLongitude()));
        vtcModelUser.getAddress().setLatt(String.valueOf(gpsTracker.getLatitude()));

        initUpdateInfo(UIConfirmChooseService.this, vtcModelUser, getVtcModelUser(), true);
    }

    @Override
    public void cmdPressDialogNo() {
        super.cmdPressDialogNo();

        initUpdateInfo(UIConfirmChooseService.this, vtcModelUser, getVtcModelUser(), false);
    }

    private String initOutData(List<VtcModelFields> lst) {

        String strOut = "";
        if (lst != null) {
            boolean stick = false;
            for (VtcModelFields value : lst) {
                if (value.isChoice()) {

                    if (stick) {
                        strOut += ", ";
                    }
                    strOut += value.getName();

                    stick = true;
                }
            }
        }

        return strOut;
    }

    private String initOutDataSkill(List<VtcModelUser.Skills> lst) {

        String strOut = "";
        if (lst != null) {
            boolean stick = false;
            for (VtcModelUser.Skills value : lst) {
                if (value.isChoice()) {

                    if (stick) {
                        strOut += ", ";
                    }
                    strOut += value.getName();

                    stick = true;
                }
            }
        }

        return strOut;
    }


    private void initDataSkill(List<VtcModelFields> lst) {


        vtcModelUser.setSkills(null);
        if (lst != null) {
            for (VtcModelFields value : lst) {
                if (value.isChoice()) {

                    VtcModelUser.Skills skills = new VtcModelUser.Skills();
                    skills.setName(value.getName());
                    skills.set_id(value.getId());

                    vtcModelUser.setSkill(skills);
                }
            }
        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType) {

            case TYPE_ACTION_UPDATE_AGENCY:

                try {

                    JSONObject jsonUser = new JSONObject(response);

                    VtcModelUser user = VtcModelUser.getDataPaserUser(jsonUser);
                    if (user != null) {

                        AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.USER_INFO, response);

                        AppCore.getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, user.isAvailable());

                        setVtcModelUser(user);

                        MainScreen.initShowInfo(getmActivity(), getVtcModelUser());

                        AppCore.CallFragmentSection(Const.UI_SERVICE_LIST, false, true);

                    } else {
                        AppCore.showLog("Register Parser User Error.....");
                    }

                    getmActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppCore.showLog("Register Json User Error..... : " + e.getMessage());
                }


                break;
            case TYPE_ACTION_REGISTER:

                try {

                    AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.AUTH_TOKEN, ParserJson.getAuthToken(response));

                    JSONObject jsonUser = new JSONObject(response);

                    VtcModelUser user = VtcModelUser.getDataPaserUser(jsonUser);
                    if (user != null) {

                        AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.USER_INFO, response);

                        AppCore.getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, user.isAvailable());

                        setVtcModelUser(user);

                        Bundle bundle = new Bundle();

                        if (vtcModelUser != null) {
                            bundle.putString(Const.KEY_BUNDLE_ACTION_PHONE_NUM, vtcModelUser.getUser_Phone_Num());
                        }

                        bundle.putString(Const.KEY_BUNDLE_ACTION_TYPE_PASSCODE, Const.TYPE_PASSCODE_REGISTER);

                        MainScreen.initShowInfo(getmActivity(), getVtcModelUser());

                        AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_PASSCODE, bundle, true, false);
                    } else {
                        AppCore.showLog("Register Parser User Error.....");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    AppCore.showLog("Register Json User Error..... : " + e.getMessage());
                }
                break;
        }
    }

    @Override
    protected void cmdOnSetSkill(List<VtcModelFields> fieldsList) {
        super.cmdOnSetSkill(fieldsList);
        this.lstFields = fieldsList;
        initDataSkill(fieldsList);
        String strOut = initOutData(fieldsList);
        if (!strOut.isEmpty()) {
            tv_field.setText(strOut);
        } else {
            tv_field.setText(getActivity().getResources().getString(R.string.title_user_info_choose_service));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callback != null){
            callback.onHandlerCallBack(-1, vtcModelUser);
        }
    }
}
