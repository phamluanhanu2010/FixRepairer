package com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strategy.intecom.vtc.fixrepairer.R;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtJobLst extends RecyclerView.Adapter<AdtJobLst.ViewHolder> {
    private onClickItem onClickItem;

    public AdtJobLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtJobLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
        }
    }

    public AdtJobLst() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdtJobLst.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_job_lst, parent, false);
        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getOnClickItem().onClickIten();
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        if(holder.mTextView.getParent()!=null)
//            ((ViewGroup)holder.mTextView.getParent()).removeView(holder.mTextView);

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public interface onClickItem {
        void onClickIten();
    }

}

