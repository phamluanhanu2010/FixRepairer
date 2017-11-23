package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtJobLst extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private Context context;

    private onClickItem onClickItem;

    private List<VtcModelOrder> orderList;

    public AdtJobLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtJobLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }


    public AdtJobLst(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void setDataFieldLst(List<VtcModelOrder> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (orderList == null) {
            return 0;
        }
        return orderList.size();
    }

    @Override
    public VtcModelOrder getItem(int position) {
        if (orderList == null) {
            return null;
        }
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.tmp_job_lst, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final VtcModelOrder vtcModelOrder = getItem(position);

        if (vtcModelOrder != null) {
            viewHolder.tv_times.setText(Utils.initConvertTimeDisplayLong(vtcModelOrder.getOrder_time()));

            viewHolder.tv_title.setText(vtcModelOrder.getField() == null ? "" : vtcModelOrder.getField().getCategory_name());
            viewHolder.tv_location.setText(vtcModelOrder.getAddress() == null ? "" : vtcModelOrder.getAddress().getName());
            viewHolder.tv_name.setText(vtcModelOrder.getUser().getName());

            String strPrice = vtcModelOrder.getField() == null ? "0.0" : vtcModelOrder.getField().getPrice();

            AppCore.setFormatCurrency(viewHolder.tv_price, strPrice);

            VtcModelOrder.Coupon_code promo = vtcModelOrder.getCoupon_code();
            if (promo != null) {
                viewHolder.btn_promo.setVisibility(TextView.VISIBLE);
            } else {
                viewHolder.btn_promo.setVisibility(TextView.INVISIBLE);
            }
//            if (vtcModelOrder.isSave()) {
//                viewHolder.img_icon_heart.setChecked(true);
//            } else {
//                viewHolder.img_icon_heart.setChecked(false);
//            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnClickItem().onClickIten(vtcModelOrder);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {

        private TextView btn_promo;
        private TextView tv_times;
        private TextView tv_title;
        private CheckBox img_icon_heart;
        private TextView tv_location;
        private TextView tv_price;
        private TextView tv_name;
        private TextView tv_phone_num;

        public ViewHolder(View v) {
            v.setTag(this);
            btn_promo = (TextView) v.findViewById(R.id.btn_promo);
            tv_times = (TextView) v.findViewById(R.id.tv_times);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            img_icon_heart = (CheckBox) v.findViewById(R.id.img_icon_heart);
            tv_location = (TextView) v.findViewById(R.id.tv_location);
            tv_price = (TextView) v.findViewById(R.id.tv_price);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_phone_num = (TextView) v.findViewById(R.id.tv_phone_num);
        }
    }

    public interface onClickItem {
        void onClickIten(VtcModelOrder vtcModelOrder);
    }
}

