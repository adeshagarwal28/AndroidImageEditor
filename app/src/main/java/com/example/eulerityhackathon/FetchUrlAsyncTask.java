package com.example.eulerityhackathon;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchUrlAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = FetchUrlAsyncTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... urls) {
        String response = null;

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set timeouts (optional)
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Get the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                response = stringBuilder.toString();
            } else {
                Log.e(TAG, "HTTP error code: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "Error fetching data: " + e.getMessage());
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            try {
                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(response);
                String imageUrl = jsonObject.optString("url");

                // Perform your desired actions with the "url" attribute
                Log.d(TAG, "Fetched image URL: " + imageUrl);


                //String url = "https://example.com/upload";
                MultipartPostAsyncTask multipartPostAsyncTask = new MultipartPostAsyncTask();
                multipartPostAsyncTask.execute(imageUrl);

                // Start the process to upload or display the image
                // ...
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Empty response");
        }
    }
}

