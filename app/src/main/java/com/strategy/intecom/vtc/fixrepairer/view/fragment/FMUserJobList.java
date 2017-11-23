package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtMyJobLst;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtSearchHistory;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 6/16/16.
 */
public class FMUserJobList extends AppCore implements SwipeRefreshLayout.OnRefreshListener {

    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;
    private ImageView btn_home;
    private SearchView edt_search;

    private Callback callback;

    private ListView mRecyclerView;
    private AdtMyJobLst mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<VtcModelOrder> orderListMỵJob;
    private List<String> items;

    public FMUserJobList() {

    }

    @SuppressLint("ValidFragment")
    public FMUserJobList(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.ui_my_job_list, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<>();
        initControler(view);
    }

    private void initControler(View view){

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_home = (ImageView) view.findViewById(R.id.btn_home);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        mRecyclerView = (ListView) view.findViewById(R.id.my_recycler_view);

        tv_title.setText(getResources().getString(R.string.nav_item_job_list));
        btn_back.setVisibility(ImageView.VISIBLE);
        btn_home.setVisibility(ImageView.GONE);
        tv_title.setSelected(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

            edt_search = (SearchView) view.findViewById(R.id.edt_search);
            edt_search.setVisibility(SearchView.VISIBLE);

            edt_search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        }

        initEvent();
    }

    private void initEvent(){

        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            edt_search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (tv_title != null) {
                        if (hasFocus) {
                            tv_title.setVisibility(TextView.GONE);
                        }
                    }
                }
            });

            edt_search.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    if (tv_title != null) {
                        tv_title.setVisibility(TextView.VISIBLE);
                    }
                    return false;
                }
            });

            edt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(!query.isEmpty()) {
                        items.add(query);
                        initSearchJob(FMUserJobList.this, query, "20", "0", StatusBookingJob.STATUS_ACCEPTED.getValuesStatus() + "|" + StatusBookingJob.STATUS_COMING.getValuesStatus() + "|" + StatusBookingJob.STATUS_WORKING.getValuesStatus(), true);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    loadHistory(newText);
                    return false;
                }
            });
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        initData();
    }

    private void initData(){

        mAdapter = new AdtMyJobLst(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnClickItem(new AdtMyJobLst.onClickItem() {
            @Override
            public void onClickIten(VtcModelOrder modelOrder) {
                Bundle bundle = new Bundle();

                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ORDER, modelOrder);

                AppCore.CallFragmentSectionWithCallback(Const.UI_JOB_DETAIL, bundle, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {

                        initGetOrderLst(FMUserJobList.this, StatusBookingJob.STATUS_ACCEPTED.getValuesStatus() + "|" + StatusBookingJob.STATUS_WORKING.getValuesStatus() + "|" + StatusBookingJob.STATUS_COMING.getValuesStatus(), false);
                    }
                });
            }
        });

        initGetOrderLst(FMUserJobList.this, StatusBookingJob.STATUS_ACCEPTED.getValuesStatus() + "|" + StatusBookingJob.STATUS_WORKING.getValuesStatus() + "|" + StatusBookingJob.STATUS_COMING.getValuesStatus(), true);
    }

    private void loadHistory(String query) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor
            String[] columns = new String[]{"_id", "text"};
            Object[] temp = new Object[]{0, "default"};

            MatrixCursor cursor = new MatrixCursor(columns);

            for (int i = 0; i < items.size(); i++) {

                temp[0] = i;
                temp[1] = items.get(i);

                cursor.addRow(temp);
            }
            edt_search.setSuggestionsAdapter(new AdtSearchHistory(getActivity(), cursor, items));
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        initGetOrderLst(FMUserJobList.this, StatusBookingJob.STATUS_ACCEPTED.getValuesStatus() + "|" + StatusBookingJob.STATUS_WORKING.getValuesStatus() + "|" + StatusBookingJob.STATUS_COMING.getValuesStatus(), false);
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);

        switch (keyType){
            case TYPE_ACTION_SEARCH:
            case TYPE_ACTION_GET_ORDER_LIST:
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    orderListMỵJob = VtcModelOrder.getOrderData(jsonArray);
                    mAdapter.setData(orderListMỵJob);

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
    }

}
