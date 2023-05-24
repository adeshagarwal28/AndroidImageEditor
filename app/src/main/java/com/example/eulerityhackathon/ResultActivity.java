package com.example.eulerityhackathon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.example.eulerityhackathon.databinding.ActivityResultBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.image.setImageURI(getIntent().getData());
        Uri imageUri = getIntent().getData();
        ImageInfo.imageUri=imageUri;
        Uri uri = imageUri;
        File file = UriToFileConverter.convertUriToFile(this, uri);
        ImageInfo.editedImage=file;
        if (file != null) {

            Log.d("ResultActivity","File conversion successful");
            // Use the file as needed
            // e.g., upload the file or perform operations on it
        } else {
            // File conversion failed, handle the error
            Log.d("ResultActivity","File conversion failed");
        }

        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ResultActivity.this, DsPhotoEditorActivity.class);
                intent.setData(imageUri);
                startActivity(intent);
            }
        });


        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Eulerity", "Upload Button clicked on via ResultActivity");

                // Save the image file
                //File imageFile = new File("/storage/emulated/0/Android/data/com.example.eulerityhackathon/files/Pictures/shared_image.jpg");
                File imageFile = file;
                //Log.d("ResultActivity","File path : " + imageFile.toString());
                if (imageFile.exists()) {
                    // Image file exists
                    // Perform your desired actions here
                    Log.d("MainActivity", "Image file exists");
                } else {
                    // Image file does not exist
                    Log.d("MainActivity", "Image file does not exist");
                }
                String url = "https://eulerity-hackathon.appspot.com/upload";
                FetchUrlAsyncTask fetchUrlAsyncTask = new FetchUrlAsyncTask();
                fetchUrlAsyncTask.execute(url);
            }
        });





    }
}
