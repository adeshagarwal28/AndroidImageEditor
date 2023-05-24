package com.example.eulerityhackathon;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;

public class UriToFileConverter {

    public static File convertUriToFile(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }

        String filePath;
        if (Build.VERSION.SDK_INT < 11) {
            // For devices below Android 11
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            // For devices running Android 11 and above
            filePath = getFilePathFromUri(context, uri);
        }

        if (filePath != null) {
            return new File(filePath);
        }

        return null;
    }

    private static String getFilePathFromUri(Context context, Uri uri) {
        String filePath = null;
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
