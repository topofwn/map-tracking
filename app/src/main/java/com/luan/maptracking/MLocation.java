package com.luan.maptracking;

public class MLocation {
    private double lat;
    private double longt;


    public MLocation(double la, double lo){
        this.lat = la;
        this.longt = lo;
    }

    public double getLat() {
        return lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }
}
