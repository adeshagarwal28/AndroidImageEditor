package com.example.eulerityhackathon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eulerityhackathon.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.image.setImageURI(getIntent().getData());
    }
}
