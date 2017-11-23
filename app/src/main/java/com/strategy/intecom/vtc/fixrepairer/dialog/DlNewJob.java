package com.strategy.intecom.vtc.fixrepairer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelJob;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.socket.client.Socket;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlNewJob extends Dialog implements View.OnClickListener{

    private Context context;

    private String message;

    private Button btn_accept_job;
    private Button btn_cancel;

    private TextView tv_title_timer;
    private TextView tv_timer;
    private TextView tv_date_time;
    private TextView tv_title_job;
    private TextView tv_title_address;
    private TextView tv_name;
    private TextView tv_phone_num;
    private TextView tv_description;

    private CountDownTimer countDownTimer;

    private onClickButton onClickButton;

    private String id = "";

    public DlNewJob(Context context, String message) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dl_new_job);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(true);

        initControler();

    }

    public void initControler() {

        tv_title_timer = (TextView) findViewById(R.id.tv_title_timer);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_date_time = (TextView) findViewById(R.id.tv_date_time);
        tv_title_job = (TextView) findViewById(R.id.tv_title_job);
        tv_title_address = (TextView) findViewById(R.id.tv_title_address);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone_num = (TextView) findViewById(R.id.tv_phone_num);
        tv_description = (TextView) findViewById(R.id.tv_description);

        btn_accept_job = (Button) findViewById(R.id.btn_accept_job);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        initEvent();
    }

    private void initEvent(){

        btn_accept_job.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        initData();
    }

    private void initData() {

        VtcModelJob vtcModelJob = VtcModelJob.initGetDataJson(message);
        if (vtcModelJob != null) {

            tv_date_time.setText(Utils.initConvertTimeDisplayLong(vtcModelJob.getOrder_time()));
            tv_title_job.setText(String.valueOf(vtcModelJob.getCategory_name() + " - " + vtcModelJob.getField_name()));
            tv_title_address.setText(String.valueOf(vtcModelJob.getAddress_name() + (vtcModelJob.getDistance().isEmpty() ? "" : " (" + vtcModelJob.getDistance() + " Km)")));
            tv_name.setText(vtcModelJob.getUser_name());
            tv_phone_num.setText(vtcModelJob.getPhone());
            tv_description.setText(vtcModelJob.getDescription());

            id = vtcModelJob.getId();
        }


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        countDownTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                int count = (int) (millisUntilFinished / 1000) - 1;

                tv_title_timer.setText(context.getResources().getString(R.string.confirm_close_dialog, count));
                tv_timer.setText(String.valueOf(count));
            }

            @Override
            public void onFinish() {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                getOnClickButton().onClickButtonCancel();
                dismiss();
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_accept_job:
                getOnClickButton().onClickButton(id);
                dismiss();
                break;

            case R.id.btn_cancel:
                getOnClickButton().onClickButtonCancel();
                dismiss();
                break;
        }
    }

    public DlNewJob.onClickButton getOnClickButton() {
        return onClickButton;
    }

    public void setOnClickButton(DlNewJob.onClickButton onClickButton) {
        this.onClickButton = onClickButton;
    }

    public interface onClickButton{
        void onClickButton(String id);
        void onClickButtonCancel();
    }
}
