package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtRuleLevel;
import com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler.AdtJuleUpgrade;
import com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler.AdtToolsLeaseLst;
import com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler.AdtToolsRenterLst;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelRating;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mr. Ha on 5/24/16.
 */
public class FMUserProfileUpgradeLevel extends AppCore implements RecyclerView.OnItemTouchListener
        , SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, View.OnTouchListener {

    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;

    private int acceptAction = -1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Button btn_my_level;
    private Button btn_rule_upgrade;

    private ListView lv_level;
    private ListView lv_jule;

    private AdtRuleLevel adtRuleLevel;
    private AdtRuleLevel adtRuleLevelJob;

    private ScrollView lout_container_rule;
    private LinearLayout lout_container_level;

    public FMUserProfileUpgradeLevel() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_user_profile_upgrade_level, container, false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initController(view);
    }

    private void initController(View view) {

        btn_back = (ImageView) view.findViewById(R.id.btn_back);

        tv_title = (TextView) view.findViewById(R.id.tv_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getmActivity()));

        btn_my_level = (Button) view.findViewById(R.id.btn_my_level);
        btn_rule_upgrade = (Button) view.findViewById(R.id.btn_rule_upgrade);

        lv_jule = (ListView) view.findViewById(R.id.lv_jule);
        lv_level = (ListView) view.findViewById(R.id.lv_level);

        lout_container_rule = (ScrollView) view.findViewById(R.id.lout_container_rule);
        lout_container_level = (LinearLayout) view.findViewById(R.id.lout_container_level);

        initEvent();
    }

    private void initEvent(){

        tv_title.setSelected(true);

        btn_back.setOnClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnItemTouchListener(this);

        btn_my_level.setOnClickListener(this);
        btn_rule_upgrade.setOnClickListener(this);

        btn_my_level.setOnTouchListener(this);
        btn_rule_upgrade.setOnTouchListener(this);

        btn_my_level.setFocusableInTouchMode(true);
        btn_rule_upgrade.setFocusableInTouchMode(true);

        initData();
    }

    private void initData(){

        tv_title.setText(getResources().getString(R.string.btn_upgrade_level_rule));

        String strJobName[] = getActivity().getResources().getStringArray(R.array.array_rating_level);

        // Dump data
        List<VtcModelRating> lstLevel = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            VtcModelRating rating = new VtcModelRating();
            rating.setUserID(new Random().nextInt(1000));
            rating.setrDescription(i + " Sửa máy tính, cài đặt lại hệ điều hành");
            rating.setrName("Nhận việc " + i);
            rating.setrLevelRepairer(0);
            rating.setrRatingCount((i % 2 == 0) ? ("+" + i) : ("-" + i));
            lstLevel.add(rating);
        }

        List<VtcModelRating> lstRule = new ArrayList<>();

        for (int i = 0; i < strJobName.length; i++){
            VtcModelRating rating = new VtcModelRating();
            rating.setUserID(new Random().nextInt(1000));
            rating.setrDescription(i + " Sửa máy tính, cài đặt lại hệ điều hành");
            rating.setrName(strJobName[i]);
            rating.setrLevelRepairer(i + 1);
            rating.setrRatingCount((i % 2 == 0) ? ("+" + i) : ("-" + i));
            lstRule.add(rating);
        }

        mAdapter = new AdtJuleUpgrade();
        mRecyclerView.setAdapter(mAdapter);

        adtRuleLevel = new AdtRuleLevel(getActivity(), lstLevel);
        lv_level.setAdapter(adtRuleLevel);

        adtRuleLevelJob = new AdtRuleLevel(getActivity(), lstRule);
        lv_jule.setAdapter(adtRuleLevelJob);

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        switch (e.getAction()) {

            case MotionEvent.ACTION_UP:
                if(acceptAction == -1) {

                }else {
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
        switch (v.getId()){
            case R.id.btn_back:
                cmdBack();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            switch (v.getId()) {
                case R.id.btn_my_level:
                    lout_container_rule.setVisibility(ScrollView.GONE);
                    lout_container_level.setVisibility(LinearLayout.VISIBLE);
                    break;

                case R.id.btn_rule_upgrade:
                    lout_container_rule.setVisibility(ScrollView.VISIBLE);
                    lout_container_level.setVisibility(LinearLayout.GONE);
                    break;
            }
        }
        return false;
    }
}
