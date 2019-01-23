package com.zugaldia.geokey;

import com.google.gson.annotations.SerializedName;

/*
 * Sample response:
 * {"Latitude":38.90962,"Reversed Address":"Connecticut Ave NW, Washington, DC 20036, USA","Longitude":-77.04341}
 */
public class LocationResponse extends BaseResponse {

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Reversed Address")
    private String address;

    public LocationResponse() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
