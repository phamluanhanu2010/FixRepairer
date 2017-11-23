package com.strategy.intecom.vtc.fixrepairer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler.AdtToolsLst;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlToolsList extends Dialog implements RecyclerView.OnItemTouchListener
        , SwipeRefreshLayout.OnRefreshListener{

    private Context context;

    private int acceptAction = -1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    int width;
    int height;

    public void setSize(int width, int height){

        this.width = width;
        this.height = height;
    }

    public DlToolsList(Context context, int width, int height) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        getWindow().setAttributes(lp);

        setContentView(R.layout.dl_tools_list);

        initControler();

    }

    public void initControler() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.addOnItemTouchListener(this);

        initData();
    }

    private void initData(){

        mAdapter = new AdtToolsLst();
        mRecyclerView.setAdapter(mAdapter);

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
}
