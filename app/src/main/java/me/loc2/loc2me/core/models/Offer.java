package me.loc2.loc2me.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ocpsoft.pretty.time.PrettyTime;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.loc2.loc2me.core.receivers.BootReceiver;
import me.loc2.loc2me.util.Ln;

@JsonIgnoreProperties(ignoreUnknown = true)
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


    private Integer id;
    private String name;
    private String image;
    private Boolean is_disposable=false;
    private Boolean is_used=false;
    private String description;
    private String created_at;
    private String updated_at;

    private String wifi_name;
    private String organization_name;
    private String avatar;

    private Long added_at;
    private int backgroundColor;
    private int textColor;

    private Offer(Parcel in) {
        id = new Integer(in.readString());
        name = in.readString();
        image = in.readString();
        is_disposable = in.readByte() != 0;
        is_used = in.readByte() != 0;
        description = in.readString();
        created_at = in.readString();
        updated_at = in.readString();

        wifi_name = in.readString();
        organization_name = in.readString();
        avatar = in.readString();

        added_at = in.readLong();
        backgroundColor = in.readInt();
        textColor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeString(name);
        dest.writeString(image);
        dest.writeByte((byte) (is_disposable ? 1 : 0));
        dest.writeByte((byte) (is_used ? 1 : 0));
        dest.writeString(description);
        dest.writeString(created_at);
        dest.writeString(updated_at);

        dest.writeString(wifi_name);
        dest.writeString(organization_name);
        dest.writeString(avatar);

        dest.writeLong(added_at);
        dest.writeInt(backgroundColor);
        dest.writeInt(textColor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("image", image)
                .add("is_disposable", is_disposable)
                .add("is_used", is_used)
                .add("description", description)
                .add("created_at", created_at)
                .add("updated_at", updated_at)

                .add("wifi_name", wifi_name)
                .add("organization_name", organization_name)
                .add("avatar", avatar)

                .add("added_at", added_at)
                .add("backgroundColor", backgroundColor)
                .add("textColor", textColor)
                .toString();
    }

    public Offer() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getAdded_at() {
        return added_at;
    }

    public void setAdded_at(Long added_at) {
        this.added_at = added_at;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public Boolean getIs_disposable() {
        return is_disposable;
    }

    public void setIs_disposable(Boolean is_disposable) {
        this.is_disposable = is_disposable;
    }

    public Boolean getIs_used() {
        return is_used;
    }

    public void setIs_used(Boolean is_used) {
        this.is_used = is_used;
    }

    public String getCreatedAsPrettyText() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        Date createdAt = null;
        try {
            createdAt = df.parse(getCreated_at());
            PrettyTime prettyTime = new PrettyTime(new Date());
            return prettyTime.format(createdAt);
        } catch (ParseException e) {
            Ln.e(e.getMessage());
            return "";
        }
    }
}
