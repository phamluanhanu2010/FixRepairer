package com.strategy.intecom.vtc.fixrepairer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VtcModelCity implements Parcelable {

    private static final String TAG = VtcModelCity.class.getName();

    private String id = "";
    private String cityName = "";
    private String districtName = "";

    private static List<VtcModelCity> lstCities = new ArrayList<>();   // List City
    private static HashMap<String, List<VtcModelCity>> lstDistrict = new HashMap<>();   // List District

    public static VtcModelCity getLstDataCity(JSONArray jsonCity){
        VtcModelCity vtcModelCity = new VtcModelCity();
        if(jsonCity != null){
            for (int i = 0; i < jsonCity.length(); i++){

                VtcModelCity modelCity = new VtcModelCity();

                JSONObject objectCity = jsonCity.optJSONObject(i);

                modelCity.setId(objectCity.optString(ParserJson.API_PARAMETER_ID_));
                modelCity.setCityName(objectCity.optString(ParserJson.API_PARAMETER_NAME));

                AppCore.showLog("-------City----- : " + modelCity.getCityName());

                JSONArray jsonDistrict = objectCity.optJSONArray(ParserJson.API_PARAMETER_DISTRICT);
                if(jsonDistrict != null){
                    List<VtcModelCity> lstModelCities = new ArrayList<>();
                    for (int j = 0; j < jsonDistrict.length(); j++){
                        JSONObject objectDistrict = jsonDistrict.optJSONObject(j);
                        if(objectDistrict != null) {
                            VtcModelCity modelDistrict = new VtcModelCity();

                            modelDistrict.setId(objectDistrict.optString(ParserJson.API_PARAMETER_ID_));
                            modelDistrict.setDistrictName(objectDistrict.optString(ParserJson.API_PARAMETER_NAME));

                            AppCore.showLog("-------District----- : " + modelDistrict.getDistrictName());

                            lstModelCities.add(modelDistrict);
                        }
                    }
                    vtcModelCity.setLstDistrict(modelCity.getCityName(), lstModelCities);
                }

                vtcModelCity.setLstCities(modelCity);
//                lst.put(modelCity.getId(), modelCity);
            }
        }

        return vtcModelCity;
    }

    public VtcModelCity(){

    }

    protected VtcModelCity(Parcel in) {
        id = in.readString();
        cityName = in.readString();
        districtName = in.readString();
    }

    public static final Creator<VtcModelCity> CREATOR = new Creator<VtcModelCity>() {
        @Override
        public VtcModelCity createFromParcel(Parcel in) {
            return new VtcModelCity(in);
        }

        @Override
        public VtcModelCity[] newArray(int size) {
            return new VtcModelCity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public HashMap<String, List<VtcModelCity>> getLstDistrict() {
//        if(lstDistrict != null){
//            lstDistrict = new HashMap<>();
//        }
        return lstDistrict;
    }

    public void setLstDistrict(HashMap<String, List<VtcModelCity>> lstDistrict) {
//        if(this.lstDistrict != null){
//            this.lstDistrict = new HashMap<>();
//        }
        this.lstDistrict = lstDistrict;
    }

    public void setLstDistrict(String nameParent, List<VtcModelCity> vtcModelCities) {
//        if(this.lstDistrict == null){
//            this.lstDistrict = new HashMap<>();
//        }
        this.lstDistrict.put(nameParent, vtcModelCities);
    }

    public List<VtcModelCity> getLstCities() {
//        if(lstCities == null){
//            lstCities = new ArrayList<>();
//        }
        return lstCities;
    }

    public void setLstCities(List<VtcModelCity> lstCities) {
        this.lstCities = lstCities;
    }

    public void setLstCities(VtcModelCity modelCities) {
//        if(this.lstCities == null){
//            this.lstCities = new ArrayList<>();
//        }
        this.lstCities.add(modelCities);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cityName);
        dest.writeString(districtName);
    }
}
