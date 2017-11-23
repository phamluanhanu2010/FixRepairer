package com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtToolsLst extends RecyclerView.Adapter<AdtToolsLst.ViewHolder> {
    private onClickItem onClickItem;

    public AdtToolsLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtToolsLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_avatar;
        public TextView tv_title;
        public TextView tv_content;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            img_avatar = (ImageView) itemLayoutView.findViewById(R.id.img_avatar);
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemLayoutView.findViewById(R.id.tv_content);
        }
    }

    public AdtToolsLst() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdtToolsLst.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_service_lst, parent, false);
        ViewHolder vh = new ViewHolder(v);

//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                getOnClickItem().onClickIten();
//            }
//        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        if(holder.mTextView.getParent()!=null)
//            ((ViewGroup)holder.mTextView.getParent()).removeView(holder.mTextView);

//        holder.tv_title.setText(mDataset[position]);
//        holder.tv_content.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public interface onClickItem {
        void onClickIten();
    }

}

