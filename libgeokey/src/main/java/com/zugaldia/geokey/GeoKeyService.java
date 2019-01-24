package com.zugaldia.geokey;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GeoKeyService {

    @GET("/from-position")
    Call<GeoKeyResponse> fromPosition(
            @Header("Authorization") String authorization,
            @Query("lat") double lat,
            @Query("lon") double lon);

    @GET("/from-geokey")
    Call<LocationResponse> fromGeoKey(
            @Header("Authorization") String authorization,
            @Query("geokey") String geoKey);

}
