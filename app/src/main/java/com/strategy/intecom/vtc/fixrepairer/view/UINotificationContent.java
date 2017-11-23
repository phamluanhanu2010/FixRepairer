package com.strategy.intecom.vtc.fixrepairer.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtNotificationMsgLst;
import com.strategy.intecom.vtc.fixrepairer.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelNotification;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

/**
 * Created by Luan Pham on 6/7/16.
 */
public class UINotificationContent extends Activity implements View.OnClickListener, RequestListener {

    private RecyclerView mRecyclerView;
    private AdtNotificationMsgLst mAdapter;
    private ArrayList<VtcModelNotification> notiLst;
    private int currentId = 0;
    private ImageView btnExit;
    private ImageView btnPrev;
    private ImageView btnNext;
    private ImageView btnRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_notification_content);

        Bundle data = getIntent().getExtras();

        String notiId = data.getString(Const.KEY_EXTRACT_NOTI_ID);
        notiLst = data.<VtcModelNotification>getParcelableArrayList(
                Const.KEY_EXTRACT_NOTIFICATION);

        btnExit = (ImageView)findViewById(R.id.btn_back);
        btnPrev = (ImageView)findViewById(R.id.img_Prev);
        btnNext = (ImageView)findViewById(R.id.img_Next);
        btnRemove = (ImageView)findViewById(R.id.img_Remove);
        btnExit.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnRemove.setOnClickListener(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager
                (UINotificationContent.this));

        for (int i = 0; i < notiLst.size(); i++) {
            if (notiId.equalsIgnoreCase(notiLst.get(i).getId())) {
                currentId = i;
                break;
            }
        }

        initController();
    }

    private void initController(){
        if (notiLst == null || notiLst.size()  == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            btnRemove.setVisibility(View.INVISIBLE);
            mRecyclerView.setAdapter(new AdtNotificationMsgLst(
                    new ArrayList<VtcModelNotification.Message>()));
            return;
        }

        VtcModelNotification noti = notiLst.get(currentId);
        if (noti == null) {
            return;
        }
        mAdapter = new AdtNotificationMsgLst(noti.getMessages());
        mRecyclerView.setAdapter(mAdapter);

        updateReaded(noti);

        if (notiLst.size()  == 1) {
            btnPrev.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
        } else if (currentId == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        } else if (currentId == (notiLst.size() - 1)) {
            btnPrev.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    private void updateReaded(VtcModelNotification notification) {
        for (VtcModelNotification.Message msg :notification.getMessages()) {
                    if (!msg.isReaded()) {
                        Map<String, String> map = new HashMap<>();
                        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN,
                                AppCore.getPreferenceUtil(this)
                                        .getValueString(PreferenceUtil.AUTH_TOKEN));

                        AppCore.getConnection(this).onExcuteProcess
                                (TypeActionConnection.TYPE_ACTION_MESSAGE_READ,
                                        RequestListener
                                                .API_CONNECTION_FEEDBACK
                                        + notification.getId()+ RequestListener.API_CONNECTION_FEEDBACK_MARK_READ +
                                        VtcNWConnection
                                        .urlEncodeUTF8
                                        (map),
                                true);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.img_Prev:
                showPrevNotification();
                break;
            case R.id.img_Next:
                showNextNotification();
                break;
            case R.id.img_Remove:
                removeNotification();
                break;
            default:
                return;
        }
    }

    private void removeNotification() {
        if (notiLst == null || notiLst.size()  == 0) {
            return;
        }
        int oldSize = notiLst.size();

        // remove item in list
        notiLst.remove(currentId);
        // TODO send remove request to server

        if (currentId > (notiLst.size()-1)) {
            currentId = notiLst.size()-1;
        }

        initController();
    }

    private void showNextNotification() {
        if (currentId < notiLst.size() - 1) {
            currentId++;
            initController();
        }
    }

    private void showPrevNotification() {
        if (currentId > 0) {
        currentId--;
        initController();}
    }

    @Override
    public void onPostExecuteError(
            TypeErrorConnection keyType, String msg)
    {

    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType,
            String response, String message) {
    }

    @Override
    public void onResultSocket(TypeActionConnection keyType, Socket socket,
            String response) {

    }
}
