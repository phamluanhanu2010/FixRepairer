package com.strategy.intecom.vtc.fixrepairer.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.service.RegistrationService;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;


/**
 * Created by Mr. Ha on 5/16/16.
 */
@SuppressLint("ValidFragment")
public class UIConfirmSignIn extends AppCore implements View.OnClickListener {

    private View viewRoot;

//    private CallbackManager callbackManager;
    private Button btn_loginphone;
    private Button btn_signin;

    public UIConfirmSignIn() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.ui_confirm_signin, container, false);

        AppCore.initToolsBar(getActivity(), false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initController(view);
    }

    private void initController(View view) {


        Intent intentService = new Intent(getActivity(), RegistrationService.class);

        if (!Utils.checkPlayServices(getActivity())) {
            getActivity().stopService(intentService);
        }

        getActivity().startService(intentService);

//        callbackManager = CallbackManager.Factory.create();

//        LoginButton loginButton = (LoginButton) this.findViewById(R.id.btn_loginfb);
//
//        loginButton.setReadPermissions(new String[]{"user_friends"});
//        // Other app specific specialization
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                callUIActivity();
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//            }
//        });


        btn_signin = (Button) view.findViewById(R.id.btn_signin);
        btn_loginphone = (Button) view.findViewById(R.id.btn_loginphone);

        btn_signin.setOnClickListener(this);
        btn_loginphone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(getActivity());

        switch (v.getId()) {

            case R.id.btn_signin:

                AppCore.CallFragmentSection(Const.UI_MY_REGISTER, true, false);

                break;
            case R.id.btn_loginphone:

                AppCore.CallFragmentSection(Const.UI_MY_LOGIN, true, false);

                break;

            default:
                return;
        }
    }

}
