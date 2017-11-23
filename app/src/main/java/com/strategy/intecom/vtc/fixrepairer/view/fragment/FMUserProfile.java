package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlCalendar;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.view.UIConfirmInfoProfile;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.zip.Inflater;

/**
 * Created by Mr. Ha on 5/19/16.
 */
public class FMUserProfile extends AppCore implements View.OnTouchListener, View.OnClickListener, Callback {

    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;

    private RatingBar rating;
    private ImageView img_edit_profile;

    private ImageView img_icon;

    private TextView tv_name;
    private TextView tv_content_level;
    private TextView tv_member_number;
    private TextView tv_address;
    private TextView tv_phone_num_content;
    private TextView tv_email_content;
    private TextView tv_for_me;

    private LinearLayout lout_container_skill;
    private LinearLayout lout_container;

    private Callback callback;

    @SuppressLint("ValidFragment")
    public FMUserProfile(Callback  callback){
        this.callback = callback;
    }

    public FMUserProfile(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_user_profile, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControler(view);
    }

    public void initControler(View view) {
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        rating = (RatingBar) view.findViewById(R.id.rating);
        img_edit_profile = (ImageView) view.findViewById(R.id.img_edit_profile);

        img_icon = (ImageView) view.findViewById(R.id.img_icon);
        tv_content_level = (TextView) view.findViewById(R.id.tv_content_level);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_member_number = (TextView) view.findViewById(R.id.tv_member_number);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_phone_num_content = (TextView) view.findViewById(R.id.tv_phone_num_content);
        tv_email_content = (TextView) view.findViewById(R.id.tv_email_content);
        tv_for_me = (TextView) view.findViewById(R.id.tv_for_me);

        lout_container_skill = (LinearLayout) view.findViewById(R.id.lout_container_skill);
        lout_container = (LinearLayout) view.findViewById(R.id.lout_container);

        tv_title.setText(getResources().getString(R.string.title_user_info));

        tv_title.setSelected(true);
        rating.setOnTouchListener(this);
        img_edit_profile.setOnClickListener(this);
        lout_container.setOnClickListener(this);
        btn_back.setOnClickListener(this);


        initSetData();
    }

    private void initSetData(){
        VtcModelUser vtcModelUser = getVtcModelUser();

        if(vtcModelUser != null){

            tv_name.setText(vtcModelUser.getUser_full_name());
            tv_member_number.setText(vtcModelUser.getId());

            String address = "";
            if(vtcModelUser.getAddress() != null) {
                address = vtcModelUser.getAddress().getName();
            }else {
                address = vtcModelUser.getUser_District() + ", " + vtcModelUser.getUser_City();
            }

            tv_address.setText(address);

            tv_phone_num_content.setText(vtcModelUser.getUser_Phone_Num());
            tv_email_content.setText(vtcModelUser.getUser_Email());
            tv_for_me.setText(vtcModelUser.getUser_Description());

            if(vtcModelUser.getSkills() != null && vtcModelUser.getSkills().size() > 0){
                for (VtcModelUser.Skills skills : vtcModelUser.getSkills()) {
                    LayoutInflater layoutInflater = (LayoutInflater) getmActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    TextView child = (TextView) layoutInflater.inflate(R.layout.tmp_textview_skill, null);

                    child.setText(skills.getName());

                    lout_container_skill.addView(child);

                }
            }

            if(vtcModelUser.getRate() != null){
                rating.setRating(vtcModelUser.getRate().getAvg_rate());
            }

            initRate(vtcModelUser);
        }
    }

    private void initRate(VtcModelUser vtcModelUser) {

        int level = vtcModelUser.getLevel();

        String sLV = "";
        switch (level) {
            case 1:
                // Thợ cấp 1
                sLV = getResources().getString(R.string.title_rating_level_1);
                level = R.drawable.ic_level_1;
                break;

            case 2:
                // Thợ cấp 2
                sLV = getResources().getString(R.string.title_rating_level_2);
                level = R.drawable.ic_level_2;
                break;

            case 3:
                // Thợ cấp 3
                sLV = getResources().getString(R.string.title_rating_level_3);
                level = R.drawable.ic_level_3;
                break;

            case 4:
                // Thợ cấp 4
                sLV = getResources().getString(R.string.title_rating_level_4);
                level = R.drawable.ic_level_4;
                break;

            case 5:
                // Thợ cấp 5
                sLV = getResources().getString(R.string.title_rating_level_5);
                level = R.drawable.ic_level_5;
                break;
        }

        tv_content_level.setText(sLV);
        img_icon.setBackgroundResource(level);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                AppCore.CallFragmentSection(Const.UI_USER_PROFILE_RATING_REVIEW);
                break;

            case MotionEvent.ACTION_UP:

                break;

            case MotionEvent.ACTION_MOVE:

                break;

            default:
                return false;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_edit_profile:

                Bundle bundle = new Bundle();
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_USER, getVtcModelUser());
                AppCore.CallFragmentSectionWithCallback(Const.UI_MY_REGISTER, bundle, FMUserProfile.this, true , false);

                break;

            case R.id.lout_container:

                AppCore.CallFragmentSection(Const.UI_USER_PROFILE_UPGRADE_LEVEL);
                break;

            case R.id.btn_back:

                cmdBack();

                break;
            default:
                return;
        }
    }

    @Override
    public <T> void onHandlerCallBack(int key, T... t) {
        AppCore.initToolsBar(getActivity(), true);
    }
}
