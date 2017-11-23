package com.strategy.intecom.vtc.fixrepairer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VtcModelUser implements Parcelable {

    private static final String TAG = VtcModelUser.class.getName();

    private String id = "";
    private String auth_token = "";
    private String User_full_name = "";
    private String User_Phone_Num = "";
    private String User_Email = "";
    private String User_Avatar = "";
    private boolean sms_verify = Boolean.FALSE;
    private String User_CMTND = "";
    private int User_City_Id = 0;
    private String User_City = "";
    private int User_District_Id = 0;
    private String User_District = "";
    private String User_Description = "";
    private int point = 0;
    private String system_id = "";
    private int level = 0;
    private String created_at = "";
    private int version = 0;
    private boolean available = Boolean.FALSE;  // Trạng thái Agency có muốn nhận việc hay ko
    private boolean is_working = Boolean.FALSE;  // Trạng thái Agency đang hoặc không làm việc
    private boolean admin_verify = Boolean.FALSE;  // Trạng thái Agency được admin chấp nhận được làm việc

    private String password = "";

    private List<Comments> comments;
    private List<Skills> skills;
    private List<Skills> skillsParent;

    private Address address;

    private VtcModelRate rate;

    public VtcModelUser() {

    }

    protected VtcModelUser(Parcel in) {
        id = in.readString();
        auth_token = in.readString();
        User_full_name = in.readString();
        User_Phone_Num = in.readString();
        User_Email = in.readString();
        User_Avatar = in.readString();
        sms_verify = in.readByte() != 0;
        User_CMTND = in.readString();
        User_City_Id = in.readInt();
        User_City = in.readString();
        User_District_Id = in.readInt();
        User_District = in.readString();
        User_Description = in.readString();
        point = in.readInt();
        system_id = in.readString();
        level = in.readInt();
        created_at = in.readString();
        version = in.readInt();
        available = in.readByte() != 0;
        is_working = in.readByte() != 0;
        password = in.readString();
        admin_verify = in.readByte() != 0;
        comments = in.createTypedArrayList(Comments.CREATOR);
        skills = in.createTypedArrayList(Skills.CREATOR);
        skillsParent = in.createTypedArrayList(Skills.CREATOR);
        address = in.readParcelable(Address.class.getClassLoader());
        rate = in.readParcelable(VtcModelRate.class.getClassLoader());
    }

    public static final Creator<VtcModelUser> CREATOR = new Creator<VtcModelUser>() {
        @Override
        public VtcModelUser createFromParcel(Parcel in) {
            return new VtcModelUser(in);
        }

        @Override
        public VtcModelUser[] newArray(int size) {
            return new VtcModelUser[size];
        }
    };

    public static VtcModelUser getDataPaserUser(JSONObject data) {

        if (data != null) {
            VtcModelUser vtcModelUser = new VtcModelUser();

            vtcModelUser.setAuth_token(data.optString(ParserJson.API_PARAMETER_TOKEN));

            JSONObject jsonUser = data.optJSONObject(ParserJson.API_PARAMETER_INFO);

            if (jsonUser == null) {
                jsonUser = data;
            }
            vtcModelUser.setId(jsonUser.optString(ParserJson.API_PARAMETER_ID_));
            vtcModelUser.setUser_full_name(jsonUser.optString(ParserJson.API_PARAMETER_NAME));
            vtcModelUser.setUser_Phone_Num(jsonUser.optString(ParserJson.API_PARAMETER_PHONE));
            vtcModelUser.setUser_Email(jsonUser.optString(ParserJson.API_PARAMETER_EMAIL));
            vtcModelUser.setUser_Avatar(jsonUser.optString(ParserJson.API_PARAMETER_AVATAR));
            vtcModelUser.setUser_CMTND(jsonUser.optString(ParserJson.API_PARAMETER_CMTND));
            vtcModelUser.setUser_City(jsonUser.optString(ParserJson.API_PARAMETER_CITY));
            vtcModelUser.setUser_District(jsonUser.optString(ParserJson.API_PARAMETER_DISTRICT));
            vtcModelUser.setUser_Description(jsonUser.optString(ParserJson.API_PARAMETER_DESCRIPTION));
            vtcModelUser.setPoint(jsonUser.optInt(ParserJson.API_PARAMETER_POINT));
            vtcModelUser.setSystem_id(jsonUser.optString(ParserJson.API_PARAMETER_SYSTEM_ID));
            vtcModelUser.setLevel(jsonUser.optInt(ParserJson.API_PARAMETER_LEVEL));
            vtcModelUser.setCreated_at(jsonUser.optString(ParserJson.API_PARAMETER_CREATE_AT));
            vtcModelUser.setVersion(jsonUser.optInt(ParserJson.API_PARAMETER_VERSION));
            vtcModelUser.setAvailable(jsonUser.optBoolean(ParserJson.API_PARAMETER_AVAILABLE));
            vtcModelUser.setIs_working(jsonUser.optBoolean(ParserJson.API_PARAMETER_IS_WORKING));
            vtcModelUser.setAdmin_verify(jsonUser.optBoolean(ParserJson.API_PARAMETER_ADMIN_VERIFY));

            vtcModelUser.setComments(Comments.lstComment(jsonUser.optJSONArray(ParserJson.API_PARAMETER_COMMENTS)));

            JSONArray arraySkill = jsonUser.optJSONArray(ParserJson.API_PARAMETER_SKILLS);

            if(arraySkill != null) {

                for (int i = 0; i < arraySkill.length(); i++) {

                    JSONObject jsonObject = arraySkill.optJSONObject(i);

                    if (jsonObject != null) {

                        Skills skills = Skills.getSkills(jsonObject);

                        vtcModelUser.setSkillsParent(skills);

                        JSONArray objSkill = jsonObject.optJSONArray(ParserJson.API_PARAMETER_CHILD);

                        if (objSkill != null) {

                            for (int j = 0; j < objSkill.length(); j++) {
                                JSONObject object = objSkill.optJSONObject(j);
                                if (object != null) {

                                    Skills skills1 = new Skills();
                                    skills1.setName(object.optString(ParserJson.API_PARAMETER_NAME));
                                    skills1.set_id(object.optString(ParserJson.API_PARAMETER_ID_));

                                    AppCore.showLog("------------------------ : " + skills1.getName());

                                    vtcModelUser.setSkill(skills1);
                                }
                            }
                        }
                    }
                }
            }

            vtcModelUser.setRate(VtcModelRate.getRateData(jsonUser.optJSONObject(ParserJson.API_PARAMETER_RATE)));
            vtcModelUser.setAddress(Address.getAddress(jsonUser.optJSONObject(ParserJson.API_PARAMETER_ADDRESS)));
            return vtcModelUser;
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(auth_token);
        dest.writeString(User_full_name);
        dest.writeString(User_Phone_Num);
        dest.writeString(User_Email);
        dest.writeString(User_Avatar);
        dest.writeByte((byte) (sms_verify ? 1 : 0));
        dest.writeString(User_CMTND);
        dest.writeInt(User_City_Id);
        dest.writeString(User_City);
        dest.writeInt(User_District_Id);
        dest.writeString(User_District);
        dest.writeString(User_Description);
        dest.writeInt(point);
        dest.writeString(system_id);
        dest.writeInt(level);
        dest.writeString(created_at);
        dest.writeInt(version);
        dest.writeByte((byte) (available ? 1 : 0));
        dest.writeByte((byte) (is_working ? 1 : 0));
        dest.writeByte((byte) (admin_verify ? 1 : 0));
        dest.writeString(password);
        dest.writeTypedList(comments);
        dest.writeTypedList(skills);
        dest.writeTypedList(skillsParent);
        dest.writeParcelable(address, flags);
        dest.writeParcelable(rate, flags);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getUser_full_name() {
        return User_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        User_full_name = user_full_name;
    }

    public String getUser_Phone_Num() {
        return User_Phone_Num;
    }

    public void setUser_Phone_Num(String user_Phone_Num) {
        User_Phone_Num = user_Phone_Num;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getUser_Avatar() {
        return User_Avatar;
    }

    public void setUser_Avatar(String user_Avatar) {
        User_Avatar = user_Avatar;
    }

    public boolean isSms_verify() {
        return sms_verify;
    }

    public void setSms_verify(boolean sms_verify) {
        this.sms_verify = sms_verify;
    }

    public String getUser_CMTND() {
        return User_CMTND;
    }

    public void setUser_CMTND(String user_CMTND) {
        User_CMTND = user_CMTND;
    }

    public int getUser_City_Id() {
        return User_City_Id;
    }

    public void setUser_City_Id(int user_City_Id) {
        User_City_Id = user_City_Id;
    }

    public String getUser_City() {
        return User_City;
    }

    public void setUser_City(String user_City) {
        User_City = user_City;
    }

    public int getUser_District_Id() {
        return User_District_Id;
    }

    public void setUser_District_Id(int user_District_Id) {
        User_District_Id = user_District_Id;
    }

    public String getUser_District() {
        return User_District;
    }

    public void setUser_District(String user_District) {
        User_District = user_District;
    }

    public String getUser_Description() {
        return User_Description;
    }

    public void setUser_Description(String user_Description) {
        User_Description = user_Description;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean is_working() {
        return is_working;
    }

    public void setIs_working(boolean is_working) {
        this.is_working = is_working;
    }

    public boolean isAdmin_verify() {
        return admin_verify;
    }

    public void setAdmin_verify(boolean admin_verify) {
        this.admin_verify = admin_verify;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    public void setSkill(Skills skill) {
        if(this.skills == null){
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }

    public List<Skills> getSkillsParent() {
        return skillsParent;
    }

    public void setSkillsParent(List<Skills> skillsParent) {
        this.skillsParent = skillsParent;
    }

    public void setSkillsParent(Skills skillsParent) {
        if(this.skillsParent == null){
            this.skillsParent = new ArrayList<>();
        }
        this.skillsParent.add(skillsParent);
    }

    public Address getAddress() {
        if(address == null){
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public VtcModelRate getRate() {
        return rate;
    }

    public void setRate(VtcModelRate rate) {
        this.rate = rate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Comments implements Parcelable {

        private int stars;
        private String customer_name;
        private String message;
        private String _id;

        protected Comments(Parcel in) {
            stars = in.readInt();
            customer_name = in.readString();
            message = in.readString();
            _id = in.readString();
        }

        public Comments() {

        }

        public static List<Comments> lstComment(JSONArray jsonArray) {

            List<Comments> commentsList = new ArrayList<>();

            if (jsonArray != null && jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    if (object != null) {

                        Comments comments = new Comments();
                        comments.setStars(object.optInt(ParserJson.API_PARAMETER_RATE_START));
                        comments.setCustomer_name(object.optString(ParserJson.API_PARAMETER_CUSTOMER_NAME));
                        comments.setMessage(object.optString(ParserJson.API_PARAMETER_MESSAGE));
                        comments.set_id(object.optString(ParserJson.API_PARAMETER_ID_));

                        commentsList.add(comments);
                    }

                }
            }

            return commentsList;
        }

        public static final Creator<Comments> CREATOR = new Creator<Comments>() {
            @Override
            public Comments createFromParcel(Parcel in) {
                return new Comments(in);
            }

            @Override
            public Comments[] newArray(int size) {
                return new Comments[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(stars);
            dest.writeString(customer_name);
            dest.writeString(message);
            dest.writeString(_id);
        }

        public int getStars() {
            return stars;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }

    public static class Skills implements Parcelable {
        private String name;
        private String _id;
        private String image;
        private int count;
        private boolean isChoice;

        public Skills() {

        }

        protected Skills(Parcel in) {
            name = in.readString();
            _id = in.readString();
            image = in.readString();
            isChoice = in.readByte() != 0;
        }

        public static final Creator<Skills> CREATOR = new Creator<Skills>() {
            @Override
            public Skills createFromParcel(Parcel in) {
                return new Skills(in);
            }

            @Override
            public Skills[] newArray(int size) {
                return new Skills[size];
            }
        };

        public static List<Skills> lstSkills(JSONArray jsonArray) {

            List<Skills> skillsList = new ArrayList<>();

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    if (object != null) {

                        Skills skills = new Skills();
                        skills.setName(object.optString(ParserJson.API_PARAMETER_NAME));
                        skills.set_id(object.optString(ParserJson.API_PARAMETER_ID_));

                        skillsList.add(skills);
                    }
                }
            }

            return skillsList;
        }

        public static Skills getSkills(JSONObject object) {

            if (object != null) {

                Skills skills = new Skills();
                skills.setName(object.optString(ParserJson.API_PARAMETER_NAME));
                skills.set_id(object.optString(ParserJson.API_PARAMETER_ID_));
                skills.setImage(object.optString(ParserJson.API_PARAMETER_THUMB));

                return skills;
            }

            return null;
        }


        public boolean isChoice() {
            return isChoice;
        }

        public void setChoice(boolean choice) {
            isChoice = choice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getImage() {
            return image;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(_id);
            dest.writeString(image);
            dest.writeByte((byte) (isChoice ? 1 : 0));
        }
    }

    public static class Address implements Parcelable {
        private String name;
        private String longt = "0.0";
        private String latt = "0.0";

        public Address() {

        }

        public static Address getAddress(JSONObject jsonObject) {
            if (jsonObject != null) {
                Address address = new Address();
                address.setName(jsonObject.optString(ParserJson.API_PARAMETER_NAME));
                address.setLongt(jsonObject.optString(ParserJson.API_PARAMETER_LONGITUDE));
                address.setLatt(jsonObject.optString(ParserJson.API_PARAMETER_LATITUDE));

                return address;
            }
            return null;
        }

        protected Address(Parcel in) {
            name = in.readString();
            longt = in.readString();
            latt = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(longt);
            dest.writeString(latt);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Address> CREATOR = new Creator<Address>() {
            @Override
            public Address createFromParcel(Parcel in) {
                return new Address(in);
            }

            @Override
            public Address[] newArray(int size) {
                return new Address[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLongt() {
            return longt;
        }

        public void setLongt(String longt) {
            this.longt = longt;
        }

        public String getLatt() {
            return latt;
        }

        public void setLatt(String latt) {
            this.latt = latt;
        }
    }
}
