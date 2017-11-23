package com.strategy.intecom.vtc.fixrepairer.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelTools;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/25/16.
 */
public class AdtToolsChoicelst extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<VtcModelTools> itemLst;

    private onClickItemTools onClickItemTools;
    private int width = 0;
    private int height = 0;

    public AdtToolsChoicelst.onClickItemTools getOnClickItemTools() {
        return onClickItemTools;
    }

    public void setOnClickItemTools(AdtToolsChoicelst.onClickItemTools onClickItemTools) {
        this.onClickItemTools = onClickItemTools;
    }

    public AdtToolsChoicelst(Context context, int width) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        itemLst = new ArrayList<>();
        this.width = width;
        this.height = width;
    }

    public void setData(List<VtcModelTools> itemLst) {
        this.itemLst = itemLst;
        notifyDataSetChanged();
    }

    public void setData(VtcModelTools item) {
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
    public Object getItem(int position) {
        if (itemLst == null) {
            return null;
        }
        return itemLst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public boolean removeItem(VtcModelTools tools){
        if (itemLst == null) {
            return false;
        }

        boolean isTrue = itemLst.remove(tools);

        notifyDataSetChanged();

        return isTrue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VtcModelTools tools = (VtcModelTools) getItem(position);

            final ViewHolderTools viewHolderTools;

            if (null == convertView) {
                convertView = mLayoutInflater.inflate(R.layout.tmp_choose_tools, parent, false);
                viewHolderTools = new ViewHolderTools(convertView);
            } else {
                viewHolderTools = (ViewHolderTools) convertView.getTag();
            }
            if(tools != null){
                initViewTools(tools, viewHolderTools);
            }
        return convertView;
    }

    private void initViewTools(final VtcModelTools tools, final ViewHolderTools holderTools){
        holderTools.tv_title.setText(tools.getName());
        holderTools.tv_count.setText(String.valueOf(tools.gettCount()));
        holderTools.tv_content.setText(String.valueOf(tools.getPrice()));

        ImageLoadAsync loadAsync = new ImageLoadAsync(context, holderTools.img_icon, width , height);
        loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(tools.gettThumb()));

        holderTools.btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = tools.gettCount();
                if(count < 10) {

                    tools.settCount(count + 1);

                    holderTools.tv_count.setText(String.valueOf(tools.gettCount()));

                    getOnClickItemTools().onClickCountTools(tools, true);
                }
            }
        });
        holderTools.btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = tools.gettCount();
                if(count > 0) {

                    tools.settCount(count - 1);

                    holderTools.tv_count.setText(String.valueOf(tools.gettCount()));

                    getOnClickItemTools().onClickCountTools(tools, false);
                }
            }
        });
        holderTools.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.settStatus(false);
                removeItem(tools);
                getOnClickItemTools().onClickDelete(tools);
            }
        });
    }

    class ViewHolderTools {
        private TextView tv_title;
        private ImageView img_icon;
        private TextView tv_count;
        private ImageView btn_up;
        private ImageView btn_down;
        private ImageView btn_delete;
        private TextView tv_content;

        public ViewHolderTools(View v) {
            v.setTag(this);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            img_icon = (ImageView) v.findViewById(R.id.img_icon);
            tv_count = (TextView) v.findViewById(R.id.tv_count);
            btn_up = (ImageView) v.findViewById(R.id.btn_up);
            btn_down = (ImageView) v.findViewById(R.id.btn_down);
            btn_delete = (ImageView) v.findViewById(R.id.btn_delete);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
        }
    }

    public interface onClickItemTools{
        void onClickCountTools(VtcModelTools tools, boolean isUp);
        void onClickDelete(VtcModelTools tools);
    }
}
