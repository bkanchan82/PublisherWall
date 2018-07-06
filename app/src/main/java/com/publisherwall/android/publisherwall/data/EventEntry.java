package com.publisherwall.android.publisherwall.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "event")
public class EventEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "edi")
    private String edi;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "url")
    private String url;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "poster_url")
    private String posterUrl;
    @ColumnInfo(name = "is_favorite")
    private String isFavorite;
    @ColumnInfo(name = "total_favorite")
    private String totalFavorite;

    public EventEntry(int id,
                      String edi,
                      String title,
                      String url,
                      String date,
                      String location,
                      String description,
                      String posterUrl,
                      String isFavorite,
                      String totalFavorite) {
        this.id = id;
        this.edi = edi;
        this.title = title;
        this.url = url;
        this.date = date;
        this.location = location;
        this.description = description;
        this.posterUrl = posterUrl;
        this.isFavorite = isFavorite;
        this.totalFavorite = totalFavorite;
    }


    @Ignore
    public EventEntry(String edi,
                      String title,
                      String url,
                      String date,
                      String location,
                      String description,
                      String posterUrl,
                      String isFavorite,
                      String totalFavorite) {
        this.edi = edi;
        this.title = title;
        this.url = url;
        this.date = date;
        this.location = location;
        this.description = description;
        this.posterUrl = posterUrl;
        this.isFavorite = isFavorite;
        this.totalFavorite = totalFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEdi() {
        return edi;
    }

    public void setEdi(String edi) {
        this.edi = edi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getTotalFavorite() {
        return totalFavorite;
    }

    public void setTotalFavorite(String totalFavorite) {
        this.totalFavorite = totalFavorite;
    }
}
