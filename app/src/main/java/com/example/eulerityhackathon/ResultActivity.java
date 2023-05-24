package com.example.eulerityhackathon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.example.eulerityhackathon.databinding.ActivityResultBinding;

import java.io.File;

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
                Toast.makeText(ResultActivity.this, "Edited Image will be uploaded", Toast.LENGTH_SHORT).show();
            }
        });







    }
}
