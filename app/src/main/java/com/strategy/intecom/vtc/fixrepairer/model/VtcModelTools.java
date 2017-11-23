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
 * Created by Mr. Ha on 6/10/16.
 */
public class VtcModelTools implements Parcelable{


    private String id;                // Mã của Công cụ
    private String name;              // Tiêu đề tên của Công cụ
    private String description;       // Mô tả về Công cụ
    private String tThumb;            // Ảnh thumb của Công cụ
    private String price;             // Giá thuê của công cụ
    private int version;              // version của công cụ so với server
    private int tCount;               // Số lượng của công cụ
    private String parentID;          // ID parent thuộc nhóm công cụ
    private boolean tStatus;          // Trạng thái của công cụ

    private static List<VtcModelTools> lstToolsParent = new ArrayList<>();   // List Tools Parent
    private static HashMap<String, List<VtcModelTools>> lstToolsChild = new HashMap<>();   // List Tools Child

    private Field field;     // Thông tin fields cha

    public VtcModelTools(){

    }

    public static VtcModelTools getLstDataTools(JSONArray jsonArrayTools){
        VtcModelTools vtcModelTools = new VtcModelTools();

//        HashMap<String, VtcModelTools> lst = new HashMap<>();
        if(jsonArrayTools != null){
            for (int i = 0; i < jsonArrayTools.length(); i++){

                VtcModelTools modelTools = new VtcModelTools();

                JSONObject objectTools = jsonArrayTools.optJSONObject(i);

                modelTools.setId(objectTools.optString(ParserJson.API_PARAMETER_ID_));
                modelTools.setName(objectTools.optString(ParserJson.API_PARAMETER_NAME));
                modelTools.setDescription(objectTools.optString(ParserJson.API_PARAMETER_DESCRIPTION));
                modelTools.setPrice(objectTools.optString(ParserJson.API_PARAMETER_PRICE));
                modelTools.setVersion(objectTools.optInt(ParserJson.API_PARAMETER_VERSION));
                modelTools.settThumb(objectTools.optString(ParserJson.API_PARAMETER_THUMB));

                JSONObject objectFields = objectTools.optJSONObject(ParserJson.API_PARAMETER_FIELD);

                if(objectFields != null){
                    modelTools.getField().setId(objectFields.optString(ParserJson.API_PARAMETER_ID_));
                    modelTools.getField().setName(objectFields.optString(ParserJson.API_PARAMETER_NAME));
                    AppCore.showLog("-------Tools Fields----- : " + modelTools.getField().getName());
                }

                AppCore.showLog("-------Tools----- : " + modelTools.getName());

                JSONArray jsonArrayChild = objectTools.optJSONArray(ParserJson.API_PARAMETER_CHILD);
                if(jsonArrayChild != null){
                    List<VtcModelTools> lstToolsChild = new ArrayList<>();
                    for (int j = 0; j < jsonArrayChild.length(); j++){
                        JSONObject objectChild = jsonArrayChild.optJSONObject(j);

                        VtcModelTools modelToolsChild = new VtcModelTools();

                        modelToolsChild.setId(objectChild.optString(ParserJson.API_PARAMETER_ID_));
                        modelToolsChild.setName(objectChild.optString(ParserJson.API_PARAMETER_NAME));
                        modelToolsChild.setDescription(objectChild.optString(ParserJson.API_PARAMETER_DESCRIPTION));
                        modelToolsChild.setPrice(objectChild.optString(ParserJson.API_PARAMETER_PRICE));
                        modelToolsChild.setVersion(objectChild.optInt(ParserJson.API_PARAMETER_VERSION));
                        modelToolsChild.setParentID(objectChild.optString(ParserJson.API_PARAMETER_PARENT));
                        modelToolsChild.settThumb(objectChild.optString(ParserJson.API_PARAMETER_THUMB));

                        AppCore.showLog("-------Tools Child----- : " + modelToolsChild.getName());

//                        modelTools.setLstToolsChild(modelToolsChild);
                        lstToolsChild.add(modelToolsChild);
                    }

                    vtcModelTools.setLstToolsChild(modelTools.getName(), lstToolsChild);
                }

                vtcModelTools.setLstToolsParent(modelTools);
//                lst.put(modelTools.getId(), modelTools);
            }
        }

        return vtcModelTools;
    }

    protected VtcModelTools(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        tThumb = in.readString();
        price = in.readString();
        version = in.readInt();
        tCount = in.readInt();
        parentID = in.readString();
        tStatus = in.readByte() != 0;
        field = in.readParcelable(Field.class.getClassLoader());
    }

    public static final Creator<VtcModelTools> CREATOR = new Creator<VtcModelTools>() {
        @Override
        public VtcModelTools createFromParcel(Parcel in) {
            return new VtcModelTools(in);
        }

        @Override
        public VtcModelTools[] newArray(int size) {
            return new VtcModelTools[size];
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
        dest.writeString(tThumb);
        dest.writeString(price);
        dest.writeInt(version);
        dest.writeInt(tCount);
        dest.writeString(parentID);
        dest.writeByte((byte) (tStatus ? 1 : 0));
        dest.writeParcelable(field, flags);
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

    public String gettThumb() {
        return tThumb;
    }

    public void settThumb(String tThumb) {
        this.tThumb = tThumb;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int gettCount() {
        return tCount;
    }

    public void settCount(int tCount) {
        this.tCount = tCount;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public boolean istStatus() {
        return tStatus;
    }

    public void settStatus(boolean tStatus) {
        this.tStatus = tStatus;
    }

    public HashMap<String, List<VtcModelTools>> getLstToolsChild() {
//        if(lstToolsChild != null){
//            lstToolsChild = new HashMap<>();
//        }
        return lstToolsChild;
    }

    public void setLstToolsChild(HashMap<String, List<VtcModelTools>> lstToolsChild) {
//        if(this.lstToolsChild != null){
//            this.lstToolsChild = new HashMap<>();
//        }
        this.lstToolsChild = lstToolsChild;
    }

    public void setLstToolsChild(String nameParent, List<VtcModelTools> toolsChild) {
//        if(this.lstToolsChild == null){
//            this.lstToolsChild = new HashMap<>();
//        }
        this.lstToolsChild.put(nameParent, toolsChild);
    }

    public List<VtcModelTools> getLstToolsParent() {
//        if(lstToolsParent == null){
//            lstToolsParent = new ArrayList<>();
//        }
        return lstToolsParent;
    }

    public void setLstToolsParent(List<VtcModelTools> lstToolsParent) {
        this.lstToolsParent = lstToolsParent;
    }

    public void setLstToolsParent(VtcModelTools toolsParent) {
//        if(this.lstToolsParent == null){
//            this.lstToolsParent = new ArrayList<>();
//        }
        this.lstToolsParent.add(toolsParent);
    }

    public Field getField() {
        if(field == null)
            field = new Field();
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public class Field implements Parcelable{

        private String id = "";
        private String name = "";

        public Field(){

        }

        protected Field(Parcel in) {
            id = in.readString();
            name = in.readString();
        }

        public final Creator<Field> CREATOR = new Creator<Field>() {
            @Override
            public Field createFromParcel(Parcel in) {
                return new Field(in);
            }

            @Override
            public Field[] newArray(int size) {
                return new Field[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
        }
    }

}
