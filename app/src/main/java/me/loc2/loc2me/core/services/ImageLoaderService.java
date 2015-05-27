package me.loc2.loc2me.core.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

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


    public void loadAvatar(String url, SimpleImageLoadingListener imageLoadingListener) {
        load(url, imageLoadingListener, getAvatarOptions(false));
    }

    public void loadAvatar(String url, ImageView imageView) {
        loadIntoView(url, imageView, getAvatarOptions(true));
    }

    public void loadImage(String url, SimpleImageLoadingListener imageLoadingListener) {
        load(url, imageLoadingListener, getImageOptions());
    }


    public void loadImage(String url, ImageView imageView) {
        loadIntoView(url, imageView, getImageOptions());
    }

    private void load(String url, SimpleImageLoadingListener imageLoadingListener, DisplayImageOptions imageOptions) {
        ImageLoader.getInstance().loadImage(
                getFullUrl(url),
                imageOptions,
                imageLoadingListener
        );
    }

    private void loadIntoView(String url, ImageView imageView, DisplayImageOptions imageOptions) {
        ImageLoader.getInstance().displayImage(getFullUrl(url), imageView, imageOptions);
    }

    private String getFullUrl(String url) {
        return Constants.Http.URL_BASE + url;
    }

    private DisplayImageOptions getAvatarOptions(boolean withBorder) {
        CircleBitmapDisplayer circleBitmapDisplayer = withBorder ? new CircleBitmapDisplayer(Color.WHITE, 3) : new CircleBitmapDisplayer();
        return getCommonOptions()
                .displayer(circleBitmapDisplayer)
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

}
