package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler.AdtToolsLeaseLst;
import com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler.AdtToolsRenterLst;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/24/16.
 */
public class FMToolsLeaseList extends AppCore implements RecyclerView.OnItemTouchListener
        , SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, View.OnTouchListener {
    private View viewRoot;

    private int acceptAction = -1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FloatingActionButton fab;

    private Button btn_renter_list;
    private Button btn_renter;
    private Button btn_history;

    private Callback callback;

    @SuppressLint("ValidFragment")
    public FMToolsLeaseList(Callback callback) {
        this.callback = callback;
    }

    public FMToolsLeaseList() {

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
        boolean isGuide = AppCore.getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_GUIDE_NEW_JOB);

        if (!isGuide) {
            AppCore.CallFragmentSection(Const.UI_GUIDE_NEW_JOB, true, false);
            AppCore.getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_GUIDE_NEW_JOB, true);
        }

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        btn_renter_list = (Button) view.findViewById(R.id.btn_renter_list);
        btn_renter = (Button) view.findViewById(R.id.btn_renter);
        btn_history = (Button) view.findViewById(R.id.btn_history);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getmActivity()));


        initEvent();
    }

    private void initEvent(){

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnItemTouchListener(this);

        fab.setOnClickListener(this);

        btn_renter_list.setFocusableInTouchMode(true);
        btn_renter.setFocusableInTouchMode(true);
        btn_history.setFocusableInTouchMode(true);

        btn_renter_list.setOnClickListener(this);
        btn_renter.setOnClickListener(this);
        btn_history.setOnClickListener(this);

        btn_renter_list.setOnTouchListener(this);
        btn_renter.setOnTouchListener(this);
        btn_history.setOnTouchListener(this);

        initData();
    }

    private void initData() {

        mAdapter = new AdtToolsLeaseLst();
        mRecyclerView.setAdapter(mAdapter);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                AppCore.CallFragmentSection(Const.UI_LEASE_NEW, true);
                break;

            default:
                return;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            switch (v.getId()) {
                case R.id.btn_renter_list:

                    mAdapter = new AdtToolsLeaseLst();
                    break;

                case R.id.btn_renter:

                    mAdapter = new AdtToolsRenterLst(getActivity(), Const.TYPE_DISPLAY_JOB_RENTER);
                    break;

                case R.id.btn_history:

                    mAdapter = new AdtToolsRenterLst(getActivity(), Const.TYPE_DISPLAY_JOB_HISTORY);
                    break;
            }

            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerView.removeAllViews();
            mRecyclerView.setAdapter(mAdapter);

        }

        return false;
    }

}
