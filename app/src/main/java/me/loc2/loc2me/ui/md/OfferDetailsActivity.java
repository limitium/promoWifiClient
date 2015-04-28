package me.loc2.loc2me.ui.md;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import me.loc2.loc2me.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OfferDetailsActivity extends Activity {

    private WebView mDetailsView;

    public static final String OFFER = "OFFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OfferStub offer = (OfferStub)getIntent().getParcelableExtra(OFFER);
        setContentView(R.layout.offer_details);
        setUpLayout(offer);
    }

    private void setUpLayout(OfferStub offer) {
        mDetailsView = (WebView)findViewById(R.id.details_view);
        String fullPath = "file:///android_asset/" + OfferConstants.ASSETS + "/" +
                offer.getBannerHtml() + ".html";
        mDetailsView.loadUrl(fullPath);
    }
}
