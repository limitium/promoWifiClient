package me.loc2.loc2me.ui.md;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import me.loc2.loc2me.R;
import me.loc2.loc2me.ui.md.animation.SlideInOutLeftItemAnimator;
import me.loc2.loc2me.util.Ln;

public class OfferListFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";

    private static final Integer CROSS_FADE_ANIMATION = 1000;

    protected RecyclerView mRecyclerView;
    protected OfferListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<OfferStub> mDataSet;
    protected TextView mNoDataTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
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
                OfferStub offerStub = new OfferStub();
                Random random = new Random();
                int index = random.nextInt(10);
                offerStub.setImageUrl("http://lorempixel.com");
                offerStub.setHeight(index * 100 + 900);
                offerStub.setBannerHtml("yellow");
                offerStub.setDescriptionFull(getString(R.string.lorem_ipsum_long));
                offerStub.setDescriptionShort(getString(R.string.lorem_ipsum_short));
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1 * (index + 1));
                offerStub.setAdded(cal.getTime());
                offerStub.setIndex(index);
                int position = mAdapter.add(offerStub);
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
                        mAdapter.remove(position);
                        if (mAdapter.hasNoOffers()) {
                            crossFade(mNoDataTextView, mRecyclerView);
                        }
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
        return mRootView;
    }

    private void goToDetails(View sharedView, OfferStub offer) {
        Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
        intent.putExtra(OfferDetailsActivity.OFFER, offer);

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

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        String[] templates = {
            "yellow",
            "red",
            "blue",
            "lime"
        };
        mDataSet = new ArrayList<>(templates.length);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            OfferStub offerStub = new OfferStub();
            int item = random.nextInt(templates.length);
            offerStub.setImageUrl("http://lorempixel.com");
            offerStub.setHeight(i * 100 + 900);
            offerStub.setBannerHtml(templates[item]);
            offerStub.setDescriptionFull(getString(R.string.lorem_ipsum_long));
            offerStub.setDescriptionShort(getString(R.string.lorem_ipsum_short));
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1 * (i + 1));
            offerStub.setAdded(cal.getTime());
            offerStub.setIndex(i);
            mDataSet.add(offerStub);
        }
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
