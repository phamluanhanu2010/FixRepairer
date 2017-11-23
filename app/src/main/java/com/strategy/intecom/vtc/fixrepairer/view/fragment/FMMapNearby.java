package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler.AdtListSkillMap;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/19/16.
 */
public class FMMapNearby extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private ImageView btn_back;
    private ImageView btn_my_location;

    private TextView tv_title_status;

    private TextView tv_title_price;
    private TextView tv_title_time;

    private LinearLayout lout_container_info_job;

    private List<VtcModelOrder> orderList;

    private VtcModelOrder vtcModelOrder;

    private RecyclerView mRecyclerView;

    private AdtListSkillMap mAdapter;

    private GPSTracker gpsTracker;

    private ArrayList<VtcModelUser.Skills> lstSkill;

    private Callback callback;

    private Bundle savedInstanceState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            vtcModelOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_ORDER);
            lstSkill = savedInstanceState.getParcelableArrayList(Const.KEY_BUNDLE_ACTION_FIELDS);
        }
        viewRoot = inflater.inflate(R.layout.ui_map_nearby, container, false);
        return viewRoot;
    }

    @SuppressLint("ValidFragment")
    public FMMapNearby(Callback callback) {
        this.callback = callback;
    }


    public FMMapNearby() {
    }

    @Override
    public void onStart() {
        super.onStart();
        onMapInitializer(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        gpsTracker = getGpsTracker(getActivity());

        init();

        initController(view);
    }

    private void init() {

        if (gpsTracker == null || gpsTracker.getLatitude() <= 0.0f || gpsTracker.getLongitude() <= 0.0f) {
            gpsTracker = getGpsTracker(getActivity());
        }

        onMapinitMapView(viewRoot, savedInstanceState, getActivity(), new double[]{gpsTracker.getLatitude(), gpsTracker.getLongitude()}, "", true);

        initGetOrderLst(FMMapNearby.this, StatusBookingJob.STATUS_FINDING, Const.TYPE_SORT_BY_DISTANCE, false);

        initActionDragMap();
    }

    private void initController(View view) {

        btn_my_location = (ImageView) view.findViewById(R.id.btn_my_location);
        lout_container_info_job = (LinearLayout) view.findViewById(R.id.lout_container_info_job);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title_status = (TextView) view.findViewById(R.id.tv_title_status);

        tv_title_price = (TextView) view.findViewById(R.id.tv_title_price);
        tv_title_time = (TextView) view.findViewById(R.id.tv_title_time);

        tv_title_time.setSelected(true);
        tv_title_price.setSelected(true);

        lout_container_info_job.setVisibility(LinearLayout.GONE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.lst_skill);

        btn_my_location.setOnClickListener(this);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdtListSkillMap(getmActivity());

        if (lstSkill == null || lstSkill.size() <= 0) {
            initGetOrderCount(FMMapNearby.this);
        }else {
            mAdapter.setData(lstSkill);
            mRecyclerView.setAdapter(mAdapter);
        }


        initEvent();
    }

    private void initEvent() {
        if(mAdapter != null) {
            mAdapter.setOnClickItemSkill(new AdtListSkillMap.onClickItemSkill() {
                @Override
                public void onClickItemSkill(VtcModelUser.Skills modelFields) {
                    if (lout_container_info_job.getVisibility() == LinearLayout.VISIBLE) {
                        initAnimationHeader(lout_container_info_job, true);
                    }
                    initGetOrderLst(FMMapNearby.this, VtcModelOrder.TYPE_ORDER_NORMAL, modelFields.get_id(), StatusBookingJob.STATUS_FINDING, Const.TYPE_SORT_BY_DISTANCE);
                }
            });
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        tv_title_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vtcModelOrder != null) {
                    Bundle bundle = new Bundle();

                    bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ORDER, vtcModelOrder);

                    AppCore.CallFragmentSectionWithCallback(Const.UI_JOB_DETAIL, bundle, new Callback() {
                        @Override
                        public <T> void onHandlerCallBack(int key, T... t) {
                            boolean isRefreshList = Boolean.FALSE;
                            if (t != null && t.length > 0 && t[0] instanceof Boolean) {
                                isRefreshList = (Boolean) t[0];
                            }
                            if (callback != null) {
                                callback.onHandlerCallBack(-1, isRefreshList);
                            }
                        }
                    });
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getResources().getString(R.string.confirm_message_choice_job));
                }
            }
        });
    }

    @Override
    public void onResume() {
        onMapResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onMapDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onMapLowMemory();
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType) {

            case TYPE_ACTION_GET_COUNT_SKILL:

                initFilterMyData(response);
                break;
            case TYPE_ACTION_GET_ORDER_LIST:
                try {
                    onMapClear();

                    initAddMyMarker(new double[]{gpsTracker.getLatitude(), gpsTracker.getLongitude()}, getmActivity().getResources().getString(R.string.title_my_location), "");

                    JSONArray jsonArray = new JSONArray(response);

                    orderList = VtcModelOrder.getOrderData(jsonArray);

                    if (orderList != null) {
                        Activity activity = getmActivity();
                        if (activity == null) {
                            activity = getActivity();
                        }

                        for (final VtcModelOrder vtcModelOrder : orderList) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initShowAllJob(vtcModelOrder);
                                }
                            });
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case TYPE_ACTION_HIRE_LIST:

                break;
        }
    }

    @Override
    protected void cmdOnClickMarkerMap(final VtcModelOrder vtcModelOrder) {
        this.vtcModelOrder = vtcModelOrder;
        if (vtcModelOrder != null) {

            String strPrice = vtcModelOrder.getField() == null ? "0.0" : vtcModelOrder.getField().getPrice();

            setFormatCurrency(tv_title_price, strPrice);

            tv_title_time.setText(Utils.initConvertTimeDisplayShort(vtcModelOrder.getOrder_time()));

            VtcModelOrder.Address address = vtcModelOrder.getAddress();

            if (address != null) {

                initShowDrawStreest(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), new LatLng(Double.parseDouble(address.getSlat()), Double.parseDouble(address.getSlong())));

                initAnimationHeader(lout_container_info_job, false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lout_container_accept_job:
                if (vtcModelOrder == null) {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getResources().getString(R.string.confirm_message_choice_job));
                }

                break;
            case R.id.btn_my_location:
                goToMyLocation(getActivity());
                break;
        }
    }

    public void initAnimationHeader(final LinearLayout linearLayout, final boolean isAnimation) {
        // the popover is showing, hide it.
        TranslateAnimation animation = null;
        if (isAnimation) {
            animation = new TranslateAnimation(0, 0, 0, -150);
        } else {
            animation = new TranslateAnimation(0, 0, -150, 0);
        }
        animation.setDuration(700);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (isAnimation) {
                    linearLayout.setVisibility(TextView.GONE);
                } else {
                    linearLayout.setVisibility(TextView.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });

        linearLayout.startAnimation(animation);
    }

    @Override
    protected void cmdOnClickViewDetail() {
        super.cmdOnClickViewDetail();
        if (vtcModelOrder != null) {
            Bundle bundle = new Bundle();

            bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ORDER, vtcModelOrder);

            AppCore.CallFragmentSectionWithCallback(Const.UI_JOB_DETAIL, bundle, new Callback() {
                @Override
                public <T> void onHandlerCallBack(int key, T... t) {
                    boolean isRefreshList = Boolean.FALSE;
                    if (t != null && t.length > 0 && t[0] instanceof Boolean) {
                        isRefreshList = (Boolean) t[0];
                    }

//                    if(isRefreshList){
//
//                        initGetOrderLst(FMMapNearby.this, StatusBookingJob.STATUS_FINDING, Const.TYPE_SORT_BY_DISTANCE, false);
//
//                        if(mRecyclerView != null){
//                            mRecyclerView.removeAllViews();
//                        }
//
//                        if(mAdapter != null){
//                            mAdapter.setData(lstSkill);
//
//                            mRecyclerView.setAdapter(mAdapter);
//                        }
//
//                    }
                    if (callback != null) {
                        callback.onHandlerCallBack(-1, isRefreshList);
                    }
                }
            });
        }
    }
    private void initFilterMyData(String response) {

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
            if(mRecyclerView != null){
                mRecyclerView.removeAllViews();
            }

            if(mAdapter != null){
                mAdapter.setData(lstSkill);

                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }
}