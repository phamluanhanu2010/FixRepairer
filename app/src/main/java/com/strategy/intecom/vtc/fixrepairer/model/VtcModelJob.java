package com.strategy.intecom.vtc.fixrepairer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 6/10/16.
 */
public class VtcModelJob implements Parcelable {

    private String id;                // Mã của công việc
    private String user_name;         // Tên của người tạo việc
    private String phone;             // Số điện thoại của người tạo việc
    private String field_name;        // Tiêu đề của công việc
    private String price;             // Số tiền được thanh toán
    private String category_name;     // Tên danh mục
    private String distance;          // Khoảng cách
    private String address_name;      // Địa chỉ nơi đến
    private String description;       // Mô tả chi tiết công việc
    private String order_time;        // Ngày làm việc
    private List<String> images;      // Danh sách ảnh của công việc


    public VtcModelJob(){

    }

    public static VtcModelJob initGetDataJson(String response){

        try {
            if(response == null || response.isEmpty()){
                return null;
            }
            VtcModelJob vtcModelJob = new VtcModelJob();

            JSONObject jsonObject = new JSONObject(response);

            JSONObject orderObj = jsonObject.optJSONObject(ParserJson.API_PARAMETER_RESSPONSE_DATA);

            if(orderObj == null){
                orderObj = jsonObject;
            }

            JSONArray image = orderObj.optJSONArray(ParserJson.API_PARAMETER_IMAGES);

            if (image != null && image.length() > 0) {

                for (int i = 0; i < image.length(); i++) {
                    JSONObject objImage = image.optJSONObject(i);

                    vtcModelJob.setImages(objImage.optString(ParserJson.API_PARAMETER_LINK));
                }
            }

            JSONObject addressObj = orderObj.optJSONObject(ParserJson.API_PARAMETER_ADDRESS);

            if(addressObj != null) {
                vtcModelJob.setAddress_name(addressObj.optString(ParserJson.API_PARAMETER_NAME));
            }

            JSONObject fieldObj = orderObj.optJSONObject(ParserJson.API_PARAMETER_FIELD);

            if(fieldObj != null) {
                vtcModelJob.setCategory_name(fieldObj.optString(ParserJson.API_PARAMETER_CATEGORY_NAME));
                vtcModelJob.setField_name(fieldObj.optString(ParserJson.API_PARAMETER_NAME));
                vtcModelJob.setPrice(fieldObj.optString(ParserJson.API_PARAMETER_PRICE));
            }

            vtcModelJob.setOrder_time(orderObj.optString(ParserJson.API_PARAMETER_ORDER_TIME));
            vtcModelJob.setId(orderObj.optString(ParserJson.API_PARAMETER_ID_));

            JSONObject userObj = orderObj.optJSONObject(ParserJson.API_PARAMETER_USER);

            if(userObj != null) {
                vtcModelJob.setPhone(userObj.optString(ParserJson.API_PARAMETER_PHONE));
                vtcModelJob.setUser_name(userObj.optString(ParserJson.API_PARAMETER_NAME));
            }

            vtcModelJob.setDescription(orderObj.optString(ParserJson.API_PARAMETER_DESCRIPTION));
            vtcModelJob.setDistance(orderObj.optString(ParserJson.API_PARAMETER_DISTANCE));


            return vtcModelJob;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected VtcModelJob(Parcel in) {
        id = in.readString();
        user_name = in.readString();
        phone = in.readString();
        field_name = in.readString();
        price = in.readString();
        category_name = in.readString();
        distance = in.readString();
        address_name = in.readString();
        description = in.readString();
        order_time = in.readString();
        images = in.createStringArrayList();
    }

    public static final Creator<VtcModelJob> CREATOR = new Creator<VtcModelJob>() {
        @Override
        public VtcModelJob createFromParcel(Parcel in) {
            return new VtcModelJob(in);
        }

        @Override
        public VtcModelJob[] newArray(int size) {
            return new VtcModelJob[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_name);
        dest.writeString(phone);
        dest.writeString(field_name);
        dest.writeString(price);
        dest.writeString(category_name);
        dest.writeString(distance);
        dest.writeString(address_name);
        dest.writeString(description);
        dest.writeString(order_time);
        dest.writeStringList(images);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setImages(String image) {
        if(images == null){
            images = new ArrayList<>();
        }
        images.add(image);
    }
}
