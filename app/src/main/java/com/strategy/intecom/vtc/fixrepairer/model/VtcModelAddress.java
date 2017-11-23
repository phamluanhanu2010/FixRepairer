package com.strategy.intecom.vtc.fixrepairer.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VtcModelAddress implements Parcelable{

    private static final String TAG = VtcModelAddress.class.getName();

    private int id = 0;
    private String distance = "";
    private String address = "";
    private String description = "";
    private double longitude = 0.0f;
    private double latitude = 0.0f;
    private boolean isSave = Boolean.FALSE;

    public VtcModelAddress(){

    }

    protected VtcModelAddress(Parcel in) {
        id = in.readInt();
        distance = in.readString();
        address = in.readString();
        description = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        isSave = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(distance);
        dest.writeString(address);
        dest.writeString(description);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeByte((byte) (isSave ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VtcModelAddress> CREATOR = new Creator<VtcModelAddress>() {
        @Override
        public VtcModelAddress createFromParcel(Parcel in) {
            return new VtcModelAddress(in);
        }

        @Override
        public VtcModelAddress[] newArray(int size) {
            return new VtcModelAddress[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }
}
