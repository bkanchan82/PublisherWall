package com.publisherwall.android.publisherwall.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class CompleteNetworkData implements
        Parcelable{

    private String sno ;
    private String networkName ;
    private String affpayingURL ;
    private String networkImageURL;
    private String networkJoinURL;
    private String img;
    private String bigImg;
    private String phone;
    private String email;
    private String offers;
    private String comm;
    private String minpay;
    private String payfrq;
    private String paymeth;
    private String refcomm;
    private String tracksoft;
    private String tracklink;
    private String twitter;
    private String facebook;
    private String description;
    private String likes;
    private String status;
    private String position;
    private String joindate;

    public CompleteNetworkData(String sno,
                               String networkName,
                               String affpayingURL,
                               String networkImageURL,
                               String networkJoinURL,
                               String img,
                               String bigImg,
                               String phone,
                               String email,
                               String offers,
                               String comm,
                               String minpay,
                               String payfrq,
                               String paymeth,
                               String refcomm,
                               String tracksoft,
                               String tracklink,
                               String twitter,
                               String facebook,
                               String description,
                               String likes,
                               String status,
                               String position,
                               String joindate) {
        this.sno = sno;
        this.networkName = networkName;
        this.affpayingURL = affpayingURL;
        this.networkImageURL = networkImageURL;
        this.networkJoinURL = networkJoinURL;
        this.img = img;
        this.bigImg = bigImg;
        this.phone = phone;
        this.email = email;
        this.offers = offers;
        this.comm = comm;
        this.minpay = minpay;
        this.payfrq = payfrq;
        this.paymeth = paymeth;
        this.refcomm = refcomm;
        this.tracksoft = tracksoft;
        this.tracklink = tracklink;
        this.twitter = twitter;
        this.facebook = facebook;
        this.description = description;
        this.likes = likes;
        this.status = status;
        this.position = position;
        this.joindate = joindate;
    }

    protected CompleteNetworkData(Parcel in) {
        sno = in.readString();
        networkName = in.readString();
        affpayingURL = in.readString();
        networkImageURL = in.readString();
        networkJoinURL = in.readString();
        img = in.readString();
        bigImg = in.readString();
        phone = in.readString();
        email = in.readString();
        offers = in.readString();
        comm = in.readString();
        minpay = in.readString();
        payfrq = in.readString();
        paymeth = in.readString();
        refcomm = in.readString();
        tracksoft = in.readString();
        tracklink = in.readString();
        twitter = in.readString();
        facebook = in.readString();
        description = in.readString();
        likes = in.readString();
        status = in.readString();
        position = in.readString();
        joindate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sno);
        dest.writeString(networkName);
        dest.writeString(affpayingURL);
        dest.writeString(networkImageURL);
        dest.writeString(networkJoinURL);
        dest.writeString(img);
        dest.writeString(bigImg);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(offers);
        dest.writeString(comm);
        dest.writeString(minpay);
        dest.writeString(payfrq);
        dest.writeString(paymeth);
        dest.writeString(refcomm);
        dest.writeString(tracksoft);
        dest.writeString(tracklink);
        dest.writeString(twitter);
        dest.writeString(facebook);
        dest.writeString(description);
        dest.writeString(likes);
        dest.writeString(status);
        dest.writeString(position);
        dest.writeString(joindate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompleteNetworkData> CREATOR = new Creator<CompleteNetworkData>() {
        @Override
        public CompleteNetworkData createFromParcel(Parcel in) {
            return new CompleteNetworkData(in);
        }

        @Override
        public CompleteNetworkData[] newArray(int size) {
            return new CompleteNetworkData[size];
        }
    };

    public String getSno() {
        return sno;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getAffpayingURL() {
        return affpayingURL;
    }

    public String getNetworkImageURL() {
        return networkImageURL;
    }

    public String getNetworkJoinURL() {
        return networkJoinURL;
    }

    public String getImg() {
        return img;
    }

    public String getBigImg() {
        return bigImg;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getOffers() {
        return offers;
    }

    public String getComm() {
        return comm;
    }

    public String getMinpay() {
        return minpay;
    }

    public String getPayfrq() {
        return payfrq;
    }

    public String getPaymeth() {
        return paymeth;
    }

    public String getRefcomm() {
        return refcomm;
    }

    public String getTracksoft() {
        return tracksoft;
    }

    public String getTracklink() {
        return tracklink;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getDescription() {
        return description;
    }

    public String getLikes() {
        return likes;
    }

    public String getStatus() {
        return status;
    }

    public String getPosition() {
        return position;
    }

    public String getJoindate() {
        return joindate;
    }
}
