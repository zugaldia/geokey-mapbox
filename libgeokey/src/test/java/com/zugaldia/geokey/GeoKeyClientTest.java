package com.zugaldia.geokey;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GeoKeyClientTest {

    private final static double DELTA = 0.1;

    private MockWebServer server;
    private HttpUrl mockUrl;

    private String loadJsonFixture(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        Scanner scanner = new Scanner(inputStream, UTF_8.name()).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String response = null;
                if (request.getPath().contains("XYZ")) {
                    response = loadJsonFixture("error.json");
                } else if (request.getPath().contains("from-position")) {
                    response = loadJsonFixture("from_position.json");
                } else if (request.getPath().contains("from-geokey")) {
                    response = loadJsonFixture("from_geokey.json");
                }
                assert response != null;
                return new MockResponse().setBody(response);
            }
        });

        server.start();
        mockUrl = server.url("");
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testSanity() {
        GeoKeyClient client = new GeoKeyClient("token");
        client.setBaseUrl(mockUrl.toString());
        Assert.assertNotNull(client);
    }

    @Test
    public void testFromPosition() throws IOException {
        GeoKeyClient client = new GeoKeyClient("token");
        client.setBaseUrl(mockUrl.toString());
        GeoKeyResponse response = client.fromPosition(38.90962, -77.04341)
                .execute().body();

        assert response != null;
        Assert.assertFalse(response.isFailure());
        Assert.assertEquals(38.90962, response.getLatitude(), DELTA);
        Assert.assertEquals(-77.04341, response.getLongitude(), DELTA);
        Assert.assertEquals("TEPA FRID MOXA FULK", response.getGeoKey());
    }

    @Test
    public void testFromGeoKey() throws IOException {
        GeoKeyClient client = new GeoKeyClient("token");
        client.setBaseUrl(mockUrl.toString());
        LocationResponse response = client.fromGeoKey("TEPA FRID MOXA FULK")
                .execute().body();

        assert response != null;
        Assert.assertFalse(response.isFailure());
        Assert.assertEquals(38.90962, response.getLatitude(), DELTA);
        Assert.assertEquals(-77.04341, response.getLongitude(), DELTA);
        Assert.assertEquals("Connecticut Ave NW, Washington, DC 20036, USA", response.getAddress());
    }

    @Test
    public void testError() throws IOException {
        GeoKeyClient client = new GeoKeyClient("token");
        client.setBaseUrl(mockUrl.toString());
        LocationResponse response = client.fromGeoKey("XYZ")
                .execute().body();

        assert response != null;
        Assert.assertTrue(response.isFailure());
        Assert.assertEquals("Failure", response.getStatus());
        Assert.assertEquals("Invalid or unrecognizable geokey.", response.getFailure());
    }
}
