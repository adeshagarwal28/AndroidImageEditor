package com.example.eulerityhackathon;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> imageUrls;
    private Context context;

    public ImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton editButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imageUrl = imageUrls.get(position);
        ImageInfo.originalUrl=imageUrl;
        Picasso.get().load(imageUrl).into(holder.imageView);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DownloadImageTask().execute(imageUrl);
                Log.d("Eulerity", "Edit Button clicked on via Image Adapter");
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }


    class DownloadImageTask extends AsyncTask<String, Void, Uri> {
        String imageUrl;
        @Override
        protected Uri doInBackground(String... urls) {
            imageUrl = urls[0];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                // Create a temporary file to save the image
                File imageFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.jpg");
                FileOutputStream outputStream = new FileOutputStream(imageFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                input.close();

                // Get the image file's content URI
                return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File("/storage/emulated/0/Android/data/com.example.eulerityhackathon/files/Pictures/shared_image.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Uri imageUri) {
            if (imageUri != null) {
                shareImage(imageUri, imageUrl);
            }
        }
    }

    private void shareImage(Uri imageUri, String imageUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");

        if (context instanceof Activity) {

            Intent dsPhotoEditorIntent = new Intent(context, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(imageUri);
            ((Activity) context).startActivityForResult(dsPhotoEditorIntent, 200);

        }

    }


}

