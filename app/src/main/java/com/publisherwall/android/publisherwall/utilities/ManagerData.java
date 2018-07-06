package com.publisherwall.android.publisherwall.utilities;

import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;

public class ManagerData implements Parcelable {

    private String managerName;
    private String email;
    private String phoneNo;
    private String skypeId;
    private String aimId;

    public ManagerData(String managerName, String email, String phoneNo, String skypeId, String aimId) {
        this.managerName = managerName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.skypeId = skypeId;
        this.aimId = aimId;
    }

    protected ManagerData(Parcel in) {
        managerName = in.readString();
        email = in.readString();
        phoneNo = in.readString();
        skypeId = in.readString();
        aimId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(managerName);
        dest.writeString(email);
        dest.writeString(phoneNo);
        dest.writeString(skypeId);
        dest.writeString(aimId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ManagerData> CREATOR = new Creator<ManagerData>() {
        @Override
        public ManagerData createFromParcel(Parcel in) {
            return new ManagerData(in);
        }

        @Override
        public ManagerData[] newArray(int size) {
            return new ManagerData[size];
        }
    };

    public String getManagerName() {
        return managerName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public String getAimId() {
        return aimId;
    }
}
