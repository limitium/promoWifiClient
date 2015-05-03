package me.loc2.loc2me.ui.md;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.loc2.loc2me.R;

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
        Picasso.with(this).load(url)
                .fit().centerCrop()
                .placeholder(R.color.blue)
                .into(mOfferDetailsImage);
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
