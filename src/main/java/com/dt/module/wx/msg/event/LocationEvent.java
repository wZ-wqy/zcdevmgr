package com.dt.module.wx.msg.event;

public class LocationEvent extends BaseEvent {

    private String Latitude;
    private String Longtitude;
    private String Precision;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(String longtitude) {
        Longtitude = longtitude;
    }

    public String getPrecision() {
        return Precision;
    }

    public void setPrecision(String precision) {
        Precision = precision;
    }


}
