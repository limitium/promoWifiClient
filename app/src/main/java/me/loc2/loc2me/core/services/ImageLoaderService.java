package me.loc2.loc2me.core.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import me.loc2.loc2me.core.Constants;

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

    public void loadImage(String url, ImageView imageView, SimpleImageLoadingListener imageLoadingListener) {
        loadIntoView(url, imageView, getImageOptions(), imageLoadingListener);
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

    private void loadIntoView(String url, ImageView imageView, DisplayImageOptions imageOptions,
                              SimpleImageLoadingListener imageLoadingListener) {
        ImageLoader.getInstance().displayImage(getFullUrl(url), imageView, imageOptions, imageLoadingListener);
    }

    private String getFullUrl(String url) {
        return Constants.Http.URL_BASE + url;
    }

    private DisplayImageOptions getAvatarOptions(final boolean withBorder) {
        return getCommonOptions()
                .preProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {
                        bitmap = squarizeAndResize(bitmap);
                        bitmap = removeTransparency(bitmap);
                        bitmap = cropCircle(bitmap);
                        return bitmap;
                    }

                    private Bitmap removeTransparency(Bitmap bitmap) {
                        Bitmap imageWithBG = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());  // Create another image the same size
                        imageWithBG.eraseColor(Color.WHITE);  // set its background to white, or whatever color you want
                        Canvas canvas = new Canvas(imageWithBG);  // create a canvas to draw on the new image
                        canvas.drawBitmap(bitmap, 0f, 0f, null); // draw old image on the background
                        bitmap.recycle();
                        return imageWithBG;
                    }

                    private Bitmap cropCircle(Bitmap bitmap) {
                        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);

                        Canvas canvas = new Canvas(output);

                        final int color = 0xff424242;
                        final Paint paint = new Paint();
                        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

                        paint.setAntiAlias(true);
                        canvas.drawARGB(0, 0, 0, 0);
                        paint.setColor(color);

                        //--CROP THE IMAGE
                        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2 - 1, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawBitmap(bitmap, rect, rect, paint);

                        //--ADD BORDER IF NEEDED
                        if (withBorder) {
                            int borderWidth = bitmap.getWidth() / 54;
                            final Paint paint2 = new Paint();
                            paint2.setAntiAlias(true);
                            paint2.setColor(Color.WHITE);
                            paint2.setStrokeWidth(borderWidth);
                            paint2.setStyle(Paint.Style.STROKE);
                            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, (float) (bitmap.getWidth() / 2 - Math.ceil(borderWidth / 2)), paint2);
                        }
                        return output;
                    }

                    private Bitmap squarizeAndResize(Bitmap bitmap) {
                        final int IMAGE_SIZE = 256;
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, IMAGE_SIZE, IMAGE_SIZE);
                        return bitmap;
                    }
                })
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
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true);
    }

}
