package com.example.koenigmap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class Archiitect {
    private String title;
    private String desc;
    private double lon,lat;
    private int image;
    private String site1;

    public Archiitect(String title, String desc, double lon, double lat, int image) {
        this.title = title;
        this.desc = desc;
        this.lon = lon;
        this.lat = lat;
        this.image = image;
        this.site1 = site1;
    }

    public String getSite1() {
        return site1;
    }

    public void setSite1(String site1) {
        this.site1 = site1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
