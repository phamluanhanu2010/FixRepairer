package com.strategy.intecom.vtc.fixrepairer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlMessage extends Dialog {

    private Context context;

    private TextView tv_message_content;
    private TextView btn_cancel;
    private TextView btn_enable_gps;
    private TextView btn_enable_wifi;

    private String content = "";

    private onClickDialogItem onClickDialogItem;

    private int width;

    public DlMessage(Context context, String content, int width) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.content = content;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_message);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        setCancelable(false);

        initControler();

    }

    public void initControler() {

        tv_message_content = (TextView) findViewById(R.id.tv_message_content);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_enable_gps = (TextView) findViewById(R.id.btn_enable_gps);
        btn_enable_wifi = (TextView) findViewById(R.id.btn_enable_wifi);

        initData();
    }

    private void initData() {

        tv_message_content.setText(content);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnClickDialogItem().onClickDialogItemCancel();
            }
        });

        btn_enable_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getOnClickDialogItem().onClickDialogItemEnableGPS();
            }
        });

        btn_enable_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getOnClickDialogItem().onClickDialogItemEnableWifi();
            }
        });

    }

    public DlMessage.onClickDialogItem getOnClickDialogItem() {
        return onClickDialogItem;
    }

    public void setOnClickDialogItem(DlMessage.onClickDialogItem onClickDialogItem) {
        this.onClickDialogItem = onClickDialogItem;
    }

    public interface onClickDialogItem{
        void onClickDialogItemCancel();
        void onClickDialogItemEnableWifi();
        void onClickDialogItemEnableGPS();
    }

}
