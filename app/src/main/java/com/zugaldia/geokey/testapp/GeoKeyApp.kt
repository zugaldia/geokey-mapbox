package com.zugaldia.geokey.testapp

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

class GeoKeyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupMapbox()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupMapbox() {
        Timber.d("Initializing Mapbox.")
        Mapbox.getInstance(applicationContext, resources.getString(R.string.mapbox_token))
    }
}
