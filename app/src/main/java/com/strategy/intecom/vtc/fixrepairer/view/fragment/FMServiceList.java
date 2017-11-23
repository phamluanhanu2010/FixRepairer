package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.strategy.intecom.vtc.fixrepairer.MainScreen;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtServiceLst;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class FMServiceList extends AppCore implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private final String TAG = getClass().getName();

    private View viewRoot;

    private ImageView btn_home;
    private TextView tv_title;

    private ListView lst_service;

    private static AdtServiceLst mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton fab;

    private LinearLayout lout_container_header;

    private TextView tv_sort;

    private Switch switch_on_off;

    private Callback callback;

    private static RequestListener requestListener;

    private ArrayList<VtcModelUser.Skills> lstSkill;

    private TypeActionConnection actionConnection = TypeActionConnection.TYPE_ACTION;

    public FMServiceList() {

    }

    @Override
    public void onResume() {
        if(switch_on_off != null) {
            switch_on_off.setChecked(getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI));
        }
        super.onResume();
    }

    @SuppressLint("ValidFragment")
    public FMServiceList(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.ui_service_job_list, container, false);

        AppCore.initToolsBar(getActivity(), true);
        requestListener = FMServiceList.this;
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSocketOrderOffer(FMServiceList.this);
        initController(view);
        initGetOrderCount(FMServiceList.this);
    }

    private void initController(View view) {
        btn_home = (ImageView) view.findViewById(R.id.btn_home);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        lst_service = (ListView) view.findViewById(R.id.lst_service);

        tv_sort = (TextView) view.findViewById(R.id.tv_sort);

        lout_container_header = (LinearLayout) view.findViewById(R.id.lout_container_header);

        switch_on_off = (Switch) view.findViewById(R.id.switch_on_off);

        switch_on_off.setChecked(getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI));

        tv_title.setSelected(true);
        tv_title.setText(getResources().getString(R.string.title_bar_danhmuc));

        if (getVtcModelFields().getLstFieldsParent().size() <= 0 || getVtcModelTools().getLstToolsParent().size() <= 0 || getVtcModelCity().getLstCities().size() <= 0) {

            actionConnection = TypeActionConnection.TYPE_ACTION;
            initGetCommonInfo(FMServiceList.this);
        }

        initEvent();

    }

    private void initEvent() {

        fab.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        final float h = getResources().getDimension(R.dimen.control_input_text_h);

        View headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.tmp_textview, lst_service, false);

        AbsListView.LayoutParams tv_header_params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, (int) h);
        headerView.setLayoutParams(tv_header_params);

        lst_service.addHeaderView(headerView);

        lst_service.setOnScrollListener(new QuickReturnListViewOnScrollListener(
                QuickReturnViewType.HEADER, lout_container_header, (int) (-h), null, 0, true) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

            }

            @Override
            public void onScroll(AbsListView listview, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(listview, firstVisibleItem, visibleItemCount, totalItemCount);

            }
        });

        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) (h + Utils.convertDpToPixels(7, getmActivity())));

        switch_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVtcModelUser() != null) {
                    actionConnection = TypeActionConnection.TYPE_ACTION_UPDATE_IS_WORKING;
                    initUpdateIsWorking(FMServiceList.this, getVtcModelUser().getId(), switch_on_off.isChecked());
                }
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainScreen.initMenu(getActivity());
            }
        });

        initData();
    }

    private void initData() {

        mAdapter = new AdtServiceLst(getActivity(), (int) getActivity().getResources().getDimension(R.dimen.size_icon_in_app_menu));
        mAdapter.setOnClickItem(new AdtServiceLst.onClickItem() {
            @Override
            public void onClickIten(VtcModelUser.Skills modelFields) {
                Bundle bundle = new Bundle();

                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_FIELDS, modelFields);

                AppCore.CallFragmentSectionWithCallback(Const.UI_JOB_LIST, bundle, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {
                        if (callback != null) {
                            callback.onHandlerCallBack(-1);
                        }
                        if (switch_on_off != null) {
                            switch_on_off.setChecked(getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI));
                        }
                        if(t != null && t.length > 0 && t[0] instanceof Boolean) {
                            boolean isRefreshList = (Boolean) t[0];
                            if (isRefreshList) {
                                initGetOrderCount(FMServiceList.this);
                            }
                        }
                    }
                });
            }
        });

        lst_service.setAdapter(mAdapter);

        tv_sort.setVisibility(TextView.GONE);

    }

    public static void initReloadData(List<VtcModelUser.Skills> lst) {

        if (mAdapter != null) {
            mAdapter.setData(lst);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
//        initGetFieldLst(FMServiceList.this);

        initGetOrderCount(FMServiceList.this);
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        actionConnection = TypeActionConnection.TYPE_ACTION;
        switch (keyType) {
            case TYPE_ACTION_GET_COUNT_SKILL:

                initFilterMyData(response);
                break;
            case TYPE_ACTION_GET_LIST_FIELD:
                break;
            case TYPE_ACTION_UPDATE_IS_WORKING:

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int idMsg = 0;
                    boolean isWorking = jsonObject.optBoolean(ParserJson.API_PARAMETER_AVAILABLE);
                    getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, isWorking);
                    if(isWorking){
                        initSocketOnEvent(requestListener);
                        idMsg = R.string.title_message_on_working;
                    }else {
                        initSocketOffEvent(requestListener);
                        idMsg = R.string.title_message_off_working;
                    }

                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(idMsg));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPostExecuteError(TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyType, msg);
        mSwipeRefreshLayout.setRefreshing(false);
        if(actionConnection == TypeActionConnection.TYPE_ACTION_UPDATE_IS_WORKING){
            getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, false);
            if (switch_on_off != null) {
                switch_on_off.setChecked(false);
            }

        }
        actionConnection = TypeActionConnection.TYPE_ACTION;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Bundle bundle = new Bundle();

                bundle.putParcelableArrayList(Const.KEY_BUNDLE_ACTION_FIELDS, lstSkill);
                AppCore.CallFragmentSectionWithCallback(Const.UI_MAP_NEARBY, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {
                        if (callback != null) {
                            callback.onHandlerCallBack(-1);
                        }
                        if(t != null && t.length > 0 && t[0] instanceof Boolean) {
                            boolean isRefreshList = (Boolean) t[0];
                            if (isRefreshList) {
                                initGetOrderCount(FMServiceList.this);
                            }
                        }
                    }
                });
                break;
        }
    }

    private void initFilterMyData(String response) {
//        if (isRunSocket) {
        boolean isCheckWorking = getPreferenceUtil(getmActivity()).getValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI);

        if (isCheckWorking) {
            initSocketOnEvent(requestListener);

            if (switch_on_off != null) {
                switch_on_off.setChecked(isCheckWorking);
            }
        } else {
            initSocketOffEvent(requestListener);
        }
//        }

        if (getVtcModelUser() != null && getVtcModelUser().getSkillsParent() != null) {

            lstSkill = (ArrayList<VtcModelUser.Skills>) getVtcModelUser().getSkillsParent();

            if (lstSkill != null && lstSkill.size() > 0) {

                for (int j = 0; j < lstSkill.size(); j++) {

                    VtcModelUser.Skills skills = lstSkill.get(j);

                    if (skills != null) {
                        skills.setCount(0);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.optJSONObject(i);

                                if (jsonObject != null) {

                                    String id = jsonObject.optString(ParserJson.API_PARAMETER_ID_);
                                    int count = jsonObject.optInt(ParserJson.API_PARAMETER_COUNT);

                                    if (id.equals(skills.get_id())) {
                                        skills.setCount(count);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            initReloadData(lstSkill);
        }
    }

//    public static void initFilterMyData(boolean isRunSocket) {
//        if(isRunSocket) {
//            boolean isCheckWorking = getPreferenceUtil(getmActivity()).getValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI);
//
//            if (isCheckWorking) {
//                initSocketOnEvent(requestListener);
//
//                if (switch_on_off != null) {
//                    switch_on_off.setChecked(isCheckWorking);
//                }
//            } else {
//                initSocketOffEvent(requestListener);
//            }
//        }
//
//        if (getVtcModelUser() != null && getVtcModelUser().getSkills() != null) {
//
//            List<VtcModelFields> lstFieldTmp = initGetListField();
//
//            if (lstFieldTmp != null && lstFieldTmp.size() > 0) {
//                List<VtcModelFields> lstFeild = new ArrayList<>();
//
//                List<VtcModelUser.Skills> lstSkill = getVtcModelUser().getSkills();
//
//                for (VtcModelFields fields : lstFieldTmp) {
//
//                    for (VtcModelUser.Skills skills : lstSkill) {
//
//                        if (fields.getId().contains(skills.get_id())) {
//                            lstFeild.add(fields);
//                        }
//                    }
//                }
//                initReloadData(lstFeild);
//            } else {
//                initReloadData(getVtcModelFields().getLstFieldsParent());
//            }
//        }
//    }
}
