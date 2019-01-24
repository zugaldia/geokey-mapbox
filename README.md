![](https://codebuild.us-east-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiV2JWL1NFNVRzR2tDYTVFbUFXSjdXbWJFRncvWCs3cDN0RDFRVFdKME1STjU1VkVHdTZpUVRrZE1rZmZYZmlyOGZEdWU0VWR6MTJGYzA4MnZDV1JHUnZZPSIsIml2UGFyYW1ldGVyU3BlYyI6IkxOSkRIOSs3Vm1hMWdPUGUiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)

# GeoKey + Mapbox integration

An Android app and library to visualize [GeoKeys](https://geokey.xyz) on a Mapbox map. From their website:

> A GeoKey is a 16 letter, four-word key that represents every square meter of the earth’s surface. 

![](/assets/device-2019-01-23-223206.gif)

_Tap on the map to get the GeoKey for a location_

![](/assets/device-2019-01-23-223403.gif)

_Look up GeoKeys on the map_

## About

This repo contains an Android project with two modules:

- `app`: A (Kotlin) Android app that visualizes GeoKeys on a map (see screenshots below).

- `libgeokey`: A self-contained (Java) library that you are free to use in your own projects.
  Because it doesn't have any Android dependencies, you can use it for backend services too.

## Usage

You start by initializing the `` client with an access token:

```kotlin
val geoKeyClient = GeoKeyClient("YOUR_TOKEN_GOES_HERE")
```

To obtain a GeoKey from a location you'd do something like the following:

```kotlin
geoKeyClient.fromPosition(latitude, longitude).enqueue(object : Callback<GeoKeyResponse> {
    override fun onResponse(call: Call<GeoKeyResponse>, response: Response<GeoKeyResponse>) {
        val geoKeyResponse = response.body()
        if (geoKeyResponse == null || geoKeyResponse.isFailure) {
            // GeoKey request failed, check `geoKeyResponse?.failure` for additional information
        } else {
            // GeoKey available in `geoKeyResponse.geoKey`
        }
    }

    override fun onFailure(call: Call<GeoKeyResponse>, t: Throwable) {
        // GeoKey request failed, check `t.message` for additional information
    }
})
```

Conversely, to obtain a location from a GeoKey:

```kotlin
geoKeyClient.fromGeoKey(geoKey).enqueue(object : Callback<LocationResponse> {
    override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
        val locationResponse = response.body()
        if (locationResponse == null || locationResponse.isFailure) {
            // GeoKey request failed, check `geoKeyResponse?.failure` for additional information
        } else {
            // Location available in `locationResponse.latitude` and `locationResponse.longitude`
        }
    }

    override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
        // GeoKey request failed, check `t.message` for additional information
    }
})
```

## License

See [LICENSE.md](/LICENSE.md).

## Contributing

If you find any issues or want to suggest any contributions, please [cut a ticket](https://github.com/zugaldia/geokey-mapbox/issues) and tag `@zugaldia`.
