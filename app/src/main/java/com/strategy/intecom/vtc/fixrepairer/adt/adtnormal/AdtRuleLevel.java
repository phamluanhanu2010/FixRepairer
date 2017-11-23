package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelRating;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelTools;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.view.fragment.FMToolsLeaseNew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/25/16.
 */
public class AdtRuleLevel extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<VtcModelRating> itemLst;

    public AdtRuleLevel(Context context, List<VtcModelRating> itemLst) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.itemLst = itemLst;
    }

    public void setData(List<VtcModelRating> itemLst) {
        this.itemLst = itemLst;
        notifyDataSetChanged();
    }

    public void setData(VtcModelRating item) {
        if (this.itemLst == null) {
            this.itemLst = new ArrayList<>();
        }
        this.itemLst.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (itemLst == null) {
            return 0;
        }
        return itemLst.size();
    }

    @Override
    public VtcModelRating getItem(int position) {
        if (itemLst == null) {
            return null;
        }
        return itemLst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VtcModelRating rating = getItem(position);

            final ViewHolder viewHolder;

            if (null == convertView) {
                convertView = mLayoutInflater.inflate(R.layout.tmp_rule_level, parent, false);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(rating != null){
                viewHolder.tv_title.setText(rating.getrName());
                viewHolder.tv_content.setText(rating.getrRatingCount());
                viewHolder.img_avatar.setVisibility(ImageView.VISIBLE);

                switch (rating.getrLevelRepairer()){

                    case 0:
                        viewHolder.img_avatar.setVisibility(ImageView.GONE);
                        break;
                    case VtcModelRating.LEVEL_REPAIRER_LV1:
                        viewHolder.img_avatar.setBackgroundResource(R.drawable.ic_level_1);
                        break;
                    case VtcModelRating.LEVEL_REPAIRER_LV2:
                        viewHolder.img_avatar.setBackgroundResource(R.drawable.ic_level_2);
                        break;
                    case VtcModelRating.LEVEL_REPAIRER_LV3:
                        viewHolder.img_avatar.setBackgroundResource(R.drawable.ic_level_3);
                        break;
                    case VtcModelRating.LEVEL_REPAIRER_LV4:
                        viewHolder.img_avatar.setBackgroundResource(R.drawable.ic_level_4);
                        break;
                    case VtcModelRating.LEVEL_REPAIRER_LV5:
                        viewHolder.img_avatar.setBackgroundResource(R.drawable.ic_level_5);
                        break;
                }


            }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_title;
        private ImageView img_avatar;
        private TextView tv_content;

        public ViewHolder(View v) {
            v.setTag(this);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
        }
    }
}
