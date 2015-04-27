package me.loc2.loc2me.ui.md;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.loc2.loc2me.R;
import me.loc2.loc2me.ui.md.animation.SlideInOutLeftItemAnimator;

public class OfferListFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final int DATASET_COUNT = 3;

    private static final int REVEAL_ANIMATION_DURATION = 1000;
    private static final int MAX_DELAY_SHOW_DETAILS_ANIMATION = 500;
    private static final int ANIMATION_DURATION_SHOW_PROFILE_DETAILS = 500;
    private static final int STEP_DELAY_HIDE_DETAILS_ANIMATION = 80;
    private static final int ANIMATION_DURATION_CLOSE_PROFILE_DETAILS = 500;
    private static final int ANIMATION_DURATION_SHOW_PROFILE_BUTTON = 300;
    private static final int CIRCLE_RADIUS_DP = 50;

    private static ShapeDrawable sOverlayShape;
    private static int sScreenWidth;
    private static int sProfileImageHeight;

    private static int pos = 0;


    protected RecyclerView mRecyclerView;
    protected OfferListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<OfferStub> mDataSet;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sScreenWidth = getResources().getDisplayMetrics().widthPixels;
        sProfileImageHeight = getResources().getDimensionPixelSize(R.dimen.height_profile_image);
        sOverlayShape = buildAvatarCircleOverlay();
        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.wise_recycler_list, container, false);
        mRootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_list_view);

//        View testButton = mRootView.findViewById(R.id.toolbar_profile_back);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OfferStub offerStub = new OfferStub();
//                offerStub.setAvatar(R.drawable.anastasia);
//                offerStub.setsProfileImageHeight(sProfileImageHeight);
//                offerStub.setsScreenWidth(sScreenWidth);
//                offerStub.setName("TEST");
//                offerStub.setAvatarShape(sOverlayShape);
//                int position = mAdapter.add(offerStub);
//                mLayoutManager.scrollToPosition(position - 1);
//            }
//        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new OfferListAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mRootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        goToProfileDetails(view, mDataSet.get(position));
                    }
                })
        );
        return mRootView;
    }

    private void goToProfileDetails(View sharedView, OfferStub offer) {
        Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
        intent.putExtra(OfferDetailsActivity.OFFER, offer);
        String transitionName = "transitionname";

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
        Bundle bundle = transitionActivityOptions.toBundle();
        getActivity().startActivity(intent, bundle);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        int[] avatars = {
                R.drawable.andriy,
                R.drawable.dmitriy};
        String[] names = getResources().getStringArray(R.array.array_names);
        mDataSet = new ArrayList<>(avatars.length);
        for (int i = 0; i < avatars.length; i++) {
            OfferStub offerStub = new OfferStub();
            offerStub.setAvatar(avatars[i]);
            offerStub.setName(names[i]);
            offerStub.setDescriptionFull(getString(R.string.lorem_ipsum_long));
            offerStub.setDescriptionShort(getString(R.string.lorem_ipsum_short));
            offerStub.setAvatarShape(sOverlayShape);
            offerStub.setsScreenWidth(sScreenWidth);
            offerStub.setsProfileImageHeight(sProfileImageHeight);
            mDataSet.add(offerStub);
        }
    }

    /**
     * This method creates a view with empty/transparent circle in it's center. This view is used
     * to cover the profile avatar.
     *
     * @return - ShapeDrawable object.
     */
    private ShapeDrawable buildAvatarCircleOverlay() {
        int radius = 666;
        ShapeDrawable overlay = new ShapeDrawable(new RoundRectShape(null,
                new RectF(
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2)),
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius}));
        overlay.getPaint().setColor(getResources().getColor(R.color.gray));
        return overlay;
    }

    private int dpToPx(int dp) {
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
    }

    private int getCircleRadiusDp() {
        return CIRCLE_RADIUS_DP;
    }

    protected int getMaxDelayShowDetailsAnimation() {
        return MAX_DELAY_SHOW_DETAILS_ANIMATION;
    }
}
