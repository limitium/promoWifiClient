package me.loc2.loc2me.ui.md;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.common.eventbus.EventBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.OfferEventService;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.OfferImage;
import me.loc2.loc2me.core.models.WifiInfo;
import me.loc2.loc2me.ui.md.animation.SlideInOutLeftItemAnimator;

public class OfferListFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";

    private static final Integer CROSS_FADE_ANIMATION = 1000;

    protected RecyclerView mRecyclerView;
    protected OfferListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<Offer> mDataSet;
    protected TextView mNoDataTextView;

    @Inject
    OfferEventService offerService;

    @Inject
    protected Bus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
        Injector.inject(this);
        eventBus.register(this);
    }

    @Subscribe
    public void addOfferToTheList(NewOfferEvent event) {
        Offer offer = event.getOffer();
        if (null != mAdapter) {
            mAdapter.add(offer);
            if (mRecyclerView.getVisibility() == View.INVISIBLE || mRecyclerView.getVisibility() == View.GONE) {
                crossFade(mRecyclerView, mNoDataTextView);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.offer_list, container, false);
        mRootView.setTag(TAG);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_view);
        mNoDataTextView = (TextView) mRootView.findViewById(R.id.no_data);

        View testButton = mRootView.findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Offer offer = new Offer();
                Random random = new Random();
                int index = random.nextInt(10);
                String indexStr = String.valueOf(index);

                offer.setId(new BigInteger(indexStr));
                offer.setName("Item " + indexStr);
                offer.setMessage(getString(R.string.lorem_ipsum_long));
                offer.setType("Type " + indexStr);
                offer.setCategory("Category " + indexStr);
                offer.setImage(new OfferImage("http://lorempixel.com/", 1080, index * 100 + 900));
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1 * (index + 1));
                offer.setAddedAt(cal.getTimeInMillis());
                int position = mAdapter.add(offer);
                mLayoutManager.scrollToPosition(position - 1);
                if (mRecyclerView.getVisibility() == View.INVISIBLE || mRecyclerView.getVisibility() == View.GONE) {
                    crossFade(mRecyclerView, mNoDataTextView);
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        SlideInOutLeftItemAnimator animator = new SlideInOutLeftItemAnimator(mRecyclerView);
        mRecyclerView.setItemAnimator(animator);
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                mRecyclerView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, int position) {
                        Offer offer = mAdapter.remove(position);
                        if (mAdapter.hasNoOffers()) {
                            crossFade(mNoDataTextView, mRecyclerView);
                        }
                        offerService.remove(offer);
                    }
                })
                .setIsVertical(false)
                .create();
        mRecyclerView.setOnTouchListener(listener);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mRootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        goToDetails(view, mDataSet.get(position));
                    }
                })
        );

        mAdapter = new OfferListAdapter(mDataSet);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mAdapter.setMetrics(displaymetrics);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter.hasNoOffers()) {
            crossFade(mNoDataTextView, mRecyclerView);
        }
        return mRootView;
    }

    private void goToDetails(View sharedView, Offer offer) {
        Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
        intent.putExtra(OfferDetailsActivity.OFFER, (Parcelable)offer);

        View statusBar = getActivity().findViewById(android.R.id.statusBarBackground);
        View sharedElement = sharedView.findViewById(R.id.offer_list_image);

        List<Pair<View, String>> pairs = new ArrayList<>();
        pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(sharedElement, getString(R.string.card_to_details)));

        ActivityOptions transitionActivityOptions;
        transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs.toArray(new Pair[pairs.size()]));
        Bundle bundle = transitionActivityOptions.toBundle();
        getActivity().startActivity(intent, bundle);
    }

    private void initDataset() {
        mDataSet = new ArrayList<>();
    }

    private void crossFade(final View viewToShow, final View viewToHide) {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        viewToShow.setAlpha(0f);
        viewToShow.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        viewToShow.animate()
                .alpha(1f)
                .setDuration(CROSS_FADE_ANIMATION)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        viewToHide.animate()
                .alpha(0f)
                .setDuration(CROSS_FADE_ANIMATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToHide.setVisibility(View.GONE);
                    }
                });
    }
}
