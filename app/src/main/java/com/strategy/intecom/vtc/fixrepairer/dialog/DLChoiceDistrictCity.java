package com.strategy.intecom.vtc.fixrepairer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelCity;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.util.List;
import java.util.Map;

/**
 * Created by PHAMLUAN on 5/31/2016.
 */
public class DLChoiceDistrictCity extends Dialog implements View.OnClickListener {

    private NumberPicker choice_district;
    private NumberPicker choice_city;

    private Button btn_cancel;
    private Button btn_accept;

    private Map<String, List<VtcModelCity>> lstDistrict;

    private String[] arrayCities;
    private String[] arrayDistrict;

    private onClickChoiceSelect onClickChoiceSelect;

    private String city = "";
    private String district = "";

    public DLChoiceDistrictCity(Context context, List<VtcModelCity> lstCities, Map<String, List<VtcModelCity>> lstDistrict) {
        super(context);
        initConvertArrayCities(lstCities);
        this.lstDistrict = lstDistrict;
    }

    private void initConvertArrayCities(List<VtcModelCity> lstCities){
        if(lstCities != null){
            arrayCities = new String[lstCities.size()];
            for (int i = 0; i < lstCities.size(); i ++){
                VtcModelCity values = lstCities.get(i);
                arrayCities[i] = values.getCityName();
            }
        }
    }

    private String[] initConvertArrayDistrict(String cityName) {
        String[] arrayDistrict = null;
        if (lstDistrict != null) {
            List<VtcModelCity> lstDis = lstDistrict.get(cityName);
            if (lstDis != null) {
                arrayDistrict = new String[lstDis.size()];
                for (int i = 0; i < lstDis.size(); i++) {
                    VtcModelCity values = lstDis.get(i);
                    arrayDistrict[i] = values.getCityName();
                }
            }
        }
        return arrayDistrict;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_choice_district_city);
        initController();
    }


    private void initController(){

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_cancel.setOnClickListener(this);
        btn_accept.setOnClickListener(this);

        choice_city = (NumberPicker) findViewById(R.id.choice_city);
        choice_district = (NumberPicker) findViewById(R.id.choice_district);

        choice_city.setMinValue(1);
        choice_city.setMaxValue(arrayCities == null ? 0 : arrayCities.length);
        choice_city.setDisplayedValues(arrayCities);

        choice_city.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                city = null;
                String display[] = picker.getDisplayedValues();
                city = display[picker.getValue() - 1];
                arrayDistrict = initConvertArrayDistrict(city);
                if(arrayDistrict != null && arrayDistrict.length > 0) {
                    choice_district.setMinValue(1);
                    choice_district.setMaxValue(arrayDistrict.length);
                    choice_district.setDisplayedValues(arrayDistrict);
                }
            }
        });

        choice_district.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                String display[] = picker.getDisplayedValues();
                district = display[picker.getValue() - 1];

                AppCore.showLog("------District : " + display);
            }
        });

        initData();
    }
    private void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                this.dismiss();
                getOnClickChoiceSelect().onClickChoice(city, district);
                break;
            case R.id.btn_cancel:
                this.dismiss();
                break;
            default:
                return;
        }
    }

    public DLChoiceDistrictCity.onClickChoiceSelect getOnClickChoiceSelect() {
        return onClickChoiceSelect;
    }

    public void setOnClickChoiceSelect(DLChoiceDistrictCity.onClickChoiceSelect onClickChoiceSelect) {
        this.onClickChoiceSelect = onClickChoiceSelect;
    }

    public interface onClickChoiceSelect{
        void onClickChoice(String cities, String district);
    }

}
