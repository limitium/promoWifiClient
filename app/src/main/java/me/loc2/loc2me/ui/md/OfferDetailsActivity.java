package me.loc2.loc2me.ui.md;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.EventBus;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.dao.OfferPersistService;
import me.loc2.loc2me.core.events.OfferChangedEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.services.ImageLoaderService;
import me.loc2.loc2me.core.services.OfferNotificationService;
import me.loc2.loc2me.events.OfferUsedEvent;
import me.loc2.loc2me.ui.MainActivity;
import me.loc2.loc2me.util.ColorUtil;
import me.loc2.loc2me.util.Ln;

public class OfferDetailsActivity extends AppCompatActivity {

    private static final long ANIM_DURATION = 300;

    @Inject
    protected NotificationManager notificationManager;
    @Inject
    protected ImageLoaderService imageLoaderService;
    @Inject
    protected OfferPersistService offerPersistService;
    @Inject
    protected Bus eventBus;

    private ImageView mOfferDetailsImage;
    private TextView mOfferDescription;
    private View imageFrame;
    private Offer offer;

    public static final String OFFER = "OFFER";

    private Map<View, Animation> showAnimations;
    private Map<View, Animation> backAnimations;
    private View mAvatar;
    private ImageView mAvatarImage;
    private TextView mOfferCompanyName;
    private TextView mOfferPromoActionName;
    private View mOfferDescriptionLayout;
    private TextView mOfferCreated;
    private Toolbar toolbar;
    private Button mButton;

    private enum State {
        CLOSED,
        OPENED
    }

    private State state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
        setContentView(R.layout.offer_details);

        offer = (Offer) getIntent().getParcelableExtra(OFFER);
        state = State.CLOSED;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(offer.getName());
            toolbar.setBackgroundColor(offer.getBackgroundColor());

            ViewCompat.setTransitionName(toolbar, getString(R.string.toolbar_transition));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setActionBarColor();
        }

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

        setUpLayout();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean isFromNotification = getIntent().getBooleanExtra(OfferNotificationService.NOTIFICATION_INTENT, false);
            if (isFromNotification) {
                loadImage();
            } else {
                loadThumbnail();
                setupWindowAnimations();
            }
        } else {
            loadImage();
        }
        loadTexts();

        closeNotificationIfExists();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setActionBarColor() {
        int color = ColorUtil.darker(offer.getBackgroundColor(), 0.85f);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
    }

    private void closeNotificationIfExists() {
        notificationManager.cancel(offer.getId());
    }

    /**
     * Use this method to instantiate your menu, and add your items to it. You
     * should return true if you have added items to it and want the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.toolbar_menu_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
            supportFinishAfterTransition();
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimations() {
        Transition sharedElementReturnTransition = getWindow().getSharedElementReturnTransition();
        sharedElementReturnTransition.setStartDelay(0);

        Transition returnTransition = getWindow().getReturnTransition();
        returnTransition.setDuration(50);
    }

    private void setUpLayout() {
        mOfferDetailsImage = (ImageView) findViewById(R.id.offer_details_image);
        mOfferDescription = (TextView) findViewById(R.id.offer_description);
        mOfferDescriptionLayout = findViewById(R.id.offer_description_layout);
        mOfferCompanyName = (TextView) mOfferDescriptionLayout.findViewById(R.id.offer_company_name);
        mOfferPromoActionName = (TextView) mOfferDescriptionLayout.findViewById(R.id.offer_promo_name);
        mOfferCreated = (TextView) mOfferDescriptionLayout.findViewById(R.id.offer_date_created);
        mButton = (Button) findViewById(R.id.use);

        imageFrame = findViewById(R.id.squared_details_image);

        mAvatar = findViewById(R.id.avatar);
        mAvatarImage = (ImageView) findViewById(R.id.avatar_image);

        createShowAnimations();
        createBackAnimations();
    }

    private void loadTexts() {
        mOfferDescription.setText(offer.getDescription());
        mOfferCompanyName.setText(offer.getOrganization_name());
        mOfferPromoActionName.setText(offer.getName());
        mOfferCreated.setText(offer.getCreatedAsPrettyText());
        mOfferDescriptionLayout.setBackgroundColor(offer.getBackgroundColor());
        imageFrame.setBackgroundColor(offer.getBackgroundColor());
        setUpUseButton();
    }

    private void setUpUseButton() {
        mButton.setVisibility(!offer.getIs_disposable() || offer.getIs_used() ? View.INVISIBLE : View.VISIBLE);
        mButton.setBackgroundColor(ColorUtil.darker(offer.getBackgroundColor(), 0.7f));
        mButton.setTextColor(Color.WHITE);
        mButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           final OfferDetailsActivity offerDetailsActivity = OfferDetailsActivity.this;

                                           eventBus.post(new OfferUsedEvent(offer));
                                           new AlertDialog.Builder(OfferDetailsActivity.this)
                                                   .setTitle(offerDetailsActivity.getString(R.string.use_title))
                                                   .setMessage(offerDetailsActivity.getString(R.string.use_question))
                                                   .setIcon(android.R.drawable.ic_dialog_alert)
                                                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                       public void onClick(DialogInterface dialog, int whichButton) {
                                                           Toast.makeText(offerDetailsActivity, offerDetailsActivity.getString(R.string.used), Toast.LENGTH_SHORT).show();
                                                           mButton.setVisibility(View.INVISIBLE);
                                                           offer.setIs_used(true);
                                                           eventBus.post(new OfferChangedEvent(offer));
                                                           loadThumbnail();
                                                           offerPersistService.updateReceived(offer);
                                                       }
                                                   })
                                                   .setNegativeButton(android.R.string.no, null)
                                                   .show();
                                       }
                                   }
        );
    }

    private void loadThumbnail() {
        imageLoaderService.loadImage(offer, mOfferDetailsImage);
    }

    private void loadImage() {
        imageLoaderService.loadImage(offer, mOfferDetailsImage, new SimpleImageLoadingListener() {
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
        for (Map.Entry<View, Animation> entry : showAnimations.entrySet()) {
            entry.getKey().startAnimation(entry.getValue());
        }
    }

    private void closeDetails() {
        for (Map.Entry<View, Animation> entry : backAnimations.entrySet()) {
            entry.getKey().startAnimation(entry.getValue());
        }
    }


    private void createShowAnimations() {
        if (null == showAnimations) {
            showAnimations = new HashMap<>();
            showAnimations.put(mAvatar, createAvatarShowAnimation());
        }
    }

    private void createBackAnimations() {
        if (null == backAnimations) {
            backAnimations = new HashMap<>();
            backAnimations.put(mAvatar, createAvatarHideAnimation());
        }
    }

    private Animation createAvatarShowAnimation() {
        imageLoaderService.loadAvatar(offer.getAvatar(), mAvatarImage);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.avatar_scale);
        animation.setDuration(ANIM_DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mAvatar.setVisibility(View.VISIBLE);
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

    private Animation createAvatarHideAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.avatar_hide_scale);
        animation.setDuration(ANIM_DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                state = State.CLOSED;
                mAvatar.setVisibility(View.INVISIBLE);
                onBackPressed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return animation;
    }

    private int dpToPx(int dp) {
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
    }


}
