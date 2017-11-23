//package com.strategy.intecom.vtc.fixrepairer.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
//import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Mr. Ha on 6/10/16.
// */
//public class VtcModelTools_Backup extends VtcModelService {
//
//    private int tCode;                // Mã của Công cụ
//    private String tName;             // Tiêu đề tên của Công cụ
//    private String tDescription;      // Mô tả về Công cụ
//    private String tThumb;            // Ảnh thumb của Công cụ
//    private String tPrice;            // Giá thuê của công cụ
//    private int tCount;               // Số lượng của công cụ
//    private boolean tStatus;          // Trạng thái của công cụ
//
//    public VtcModelTools_Backup(){
//
//    }
//
//    protected VtcModelTools_Backup(Parcel in) {
//        tCode = in.readInt();
//        tName = in.readString();
//        tDescription = in.readString();
//        tThumb = in.readString();
//        tPrice = in.readString();
//        tCount = in.readInt();
//        tStatus = in.readByte() != 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(tCode);
//        dest.writeString(tName);
//        dest.writeString(tDescription);
//        dest.writeString(tThumb);
//        dest.writeString(tPrice);
//        dest.writeInt(tCount);
//        dest.writeByte((byte) (tStatus ? 1 : 0));
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<VtcModelTools> CREATOR = new Creator<VtcModelTools>() {
//        @Override
//        public VtcModelTools createFromParcel(Parcel in) {
//            return new VtcModelTools(in);
//        }
//
//        @Override
//        public VtcModelTools[] newArray(int size) {
//            return new VtcModelTools[size];
//        }
//    };
//
//    public int gettCode() {
//        return tCode;
//    }
//
//    public void settCode(int tCode) {
//        this.tCode = tCode;
//    }
//
//    public String gettName() {
//        return tName;
//    }
//
//    public void settName(String tName) {
//        this.tName = tName;
//    }
//
//    public String gettDescription() {
//        return tDescription;
//    }
//
//    public void settDescription(String tDescription) {
//        this.tDescription = tDescription;
//    }
//
//    public String gettThumb() {
//        return tThumb;
//    }
//
//    public void settThumb(String tThumb) {
//        this.tThumb = tThumb;
//    }
//
//    public String gettPrice() {
//        return tPrice;
//    }
//
//    public void settPrice(String tPrice) {
//        this.tPrice = tPrice;
//    }
//
//    public int gettCount() {
//        return tCount;
//    }
//
//    public void settCount(int tCount) {
//        this.tCount = tCount;
//    }
//
//    public boolean istStatus() {
//        return tStatus;
//    }
//
//    public void settStatus(boolean tStatus) {
//        this.tStatus = tStatus;
//    }
//}
