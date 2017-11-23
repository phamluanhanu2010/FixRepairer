package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixrepairer.BuildConfig;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.adt.adtnormal.AdtPlaceSlidesImage;
import com.strategy.intecom.vtc.fixrepairer.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Ha on 5/19/16.
 */
@SuppressLint("ValidFragment")
public class FMJobDetail extends AppCore implements View.OnClickListener,
        ViewPager.OnPageChangeListener{

    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;

    private Button btn_map;
    private Button btn_call;
    private Button btn_email;
    private TextView tv_title_job;
    private TextView tv_price;
    private TextView tv_title_time_content;
    private TextView tv_name;
    private TextView tv_thongtin_khachhang_content;
    private TextView tv_phone_num;
    private TextView tv_phone_num_content;
    private TextView tv_email;
    private TextView tv_email_content;
    private TextView tv_title_requirement_content;
    private TextView tv_title_job_num_content;
    private TextView tv_title_status;

    private Button btn_accept_job;
    private Button btn_success;
    private Button btn_cancel;

    private LinearLayout lout_container_accept_job;
    private LinearLayout lout_container_view_image;
    private AdtPlaceSlidesImage mAdapter;
    private ViewPager mPager;
    private PageIndicator mIndicator;

    private VtcModelOrder vtcModelOrder;

    private int idActionDialog = 0;
    private String orderID = "";

    private boolean isRefreshList = Boolean.FALSE;

    private boolean isHistory = Boolean.FALSE;

    private String strTypeOrder = VtcModelOrder.TYPE_ORDER_FAST;

    private TypeActionConnection statusTypeJob = TypeActionConnection.TYPE_ACTION_ORDER_ACCEPT;

    private Callback callback;

    public FMJobDetail(Callback callback){
        this.callback = callback;
    }

    public FMJobDetail(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        vtcModelOrder = null;
        if (savedInstanceState != null) {
            vtcModelOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_ORDER);
            orderID = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_ID);
            isHistory = savedInstanceState.getBoolean(Const.KEY_BUNDLE_ACTION_HISTORY);
         }
        viewRoot = inflater.inflate(R.layout.ui_job_list_detail, container, false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initController(view);
    }

    private void initController(View view){
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        btn_map = (Button) view.findViewById(R.id.btn_map);
        btn_call = (Button) view.findViewById(R.id.btn_call);
        btn_email = (Button) view.findViewById(R.id.btn_email);
        tv_title_job = (TextView) view.findViewById(R.id.tv_title_job);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_title_time_content = (TextView) view.findViewById(R.id.tv_title_time_content);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_thongtin_khachhang_content = (TextView) view.findViewById(R.id.tv_thongtin_khachhang_content);
        tv_phone_num = (TextView) view.findViewById(R.id.tv_phone_num);
        tv_phone_num_content = (TextView) view.findViewById(R.id.tv_phone_num_content);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_email_content = (TextView) view.findViewById(R.id.tv_email_content);
        tv_title_requirement_content = (TextView) view.findViewById(R.id.tv_title_requirement_content);
        tv_title_job_num_content = (TextView) view.findViewById(R.id.tv_title_job_num_content);
        tv_title_status = (TextView) view.findViewById(R.id.tv_title_status);

        btn_accept_job = (Button) view.findViewById(R.id.btn_accept_job);
        btn_success = (Button) view.findViewById(R.id.btn_success);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        lout_container_accept_job = (LinearLayout) view.findViewById(R.id.lout_container_accept_job);
        lout_container_view_image = (LinearLayout) view.findViewById(R.id.lout_container_view_image);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        tv_title.setSelected(true);

        if(vtcModelOrder != null) {
            initData();
        }else {
            initGetInfoJob(FMJobDetail.this, orderID);
        }
    }

    private void initData() {

        String strPrice = vtcModelOrder.getField() == null ? "0.0" : vtcModelOrder.getField().getPrice();

        AppCore.setFormatCurrency(tv_price, strPrice);

        tv_title_job.setText(vtcModelOrder.getField() == null ? "" : vtcModelOrder.getField().getCategory_name());
        tv_title_time_content.setText(Utils.initConvertTimeDisplayLong(vtcModelOrder.getOrder_time()));
        tv_name.setText(vtcModelOrder.getUser() == null ? "" : vtcModelOrder.getUser().getName());
        tv_thongtin_khachhang_content.setText(vtcModelOrder.getAddress() == null ? "" : vtcModelOrder.getAddress().getName());
        tv_phone_num_content.setText(vtcModelOrder.getUser() == null ? "" : vtcModelOrder.getUser().getPhone());
        tv_email_content.setText(vtcModelOrder.getUser() == null ? "" : vtcModelOrder.getUser().getEmail());
        tv_title_requirement_content.setText(vtcModelOrder.getDescription());
        tv_title_job_num_content.setText(vtcModelOrder.getId());

        tv_title.setText(String.valueOf(vtcModelOrder.getField().getCategory_name() + " - " + vtcModelOrder.getField().getName()));

        strTypeOrder = vtcModelOrder.getType();

        if (vtcModelOrder.getImages().size() > 0) {
            lout_container_view_image.setVisibility(LinearLayout.VISIBLE);

            int[] sizeScreen = Utils.getSizeScreen(getActivity());

            int width = sizeScreen[0] - ((int) getActivity().getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2);
            int heigth = width / 2;

            mAdapter = new AdtPlaceSlidesImage(FMJobDetail.this.getChildFragmentManager(), vtcModelOrder.getImages(), getActivity(), width, heigth);
            mPager.setAdapter(mAdapter);

            mIndicator.setViewPager(mPager);

            ((CirclePageIndicator) mIndicator).setSnap(true);
        } else {

            lout_container_view_image.setVisibility(LinearLayout.GONE);
        }

        tv_name.setVisibility(Button.GONE);
        tv_phone_num.setVisibility(TextView.GONE);
        tv_phone_num_content.setVisibility(TextView.GONE);
        btn_call.setVisibility(Button.INVISIBLE);
        tv_title_status.setVisibility(TextView.GONE);
        btn_email.setVisibility(Button.INVISIBLE);
        tv_email_content.setVisibility(Button.GONE);
        tv_email.setVisibility(Button.GONE);

        String status = vtcModelOrder.getStatus();

        if (status.equals(StatusBookingJob.STATUS_FINDING.getValuesStatus())) {
            lout_container_accept_job.setVisibility(LinearLayout.VISIBLE);
            btn_accept_job.setVisibility(Button.VISIBLE);
        } else if (status.equals(StatusBookingJob.STATUS_WORKING.getValuesStatus())) {
            initTypeWorking();
        } else if (status.equals(StatusBookingJob.STATUS_FINISH.getValuesStatus())) {
            initTypeFinished();
        } else if (status.equals(StatusBookingJob.STATUS_EXPIRED.getValuesStatus())) {
            initTypeExpried();
        } else if (status.equals(StatusBookingJob.STATUS_CANCEL.getValuesStatus())) {
            initTypeCancel();
        } else if (status.equals(StatusBookingJob.STATUS_ACCEPTED.getValuesStatus())) {
            initTypeAccept();
        } else if (status.equals(StatusBookingJob.STATUS_COMING.getValuesStatus())) {
            initTypeComing();
        } else if (status.equals(StatusBookingJob.STATUS_USER_CANCEL.getValuesStatus())) {
            initTypeUserCancel();
        }

        if(isHistory){
            tv_email_content.setVisibility(Button.GONE);
            tv_email.setVisibility(Button.GONE);
            tv_name.setVisibility(Button.GONE);
            btn_call.setVisibility(Button.INVISIBLE);
            btn_email.setVisibility(Button.INVISIBLE);
            btn_map.setVisibility(Button.GONE);
//            tv_title_status.setVisibility(TextView.GONE);
            tv_phone_num.setVisibility(TextView.GONE);
            tv_phone_num_content.setVisibility(TextView.GONE);
        }

        initEvent();
    }

    private void initEvent(){

        mIndicator.setOnPageChangeListener(this);
        btn_map.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_email.setOnClickListener(this);
        btn_accept_job.setOnClickListener(this);
        btn_success.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map:
                Bundle bundle = new Bundle();

                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ORDER, vtcModelOrder);

                AppCore.CallFragmentSectionWithCallback(Const.UI_JOB_DETAIL_MAP, bundle, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {
                        if(t != null && t.length > 0){
                            if(t[0] instanceof VtcModelOrder) {
                                vtcModelOrder = (VtcModelOrder) t[0];
                                initData();
                            }
                            if(t[1] instanceof Boolean){
                                isRefreshList = (Boolean) t[1];
                            }
                        }
                    }
                });
                break;

            case R.id.btn_call:
                idActionDialog = R.id.btn_call;
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", String.valueOf("Gọi " + String.valueOf(vtcModelOrder.getUser() == null ? "" : vtcModelOrder.getUser().getPhone())));
                break;

            case R.id.btn_email:
                idActionDialog = R.id.btn_email;
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", String.valueOf("Nhắn tin cho " + String.valueOf(vtcModelOrder.getUser() == null ? "" : vtcModelOrder.getUser().getPhone())));
                break;

            case R.id.btn_accept_job:
                if (vtcModelOrder != null && getVtcModelUser() != null) {
                    initAcceptOrder(FMJobDetail.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                }
                break;

            case R.id.btn_success:

                switch (statusTypeJob) {
                    case TYPE_ACTION_ORDER_UPDATE_CANCEL:

                        if (vtcModelOrder != null) {
                            initUpdateOrdeCancel(FMJobDetail.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                        }
                        break;
                    case TYPE_ACTION_ORDER_UPDATE_WORKING:

                        if (vtcModelOrder != null) {
                            initUpdateOrdeWorking(FMJobDetail.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                        }
                        break;
                    case TYPE_ACTION_ORDER_UPDATE_FINISHED:

                        if (vtcModelOrder != null) {
                            initUpdateOrdeFinished(FMJobDetail.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                        }
                        break;
                    case TYPE_ACTION_ORDER_UPDATE_COMING:

                        if (vtcModelOrder != null) {
                            initUpdateOrdeComing(FMJobDetail.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                        }
                        break;
                }

                break;


            case R.id.btn_cancel:

                if (vtcModelOrder != null) {
                    initUpdateOrdeCancel(FMJobDetail.this, vtcModelOrder.getId(), getVtcModelUser().getId());
                }
                break;

            case R.id.btn_back:
                if (AppCore.getPreferenceUtil(getActivity()).getValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST).isEmpty()) {
                    cmdBack();
                } else {
                    AppCore.initReceived(Const.UI_MY_JOB_FAST, this.getResources().getString(R.string.validate_check_exist_job));
                }
                break;

            default:
                return;
        }
    }


    @Override
    public void cmdPressDialogYes(String response) {
        super.cmdPressDialogYes(response);

        String sPhone = String.valueOf(vtcModelOrder.getUser() == null ? "" : vtcModelOrder.getUser().getPhone());

        switch (idActionDialog) {

            case R.id.btn_call:
                initCallIntentPhone(sPhone);
                break;

            case R.id.btn_email:
//                CallSendEmailForChoose(new String[]{vtcModelOrder.getUser() == null ? "" : vtcModelOrder.getUser().getEmail()}, getResources().getString(R.string.app_name));

                initCallIntentMessage(sPhone, getResources().getString(R.string.app_name));

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);

        switch (keyType){
            case TYPE_ACTION_ORDER_ACCEPT:

                isRefreshList = Boolean.TRUE;

                if(vtcModelOrder != null){
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_ACCEPTED.getValuesStatus());
                }
                initTypeAccept();
                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;

            case TYPE_ACTION_ORDER_UPDATE_WORKING:

                if(vtcModelOrder != null){
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_WORKING.getValuesStatus());
                }
                initTypeWorking();
                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;
            case TYPE_ACTION_ORDER_UPDATE_FINISHED:

                if(vtcModelOrder != null){
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_FINISH.getValuesStatus());

                    if(vtcModelOrder.getType().equals(VtcModelOrder.TYPE_ORDER_FAST)) {
                        AppCore.getPreferenceUtil(getmActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, true);
                    }
                }
                initTypeFinished();

                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;
            case TYPE_ACTION_ORDER_UPDATE_COMING:

                if(vtcModelOrder != null){
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_COMING.getValuesStatus());
                }
                initTypeComing();
                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                break;
            case TYPE_ACTION_ORDER_UPDATE_CANCEL:

                AppCore.getPreferenceUtil(getmActivity()).setValueBoolean(PreferenceUtil.IS_RECEIVE_NOTI, true);
                if(vtcModelOrder != null){
                    vtcModelOrder.setStatus(StatusBookingJob.STATUS_CANCEL.getValuesStatus());
                }
                initTypeCancel();
                AppCore.showToastShort(getView(), getResources().getString(R.string.title_dialog_process_success));
                if(callback != null){
                    callback.onHandlerCallBack(-1);
                }
                cmdBack();
                break;

            case TYPE_ACTION_ORDER_GET_DETAIL:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);

                    if(vtcModelOrder != null) {
                        initData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onPostExecuteError(TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyType, msg);

        switch (keyType){
            case TYPE_CONNECTION_ERROR_CODE_EXIST_ACCEPT:
            case TYPE_CONNECTION_ERROR_CODE_ORDER_CLOSE:
            case TYPE_CONNECTION_ERROR_CODE_ORDER_OVERWRITE:
                AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");
                getmActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;

        }
    }

    private void initGone(){

        tv_name.setVisibility(Button.VISIBLE);
        btn_success.setVisibility(Button.VISIBLE);
        btn_cancel.setVisibility(Button.VISIBLE);
        btn_accept_job.setVisibility(Button.VISIBLE);
        tv_title_status.setVisibility(TextView.VISIBLE);
        tv_phone_num.setVisibility(TextView.VISIBLE);
        tv_phone_num_content.setVisibility(TextView.VISIBLE);
        btn_call.setVisibility(Button.VISIBLE);
        btn_email.setVisibility(Button.VISIBLE);
        tv_email_content.setVisibility(Button.VISIBLE);
        tv_email.setVisibility(Button.VISIBLE);
    }

    private void initTypeAccept(){

        initGone();

        btn_accept_job.setVisibility(Button.GONE);
        tv_title_status.setBackgroundColor(Color.GREEN);

        if(strTypeOrder.equals(VtcModelOrder.TYPE_ORDER_FAST)){
            tv_title_status.setText(getResources().getString(R.string.btn_job_comming));
            btn_success.setText(getResources().getString(R.string.btn_job_comming_ok));
            statusTypeJob = TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_WORKING;
        }else {
            tv_title_status.setText(getResources().getString(R.string.btn_job_detail_danhanviec));
            btn_success.setText(getResources().getString(R.string.btn_job_go));
            statusTypeJob = TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_COMING;
        }

        btn_cancel.setText(getResources().getString(R.string.btn_job_detail_cancel));
    }


    private void initTypeWorking(){

        initGone();
        tv_title_status.setBackgroundColor(Color.GREEN);
        btn_accept_job.setVisibility(Button.GONE);
        btn_success.setText(getResources().getString(R.string.btn_job_detail_success));
        tv_title_status.setText(getResources().getString(R.string.btn_job_go_run_));

        statusTypeJob = TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_FINISHED;
    }


    private void initTypeFinished(){

        initGone();
        AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");
        lout_container_accept_job.setVisibility(LinearLayout.GONE);
        tv_title_status.setBackgroundColor(Color.GREEN);
        btn_success.setVisibility(Button.GONE);
        btn_cancel.setVisibility(Button.GONE);
        btn_accept_job.setVisibility(Button.GONE);
        tv_title_status.setText(getResources().getString(R.string.btn_job_detail_success));
    }

    private void initTypeComing(){

        initGone();
        btn_accept_job.setVisibility(Button.GONE);
        tv_title_status.setBackgroundColor(Color.GREEN);
        tv_title_status.setText(getResources().getString(R.string.btn_job_comming_ok));
        btn_success.setText(getResources().getString(R.string.btn_job_go_run));
        statusTypeJob = TypeActionConnection.TYPE_ACTION_ORDER_UPDATE_WORKING;

        btn_cancel.setText(getResources().getString(R.string.btn_job_detail_cancel));
    }

    private void initTypeCancel(){

        initGone();
        AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");
        lout_container_accept_job.setVisibility(LinearLayout.GONE);
        tv_title_status.setBackgroundColor(Color.RED);
        tv_title_status.setText(getResources().getString(R.string.btn_new_job_cancel));
    }

    private void initTypeUserCancel(){

        initGone();
        AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");
        lout_container_accept_job.setVisibility(LinearLayout.GONE);
        tv_title_status.setBackgroundColor(Color.RED);
        tv_title_status.setText(getResources().getString(R.string.btn_job_user_cancel_job));
    }

    private void initTypeExpried(){

        initGone();
        AppCore.getPreferenceUtil(getmActivity()).setValueString(PreferenceUtil.USER_ACCEPTED_JOB_FAST, "");
        lout_container_accept_job.setVisibility(LinearLayout.GONE);
        tv_title_status.setBackgroundColor(Color.RED);
        tv_title_status.setVisibility(TextView.VISIBLE);
        tv_title_status.setText(getResources().getString(R.string.btn_job_expried));
    }

    @Override
    public void onDestroyView() {

        if(callback != null){
            callback.onHandlerCallBack(-1, isRefreshList);
        }
        super.onDestroyView();
    }

    @Override
    protected void cmdOnUserCancelJob() {
        super.cmdOnUserCancelJob();
        cmdBack();
    }
}
