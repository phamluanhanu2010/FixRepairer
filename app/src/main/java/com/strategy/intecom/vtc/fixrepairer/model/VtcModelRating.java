package com.strategy.intecom.vtc.fixrepairer.model;


/**
 * Created by Mr. Ha on 6/10/16.
 */
public class VtcModelRating {

    private int userID;               // User ID
    private String rName;             // Tiêu đề rating
    private String rDescription;      // Mô tả về rating
    private String rRatingCount;      // Số lượng Rating
    private int rLevelRepairer = 0;       // Trạng thái của rating

    public static final int LEVEL_REPAIRER_LV1 = 1;
    public static final int LEVEL_REPAIRER_LV2 = 2;
    public static final int LEVEL_REPAIRER_LV3 = 3;
    public static final int LEVEL_REPAIRER_LV4 = 4;
    public static final int LEVEL_REPAIRER_LV5 = 5;

    public VtcModelRating(){

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrDescription() {
        return rDescription;
    }

    public void setrDescription(String rDescription) {
        this.rDescription = rDescription;
    }

    public String getrRatingCount() {
        return rRatingCount;
    }

    public void setrRatingCount(String rRatingCount) {
        this.rRatingCount = rRatingCount;
    }

    public int getrLevelRepairer() {
        return rLevelRepairer;
    }

    public void setrLevelRepairer(int rLevelRepairer) {
        this.rLevelRepairer = rLevelRepairer;
    }
}
