package com.zugaldia.geokey;

import com.google.gson.annotations.SerializedName;

/*
 * Sample response:
 * {"Latitude":38.90962,"GeoKey":"TEPA FRID MOXA FULK","Longitude":-77.04341}
 */
public class GeoKeyResponse extends BaseResponse {

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("GeoKey")
    private String geoKey;

    public GeoKeyResponse() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGeoKey() {
        return geoKey;
    }

    public void setGeoKey(String geoKey) {
        this.geoKey = geoKey;
    }
}
