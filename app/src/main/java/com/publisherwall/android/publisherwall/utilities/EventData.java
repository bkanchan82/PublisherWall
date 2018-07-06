package com.publisherwall.android.publisherwall.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import com.publisherwall.android.publisherwall.data.EventEntry;

public class EventData implements Parcelable{

    private String edi;
    private String title;
    private String url;
    private String date;
    private String location;
    private String description;
    private String posterUrl;
    private String isFavorite;
    private String favoriteCount;

    public EventData(String edi, String title, String url, String date, String location, String posterUrl, String isFavorite, String description, String favoriteCount) {
        this.edi = edi;
        this.title = title;
        this.url = url;
        this.date = date;
        this.location = location;
        this.posterUrl = posterUrl;
        this.isFavorite = isFavorite;
        this.description = description;
        this.favoriteCount = favoriteCount;
    }

    public EventData(EventEntry eventEntry) {
        this.edi = eventEntry.getEdi();
        this.title = eventEntry.getTitle();
        this.url = eventEntry.getUrl();
        this.date = eventEntry.getDate();
        this.location = eventEntry.getLocation();
        this.posterUrl = eventEntry.getPosterUrl();
        this.isFavorite = eventEntry.getIsFavorite();
        this.description = eventEntry.getDescription();
        this.favoriteCount = eventEntry.getTotalFavorite();
    }

    protected EventData(Parcel in) {
        edi = in.readString();
        title = in.readString();
        url = in.readString();
        date = in.readString();
        location = in.readString();
        description = in.readString();
        posterUrl = in.readString();
        isFavorite = in.readString();
        favoriteCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(edi);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(posterUrl);
        dest.writeString(isFavorite);
        dest.writeString(favoriteCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventData> CREATOR = new Creator<EventData>() {
        @Override
        public EventData createFromParcel(Parcel in) {
            return new EventData(in);
        }

        @Override
        public EventData[] newArray(int size) {
            return new EventData[size];
        }
    };

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getEdi() {
        return edi;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
