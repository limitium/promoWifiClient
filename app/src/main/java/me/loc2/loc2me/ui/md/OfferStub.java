package me.loc2.loc2me.ui.md;

import android.graphics.drawable.ShapeDrawable;

public class OfferStub {

    private static final int CIRCLE_RADIUS_DP = 50;

    int avatar;
    String name;
    String descriptionShort;
    String descriptionFull;
    int sScreenWidth;
    int sProfileImageHeight;

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

    protected int getCircleRadiusDp() {
        return CIRCLE_RADIUS_DP;
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
