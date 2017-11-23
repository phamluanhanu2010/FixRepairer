package com.strategy.intecom.vtc.fixrepairer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luan Pham on 6/7/16.
 */
public class UISupport extends AppCore implements View.OnClickListener {
    private EditText mFeedbackContent;

    private View viewRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_support, container, false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initController(view);
    }

    private void initController(View view) {
        mFeedbackContent = (EditText) view.findViewById(R.id.etxt_Support_Feedback);
        ImageView btnExit = (ImageView) view.findViewById(R.id.btn_back);
        btnExit.setOnClickListener(this);

        Button btnCallSupport = (Button) view.findViewById(R.id.btn_Call_Support);
        btnCallSupport.setOnClickListener(this);
        Button btnSupportAccept = (Button) view.findViewById(R.id.btn_Support_Accept);
        btnSupportAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                Utils.hideKeyboard(getActivity());
                cmdBack();
                break;
            case R.id.btn_Support_Accept:
                sendFeedback();
                break;
            case R.id.btn_Call_Support:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", String.valueOf("Gọi " + Const.SUPPORT_CALL_PHONE));
                break;
            default:
                return;
        }
    }

    @Override
    public void cmdPressDialogYes(String response) {
        super.cmdPressDialogYes(response);

        initCallIntentPhone(Const.SUPPORT_CALL_PHONE);
    }

    private void sendFeedback() {

        String message = mFeedbackContent.getText().toString();

        if (TextUtils.isEmpty(message)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN,
                AppCore.getPreferenceUtil(getActivity())
                        .getValueString(PreferenceUtil.AUTH_TOKEN));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject
                    .put(ParserJson.API_PARAMETER_NAME, AppCore.getVtcModelUser
                            ().getUser_full_name());
            jsonObject.put(ParserJson.API_PARAMETER_PHONE, AppCore.getVtcModelUser
                    ().getUser_Phone_Num());
            jsonObject.put(ParserJson.API_PARAMETER_MESSAGE, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppCore.getConnection(this).onExcuteProcess
                (TypeActionConnection.TYPE_ACTION_SEND_FEEDBACK,
                        RequestListener
                                .API_CONNECTION_SEND_FEEDBACK +
                                VtcNWConnection
                                        .urlEncodeUTF8
                                                (map),
                        jsonObject);
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);

        Utils.hideKeyboard(getActivity());
        AppCore.showToastLong(getView(), getActivity().getResources().getString(R.string.title_dialog_process_success));
        cmdBack();
    }
}
