package me.loc2.loc2me.ui.md;

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

import java.util.ArrayList;
import java.util.List;

import me.loc2.loc2me.R;
import me.loc2.loc2me.ui.md.animation.SlideInOutLeftItemAnimator;
import me.loc2.loc2me.util.SafeAsyncTask;

public class OfferListFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final int DATASET_COUNT = 3;

    private static final int CIRCLE_RADIUS_DP = 50;
    private static ShapeDrawable sOverlayShape;
    private static int sScreenWidth;
    private static int sProfileImageHeight;


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
        View rootView = inflater.inflate(R.layout.wise_recycler_list, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_list_view);


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new OfferListAdapter(mDataSet);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                Thread.sleep(3000);
                mAdapter.add(createStub(1), 1);
                return true;
            }

            private OfferStub createStub(int i) {
                OfferStub testItem = new OfferStub();
                testItem.setAvatar(R.drawable.anastasia);
                testItem.setName("Anastasia");
                testItem.setDescriptionShort("TEST ITEM " + i);
                testItem.setAvatarShape(sOverlayShape);
                testItem.setsScreenWidth(sScreenWidth);
                testItem.setsProfileImageHeight(sProfileImageHeight);
                return testItem;
            }
        }.execute();


        return rootView;
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
}
