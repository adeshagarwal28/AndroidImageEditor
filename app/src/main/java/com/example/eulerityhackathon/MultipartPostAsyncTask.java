package com.example.eulerityhackathon;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MultipartPostAsyncTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = MultipartPostAsyncTask.class.getSimpleName();

    @Override
    protected Boolean doInBackground(String... params) {
        String url = params[0];

        String response = null;

        try {
            File file = ImageInfo.editedImage;
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("image/*"), file))
                    .addFormDataPart("appid", "adesh.agarwal@outlook.com")
                    .addFormDataPart("original", ImageInfo.originalUrl)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(final Call call, final IOException e) {
                    Log.d(TAG,"1.UPLOAD FAILED : "+e);
                }

                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        // Handle the error
                        Log.d(TAG,"UPLOAD FAILED");
                    }
                    // Upload successful
                    Log.d(TAG,"UPLOAD SUCCESS");
                }
            });

            return true;
        } catch (Exception ex) {
            // Handle the error
            Log.d(TAG,"2. UPLOAD FAILED : "+ex);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        if (response != null) {
            // Process the response
            Log.d(TAG, "Response: " + response);
        } else {
            Log.e(TAG, "Empty response");
        }
    }
}
