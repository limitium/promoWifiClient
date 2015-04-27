package me.loc2.loc2me.ui.md;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.loc2.loc2me.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OfferDetailsActivity extends ActionBarActivity {

    private static final long ANIM_DURATION = 1000;
    private View bgViewGroup;

    public static final String OFFER = "OFFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OfferStub offer = (OfferStub)getIntent().getParcelableExtra(OFFER);
//        OfferDetailedFragment offerDetailedFragment;
//        if (savedInstanceState == null) {
//            offerDetailedFragment = new OfferDetailedFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, offerDetailedFragment)
//                    .commit();
//        }
//        else {
//            offerDetailedFragment = (OfferDetailedFragment)getSupportFragmentManager().findFragmentById(R.id.container);
//        }
//        offerDetailedFragment.setOffer(offer);
        setContentView(R.layout.offer_details);
        setUpLayout();
        setUpWindowAnimations();
    }

    private void setUpLayout() {
        bgViewGroup = findViewById(R.id.backgroundViewGroup);
    }

    private void setUpWindowAnimations() {
        setupEnterAnimations();
        setupExitAnimations();
    }

    private void setupEnterAnimations() {
        Transition enterTransition = getWindow().getSharedElementEnterTransition();
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}

            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealShow(bgViewGroup);
            }

            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    private void setupExitAnimations() {
        Transition sharedElementReturnTransition = getWindow().getSharedElementReturnTransition();
        sharedElementReturnTransition.setStartDelay(ANIM_DURATION);


        Transition returnTransition = getWindow().getReturnTransition();
        returnTransition.setDuration(ANIM_DURATION);
        returnTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                animateRevealHide(bgViewGroup);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    private void animateRevealShow(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void animateRevealHide(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int initialRadius = viewRoot.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewRoot.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(ANIM_DURATION);
        anim.start();
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
