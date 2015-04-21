package me.loc2.loc2me.ui;

import android.os.Bundle;
import android.widget.TextView;

import me.loc2.loc2me.R;

import butterknife.InjectView;
import me.loc2.loc2me.core.models.Offer;

import static me.loc2.loc2me.core.Constants.Extra.WISE_ITEM;

public class OfferActivity extends BootstrapActivity {

    private Offer offer;

    @InjectView(R.id.tv_title) protected TextView title;
    @InjectView(R.id.tv_content) protected TextView content;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wise);

        if (getIntent() != null && getIntent().getExtras() != null) {
            offer = (Offer) getIntent().getExtras().getSerializable(WISE_ITEM);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(offer.getName());

        title.setText(offer.getType());
        content.setText(offer.getMessage());

    }

}
