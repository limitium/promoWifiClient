package me.loc2.loc2me.ui.md;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import me.loc2.loc2me.R;
import me.loc2.loc2me.util.Ln;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OfferDetailsActivity extends Activity {

    private static final long ANIM_DURATION = 300;

    private ImageView mOfferDetailsImage;
    private View imageFrame;
    private OfferStub offer;

    public static final String OFFER = "OFFER";

    private Animation mBackButtonShowAnimation;
    private Animation mBackButtonHideAnimation;
    private View mBackButton;

    private enum State {
        CLOSED,
        OPENED
    }

    private State state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offer = (OfferStub)getIntent().getParcelableExtra(OFFER);
        state = State.CLOSED;
        setContentView(R.layout.offer_details);

        // Postpone the transition until the window's decor view has
        // finished its layout.
        postponeEnterTransition();

        final View decor = getWindow().getDecorView();
        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                decor.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

        setUpLayout();
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        setupEnterAnimations();
        setupExitAnimations();
    }

    private void setupEnterAnimations() {
        final Transition enterTransition = getWindow().getSharedElementEnterTransition();
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                loadImage();
                enterTransition.removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                enterTransition.removeListener(this);
            }

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    @Override
    public void onBackPressed() {
        if (state == State.OPENED) {
            closeDetails();
        } else {
            super.onBackPressed();
        }
    }

    private void setupExitAnimations() {
        Transition sharedElementReturnTransition = getWindow().getSharedElementReturnTransition();
        sharedElementReturnTransition.setStartDelay(ANIM_DURATION);

        Transition returnTransition = getWindow().getReturnTransition();
        returnTransition.setDuration(ANIM_DURATION);

        returnTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Ln.d("Exit animation transition start");
            }

            @Override
            public void onTransitionEnd(Transition transition) {}

            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    private void setUpLayout() {
        mOfferDetailsImage = (ImageView)findViewById(R.id.offer_details_image);
        imageFrame = findViewById(R.id.squared_details_image);

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        createBackButtonShowAnimation();
        createBackButtonHideAnimation();

        loadThumbnail();
    }

    private void loadThumbnail() {
        String url = buildUrl(offer.getThumbnailUrl());
        DisplayImageOptions imageLoadingOptions = getDisplayImageOptions();
        ImageLoader.getInstance().displayImage(url, mOfferDetailsImage, imageLoadingOptions);
    }

    private void loadImage() {
        String url = buildUrl(offer.getImageUrl());
        DisplayImageOptions imageLoadingOptions = getDisplayImageOptions();
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
                state = State.OPENED;
                animateOpenDetails();
            }
        });

    }

    private void animateOpenDetails() {
        mBackButton.setY(imageFrame.getHeight() - mBackButton.getHeight()/2);
        mBackButton.startAnimation(mBackButtonShowAnimation);
    }

    private void closeDetails() {
        mBackButton.startAnimation(mBackButtonHideAnimation);
    }

    private int dpToPx(int dp) {
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
    }

    private DisplayImageOptions getDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .build();
    }

    private String buildUrl(String imageUrl) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = offer.getHeight();
        int width = metrics.widthPixels;
        String url = imageUrl + "/" + String.valueOf(width) + "/"
                + String.valueOf(height) + "/animals/";
        Ln.d("Loading url: " + url);
        return url;
    }

    private void createBackButtonShowAnimation() {
        if (mBackButtonShowAnimation == null) {
            mBackButtonShowAnimation = AnimationUtils.loadAnimation(this, R.anim.profile_button_scale);
            mBackButtonShowAnimation.setDuration(ANIM_DURATION);
            mBackButtonShowAnimation.setInterpolator(new AccelerateInterpolator());
            mBackButtonShowAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mBackButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void createBackButtonHideAnimation() {
        if (mBackButtonHideAnimation == null) {
            mBackButtonHideAnimation = AnimationUtils.loadAnimation(this, R.anim.profile_button_hide_scale);
            mBackButtonHideAnimation.setDuration(ANIM_DURATION);
            mBackButtonHideAnimation.setInterpolator(new AccelerateInterpolator());
            mBackButtonHideAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    state = State.CLOSED;
                    mBackButton.setVisibility(View.INVISIBLE);
                    onBackPressed();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

}
