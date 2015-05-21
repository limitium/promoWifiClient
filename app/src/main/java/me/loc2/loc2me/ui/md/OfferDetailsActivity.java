package me.loc2.loc2me.ui.md;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.loc2.loc2me.R;
import me.loc2.loc2me.core.Constants;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.OfferImage;
import me.loc2.loc2me.dao.OfferDAO;
import me.loc2.loc2me.util.Ln;

public class OfferDetailsActivity extends AppCompatActivity {

    private static final long ANIM_DURATION = 300;

    private ImageView mOfferDetailsImage;
    private TextView mOfferDescription;
    private View imageFrame;
    private Offer offer;
    private OfferDAO offerDAO;

    public static final String OFFER = "OFFER";

    private Map<View, Animation> showAnimations;
    private Map<View, Animation> backAnimations;
    private View mBackButton;

    private enum State {
        CLOSED,
        OPENED
    }

    private State state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offer = (Offer) getIntent().getParcelableExtra(OFFER);
        state = State.CLOSED;
        setContentView(R.layout.offer_details);

        // Postpone the transition until the window's decor view has
        // finished its layout.
        final Activity thisActivity = this;
        ActivityCompat.postponeEnterTransition(thisActivity);
        final View decor = getWindow().getDecorView();
        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                decor.getViewTreeObserver().removeOnPreDrawListener(this);
                ActivityCompat.startPostponedEnterTransition(thisActivity);
                return true;
            }
        });

        offerDAO = new OfferDAO(this);
        Ln.e("all offers: " + Arrays.asList(offerDAO.findAllReceived().toArray()));

        setUpLayout();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        } else {
            loadImage();
        }
    }

    /**
     * Use this method to instantiate your menu, and add your items to it. You
     * should return true if you have added items to it and want the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                goToList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupWindowAnimations() {
        setupEnterAnimations();
        setupExitAnimations();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        goToList();
    }

    private void goToList() {
        if (state == State.OPENED) {
            closeDetails();
        } else {
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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

    private void setUpLayout() {
        mOfferDetailsImage = (ImageView) findViewById(R.id.offer_details_image);
        mOfferDescription = (TextView) findViewById(R.id.offer_description);
        mOfferDescription.setMovementMethod(new ScrollingMovementMethod());
        imageFrame = findViewById(R.id.squared_details_image);

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        createShowAnimations();
        createBackAnimations();

        loadThumbnail();
        loadDescription();
    }

    private void loadDescription() {
        mOfferDescription.setText(offer.toString());
    }

    private void loadThumbnail() {
        String url = buildUrl(offer.getImage());
        DisplayImageOptions imageLoadingOptions = getDisplayImageOptions();
        ImageLoader.getInstance().displayImage(url, mOfferDetailsImage, imageLoadingOptions);
    }

    private void loadImage() {
        String url = buildUrl(offer.getImage());
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
        mBackButton.setY(imageFrame.getHeight() - mBackButton.getHeight() / 2);
        for (Map.Entry<View, Animation> entry: showAnimations.entrySet()) {
            entry.getKey().startAnimation(entry.getValue());
        }
    }

    private void closeDetails() {
        for (Map.Entry<View, Animation> entry: backAnimations.entrySet()) {
            entry.getKey().startAnimation(entry.getValue());
        }
    }

    private int dpToPx(int dp) {
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
    }

    private DisplayImageOptions getDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.NONE_SAFE)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();
    }

    private String buildUrl(String image) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        String url = image.getBaseUrl() + "/" + String.valueOf(image.getWidth()) + "/"
//                + String.valueOf(image.getHeight()) + "/animals/";
        String url = Constants.Http.URL_BASE+image;
        Ln.d("Loading url: " + url);
        return url;
    }

    private void createShowAnimations() {
        if (null == showAnimations) {
            showAnimations = new HashMap<>();
            showAnimations.put(mBackButton, createBackButtonShowAnimation());
            showAnimations.put(mOfferDescription, createTextDescriptionShowAnimation());
        }
    }

    private void createBackAnimations() {
        if (null == backAnimations) {
            backAnimations = new HashMap<>();
            backAnimations.put(mBackButton, createBackButtonHideAnimation());
            backAnimations.put(mOfferDescription, createTextDescriptionHideAnimation());
        }
    }

    private Animation createBackButtonShowAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.back_button_scale);
        animation.setDuration(ANIM_DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
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
        return animation;
    }

    private Animation createBackButtonHideAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.back_button_hide_scale);
        animation.setDuration(ANIM_DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
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
        return animation;
    }

    private Animation createTextDescriptionShowAnimation() {
        float x = mOfferDescription.getX();
        float y = getResources().getDisplayMetrics().heightPixels;
        float newY = imageFrame.getHeight();
        TranslateAnimation animation = new TranslateAnimation(x, x,
                y, newY);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(ANIM_DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mOfferDescription.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return animation;
    }

    private Animation createTextDescriptionHideAnimation() {
        float x = mOfferDescription.getX();
        float y = getResources().getDisplayMetrics().heightPixels;
        float newY = imageFrame.getHeight();
        TranslateAnimation animation = new TranslateAnimation(x, x,
                newY, y);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(ANIM_DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mOfferDescription.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return animation;
    }
}
