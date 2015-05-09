package me.loc2.loc2me.ui.md;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class OfferStub implements Parcelable {

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<OfferStub> CREATOR = new Parcelable.Creator<OfferStub>() {
        public OfferStub createFromParcel(Parcel in) {
            return new OfferStub(in);
        }

        public OfferStub[] newArray(int size) {
            return new OfferStub[size];
        }
    };

    private String imageUrl;
    private String descriptionShort;
    private String descriptionFull;
    private String bannerHtml;
    private int height;
    private Date added;

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private OfferStub(Parcel in) {
        imageUrl = in.readString();
        descriptionShort = in.readString();
        descriptionFull = in.readString();
        bannerHtml = in.readString();
        height = in.readInt();
        added = new Date(in.readLong());
    }

    public OfferStub() {
        added = new Date();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(descriptionShort);
        dest.writeString(descriptionFull);
        dest.writeString(bannerHtml);
        dest.writeInt(height);
        dest.writeLong(added.getTime());
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public void setDescriptionFull(String descriptionFull) {
        this.descriptionFull = descriptionFull;
    }

    public String getBannerHtml() {
        return bannerHtml;
    }

    public void setBannerHtml(String bannerHtml) {
        this.bannerHtml = bannerHtml;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThumbnailUrl() {
        return getImageUrl();
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
