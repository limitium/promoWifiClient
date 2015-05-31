package me.loc2.loc2me.core.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import me.loc2.loc2me.R;
import me.loc2.loc2me.core.Constants;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.util.ColorUtil;

public class ImageLoaderService {

    private final Context context;

    public ImageLoaderService(Context context) {
        this.context = context;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
    }


    public void loadAvatar(String url, SimpleImageLoadingListener imageLoadingListener) {
        load(url, imageLoadingListener, getAvatarOptions(false));
    }

    public void loadAvatar(String url, ImageView imageView) {
        loadIntoView(url, imageView, getAvatarOptions(true));
    }

    public void loadImage(Offer offer, SimpleImageLoadingListener imageLoadingListener) {
        load(offer.getImage(), imageLoadingListener, getImageOptions(offer));
    }

    public void loadImage(Offer offer, ImageView imageView, SimpleImageLoadingListener imageLoadingListener) {
        loadIntoView(offer.getImage(), imageView, getImageOptions(offer), imageLoadingListener);
    }

    public void loadImage(Offer offer, ImageView imageView) {
        loadIntoView(offer.getImage(), imageView, getImageOptions(offer));
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

    private DisplayImageOptions getImageOptions(final Offer offer) {
        return getCommonOptions()
                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap original) {
                        if (!offer.getIs_used()) {
                            return original;
                        }
                        Bitmap bitmap = Bitmap.createBitmap(
                                original.getWidth(), original.getHeight(),
                                Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);

                        canvas.drawBitmap(original, 0, 0, null);

                        Paint maskPaint = new Paint();
                        Bitmap mask = CreateText(original.getWidth(), original.getHeight());
                        canvas.drawBitmap(mask, 0, 0, maskPaint);

                        return bitmap;
                    }

                    private Bitmap CreateText(int width, int height) {
                        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);

                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(Color.HSVToColor(200, ColorUtil.getHSV(ColorUtil.darker(offer.getBackgroundColor(), 0.7f))));
                        int top = height / 6;
                        canvas.drawRect(0, top, width, 2 * top, paint);

                        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                                | Paint.LINEAR_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
                        textPaint.setStyle(Paint.Style.FILL);
                        textPaint.setColor(offer.getTextColor());

                        //text suitble text size
                        final float testTextSize = 48f;
                        // Get the bounds of the text, using our testTextSize.
                        textPaint.setTextSize(testTextSize);
                        Rect bounds = new Rect();
                        String text = context.getString(R.string.used);
                        textPaint.getTextBounds(text, 0, text.length(), bounds);

                        // Calculate the desired size as a proportion of our testTextSize.
                        float desiredTextSize = testTextSize * width * 0.9f / bounds.width();
                        textPaint.setTextSize(Math.min(desiredTextSize, top));

                        textPaint.getTextBounds(text, 0, text.length(), bounds);

                        canvas.drawText(text, (width - bounds.width()) / 2f, 2f * top - ((top - bounds.height()) / 2), textPaint);
                        return bitmap;
                    }
                })
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
