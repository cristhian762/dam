package com.mattiuzzi.fontana.cristhian.galeriapublica.data;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Date;

public class ImageData {
    private final Uri uri;
    private final Bitmap thumb;
    private final String fileName;
    private final Date date;
    private final int size;

    public ImageData(@NonNull final Uri uri, @NonNull final Bitmap thumb,
                     @NonNull final String fileName, @NonNull final Date date,
                     final int size) {
        this.uri = uri;
        this.thumb = thumb;
        this.fileName = fileName;
        this.date = date;
        this.size = size;
    }

    public Uri getUri() {
        return uri;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public String getFileName() {
        return fileName;
    }

    public Date getDate() {
        return date;
    }

    public int getSize() {
        return size;
    }
}