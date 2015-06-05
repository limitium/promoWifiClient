package me.loc2.loc2me.ui.md;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import me.loc2.loc2me.Injector;
import me.loc2.loc2me.R;
import me.loc2.loc2me.core.dao.OfferPersistService;
import me.loc2.loc2me.core.events.NewOfferEvent;
import me.loc2.loc2me.core.events.OfferChangedEvent;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.services.ImageLoaderService;
import me.loc2.loc2me.ui.graphics.Animations;
import me.loc2.loc2me.ui.md.animation.SlideInOutLeftItemAnimator;

public class OfferListFragment extends Fragment implements BackPressListener {

    private static final String TAG = "RecyclerViewFragment";
    public static final int SPACE_BETWEEN_CARDS = 16;

    protected View mRootView;
    protected RecyclerView mRecyclerView;
    protected OfferListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<Offer> mDataSet;
    protected TextView mNoDataTextView;

    @Inject
    protected OfferPersistService offerPersistService;

    @Inject
    protected ImageLoaderService imageLoaderService;

    @Inject
    protected NotificationManager notificationManager;

    @Inject
    protected Bus eventBus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
        initDataSet();
        eventBus.register(this);

    }

    @Subscribe
    public void addOfferToTheList(NewOfferEvent event) {
        Offer offer = event.getOffer();
        if (null != mAdapter) {
            int position = mAdapter.add(offer);
            mLayoutManager.scrollToPosition(position - 1);
            if (mRecyclerView.getVisibility() == View.INVISIBLE || mRecyclerView.getVisibility() == View.GONE) {
                Animations.crossFade(mRecyclerView, mNoDataTextView);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.offer_list, container, false);
        mRootView.setTag(TAG);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_view);
        mNoDataTextView = (TextView) mRootView.findViewById(R.id.no_data);

        setUpListAppearance();
        setUpListEvents();
        setUpListData();
        if (noOffers()) {
            Animations.crossFade(mNoDataTextView, mRecyclerView);
        }
        return mRootView;
    }

    @Subscribe
    public void onOfferChangedEvent(OfferChangedEvent offerChangedEvent) {
        if (mAdapter != null) {
            mAdapter.updateOffer(offerChangedEvent.getOffer());
        }
    }

    private void setUpListData() {
        mAdapter = new OfferListAdapter(mDataSet, imageLoaderService);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mAdapter.setMetrics(displaymetrics);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpListEvents() {
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
                            Animations.crossFade(mNoDataTextView, mRecyclerView);
                        }
                        notificationManager.cancel(offer.getId());
                        offerPersistService.saveDeleted(offer.getId());
                        offerPersistService.deleteReceived(offer.getId());
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
    }

    private void setUpListAppearance() {
        //TODO: Add tablet support with grid manager (2 columns)
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final RecyclerView.ItemDecoration decoration = new DividerItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()), 2);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView) {
            @Override
            public void onAddFinished(RecyclerView.ViewHolder item) {
                //WO for fix divider space
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void goToDetails(View sharedView, Offer offer) {
        Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
        intent.putExtra(OfferDetailsActivity.OFFER, (Parcelable) offer);

        View statusBar = getActivity().findViewById(android.R.id.statusBarBackground);
        View sharedElement = sharedView.findViewById(R.id.relaGrid);
        View toolbar = getActivity().findViewById(R.id.toolbar);

        List<Pair<View, String>> pairs = new ArrayList<>();
        pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(sharedElement, getString(R.string.card_to_details)));
        pairs.add(Pair.create(toolbar, getString(R.string.toolbar_transition)));
        ActivityOptionsCompat transitionActivityOptions;
        transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                pairs.get(0), pairs.get(1), pairs.get(2));
        Bundle bundle = transitionActivityOptions.toBundle();
        getActivity().startActivity(intent, bundle);
    }

    private boolean noOffers() {
        return mAdapter.hasNoOffers();
    }

    /**
     * As new offers are received through events no
     * service calls been made here. This method is still here just
     * to abstract data initialization.
     */
    private void initDataSet() {
        mDataSet = offerPersistService.findAllReceived();
        Collections.sort(mDataSet, new Comparator<Offer>() {
            @Override
            public int compare(Offer lhs, Offer rhs) {
                return (int) (rhs.getAdded_at() - lhs.getAdded_at());
            }
        });
    }

    @Override
    public void goBack(Activity activity) {
        activity.finish();
    }
}
