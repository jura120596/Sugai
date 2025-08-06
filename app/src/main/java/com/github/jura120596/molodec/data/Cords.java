package com.github.jura120596.molodec.data;

public class Cords {

    private double lat;
    private double lng;

    public Cords(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public Cords(){

    }

    @Override
    public String toString() {
        return "Cords " +
                "lat=" + lat +
                ", lng=" + lng ;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
