package me.loc2.loc2me.ui.md;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.loc2.loc2me.R;

public class OfferListFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final int DATASET_COUNT = 14;

    protected RecyclerView mRecyclerView;
    protected OfferListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mAdapter = new OfferListAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        return rootView;
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        mDataset[0]= "Animal Messenger";
        mDataset[1]= "Animal Shapes";
        mDataset[2]= "Animate Dead";
        mDataset[3]= "Animate Objects";
        mDataset[4]= "Antilife Shell";
        mDataset[5]= "Antimagic Field";
        mDataset[6]= "Antipathy-Sympathy";
        mDataset[7]= "Arcane Eye";
        mDataset[8]= "Arcane Gate";
        mDataset[9]= "Arcane Lock";
        mDataset[10]= "Armor of Agathys";
        mDataset[11]= "Arms of Hadar";
        mDataset[12]= "Astral Projection";
        mDataset[13]= "Augury";
    }

}
