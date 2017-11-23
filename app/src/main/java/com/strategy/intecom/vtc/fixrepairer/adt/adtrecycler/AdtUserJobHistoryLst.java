package com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;

import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtUserJobHistoryLst extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private StatusBookingJob statusBookingJob = StatusBookingJob.STATUS;

    private Activity context;

    private onClickItem onClickItem;

    private List<VtcModelOrder> lst = null;

    public void setOnClickItem(AdtUserJobHistoryLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public AdtUserJobHistoryLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public AdtUserJobHistoryLst(Activity context) {
        this.context = context;
    }

    public void setData(List<VtcModelOrder> lst, StatusBookingJob statusBookingJob){
        this.lst = lst;
        this.statusBookingJob = statusBookingJob;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_job_lst, parent, false);
            final ViewHolderItem vhItem = new ViewHolderItem(v);

            switch (statusBookingJob) {

                case STATUS_FINISH:

                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
                    vhItem.btn_promo.setBackgroundResource(R.drawable.ripple_selector_yellow_border);
                    vhItem.img_icon_heart.setVisibility(CheckBox.GONE);
                    vhItem.ratingBar.setVisibility(RatingBar.VISIBLE);

                    break;

                case STATUS_CANCEL:

                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
                    vhItem.btn_promo.setBackgroundResource(R.drawable.ripple_selector_gray_border);
                    vhItem.img_icon_heart.setVisibility(CheckBox.GONE);
                    vhItem.ratingBar.setVisibility(RatingBar.GONE);

                    break;
                case STATUS_EXPIRED:

                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
                    vhItem.btn_promo.setBackgroundResource(R.drawable.ripple_selector_black_border);
                    vhItem.btn_promo.setTextColor(Color.WHITE);
                    vhItem.img_icon_heart.setVisibility(CheckBox.GONE);
                    vhItem.ratingBar.setVisibility(RatingBar.GONE);

                    break;

            }

            return vhItem;
        } else if (viewType == TYPE_HEADER) {

            View vs = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_tools_lease_renter, parent, false);

            //inflate your layout and pass it to view holder
            ViewHolderHeader vhHeader = new ViewHolderHeader(vs);
            return vhHeader;
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItem) {
            final VtcModelOrder vtcModelOrder = getItem(position);

            final ViewHolderItem holderItem = (ViewHolderItem) holder;

        } else if (holder instanceof ViewHolderHeader) {
            //cast holder to VHHeader and set data for header.
        }
    }

    private VtcModelOrder getItem(int position) {
        if(lst == null) {
            return null;
        }
        return lst.get(position);
    }


    @Override
    public int getItemCount() {
        if(lst == null) {
            return 0;
        }
        return lst.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface onClickItem {
        void onClickIten();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {

        private View view;
        private TextView btn_promo;
        private CheckBox img_icon_heart;
        private RatingBar ratingBar;

        public ViewHolderItem(View itemView) {
            super(itemView);
            this.view = itemView;
            btn_promo = (TextView) itemView.findViewById(R.id.btn_promo);
            img_icon_heart = (CheckBox) itemView.findViewById(R.id.img_icon_heart);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {

        public ViewHolderHeader(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

    }
}

