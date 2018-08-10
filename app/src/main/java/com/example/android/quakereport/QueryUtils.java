package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String UTIL_TAG = QueryUtils.class.getSimpleName();

    private static final String USGS_QUAKE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Quake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Quake> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Quake> earthquakes = new ArrayList<>();

        URL quakeUrl = createUrl(USGS_QUAKE_URL);

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        String jsonResponse = null;
        try {

            jsonResponse = makeHttpRequest(quakeUrl);

            earthquakes = extractEarthquakesToJSON(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static List<Quake> extractEarthquakesToJSON(String jsonResponse) {

        JSONObject quakeObject = null;
        try {

            List<Quake> earthquakes = new ArrayList<Quake>();
            quakeObject = new JSONObject(jsonResponse);

            if (quakeObject != null) {
                JSONArray quakesArray = quakeObject.getJSONArray("features");

                for (int i = 0; i < quakesArray.length(); i++) {
                    JSONObject quakeObj = quakesArray.getJSONObject(i);
                    JSONObject quakeProperties = quakeObj.getJSONObject("properties");

                    long milliseconds = quakeProperties.getLong("time");


                    Quake newQuake = new Quake(quakeProperties.getDouble("mag"), quakeProperties.getString("place"), milliseconds, quakeProperties.getString("url"));
                    earthquakes.add(newQuake);
                }
            }

            return earthquakes;

        } catch (JSONException e) {
            Log.e(UTIL_TAG, "Error in parsing json response: " + e.getLocalizedMessage());
        }

        return null;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(UTIL_TAG, "Problem in retrieving earthquake results" + httpURLConnection.getResponseCode());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }

        return stringBuilder.toString();
    }

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(UTIL_TAG, "Error while creating url: " + e.getLocalizedMessage());
        }

        return url;
    }

}

