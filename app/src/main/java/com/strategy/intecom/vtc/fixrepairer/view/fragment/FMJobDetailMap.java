package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/19/16.
 */
@SuppressLint("ValidFragment")
public class FMJobDetailMap extends AppCore {

    private View viewRoot;

    private ImageView btn_back;
    private ImageView btn_my_location;

    private EditText edt_search;
    private TextView tv_title_price;
    private TextView tv_title_time;
    private TextView tv_title_job;
    private TextView tv_title_status;
    private ImageView ing_search;

//    private LinearLayout lout_container_accept_job;

    private boolean isRefreshList = Boolean.FALSE;

    private VtcModelOrder vtcModelOrder;

    private GPSTracker gpsTracker;

    private Callback callback;

    public FMJobDetailMap(Callback callback) {
        this.callback = callback;
    }

    public FMJobDetailMap() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            vtcModelOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_ORDER);

        }
        viewRoot = inflater.inflate(R.layout.ui_job_list_detail_map, container, false);
        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        onMapInitializer(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gpsTracker = getGpsTracker(getActivity());
        onMapinitMapView(view, savedInstanceState, getActivity(), new double[]{gpsTracker.getLatitude(), gpsTracker.getLongitude()});

        init();

        initController(view);
    }

    private void init(){
        if(gpsTracker == null || gpsTracker.getLatitude() <= 0.0f || gpsTracker.getLongitude() <= 0.0f){
            gpsTracker = getGpsTracker(getActivity());
        }

        if (vtcModelOrder != null && vtcModelOrder.getAddress() != null) {

//            VtcModelUser.Address address = null;
//            if (getVtcModelUser() != null) {
//                address = getVtcModelUser().getAddress();
//            }

//            String strAddress = "";
//            if (address != null) {
//                strAddress = address.getName();
//            }

            double lat = 0.0f;
            double longt = 0.0f;

            try {
                lat = Double.parseDouble(vtcModelOrder.getAddress().getSlat());
                longt = Double.parseDouble(vtcModelOrder.getAddress().getSlong());

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            initShowDistance(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), getmActivity().getResources().getString(R.string.title_my_location), new LatLng(lat, longt), vtcModelOrder.getAddress().getName(), "");
        }
    }

//    @Override
//    protected void cmdOnRefreshLocation() {
//        super.cmdOnRefreshLocation();
//        init();
//    }

    private void initController(View view) {

        btn_my_location = (ImageView) view.findViewById(R.id.btn_my_location);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        ing_search = (ImageView) view.findViewById(R.id.ing_search);
        tv_title_status = (TextView) view.findViewById(R.id.tv_title_status);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        tv_title_price = (TextView) view.findViewById(R.id.tv_title_price);
        tv_title_time = (TextView) view.findViewById(R.id.tv_title_time);
        tv_title_job = (TextView) view.findViewById(R.id.tv_title_job);
//        lout_container_accept_job = (LinearLayout) view.findViewById(R.id.lout_container_accept_job);
        tv_title_job.setSelected(true);
        ing_search.setVisibility(ImageView.GONE);
        edt_search.setEnabled(false);

        initButton();

        initEvent();
    }

    private void initButton() {
        if (vtcModelOrder != null && getVtcModelUser() != null) {
            String status = vtcModelOrder.getStatus();
            if (status.equals(StatusBookingJob.STATUS_FINDING.getValuesStatus())) {
                tv_title_status.setBackgroundResource(R.color.color_yellow);
                tv_title_status.setText(getResources().getString(R.string.btn_job_detail_nhanviec));
            } else if (status.equals(StatusBookingJob.STATUS_WORKING.getValuesStatus())) {
                tv_title_status.setBackgroundColor(Color.GREEN);
                tv_title_status.setText(getResources().getString(R.string.btn_job_go_run_));
            } else if (status.equals(StatusBookingJob.STATUS_FINISH.getValuesStatus())) {
                tv_title_status.setBackgroundColor(Color.GREEN);
                tv_title_status.setText(getResources().getString(R.string.btn_job_detail_success));
            } else if (status.equals(StatusBookingJob.STATUS_EXPIRED.getValuesStatus())) {
                tv_title_status.setBackgroundColor(Color.RED);
                tv_title_status.setText(getResources().getString(R.string.btn_job_expried));
            } else if (status.equals(StatusBookingJob.STATUS_CANCEL.getValuesStatus())) {
                tv_title_status.setBackgroundColor(Color.RED);
                tv_title_status.setText(getResources().getString(R.string.btn_job_cancel_));
            } else if (status.equals(StatusBookingJob.STATUS_ACCEPTED.getValuesStatus())) {
                tv_title_status.setBackgroundColor(Color.GREEN);
                tv_title_status.setText(getResources().getString(R.string.btn_job_detail_danhanviec));
            } else if (status.equals(StatusBookingJob.STATUS_COMING.getValuesStatus())) {
                tv_title_status.setBackgroundColor(Color.GREEN);
                tv_title_status.setText(getResources().getString(R.string.btn_job_comming_ok));
            }
        }
    }

    private void initEvent() {

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_title_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vtcModelOrder != null && getVtcModelUser() != null) {

                    String status = vtcModelOrder.getStatus();
                    if (status.equals(StatusBookingJob.STATUS_FINDING.getValuesStatus())) {
                        initAcceptOrder(FMJobDetailMap.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                    } else if (status.equals(StatusBookingJob.STATUS_WORKING.getValuesStatus())) {
                        initUpdateOrdeFinished(FMJobDetailMap.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                    } else if (status.equals(StatusBookingJob.STATUS_ACCEPTED.getValuesStatus())) {
                        initUpdateOrdeComing(FMJobDetailMap.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                    } else if (status.equals(StatusBookingJob.STATUS_COMING.getValuesStatus())) {
                        initUpdateOrdeWorking(FMJobDetailMap.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                    }

                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        btn_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyLocation(getActivity());
            }
        });

        initData();
    }

    private void initData() {
        if (vtcModelOrder != null) {

            String strPrice = vtcModelOrder.getField() == null ? "0.0" : vtcModelOrder.getField().getPrice();

            setFormatCurrency(tv_title_price, strPrice);

            edt_search.setText(vtcModelOrder.getAddress() == null ? "" : vtcModelOrder.getAddress().getName());
            tv_title_time.setText(Utils.initConvertTimeDisplayShort(vtcModelOrder.getOrder_time()));
            tv_title_job.setText(vtcModelOrder.getDescription());
        }
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
    protected void cmdOnClickMarkerMap(final VtcModelOrder vtcModelOrder) {
        super.cmdOnClickMarkerMap(vtcModelOrder);

    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType) {
            case TYPE_ACTION_ORDER_ACCEPT:
//                if (callback != null && vtcModelOrder != null) {
//                    callback.onHandlerCallBack(-1, vtcModelOrder, isRefreshList);
//                }
                if (vtcModelOrder != null) {
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_ACCEPTED.getValuesStatus());
                }
                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;
            case TYPE_ACTION_ORDER_UPDATE_WORKING:
                if (vtcModelOrder != null) {
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_WORKING.getValuesStatus());
                }
                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;
            case TYPE_ACTION_ORDER_UPDATE_FINISHED:
                AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");
                if (vtcModelOrder != null) {
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_FINISH.getValuesStatus());

                    if (vtcModelOrder.getType().equals(VtcModelOrder.TYPE_ORDER_FAST)) {
                        AppCore.getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, true);
                    }
                }

                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;
            case TYPE_ACTION_ORDER_UPDATE_COMING:
                if (vtcModelOrder != null) {
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_COMING.getValuesStatus());
                }
                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;
        }

        initButton();
    }

    @Override
    public void onDestroyView() {
        if (callback != null && vtcModelOrder != null) {
            callback.onHandlerCallBack(-1, vtcModelOrder, isRefreshList);
        }
        Const.UI_CURRENT_CONTEXT = Const.UI_JOB_DETAIL;
        super.onDestroyView();
    }


    @Override
    protected void cmdOnUserCancelJob() {
        super.cmdOnUserCancelJob();
        AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");

        getmActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}