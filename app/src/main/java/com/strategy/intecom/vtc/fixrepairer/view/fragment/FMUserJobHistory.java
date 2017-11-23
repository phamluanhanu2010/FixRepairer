package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtUserJobHistoryLst;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Mr. Ha on 6/16/16.
 */
public class FMUserJobHistory extends AppCore implements RecyclerView.OnItemTouchListener
        , SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;

    private int acceptAction = -1;

    private ListView mRecyclerView;
    private AdtUserJobHistoryLst mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

//    private FloatingActionButton fab;

    private Button btn_renter_list;
    private Button btn_renter;
    private Button btn_history;

    private LinearLayout lout_container_title;
    private TextView tv_message_empty;

    private StatusBookingJob statusBookingJob = StatusBookingJob.STATUS_FINISH;

    private List<VtcModelOrder> orderListHistorySuccess;
    private List<VtcModelOrder> orderListHistoryCancel;
    private List<VtcModelOrder> orderListHistoryReject;

    private Callback callback;

//    private boolean isGuide = Boolean.FALSE;

    @SuppressLint("ValidFragment")
    public FMUserJobHistory(Callback callback) {
        this.callback = callback;
    }

    public FMUserJobHistory() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_tools_lease_list, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControler(view);
    }

    private void initControler(View view) {

//        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        btn_back = (ImageView) view.findViewById(R.id.btn_back);

        tv_title = (TextView) view.findViewById(R.id.tv_title);

        lout_container_title = (LinearLayout) view.findViewById(R.id.lout_container_title);

        tv_message_empty = (TextView) view.findViewById(R.id.tv_message_empty);

//        isGuide = AppCore.getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_GUIDE_NEW_JOB);
//
//        if(!isGuide){

//            lout_container_title.setVisibility(LinearLayout.GONE);
//            tv_message_empty.setVisibility(TextView.VISIBLE);
//
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    AppCore.initToolsBar(getActivity(), false);
//                }
//            });
//
//            AppCore.CallFragmentSectionWithCallback(Const.UI_GUIDE_NEW_JOB, new Callback() {
//                @Override
//                public <T> void onHandlerCallBack(int key, T... t) {
//
//                    View headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//                            .inflate(R.layout.header_tools_lease_renter, mRecyclerView, false);
//                    mRecyclerView.addHeaderView(headerView);
//
//                    initGetOrderLst(FMUserJobHistory.this, statusBookingJob.getValuesStatus(), true);
//                }
//            }, true, false);
//            AppCore.getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_GUIDE_NEW_JOB, false);
//        }else {

            initGetOrderLst(FMUserJobHistory.this, statusBookingJob.getValuesStatus(), true);
//        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);


        mRecyclerView = (ListView) view.findViewById(R.id.my_recycler_view);

        btn_renter_list = (Button) view.findViewById(R.id.btn_renter_list);
        btn_renter = (Button) view.findViewById(R.id.btn_renter);
        btn_history = (Button) view.findViewById(R.id.btn_history);

        btn_renter_list.setBackgroundResource(R.drawable.selector_yellow_true);

        initEvent();
    }

//    private void initShowScreen(){
//        if ((orderListHistorySuccess == null || orderListHistorySuccess.size() <= 0) && (orderListHistoryCancel == null || orderListHistoryCancel.size() <= 0) && (orderListHistoryReject == null || orderListHistoryReject.size() <= 0)) {
//
////            fab.setVisibility(FloatingActionButton.GONE);
//
//            lout_container_title.setVisibility(LinearLayout.GONE);
//
//            tv_message_empty.setVisibility(TextView.VISIBLE);
//        }else {
//
////            fab.setVisibility(FloatingActionButton.VISIBLE);
//
//            lout_container_title.setVisibility(LinearLayout.VISIBLE);
//
//            tv_message_empty.setVisibility(TextView.GONE);
//        }
//    }

    private void initEvent(){

        btn_back.setOnClickListener(this);
        tv_title.setSelected(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);

//        mRecyclerView.addOnItemTouchListener(this);

        btn_renter_list.setOnClickListener(this);
        btn_renter.setOnClickListener(this);
        btn_history.setOnClickListener(this);

//        fab.setOnClickListener(this);

        initData();
    }

    private void initData() {

        tv_title.setText(getResources().getString(R.string.nav_item_job_history));
        mAdapter = new AdtUserJobHistoryLst(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        btn_renter_list.setText(getActivity().getResources().getString(R.string.btn_job_detail_success));
        btn_renter.setText(getActivity().getResources().getString(R.string.btn_new_job_cancel));
        btn_history.setText(getActivity().getResources().getString(R.string.btn_job_cancel));

//        fab.setVisibility(FloatingActionButton.GONE);

        mAdapter.setOnClickItem(new AdtUserJobHistoryLst.onClickItem() {
            @Override
            public void onClickIten(VtcModelOrder modelOrder) {

                Bundle bundle = new Bundle();

                bundle.putBoolean(Const.KEY_BUNDLE_ACTION_HISTORY, Boolean.TRUE);
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ORDER, modelOrder);

                AppCore.CallFragmentSection(Const.UI_JOB_DETAIL, bundle);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        switch (e.getAction()) {

            case MotionEvent.ACTION_UP:
                if (acceptAction == -1) {

                } else {
                    acceptAction = -1;
                }

                break;
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:
                acceptAction = MotionEvent.ACTION_MOVE;
                break;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        initDataAdater(null, statusBookingJob);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                AppCore.CallFragmentSection(Const.UI_LEASE_NEW);
                break;
            case R.id.btn_renter_list:
                initButtonTab(R.id.btn_renter_list);

                initDataAdater(orderListHistorySuccess, statusBookingJob);
                break;

            case R.id.btn_renter:
                initButtonTab(R.id.btn_renter);

                initDataAdater(orderListHistoryCancel, statusBookingJob);
                break;

            case R.id.btn_history:
                initButtonTab(R.id.btn_history);

                initDataAdater(orderListHistoryReject, statusBookingJob);
                break;

            case R.id.btn_back:
                cmdBack();
                break;
            default:
                return;
        }
    }

    private void initButtonTab(int idView) {
        mSwipeRefreshLayout.setRefreshing(false);

        btn_renter_list.setBackgroundResource(R.drawable.selector_yellow_false);
        btn_renter.setBackgroundResource(R.drawable.selector_yellow_false);
        btn_history.setBackgroundResource(R.drawable.selector_yellow_false);
        switch (idView) {
            case R.id.btn_renter_list:

                statusBookingJob = StatusBookingJob.STATUS_FINISH;

                btn_renter_list.setBackgroundResource(R.drawable.selector_yellow_true);
                break;

            case R.id.btn_renter:

                statusBookingJob = StatusBookingJob.STATUS_CANCEL;

                btn_renter.setBackgroundResource(R.drawable.selector_yellow_true);
                break;
            case R.id.btn_history:

                statusBookingJob = StatusBookingJob.STATUS_USER_CANCEL;

                btn_history.setBackgroundResource(R.drawable.selector_yellow_true);
                break;

        }
    }

    private void initDataAdater(List<VtcModelOrder> lst, StatusBookingJob statusBookingJob){
//        if(lst != null) {
//            mRecyclerView.removeAllViews();
//            mAdapter.setData(lst, statusBookingJob);
//        }else {
            initGetOrderLst(FMUserJobHistory.this, statusBookingJob.getValuesStatus(), false);
//        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        if (keyType == TypeActionConnection.TYPE_ACTION_GET_ORDER_LIST) {
            try {
                JSONArray jsonArray = new JSONArray(response);

                switch (statusBookingJob){
                    case STATUS_FINISH:
                        orderListHistorySuccess = VtcModelOrder.getOrderData(jsonArray);
                        mAdapter.setData(orderListHistorySuccess, statusBookingJob);
                        break;
                    case STATUS_CANCEL:
                        orderListHistoryCancel = VtcModelOrder.getOrderData(jsonArray);
                        mAdapter.setData(orderListHistoryCancel, statusBookingJob);
                        break;
                    case STATUS_USER_CANCEL:
                        orderListHistoryReject = VtcModelOrder.getOrderData(jsonArray);
                        mAdapter.setData(orderListHistoryReject, statusBookingJob);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if(!isGuide){
//
//                fab.setVisibility(FloatingActionButton.VISIBLE);
//
//                AppCore.initToolsBar(getActivity(), true);
//
//                if(mAdapter.getCount() > 0) {
//                    initShowScreen();
//                }
//
//                isGuide = AppCore.getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_GUIDE_NEW_JOB);
//            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onPostExecuteError(TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyType, msg);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
