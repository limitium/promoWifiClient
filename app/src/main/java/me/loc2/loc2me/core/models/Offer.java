package me.loc2.loc2me.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.math.BigInteger;

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
    private String wifi_name;
    private String description;
    private String created_at;
    private String updated_at;
    private long addedAt;
    private WifiInfo wifiInfo;
    private OfferImage _image;

    private Offer(Parcel in) {
        id = new BigInteger(in.readString());
        wifi_name = in.readString();
        description = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        addedAt = in.readLong();
        _image = in.readParcelable(OfferImage.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeString(wifi_name);
        dest.writeString(description);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeLong(addedAt);
        dest.writeParcelable(_image, flags);
    }


    public Offer() {

    }


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getWifi_name() {
        return wifi_name;
    }

    public void setWifi_name(String wifi_name) {
        this.wifi_name = wifi_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public OfferImage get_image() {
        return _image;
    }

    public void set_image(OfferImage _image) {
        this._image = _image;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("wifi_name", wifi_name)
                .add("created_at", created_at)
                .add("description", description)
                .add("updated_at", updated_at)
                .add("addedAt", addedAt)
                .add("image", _image)
                .toString();
    }

}
