package me.loc2.loc2me.core.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import me.loc2.loc2me.core.Constants;
import me.loc2.loc2me.ui.md.CircleBitmapDisplayer;

public class ImageLoaderService {

    public ImageLoaderService(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
    }

    private DisplayImageOptions getAvatarOptions() {
        return getCommonOptions()
                .displayer(new CircleBitmapDisplayer())
                .build();
    }

    private DisplayImageOptions getImageOptions() {
        return getCommonOptions()
                .build();
    }

    private DisplayImageOptions.Builder getCommonOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.NONE_SAFE)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true);
    }

    public void loadAvatar(String avatarUrl, SimpleImageLoadingListener imageLoadingListener) {
        ImageLoader.getInstance().loadImage(
                getFullUrl(avatarUrl),
                getAvatarOptions(),
                imageLoadingListener
        );
    }

    public String getFullUrl(String url) {
        return Constants.Http.URL_BASE + url;
    }
}
