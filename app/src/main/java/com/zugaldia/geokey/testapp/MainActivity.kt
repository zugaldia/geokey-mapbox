package com.zugaldia.geokey.testapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.zugaldia.geokey.GeoKeyClient
import com.zugaldia.geokey.GeoKeyResponse
import com.zugaldia.geokey.LocationResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class MainActivity : AppCompatActivity(),
    View.OnClickListener,
    MapboxMap.OnMapClickListener {

    lateinit var geoKeyClient: GeoKeyClient
    lateinit var mapboxMap: MapboxMap

    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupMapView(savedInstanceState)
        buttonGo.setOnClickListener(this)
        geoKeyClient = GeoKeyClient(resources.getString(R.string.geokey_token))
    }

    private fun setupMapView(savedInstanceState: Bundle?) {
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { it ->
            mapboxMap = it
            it.setStyle(Style.MAPBOX_STREETS)
            it.addOnMapClickListener(this)
            Timber.d("Map is ready.")
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            mapView.onSaveInstanceState(it)
        }
    }

    override fun onClick(v: View?) {
        showMessage("Obtaining location...")
        geoKeyClient.fromGeoKey(textGeoKey.text.toString()).enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
                val locationResponse = response.body()
                if (locationResponse == null || locationResponse.isFailure) {
                    val error = locationResponse?.failure ?: "Empty response."
                    showMessage("GeoKey request failed: $error")
                } else {
                    showMessage("GeoKey request succeeded.")
                    setMarker(locationResponse.latitude, locationResponse.longitude, textGeoKey.text.toString())
                    mapboxMap.animateCamera(
                        CameraUpdateFactory
                            .newCameraPosition(
                                CameraPosition.Builder()
                                    .target(LatLng(locationResponse.latitude, locationResponse.longitude))
                                    .zoom(15.0)
                                    .build()
                            ), 1_000
                    )
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                showMessage("GeoKey request failed: ${t.message}")
            }
        })
    }

    override fun onMapClick(point: LatLng): Boolean {
        showMessage("Obtaining GeoKey...")
        geoKeyClient.fromPosition(point.latitude, point.longitude).enqueue(object : Callback<GeoKeyResponse> {
            override fun onResponse(call: Call<GeoKeyResponse>, response: Response<GeoKeyResponse>) {
                val geoKeyResponse = response.body()
                if (geoKeyResponse == null || geoKeyResponse.isFailure) {
                    val error = geoKeyResponse?.failure ?: "Empty response."
                    showMessage("GeoKey request failed: $error")
                } else {
                    showMessage("GeoKey request succeeded.")
                    setMarker(point.latitude, point.longitude, geoKeyResponse.geoKey)
                    textGeoKey.setText(geoKeyResponse.geoKey)
                }
            }

            override fun onFailure(call: Call<GeoKeyResponse>, t: Throwable) {
                showMessage("GeoKey request failed: ${t.message}")
            }
        })

        return true
    }

    private fun showMessage(message: String) {
        Timber.d(message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setMarker(latitude: Double, longitude: Double, geoKey: String) {
        currentMarker?.let { mapboxMap.removeMarker(it) }
        currentMarker = mapboxMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title("You are here:\n$geoKey")
        )
    }
}
