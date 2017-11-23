package com.strategy.intecom.vtc.fixrepairer.adt.adtrecycler;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixrepairer.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtListSkillMap extends RecyclerView.Adapter<AdtListSkillMap.ViewHolder> {
    private Activity context;

    private List<VtcModelUser.Skills> lstSkill;
    private onClickItemSkill onClickItemSkill;
    private int width = 0;

    public AdtListSkillMap(Activity context) {
        this.context = context;
        this.lstSkill = new ArrayList<>();
        this.width = (int) context.getResources().getDimension(R.dimen.size_icon_in_app_menu);
    }

    public void setData(List<VtcModelUser.Skills> lstSkill){
        this.lstSkill = lstSkill;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_skill_map, parent, false);
        ViewHolder vhItem = new ViewHolder(v);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(lstSkill != null){
            final VtcModelUser.Skills skills = lstSkill.get(position);
            if(skills != null){

                holder.tv_skill_name.setSelected(true);
                holder.tv_skill_name.setText(skills.getName());

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(skills.getCount() > 0) {
                            holder.tv_badges.setVisibility(TextView.VISIBLE);
                            holder.tv_badges.setText(String.valueOf(skills.getCount()));
                        }else {
                            holder.tv_badges.setVisibility(TextView.GONE);
                        }
                    }
                });

                AbsListView.LayoutParams tv_header_params = new AbsListView.LayoutParams( width * 2,
                        AbsListView.LayoutParams.WRAP_CONTENT);
                holder.view.setLayoutParams(tv_header_params);

                ImageLoadAsync loadAsync = new ImageLoadAsync(AppCore.getmActivity(), holder.img_avatar, width , width);
                loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(skills.getImage()));

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOnClickItemSkill().onClickItemSkill(skills);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (lstSkill == null) {
            return 0;
        }
        return lstSkill.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView img_avatar;
        private TextView tv_skill_name;
        private TextView tv_badges;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            tv_skill_name = (TextView) itemView.findViewById(R.id.tv_skill_name);
            tv_badges = (TextView) itemView.findViewById(R.id.tv_badges);
        }
    }

    public AdtListSkillMap.onClickItemSkill getOnClickItemSkill() {
        return onClickItemSkill;
    }

    public void setOnClickItemSkill(AdtListSkillMap.onClickItemSkill onClickItemSkill) {
        this.onClickItemSkill = onClickItemSkill;
    }

    public interface onClickItemSkill{
        void onClickItemSkill(VtcModelUser.Skills modelFields);
    }
}
