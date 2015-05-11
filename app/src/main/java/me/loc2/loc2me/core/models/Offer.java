package me.loc2.loc2me.core.models;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class Offer implements Parcelable, Serializable {

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };


    private BigInteger id;
    private String name;
    private String message;
    private String type;
    private String category;
    private String img;
    private long addedAt;
    private WifiInfo wifiInfo;
    private int height;

    private Offer(Parcel in) {
        id = new BigInteger(in.readString());
        name = in.readString();
        message = in.readString();
        type = in.readString();
        category = in.readString();
        img = in.readString();
        addedAt = in.readLong();
        height = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeString(name);
        dest.writeString(message);
        dest.writeString(type);
        dest.writeString(category);
        dest.writeString(img);
        dest.writeLong(addedAt);
        dest.writeInt(height);
    }


    public Offer() {

    }


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }

    public WifiInfo getWifiInfo() {
        return wifiInfo;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
}
