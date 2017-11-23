package com.strategy.intecom.vtc.fixrepairer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/17/16.
 */
public class UIConfirmReCode extends AppCore {
    private View viewRoot;


    private EditText edt_phone_num;
    private EditText edt_new_password;
    private EditText edt_re_new_password;

    private Button btn_submit;

    private String phone_num = "";

    public UIConfirmReCode() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            phone_num = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_PHONE_NUM);
        }
        viewRoot = inflater.inflate(R.layout.ui_input_password_code, container, false);

        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initController(view);
    }

    private void initController(View view) {
        edt_phone_num = (EditText) view.findViewById(R.id.edt_phone_num);
        edt_new_password = (EditText) view.findViewById(R.id.edt_new_password);
        edt_re_new_password = (EditText) view.findViewById(R.id.edt_re_new_password);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        edt_new_password.setVisibility(EditText.VISIBLE);
        edt_re_new_password.setVisibility(EditText.VISIBLE);

        edt_phone_num.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        edt_phone_num.setHint(getResources().getString(R.string.title_confirm_code));
        btn_submit.setText(getResources().getString(R.string.btn_change_password));

        initEvent();
    }

    public String getEdt_phone_num() {
        if(edt_phone_num == null){
            return "";
        }
        return edt_phone_num.getText().toString().trim();
    }

    public String getEdt_new_password() {
        if(edt_new_password == null){
            return "";
        }
        return edt_new_password.getText().toString().trim();
    }

    public String getEdt_re_new_password() {
        if(edt_re_new_password == null){
            return "";
        }
        return edt_re_new_password.getText().toString().trim();
    }

    private int validateData(){
        if(getEdt_phone_num().length() < 6 || getEdt_phone_num().length() > 6){
            return R.string.title_confirm_code_error;
        }else if(getEdt_new_password().length() < 8 || getEdt_new_password().length() > 12){
            return R.string.validate_password;
        }else if(!getEdt_new_password().equals(getEdt_re_new_password())){
            return R.string.validate_re_password;
        }

        return 0;
    }

    private void initEvent(){
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int message = validateData();
                if(message == 0) {
                    initFogotPasswordConfirmCode(UIConfirmReCode.this, phone_num, getEdt_phone_num(), getEdt_new_password());
                }else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getResources().getString(message));
                }
            }
        });
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        if(keyType == TypeActionConnection.TYPE_ACTION_CONFIRM_CODE){

            initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", response);

            cmdBack();
        }
    }
}
