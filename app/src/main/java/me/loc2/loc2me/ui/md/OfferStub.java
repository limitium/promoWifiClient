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

    int avatar;
    String name;
    String descriptionShort;
    String descriptionFull;
    int sScreenWidth;
    int sProfileImageHeight;

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private OfferStub(Parcel in) {
        avatar = in.readInt();
        name = in.readString();
        descriptionShort = in.readString();
        descriptionFull = in.readString();
        sScreenWidth = in.readInt();
        sProfileImageHeight = in.readInt();
    }

    public OfferStub() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(avatar);
        dest.writeString(name);
        dest.writeString(descriptionShort);
        dest.writeString(descriptionFull);
        dest.writeInt(sScreenWidth);
        dest.writeInt(sProfileImageHeight);
    }

    private ShapeDrawable avatarShape;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setAvatarShape(ShapeDrawable avatarShape) {
        this.avatarShape = avatarShape;
    }

    public ShapeDrawable getAvatarShape() {
        return avatarShape;
    }

    public int getsScreenWidth() {
        return sScreenWidth;
    }

    public void setsScreenWidth(int sScreenWidth) {
        this.sScreenWidth = sScreenWidth;
    }

    public int getsProfileImageHeight() {
        return sProfileImageHeight;
    }

    public void setsProfileImageHeight(int sProfileImageHeight) {
        this.sProfileImageHeight = sProfileImageHeight;
    }
}
