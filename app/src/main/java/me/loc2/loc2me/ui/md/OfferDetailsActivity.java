package me.loc2.loc2me.ui.md;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.loc2.loc2me.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OfferDetailsActivity extends ActionBarActivity {

    public static final String OFFER = "OFFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OfferStub offer = (OfferStub)getIntent().getParcelableExtra(OFFER);
        OfferDetailedFragment offerDetailedFragment;
        if (savedInstanceState == null) {
            offerDetailedFragment = new OfferDetailedFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, offerDetailedFragment)
                    .commit();
        }
        else {
            offerDetailedFragment = (OfferDetailedFragment)getSupportFragmentManager().findFragmentById(R.id.container);
        }
        offerDetailedFragment.setOffer(offer);
        setContentView(R.layout.offer_details);
    }

    /**
     * A fragment containing spell information
     */
    public static class OfferDetailedFragment extends Fragment {

        private OfferStub offer;
        private TextView mTextViewName;
        private TextView mTextViewDescription;
        private ImageView mAvatarView;


        public OfferDetailedFragment() {
        }

        private void loadData() {
//            mTextViewName.setText(offer.getName());
//            mTextViewDescription.setText(offer.getDescriptionFull());
            Picasso.with(getActivity()).load(offer.getAvatar())
                    .resize(offer.getsScreenWidth(), offer.getsProfileImageHeight()).centerCrop()
                    .placeholder(R.color.blue)
                    .into(mAvatarView);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.offer_details_fragment, container, false);
//            mTextViewName = (TextView) rootView.findViewById(R.id.text_view_name);
//            mTextViewDescription = (TextView) rootView.findViewById(R.id.text_view_full_description);
            mAvatarView = (ImageView)rootView.findViewById(R.id.image_view_avatar);
            if (offer != null)
                loadData();
            return rootView;
        }

        public OfferStub getOffer() {
            return offer;
        }

        public void setOffer(OfferStub offer) {
            this.offer = offer;
        }
    }
}
