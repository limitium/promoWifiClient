package me.loc2.loc2me.ui.md;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import me.loc2.loc2me.R;
import me.loc2.loc2me.util.Ln;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OfferDetailsActivity extends Activity {

    private ImageView mOfferDetailsImage;

    public static final String OFFER = "OFFER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OfferStub offer = (OfferStub)getIntent().getParcelableExtra(OFFER);
        setContentView(R.layout.offer_details);
        setUpLayout(offer);
    }

    private void setUpLayout(OfferStub offer) {
        mOfferDetailsImage = (ImageView)findViewById(R.id.offer_list_image_details);
        String url = buildUrl(offer.getImageUrl());
        DisplayImageOptions imageLoadingOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();
        ImageLoader.getInstance().displayImage(url, mOfferDetailsImage, imageLoadingOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Ln.i("On loading started");
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Ln.i("On loading failed");
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Ln.i("On loading complete");
            }
        });
    }

    private String buildUrl(String imageUrl) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        return imageUrl + "/" + String.valueOf(width) + "/"
                + String.valueOf(height) + "/fashion/";
    }

}
