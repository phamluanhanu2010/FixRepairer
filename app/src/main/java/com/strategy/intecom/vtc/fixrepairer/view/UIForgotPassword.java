package com.strategy.intecom.vtc.fixrepairer.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Ha on 5/17/16.
 */
public class UIForgotPassword extends AppCore {

    private View viewRoot;

    private EditText edt_phone_num;

    private Button btn_submit;

    private ImageView img_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AppCore.initToolsBar(getActivity(), false);
        viewRoot = inflater.inflate(R.layout.ui_input_password_code, container, false);

        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initController(view);
    }

    @SuppressLint("CutPasteId")
    private void initController(View view) {

        edt_phone_num = (EditText) view.findViewById(R.id.edt_phone_num);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        img_back = (ImageView) view.findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getActivity());

                cmdBack();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getActivity());
                if(Utils.validatePhoneNumber(getTxtPhone())) {
                    initFogotPassword(UIForgotPassword.this, getEdt_phone_num());
                }else {
                    edt_phone_num.requestFocus();
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(R.string.validate_phone_num));
                }
            }
        });
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

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        if(keyType == TypeActionConnection.TYPE_ACTION_FORGOT_PASSWORD){

            Bundle bundle = new Bundle();

            bundle.putString(Const.KEY_BUNDLE_ACTION_PHONE_NUM, getEdt_phone_num());

            AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_CHANGE_FORGOT_PASSWORD, bundle, true, false);
        }
    }

}
