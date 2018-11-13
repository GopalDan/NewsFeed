package com.example.gopal.newsfeed;

/**
 * Created by Gopal on 11/1/2018.
 */

import android.text.TextUtils;
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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class with methods to perform the HTTP request and
 * parse the response.
 */

public  class Helper {

    /** Tag for the log messages */
    public static final String LOG_TAG = Helper.class.getSimpleName();

    /**
     * Query the Guardian API and return an {@link Event} object to represent a single news.
     */
    public static List<Event> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract relevant fields from the JSON response and create a list of Newss
        List<Event> newsList = extractFeatureFromJson(jsonResponse);
        // Return the list of news
        return newsList;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000 );
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Event> extractFeatureFromJson(String earthquakeJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to the list
        List<Event> newsList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(earthquakeJSON);
            JSONObject response = root.getJSONObject("response");
            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i < earthquakeJSON.length(); i++) {
                JSONObject arrayElement = resultArray.getJSONObject(i);
                String webTitle = arrayElement.getString("webTitle");
                String publishingDate = arrayElement.getString("webPublicationDate");
                String webUrl = arrayElement.getString("webUrl");
                JSONObject fieldObject = arrayElement.getJSONObject("fields");
                String thumbnail = fieldObject.getString("thumbnail");
                String author = fieldObject.getString("byline");
                newsList.add(new Event(webTitle,thumbnail,webUrl,author, publishingDate));
            }
        }
        catch (JSONException e){
            Log.i("Helper","Json parsing error");
        }
        return newsList;

    }
}
