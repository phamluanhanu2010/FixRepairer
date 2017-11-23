package com.strategy.intecom.vtc.fixrepairer.view;

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

import com.strategy.intecom.vtc.fixrepairer.MainScreen;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMServiceList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Ha on 5/16/16.
 */
public class UIConfirmPassCode extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private EditText edt_passcode;
    private Button btn_next;
    private ImageView img_back;
    private TextView tv_description_number;
    private Button btn_passcode;

    private String phoneNum = "";
    private String typePasscode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            phoneNum = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_PHONE_NUM);
            typePasscode = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_TYPE_PASSCODE);
        }

        viewRoot = inflater.inflate(R.layout.ui_confirm_pascode, container, false);

        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControler(view);
    }


    private void initControler(View view) {

        edt_passcode = (EditText) view.findViewById(R.id.edt_passcode);

        tv_description_number = (TextView) view.findViewById(R.id.tv_description_number);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_passcode = (Button) view.findViewById(R.id.btn_passcode);

        tv_description_number.setText(phoneNum);
        initEvents();
    }

    private void initEvents() {

        img_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_passcode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                cmdBack();
                break;
            case R.id.btn_next:
                int message = validatePasscode();
                if (message == 0) {
                    initConfirmPassCode(UIConfirmPassCode.this, phoneNum, getEdt_passcode());
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(message));
                }
                break;
            case R.id.btn_passcode:
                initGetPassCode(UIConfirmPassCode.this, phoneNum, typePasscode);
                break;
            default:
                return;
        }
    }

    public String getEdt_passcode() {
        if (edt_passcode == null) {
            return "";
        }
        return edt_passcode.getText().toString().trim();
    }

    private int validatePasscode() {
        if (getEdt_passcode().equals("")) {
            return R.string.validate_passcode;
        } else if (getEdt_passcode().length() > 6) {
            return R.string.validate_passcode;
        } else if (getEdt_passcode().length() < 6) {
        }
        return 0;
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType) {
            case TYPE_ACTION_CONFIRM_PASSCODE:

                AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.AUTH_TOKEN, ParserJson.getAuthToken(response));

                try {
                    JSONObject jsonUser = new JSONObject(response);
                    VtcModelUser user = VtcModelUser.getDataPaserUser(jsonUser);
                    if (user != null) {

                        AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.USER_INFO, response);

                        AppCore.getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, user.isAvailable());

                        setVtcModelUser(user);

                        AppCore.CallFragmentSection(Const.UI_SERVICE_LIST, false, true);

                        initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", message);

                        getmActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } else {
                        AppCore.showLog("Login Parser User Error.....");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppCore.showLog("Login Json User Error..... : " + e.getMessage());
                }
                break;
        }
    }

}

