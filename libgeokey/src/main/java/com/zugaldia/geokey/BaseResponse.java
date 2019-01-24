package com.zugaldia.geokey;

import com.google.gson.annotations.SerializedName;

/*
 * Sample response:
 */
public class BaseResponse {

    @SerializedName("Status")
    private String status;

    @SerializedName("Failure Mode")
    private String failure;

    public BaseResponse() {
    }

    public boolean isFailure() {
        return status != null || failure != null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }
}
