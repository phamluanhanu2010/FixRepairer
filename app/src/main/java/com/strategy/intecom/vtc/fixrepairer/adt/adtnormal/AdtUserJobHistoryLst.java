package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtUserJobHistoryLst extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

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
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<VtcModelOrder> lst, StatusBookingJob statusBookingJob){
        this.lst = null;
        this.lst = lst;
        this.statusBookingJob = statusBookingJob;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (lst == null) {
            return 0;
        }
        return lst.size();
    }

    @Override
    public VtcModelOrder getItem(int position) {
        if (lst == null) {
            return null;
        }
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        if (getItemViewType(position) == TYPE_ITEM) {

        final ViewHolderItem vhItem;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.tmp_job_lst, parent, false);
            vhItem = new ViewHolderItem(convertView);
        } else {
            vhItem = (ViewHolderItem) convertView.getTag();
        }

        final VtcModelOrder modelOrder = getItem(position);

        if (modelOrder != null) {

            String title = "";
            if(modelOrder.getField() != null ){
                title += modelOrder.getField().getCategory_name() + " - " + modelOrder.getField().getName();
            }

            vhItem.tv_times.setText(Utils.initConvertTimeDisplayLong(modelOrder.getOrder_time()));
            vhItem.tv_title.setText(title);
            vhItem.tv_location.setText(modelOrder.getAddress() == null ? "" : modelOrder.getAddress().getName() );
            vhItem.tv_name.setText(modelOrder.getUser() == null ? "" : modelOrder.getUser().getName());

            String strPrice = modelOrder.getField() == null ? "0.0" : modelOrder.getField().getPrice();

            AppCore.setFormatCurrency(vhItem.tv_price, strPrice);

            vhItem.img_icon_heart.setVisibility(CheckBox.GONE);
            vhItem.ratingBar.setVisibility(RatingBar.GONE);
//            switch (statusBookingJob) {
//
//                case STATUS_FINISH:
//
//                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
//                    vhItem.btn_promo.setBackgroundResource(R.drawable.ripple_selector_yellow_border);
//                    vhItem.img_icon_heart.setVisibility(CheckBox.GONE);
//                    vhItem.ratingBar.setVisibility(RatingBar.GONE);
//                    vhItem.ratingBar.setRating(modelOrder.getRate());
//                    if(modelOrder.getCoupon_code() != null && !modelOrder.getCoupon_code().getCode().isEmpty()) {
//                        vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_new_job_promo));
//                    }else {
//                        vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_job_detail_success));
//                    }
//                    break;
//                case STATUS_CANCEL:
//
//                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
//                    vhItem.btn_promo.setBackgroundResource(R.drawable.ripple_selector_gray_border);
//                    vhItem.img_icon_heart.setVisibility(CheckBox.GONE);
//                    vhItem.ratingBar.setVisibility(RatingBar.GONE);
//
//                    vhItem.img_icon_heart.setChecked(modelOrder.isSave());
//                    if(modelOrder.getCoupon_code() != null && !modelOrder.getCoupon_code().getCode().isEmpty()) {
//                        vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_new_job_promo));
//                    }else {
//                        vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_new_job_cancel));
//                    }
//                    break;
//                case STATUS_USER_CANCEL:
//
//                    vhItem.btn_promo.setVisibility(TextView.VISIBLE);
//                    vhItem.btn_promo.setBackgroundResource(R.drawable.ripple_selector_black_border);
//                    vhItem.btn_promo.setTextColor(Color.WHITE);
//                    vhItem.img_icon_heart.setVisibility(CheckBox.GONE);
//                    vhItem.ratingBar.setVisibility(RatingBar.GONE);
//
//                    vhItem.img_icon_heart.setChecked(modelOrder.isSave());
//                    if(modelOrder.getCoupon_code() != null && !modelOrder.getCoupon_code().getCode().isEmpty()) {
//                        vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_new_job_promo));
//                    }else {
//                        vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_job_cancel));
//                    }
//                    break;
//            }

            if(modelOrder.getCoupon_code() != null && !modelOrder.getCoupon_code().getCode().isEmpty()) {
                vhItem.btn_promo.setVisibility(TextView.VISIBLE);
                vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_new_job_promo));
            }else {
                vhItem.btn_promo.setVisibility(TextView.INVISIBLE);
//                vhItem.btn_promo.setText(context.getResources().getText(R.string.btn_job_detail_success));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnClickItem().onClickIten(modelOrder);
                }
            });
        }
        return convertView;
    }

    public interface onClickItem {
        void onClickIten(VtcModelOrder service);
    }

    public static class ViewHolderItem {

        private View view;
        private TextView btn_promo;
        private TextView tv_times;
        private TextView tv_title;
        private TextView tv_location;
        private TextView tv_price;
        private TextView tv_name;
        private CheckBox img_icon_heart;
        private RatingBar ratingBar;

        public ViewHolderItem(View itemView) {
            itemView.setTag(this);
            this.view = itemView;
            btn_promo = (TextView) itemView.findViewById(R.id.btn_promo);
            tv_times = (TextView) itemView.findViewById(R.id.tv_times);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            img_icon_heart = (CheckBox) itemView.findViewById(R.id.img_icon_heart);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }

}

