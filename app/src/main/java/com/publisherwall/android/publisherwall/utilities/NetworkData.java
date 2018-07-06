package com.publisherwall.android.publisherwall.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import com.publisherwall.android.publisherwall.data.NetworkEntry;

public class NetworkData implements Parcelable{

    private String sno;
    private String name;
    private String imageUrl;
    private String joinUrl;
    private String commission;
    private String minPay;
    private String payFrequency;
    private String offers;
    private String shareUrl;

    public NetworkData(String sno, String name, String imageUrl, String joinUrl, String commission, String minPay, String payFrequency, String offers,String ShareUrl) {
        this.sno = sno;
        this.name = name;
        this.imageUrl = imageUrl;
        this.joinUrl = joinUrl;
        this.commission = commission;
        this.minPay = minPay;
        this.payFrequency = payFrequency;
        this.offers = offers;
        this.shareUrl = ShareUrl;
    }

    public NetworkData(NetworkEntry networkEntry) {
        this.sno = networkEntry.getNetworkSno();
        this.name = networkEntry.getNetworkName();
        this.imageUrl = networkEntry.getNetworkImageUrl();
        this.joinUrl = networkEntry.getNetworkJoinUrl();
        this.commission = networkEntry.getNetworkCommission();
        this.minPay = networkEntry.getNetworkMinPay();
        this.payFrequency = networkEntry.getNetworkPayFrequency();
        this.offers = networkEntry.getNetworkOffers();
        this.shareUrl = networkEntry.getNetworkShareUrl();
    }
    protected NetworkData(Parcel in) {
        sno = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        joinUrl = in.readString();
        commission = in.readString();
        minPay = in.readString();
        payFrequency = in.readString();
        offers = in.readString();
        shareUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sno);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(joinUrl);
        dest.writeString(commission);
        dest.writeString(minPay);
        dest.writeString(payFrequency);
        dest.writeString(offers);
        dest.writeString(shareUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NetworkData> CREATOR = new Creator<NetworkData>() {
        @Override
        public NetworkData createFromParcel(Parcel in) {
            return new NetworkData(in);
        }

        @Override
        public NetworkData[] newArray(int size) {
            return new NetworkData[size];
        }
    };

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getJoinUrl() {
        return joinUrl;
    }

    public void setJoinUrl(String joinUrl) {
        this.joinUrl = joinUrl;
    }

    public String getCommission() {
        return commission;
    }

    public String getMinPay() {
        return minPay;
    }

    public String getPayFrequency() {
        return payFrequency;
    }

    public String getOffers() {
        return offers;
    }

    public String getShareUrl() {
        return shareUrl;
    }
}
