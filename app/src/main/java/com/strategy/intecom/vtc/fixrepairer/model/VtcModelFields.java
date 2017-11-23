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
public class VtcModelFields implements Parcelable{

    private static final String TAG = VtcModelFields.class.getName();

    private String id = "";
    private String name = "";
    private String description = "";
    private String price = "";
    private String image = "";
    private int version = 0;
    private boolean isChoice = false;

    private static List<VtcModelFields> lstFields = new ArrayList<>();   // List Fields
    private static HashMap<String, List<VtcModelFields>> lstFieldsChild = new HashMap<>();   // List Fields Child
    private static List<VtcModelFields> lstFieldsChildModel = new ArrayList<>();   // List Fields

    protected VtcModelFields(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readString();
        image = in.readString();
        version = in.readInt();
        isChoice = in.readByte() != 0;
    }

    public static final Creator<VtcModelFields> CREATOR = new Creator<VtcModelFields>() {
        @Override
        public VtcModelFields createFromParcel(Parcel in) {
            return new VtcModelFields(in);
        }

        @Override
        public VtcModelFields[] newArray(int size) {
            return new VtcModelFields[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(image);
        dest.writeInt(version);
        dest.writeByte((byte) (isChoice ? 1 : 0));
    }

    public static VtcModelFields getLstDataFields(JSONArray jsonCity){
        VtcModelFields vtcModelFields = new VtcModelFields();
        lstFields = new ArrayList<>();   // List Fields
        lstFieldsChild = new HashMap<>();   // List Fields Child
        lstFieldsChildModel = new ArrayList<>();   // List Fields

        if(jsonCity != null){
            for (int i = 0; i < jsonCity.length(); i++){

                VtcModelFields modelFields = new VtcModelFields();

                JSONObject jsonFields = jsonCity.optJSONObject(i);

                modelFields.setId(jsonFields.optString(ParserJson.API_PARAMETER_ID_));
                modelFields.setName(jsonFields.optString(ParserJson.API_PARAMETER_NAME));
                modelFields.setDescription(jsonFields.optString(ParserJson.API_PARAMETER_DESCRIPTION));
                modelFields.setPrice(jsonFields.optString(ParserJson.API_PARAMETER_PRICE));
                modelFields.setImage(jsonFields.optString(ParserJson.API_PARAMETER_THUMB));
                modelFields.setVersion(jsonFields.optInt(ParserJson.API_PARAMETER_VERSION));

                AppCore.showLog("-------Fields----- : " + modelFields.getId());

                JSONArray jsonArrayChild = jsonFields.optJSONArray(ParserJson.API_PARAMETER_CHILD);
                if(jsonArrayChild != null){
                    List<VtcModelFields> lstFields = new ArrayList<>();
                    for (int j = 0; j < jsonArrayChild.length(); j++){
                        JSONObject objectChild = jsonArrayChild.optJSONObject(j);

                        VtcModelFields modelFieldsChild = new VtcModelFields();

                        modelFieldsChild.setId(objectChild.optString(ParserJson.API_PARAMETER_ID_));
                        modelFieldsChild.setName(objectChild.optString(ParserJson.API_PARAMETER_NAME));
                        modelFieldsChild.setDescription(objectChild.optString(ParserJson.API_PARAMETER_DESCRIPTION));
                        modelFieldsChild.setPrice(objectChild.optString(ParserJson.API_PARAMETER_PRICE));
                        modelFieldsChild.setImage(objectChild.optString(ParserJson.API_PARAMETER_THUMB));
                        modelFieldsChild.setVersion(objectChild.optInt(ParserJson.API_PARAMETER_VERSION));

                        AppCore.showLog("-------Fields Child----- : " + modelFieldsChild.getId());

                        lstFields.add(modelFieldsChild);
                        vtcModelFields.setLstFieldsChildModel(modelFieldsChild);
                    }
                    vtcModelFields.setLstFieldsChild(modelFields.getName(), lstFields);
                }

                vtcModelFields.setLstFields(modelFields);
            }
        }

        return vtcModelFields;
    }

    public VtcModelFields(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public HashMap<String, List<VtcModelFields>> getLstFieldsChild() {
//        if(lstFieldsChild != null){
//            lstFieldsChild = new HashMap<>();
//        }
        return lstFieldsChild;
    }

    public void setLstFieldsChild(HashMap<String, List<VtcModelFields>> lstFieldsChild) {
//        if(this.lstFieldsChild != null){
//            this.lstFieldsChild = new HashMap<>();
//        }
        this.lstFieldsChild = lstFieldsChild;
    }

    public void setLstFieldsChild(String nameParent, List<VtcModelFields> lstFields) {
//        if(this.lstFieldsChild == null){
//            this.lstFieldsChild = new HashMap<>();
//        }
        this.lstFieldsChild.put(nameParent, lstFields);
    }

    public List<VtcModelFields> getLstFieldsParent() {
//        if(lstFields == null){
//            lstFields = new ArrayList<>();
//        }
        return lstFields;
    }

    public void setLstFields(List<VtcModelFields> lstFields) {
        this.lstFields = lstFields;
    }

    public void setLstFields(VtcModelFields modelFields) {
//        if(this.lstFields == null){
//            this.lstFields = new ArrayList<>();
//        }
        this.lstFields.add(modelFields);
    }

    public List<VtcModelFields> getLstFieldsChildModel() {
        return lstFieldsChildModel;
    }

    public void setLstFieldsChildModel(List<VtcModelFields> lstFieldsChildModel) {
        VtcModelFields.lstFieldsChildModel = lstFieldsChildModel;
    }

    public void setLstFieldsChildModel(VtcModelFields childModel) {
        VtcModelFields.lstFieldsChildModel.add(childModel);
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }
}
