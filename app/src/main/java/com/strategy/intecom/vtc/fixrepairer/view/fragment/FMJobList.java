package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtJobLst;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtSearchHistory;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
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
import java.util.List;

/**
 * Created by Mr. Ha on 5/19/16.
 */
public class FMJobList extends AppCore implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;
    private ImageView btn_home;
    private SearchView edt_search;

    private ListView lst_service;
    private AdtJobLst mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton fab;

    private LinearLayout lout_container_header;

    private TextView tv_sort;

    private Switch switch_on_off;

    private VtcModelUser.Skills modelFields;

    private final int loadDataOffset = 20;

    private Callback callback;

    private List<VtcModelOrder> orderList;

    private String sortBy;

    private List<String> items;

    private TypeActionConnection actionConnection = TypeActionConnection.TYPE_ACTION;

    public FMJobList() {

    }

    @SuppressLint("ValidFragment")
    public FMJobList(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_service_job_list, container, false);
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            modelFields = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_FIELDS);
        }
        items = new ArrayList<>();

        sortBy = "";

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
        btn_home = (ImageView) view.findViewById(R.id.btn_home);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        lst_service = (ListView) view.findViewById(R.id.lst_service);

        switch_on_off = (Switch) view.findViewById(R.id.switch_on_off);

        lout_container_header = (LinearLayout) view.findViewById(R.id.lout_container_header);

        tv_sort = (TextView) view.findViewById(R.id.tv_sort);

        btn_back.setVisibility(ImageView.VISIBLE);
        btn_home.setVisibility(ImageView.GONE);
        tv_title.setSelected(true);
        tv_sort.setOnClickListener(this);

        if(modelFields != null) {
            tv_title.setText(modelFields.getName());
            initCallApi(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

            edt_search = (SearchView) view.findViewById(R.id.edt_search);
            edt_search.setVisibility(SearchView.VISIBLE);

            edt_search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        }

        initEvent();
    }

    private void initEvent() {

        mSwipeRefreshLayout.setOnRefreshListener(this);

        float h = getResources().getDimension(R.dimen.control_input_text_h);

        View headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.tmp_textview, lst_service, false);

        AbsListView.LayoutParams tv_header_params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, (int) (h * 2));
        headerView.setLayoutParams(tv_header_params);

        lst_service.addHeaderView(headerView);

        lst_service.setOnScrollListener(new QuickReturnListViewOnScrollListener(
                QuickReturnViewType.HEADER, lout_container_header, (int) (-(h * 2)), null, 0, true) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

            }

            @Override
            public void onScroll(AbsListView listview, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(listview, firstVisibleItem, visibleItemCount, totalItemCount);
                if ((firstVisibleItem + visibleItemCount >= (totalItemCount - loadDataOffset))) {

                }
            }
        });

        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) ((h * 2) + Utils.convertDpToPixels(7, getmActivity())));

        switch_on_off.setChecked(getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI));

        switch_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVtcModelUser() != null) {

                    actionConnection = TypeActionConnection.TYPE_ACTION_UPDATE_IS_WORKING;
                    initUpdateIsWorking(FMJobList.this, getVtcModelUser().getId(), switch_on_off.isChecked());
                }

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                cmdBack();
            }
        });


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
                        initSearchJob(FMJobList.this, query, "20", "0", StatusBookingJob.STATUS_FINDING.getValuesStatus(), true);
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

        initData();
    }

    private void initData() {

        mAdapter = new AdtJobLst(getActivity());
        mAdapter.setOnClickItem(new AdtJobLst.onClickItem() {
            @Override
            public void onClickIten(VtcModelOrder vtcModelOrder) {
                Bundle bundle = new Bundle();

                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ORDER, vtcModelOrder);

                AppCore.CallFragmentSectionWithCallback(Const.UI_JOB_DETAIL, bundle, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {

                        if (modelFields != null) {
                            initCallApi(false);
                        }
                    }
                });
            }
        });
        lst_service.setAdapter(mAdapter);
        lst_service.setDivider(null);
        lst_service.setDividerHeight(0);

        fab.setVisibility(FloatingActionButton.GONE);

        tv_sort.setText(Html.fromHtml(getResources().getString(R.string.title_sort_by, Utils.initTextBold(getResources().getString(R.string.title_sort_by_all)))));
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        initCallApi(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        switch_on_off.setChecked(getPreferenceUtil(getActivity()).getValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI));
    }

    private void initCallApi(boolean isShowProcess){
        actionConnection = TypeActionConnection.TYPE_ACTION;
        initGetOrderLst(FMJobList.this, "0", VtcModelOrder.TYPE_ORDER_NORMAL, modelFields.get_id(), StatusBookingJob.STATUS_FINDING, sortBy, isShowProcess);
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
    public void onDestroyView() {
        if (callback != null) {
            callback.onHandlerCallBack(-1);
        }
        super.onDestroyView();
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        actionConnection = TypeActionConnection.TYPE_ACTION;
        switch (keyType){
            case TYPE_ACTION_SEARCH:
            case TYPE_ACTION_GET_ORDER_LIST:
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    orderList = VtcModelOrder.getOrderData(jsonArray);

                    mAdapter.setDataFieldLst(orderList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_ACTION_UPDATE_IS_WORKING:

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int idMsg;
                    boolean isWorking = jsonObject.optBoolean(ParserJson.API_PARAMETER_AVAILABLE);
                    getPreferenceUtil(getActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, isWorking);
                    if(isWorking){
                        initSocketOnEvent(FMJobList.this);
                        idMsg = R.string.title_message_on_working;
                    }else {
                        initSocketOffEvent(FMJobList.this);
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
            actionConnection = TypeActionConnection.TYPE_ACTION;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sort:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_SHORTED_BY);
                break;
        }
    }

    @Override
    protected void cmdOnClickSort(String charShort) {
        super.cmdOnClickSort(charShort);
        sortBy = charShort;
        if(charShort.isEmpty()){
            tv_sort.setText(Html.fromHtml(getResources().getString(R.string.title_sort_by, "<b>" + getResources().getString(R.string.title_sort_by_all) + "</b>")));
        }else if(charShort.equals(Const.TYPE_SORT_BY_TIME)){
            tv_sort.setText(Html.fromHtml(getResources().getString(R.string.title_sort_by, "<b>" + getResources().getString(R.string.title_sort_by_new) + "</b>")));
        }else if(charShort.equals(Const.TYPE_SORT_BY_DISTANCE)){
            tv_sort.setText(Html.fromHtml(getResources().getString(R.string.title_sort_by, "<b>" + getResources().getString(R.string.title_sort_by_nearby) + "</b>")));
        }
        initCallApi(true);
    }

}
