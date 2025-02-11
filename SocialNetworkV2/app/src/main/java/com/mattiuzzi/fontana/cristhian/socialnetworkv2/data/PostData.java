package com.mattiuzzi.fontana.cristhian.socialnetworkv2.data;

import android.graphics.Bitmap;

public class PostData {
    private final int id;
    private final String image;
    private final String authorImage;
    private final String date;
    private final String text;
    private final String authorName;
    private final String authorLogin;


    public PostData(int id, String image, String authorImage, String date, String text, String authorName, String authorLogin) {
        this.id = id;
        this.image = image;
        this.authorImage = authorImage;
        this.date = date;
        this.text = text;
        this.authorName = authorName;
        this.authorLogin = authorLogin;
    }

    public int getId() { return this.id; }
    public String getImage() { return this.image; }
    public String getAuthorImage() { return this.authorImage; }
    public String getDate() { return this.date; }
    public String getText() { return this.text; }
    public String getAuthorName() { return this.authorName; }
    public String getAuthorLogin() { return this.authorLogin; }
}
