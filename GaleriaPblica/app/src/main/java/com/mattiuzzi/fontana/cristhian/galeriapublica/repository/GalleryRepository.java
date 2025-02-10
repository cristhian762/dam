package com.mattiuzzi.fontana.cristhian.galeriapublica.repository;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.mattiuzzi.fontana.cristhian.galeriapublica.R;
import com.mattiuzzi.fontana.cristhian.galeriapublica.data.ImageData;
import com.mattiuzzi.fontana.cristhian.galeriapublica.utils.Utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalleryRepository {

    private final Context context;

    public GalleryRepository(@NonNull Context context) {
        this.context = context;
    }

    public List<ImageData> loadImageData(int limit, int offset) throws FileNotFoundException {
        List<ImageData> imageDataList = new ArrayList<>();
        int width = (int) context.getResources().getDimension(R.dimen.im_width);
        int height = (int) context.getResources().getDimension(R.dimen.im_height);

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE
        };

        try (Cursor cursor = queryImages(projection, limit, offset)) {
            if (cursor == null) {
                return imageDataList;
            }

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                String name = cursor.getString(nameColumn);
                int dateAdded = cursor.getInt(dateAddedColumn);
                int size = cursor.getInt(sizeColumn);
                Bitmap thumb = Utils.getBitmap(context, contentUri, width, height);

                imageDataList.add(new ImageData(contentUri, thumb, name, new Date(dateAdded * 1000L), size));
            }
        }
        return imageDataList;
    }

    private Cursor queryImages(String[] projection, int limit, int offset) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            Bundle queryArgs = new Bundle();
            queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, limit);
            queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, offset);
            queryArgs.putString(ContentResolver.QUERY_ARG_SORT_COLUMNS, MediaStore.Images.Media.DATE_ADDED);
            queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_ASCENDING);

            return context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    queryArgs,
                    null
            );
        } else {
            String sortOrder = MediaStore.Images.Media.DATE_ADDED + " ASC LIMIT " + limit + " OFFSET " + offset;
            return context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder
            );
        }
    }
}
