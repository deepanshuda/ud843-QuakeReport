package com.example.android.quakereport;

public class Quake {

    private Double magnitude;
    private String place;
    private long date;

    public Quake(Double magnitude, String place, long date) {
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
