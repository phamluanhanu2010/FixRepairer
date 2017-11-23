package com.strategy.intecom.vtc.fixrepairer.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.interfaces.Callback;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;


/**
 * Created by Mr. Ha on 7/27/16.
 */
public class FMSearchPinMap extends AppCore {

    public static String TAG = FMSearchPinMap.class.getName();
    private View viewRoot;

    private Button btn_ok;

    private Callback callback;

    private ImageView btn_back;
    private TextView tv_title;
    private ImageView btn_my_location;

    private GPSTracker gpsTracker;

    private Bundle savedInstanceState;

    private VtcModelAddress vtcModelAddress;

    public FMSearchPinMap() {

    }

    @Override
    public void onStart() {
        super.onStart();
        onMapInitializer(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        onMapResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onMapDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onMapLowMemory();
    }

    @SuppressLint("ValidFragment")
    public FMSearchPinMap(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            vtcModelAddress = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_ADDRESS);
        }
        viewRoot = inflater.inflate(R.layout.ui_pin_map, container, false);
        Utils.hideKeyboard(getmActivity());
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.savedInstanceState = savedInstanceState;

        initController(view);

        init();

        initActionPinEvent();
    }

    private void init(){

        gpsTracker = getGpsTracker(getmActivity());

        double latitude = 0.0f;
        double longitude = 0.0f;

        if(vtcModelAddress == null){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }else {
            latitude = vtcModelAddress.getLatitude();
            longitude = vtcModelAddress.getLongitude();
            tv_title.setText(vtcModelAddress.getAddress());
        }
        onMapinitMapView(viewRoot, savedInstanceState, getActivity(), new double[]{latitude, longitude});
    }

//    @Override
//    protected void cmdOnRefreshLocation() {
//        super.cmdOnRefreshLocation();
//        init();
//    }

    private void initController(View view) {

        btn_my_location = (ImageView) view.findViewById(R.id.btn_my_location);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        tv_title.setSelected(true);

        initEvent();

    }

    private void initEvent() {

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onHandlerCallBack(3, vtcModelAddress);
                }
                cmdBack();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        btn_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyLocation(getActivity());
            }
        });

    }

    @Override
    protected void cmdOnGetLocation(final LatLng destPosition) {
        super.cmdOnGetLocation(destPosition);

        new ReadAddressTask().execute(destPosition);
    }

    private class ReadAddressTask extends AsyncTask<LatLng, Void, VtcModelAddress> {

        @Override
        protected void onPreExecute() {
            Activity activity = getActivity();
            if (activity == null) {
                activity = getmActivity();
            }
            if (activity == null) {
                return;
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (btn_ok != null) {
                        btn_ok.setEnabled(false);
                        btn_ok.setAlpha(0.8f);
                    }
                }
            });
            super.onPreExecute();
        }

        @Override
        protected VtcModelAddress doInBackground(LatLng... sourcePosition) {
            return getAddressFromLocation(getActivity(), sourcePosition[0]);
        }

        @Override
        protected void onPostExecute(final VtcModelAddress result) {
            super.onPostExecute(result);
            if (result == null) {
                return;
            }
            Activity activity = getActivity();
            if (activity == null) {
                activity = getmActivity();
            }
            if (activity == null) {
                return;
            }
            vtcModelAddress = result;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if (isTrue) {
//                        AppCore.initSettitle(Const.TYPE_MENU_NOMAL, vtcModelAddress.getAddress());
//                    }

                    tv_title.setText(vtcModelAddress.getAddress());

                    if (btn_ok != null) {
                        btn_ok.setEnabled(true);
                        btn_ok.setAlpha(1.0f);
                        if (callback != null) {
                            callback.onHandlerCallBack(2, vtcModelAddress);
                        }
                    }
//                    isTrue = Boolean.TRUE;
                }
            });
        }
    }
}
