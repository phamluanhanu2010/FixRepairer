package com.strategy.intecom.vtc.fixrepairer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtItemSkill;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelJob;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlListSkill extends Dialog implements View.OnClickListener{

    private Context context;

    private TextView btn_accept;
    private TextView btn_cancel;
    private ExpandableListView lv_history_warning;

    private onClickButton onClickButton;

    private AdtItemSkill listAdapter;
    private List<VtcModelFields> listChoice;
    private List<String> listDataHeader;
    private HashMap<String, List<VtcModelFields>> listDataChild;

    private int width = 0;

    public DlListSkill(Context context, List<VtcModelFields> listChoice, HashMap<String, List<VtcModelFields>> listDataChild, int width) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.listChoice = listChoice;
        this.listDataChild = listDataChild;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dl_list_skill);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(true);

        initController();

    }

    public void initController() {

        listDataHeader = new ArrayList<>();

        lv_history_warning = (ExpandableListView) findViewById(R.id.lv_history_warning);

        btn_accept = (TextView) findViewById(R.id.btn_accept);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);

        initEvent();
    }

    private void initEvent(){

        btn_accept.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        initData();
    }

    private void initData() {

        List<VtcModelFields> fieldsList = AppCore.getVtcFieldsParentList();
        if(fieldsList != null) {
            if(listDataHeader != null){
                listDataHeader = new ArrayList<>();
            }
            for (VtcModelFields fields : fieldsList) {
                if (fields != null) {
                    listDataHeader.add(fields.getName());
                }
            }
        }

        listAdapter = new AdtItemSkill(context, listChoice);
        listAdapter.setData(listDataHeader, listDataChild);
        // setting list adapter
        lv_history_warning.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_accept:
                getOnClickButton().onClickButton(listAdapter.getListChoiceItem());
                listAdapter.initClearData();
                dismiss();
                break;

            case R.id.btn_cancel:
                getOnClickButton().onClickButtonCancel();
                listAdapter.initClearData();
                dismiss();
                break;
        }
    }

    public DlListSkill.onClickButton getOnClickButton() {
        return onClickButton;
    }

    public void setOnClickButton(DlListSkill.onClickButton onClickButton) {
        this.onClickButton = onClickButton;
    }

    public interface onClickButton{
        void onClickButton(List<VtcModelFields> fieldsList);
        void onClickButtonCancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        listAdapter.initClearData();
        context = null;
        btn_accept = null;
        btn_cancel = null;
        lv_history_warning = null;
        onClickButton = null;
        listAdapter = null;
        listDataHeader = null;
        listDataChild = null;
        listChoice = null;
    }
}
