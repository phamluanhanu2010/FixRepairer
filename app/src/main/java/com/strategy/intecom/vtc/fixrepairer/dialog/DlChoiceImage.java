package com.strategy.intecom.vtc.fixrepairer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlChoiceImage extends Dialog {

    private Context context;

    private TextView tv_librery;
    private TextView tv_capture;

    private Button btn_cancel;

    private onClickDialogItem onClickDialogItem;

    private int width;

    public DlChoiceImage(Context context, int width) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_choice_image);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getWindow().getAttributes().verticalMargin = 0.05F;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = width;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);

        setCancelable(false);

        initControler();

    }

    public void initControler() {

        tv_librery = (TextView) findViewById(R.id.tv_librery);
        tv_capture = (TextView) findViewById(R.id.tv_capture);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        initData();
    }

    private void initData() {

        tv_librery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnClickDialogItem().onClickDialogItemLibrary();
                dismiss();
            }
        });

        tv_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnClickDialogItem().onClickDialogItemCapture();
                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public DlChoiceImage.onClickDialogItem getOnClickDialogItem() {
        return onClickDialogItem;
    }

    public void setOnClickDialogItem(DlChoiceImage.onClickDialogItem onClickDialogItem) {
        this.onClickDialogItem = onClickDialogItem;
    }

    public interface onClickDialogItem {
        void onClickDialogItemCapture();

        void onClickDialogItemLibrary();
    }

}
