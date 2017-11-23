package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelFields;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.view.custom.imagecus.ImageViewSize;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtServiceLst extends BaseAdapter {

    private Activity context;

    private LayoutInflater mLayoutInflater;

    private onClickItem onClickItem;

    private List<VtcModelUser.Skills> lst = null;

    public AdtServiceLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    private int width = 0;
    private int height = 0;

    private int offset = 0;

    public void setOnClickItem(AdtServiceLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public AdtServiceLst(Activity context, int width) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.width = width;
        this.height = width;
    }

    public void setData(List<VtcModelUser.Skills> lst){
        this.lst = lst;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(lst == null){
            return 0;
        }
        return lst.size();
    }

    @Override
    public VtcModelUser.Skills getItem(int position) {
        if(lst == null) {
            return null;
        }
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.tmp_service_lst, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final VtcModelUser.Skills modelFields = lst.get(position);

        if(modelFields != null) {

            viewHolder.tv_title.setText(modelFields.getName());
            viewHolder.tv_content.setText(String.valueOf(modelFields.getCount() + " " + context.getResources().getString(R.string.title_my_job)));

            ImageLoadAsync loadAsync = new ImageLoadAsync(context, viewHolder.img_avatar, width , height);
            loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(modelFields.getImage()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnClickItem().onClickIten(modelFields);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {

        ImageViewSize img_avatar;
        TextView tv_title;
        TextView tv_content;

        public ViewHolder(View v) {
            v.setTag(this);
            img_avatar = (ImageViewSize) v.findViewById(R.id.img_avatar);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
        }
    }

    public interface onClickItem {
        void onClickIten(VtcModelUser.Skills service);
    }
}

