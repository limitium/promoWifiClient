package me.loc2.loc2me.ui.md;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.loc2.loc2me.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OfferDetailsActivity extends Activity {

    private static final long ANIM_DURATION = 300;
    private View bgViewGroup;

    public static final String OFFER = "OFFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OfferStub offer = (OfferStub)getIntent().getParcelableExtra(OFFER);
        setContentView(R.layout.offer_details);
        setUpLayout(offer);
        setUpWindowAnimations();
    }

    private void setUpLayout(OfferStub offer) {
        bgViewGroup = findViewById(R.id.backgroundViewGroup);
        TextView mNameView = (TextView)findViewById(R.id.text_view_name);
        mNameView.setText(offer.getName());
        TextView mFullDescriptionView = (TextView)findViewById(R.id.text_view_full_description);
        mFullDescriptionView.setText(offer.getDescriptionFull());
        ImageView mAvatarView = (ImageView)findViewById(R.id.image_view_details_avatar);
        Picasso.with(OfferDetailsActivity.this).load(offer.getLogo())
                .resize(offer.getsScreenWidth(),
                        offer.getsProfileImageHeight()).centerCrop()
                .placeholder(R.color.blue)
                .into(mAvatarView);
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
}
