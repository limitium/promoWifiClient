package me.loc2.loc2me.ui.md;

import android.app.ActivityOptions;
import android.content.Intent;
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

public class OfferListFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";

    protected RecyclerView mRecyclerView;
    protected OfferListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<OfferStub> mDataSet;

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

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_view);

//        View testButton = mRootView.findViewById(R.id.toolbar_profile_back);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OfferStub offerStub = new OfferStub();
//                offerStub.setLogo(R.drawable.anastasia);
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

    private void goToProfileDetails(View view, OfferStub offer) {
        View sharedView = view.findViewById(R.id.image_view_avatar);
        Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
        intent.putExtra(OfferDetailsActivity.OFFER, offer);
        String transitionName = getString(R.string.transition_to_details);
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
        mDataSet = new ArrayList<>(avatars.length);
        for (int avatar : avatars) {
            OfferStub offerStub = new OfferStub();
            offerStub.setLogo(avatar);
            offerStub.setDescriptionFull(getString(R.string.lorem_ipsum_long));
            offerStub.setDescriptionShort(getString(R.string.lorem_ipsum_short));
            mDataSet.add(offerStub);
        }
    }
}
