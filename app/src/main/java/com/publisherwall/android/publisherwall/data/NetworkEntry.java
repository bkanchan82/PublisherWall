package com.publisherwall.android.publisherwall.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v7.util.DiffUtil;

import java.util.Date;

@Entity(tableName = "network")
public class NetworkEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "network_sno")
    private String networkSno;
    @ColumnInfo(name = "name")
    private String networkName;
    @ColumnInfo(name = "image_url")
    private String networkImageUrl;
    @ColumnInfo(name = "join_url")
    private String networkJoinUrl;
    @ColumnInfo(name = "logo_url")
    private String networkLogoUrl;
    @ColumnInfo(name = "commission")
    private String networkCommission;
    @ColumnInfo(name = "min_pay")
    private String networkMinPay;
    @ColumnInfo(name = "pay_frequency")
    private String networkPayFrequency;
    @ColumnInfo(name = "offers")
    private String networkOffers;
    @ColumnInfo(name = "share_url")
    private String networkShareUrl;
    @ColumnInfo(name = "is_like")
    private String networkIsLike;
    @ColumnInfo(name = "facorite_count")
    private String networkFavoriteCount;

    @Ignore
    public NetworkEntry(String networkSno,
                        String networkName,
                        String networkImageUrl,
                        String networkJoinUrl,
                        String networkLogoUrl,
                        String networkCommission,
                        String networkMinPay,
                        String networkPayFrequency,
                        String networkOffers,
                        String networkShareUrl,
                        String networkIsLike,
                        String networkFavoriteCount) {
        this.networkSno = networkSno;
        this.networkName = networkName;
        this.networkImageUrl = networkImageUrl;
        this.networkJoinUrl = networkJoinUrl;
        this.networkLogoUrl = networkLogoUrl;
        this.networkCommission = networkCommission;
        this.networkMinPay = networkMinPay;
        this.networkPayFrequency = networkPayFrequency;
        this.networkOffers = networkOffers;
        this.networkShareUrl = networkShareUrl;
        this.networkIsLike = networkIsLike;
        this.networkFavoriteCount = networkFavoriteCount;
    }

    public NetworkEntry(int id,
                        String networkSno,
                        String networkName,
                        String networkImageUrl,
                        String networkJoinUrl,
                        String networkLogoUrl,
                        String networkCommission,
                        String networkMinPay,
                        String networkPayFrequency,
                        String networkOffers,
                        String networkShareUrl,
                        String networkIsLike,
                        String networkFavoriteCount) {
        this.id = id;
        this.networkSno = networkSno;
        this.networkName = networkName;
        this.networkImageUrl = networkImageUrl;
        this.networkJoinUrl = networkJoinUrl;
        this.networkLogoUrl = networkLogoUrl;
        this.networkCommission = networkCommission;
        this.networkMinPay = networkMinPay;
        this.networkPayFrequency = networkPayFrequency;
        this.networkOffers = networkOffers;
        this.networkShareUrl = networkShareUrl;
        this.networkIsLike = networkIsLike;
        this.networkFavoriteCount = networkFavoriteCount;
    }

    public static final DiffUtil.ItemCallback<NetworkEntry> DIFF_CALLBACK = new DiffUtil.ItemCallback<NetworkEntry>() {
        @Override
        public boolean areItemsTheSame(NetworkEntry oldItem, NetworkEntry newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(NetworkEntry oldItem, NetworkEntry newItem) {
            return oldItem.networkSno.equals(newItem.networkSno);
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNetworkSno() {
        return networkSno;
    }

    public void setNetworkSno(String networkSno) {
        this.networkSno = networkSno;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getNetworkImageUrl() {
        return networkImageUrl;
    }

    public void setNetworkImageUrl(String networkImageUrl) {
        this.networkImageUrl = networkImageUrl;
    }

    public String getNetworkJoinUrl() {
        return networkJoinUrl;
    }

    public void setNetworkJoinUrl(String networkJoinUrl) {
        this.networkJoinUrl = networkJoinUrl;
    }

    public String getNetworkLogoUrl() {
        return networkLogoUrl;
    }

    public void setNetworkLogoUrl(String networkLogoUrl) {
        this.networkLogoUrl = networkLogoUrl;
    }

    public String getNetworkCommission() {
        return networkCommission;
    }

    public void setNetworkCommission(String networkCommission) {
        this.networkCommission = networkCommission;
    }

    public String getNetworkMinPay() {
        return networkMinPay;
    }

    public void setNetworkMinPay(String networkMinPay) {
        this.networkMinPay = networkMinPay;
    }

    public String getNetworkPayFrequency() {
        return networkPayFrequency;
    }

    public void setNetworkPayFrequency(String networkPayFrequency) {
        this.networkPayFrequency = networkPayFrequency;
    }

    public String getNetworkOffers() {
        return networkOffers;
    }

    public void setNetworkOffers(String networkOffers) {
        this.networkOffers = networkOffers;
    }

    public String getNetworkShareUrl() {
        return networkShareUrl;
    }

    public void setNetworkShareUrl(String networkShareUrl) {
        this.networkShareUrl = networkShareUrl;
    }

    public String getNetworkIsLike() {
        return networkIsLike;
    }

    public void setNetworkIsLike(String networkIsLike) {
        this.networkIsLike = networkIsLike;
    }

    public String getNetworkFavoriteCount() {
        return networkFavoriteCount;
    }

    public void setNetworkFavoriteCount(String networkFavoriteCount) {
        this.networkFavoriteCount = networkFavoriteCount;
    }
}
