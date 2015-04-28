package me.loc2.loc2me.ui.md;

import android.graphics.drawable.ShapeDrawable;
import android.os.Parcel;
import android.os.Parcelable;

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

    private int logo;
    private String descriptionShort;
    private String descriptionFull;
    private String bannerHtml;

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private OfferStub(Parcel in) {
        logo = in.readInt();
        descriptionShort = in.readString();
        descriptionFull = in.readString();
        bannerHtml = in.readString();
    }

    public OfferStub() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(logo);
        dest.writeString(descriptionShort);
        dest.writeString(descriptionFull);
        dest.writeString(bannerHtml);
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
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
}
