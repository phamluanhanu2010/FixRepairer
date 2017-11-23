package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelCity;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtCityLst extends ArrayAdapter<VtcModelCity> {

    private Activity context;

    private LayoutInflater mLayoutInflater;

    private List<VtcModelCity> lstCity = null;

    public AdtCityLst(Activity context) {
        super(context, R.layout.tmp_textview);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        lstCity = new ArrayList<>();
    }

    public void initSendDataCity(List<VtcModelCity> lst) {
        clear();
        this.lstCity = lst;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (lstCity == null) {
            return 0;
        }
        return lstCity.size();
    }

    @Override
    public VtcModelCity getItem(int position) {

        if (lstCity == null) {
            return null;
        }
        return lstCity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    private View initView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.tmp_textview, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final VtcModelCity modelCity = getItem(position);

        if (modelCity != null) {
            String name = String.valueOf(modelCity.getDistrictName());
            if (name.equals("")) {
                name = String.valueOf(modelCity.getCityName());
            }

            viewHolder.tv_title.setText(name);
        }

        return convertView;
    }

    class ViewHolder {

        private TextView tv_title;

        public ViewHolder(View v) {
            v.setTag(this);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
        }
    }
}

