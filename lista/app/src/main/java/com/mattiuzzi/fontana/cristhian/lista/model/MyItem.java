package com.mattiuzzi.fontana.cristhian.lista.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MyItem implements Parcelable {
    public Uri photo;
    public String title;
    public String description;

    // Construtor
    public MyItem(Uri photo, String title, String description) {
        this.photo = photo;
        this.title = title;
        this.description = description;
    }

    // Implementação do Parcelable
    protected MyItem(Parcel in) {
        photo = in.readParcelable(Uri.class.getClassLoader());
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<MyItem> CREATOR = new Creator<MyItem>() {
        @Override
        public MyItem createFromParcel(Parcel in) {
            return new MyItem(in);
        }

        @Override
        public MyItem[] newArray(int size) {
            return new MyItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(photo, flags);
        dest.writeString(title);
        dest.writeString(description);
    }
}
