package me.loc2.loc2me.core.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferImage implements Parcelable {

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<OfferImage> CREATOR = new Parcelable.Creator<OfferImage>() {
        public OfferImage createFromParcel(Parcel in) {
            return new OfferImage(in);
        }

        public OfferImage[] newArray(int size) {
            return new OfferImage[size];
        }
    };


    private String baseUrl;
    private int height;
    private int width;

    public OfferImage() {

    }

    public OfferImage(String baseUrl, int height, int width) {
        this.baseUrl = baseUrl;
        this.height = height;
        this.width = width;
    }

    public OfferImage(Parcel in) {
        baseUrl = in.readString();
        height = in.readInt();
        width = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(baseUrl);
        dest.writeInt(height);
        dest.writeInt(width);
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
