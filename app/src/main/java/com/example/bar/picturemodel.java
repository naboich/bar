package com.example.bar;

import android.net.Uri;

public class picturemodel {

    String imagename;
    Uri image_review;

    public picturemodel() {
    }

    public picturemodel(String imagename, Uri image_review) {
        this.imagename = imagename;
        this.image_review = image_review;
    }

    public Uri getImage_review() {
        return image_review;
    }

    public void setImage_review(Uri image_review) {
        this.image_review = image_review;
    }

    public picturemodel(String imagename) {
        this.imagename = imagename;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}
