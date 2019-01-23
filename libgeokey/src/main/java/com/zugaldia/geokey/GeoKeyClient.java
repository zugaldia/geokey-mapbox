package com.zugaldia.geokey;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoKeyClient {

    private static final String BASE_URL_GEOKEY = "https://planetxy.com/";

    private final String token;
    private String baseUrl;
    private GeoKeyService service = null;

    public GeoKeyClient(String token) {
        this.token = token;
        this.baseUrl = BASE_URL_GEOKEY;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Call<GeoKeyResponse> fromPosition(double latitude, double longitude) {
        return getService().fromPosition(
                String.format("Token %s", token),
                latitude, longitude
        );
    }

    public Call<LocationResponse> fromGeoKey(String geoKey) {
        return getService().fromGeoKey(
                String.format("Token %s", token),
                geoKey
        );
    }

    private GeoKeyService getService() {
        if (service != null) return service;

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()));

        Retrofit retrofit = retrofitBuilder.build();
        service = retrofit.create(GeoKeyService.class);
        return service;
    }
}
