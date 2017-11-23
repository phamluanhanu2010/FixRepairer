package com.strategy.intecom.vtc.fixrepairer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;

import org.json.JSONObject;

/**
 * Created by Mr. Ha on 6/29/16.
 */
public class VtcModelRate implements Parcelable{

    private int total_rate;
    private int avg_rate;
    private int one_star;
    private int two_stars;
    private int three_stars;
    private int four_stars;
    private int five_stars;

    public VtcModelRate(){

    }

    public static VtcModelRate getRateData(JSONObject data){

        if(data != null){
            VtcModelRate vtcModelRate = new VtcModelRate();
            vtcModelRate.setTotal_rate(data.optInt(ParserJson.API_PARAMETER_RATE_TOTAL));
            vtcModelRate.setAvg_rate(data.optInt(ParserJson.API_PARAMETER_RATE_AVG));
            vtcModelRate.setOne_star(data.optInt(ParserJson.API_PARAMETER_RATE_START_ONE));
            vtcModelRate.setTwo_stars(data.optInt(ParserJson.API_PARAMETER_RATE_START_TWO));
            vtcModelRate.setThree_stars(data.optInt(ParserJson.API_PARAMETER_RATE_START_THREE));
            vtcModelRate.setFour_stars(data.optInt(ParserJson.API_PARAMETER_RATE_START_FOUR));
            vtcModelRate.setFive_stars(data.optInt(ParserJson.API_PARAMETER_RATE_START_FIVE));
            return vtcModelRate;
        }

        return null;
    }

    protected VtcModelRate(Parcel in) {
        total_rate = in.readInt();
        avg_rate = in.readInt();
        one_star = in.readInt();
        two_stars = in.readInt();
        three_stars = in.readInt();
        four_stars = in.readInt();
        five_stars = in.readInt();
    }

    public static final Creator<VtcModelRate> CREATOR = new Creator<VtcModelRate>() {
        @Override
        public VtcModelRate createFromParcel(Parcel in) {
            return new VtcModelRate(in);
        }

        @Override
        public VtcModelRate[] newArray(int size) {
            return new VtcModelRate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total_rate);
        dest.writeInt(avg_rate);
        dest.writeInt(one_star);
        dest.writeInt(two_stars);
        dest.writeInt(three_stars);
        dest.writeInt(four_stars);
        dest.writeInt(five_stars);
    }

    public int getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(int total_rate) {
        this.total_rate = total_rate;
    }

    public int getAvg_rate() {
        return avg_rate;
    }

    public void setAvg_rate(int avg_rate) {
        this.avg_rate = avg_rate;
    }

    public int getOne_star() {
        return one_star;
    }

    public void setOne_star(int one_star) {
        this.one_star = one_star;
    }

    public int getTwo_stars() {
        return two_stars;
    }

    public void setTwo_stars(int two_stars) {
        this.two_stars = two_stars;
    }

    public int getThree_stars() {
        return three_stars;
    }

    public void setThree_stars(int three_stars) {
        this.three_stars = three_stars;
    }

    public int getFour_stars() {
        return four_stars;
    }

    public void setFour_stars(int four_stars) {
        this.four_stars = four_stars;
    }

    public int getFive_stars() {
        return five_stars;
    }

    public void setFive_stars(int five_stars) {
        this.five_stars = five_stars;
    }
}
