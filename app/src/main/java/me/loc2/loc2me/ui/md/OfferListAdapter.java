package me.loc2.loc2me.ui.md;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.loc2.loc2me.R;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private static final String TAG = "OfferListAdapter";

    private List<OfferStub> mDataSet;
    private DisplayMetrics metrics;


    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mOfferItemImage;
        private View mOfferListButtons;
        private final Context context;
        private final DisplayMetrics metrics;

        public ViewHolder(View v, int viewGroupWidh, DisplayMetrics metrics) {
            super(v);
            this.metrics = metrics;
            // Define click listener for the ViewHolder's View.
            mOfferItemImage = (ImageView) v.findViewById(R.id.offer_list_image);
            mOfferListButtons = v.findViewById(R.id.offer_list_buttons);
            context = v.getContext();
        }

        public void loadData(OfferStub offerStub) {
            String url = buildUrl(offerStub.getImageUrl(), offerStub.getHeight());
            Picasso.with(context).load(url)
                    .into(mOfferItemImage);
        }

        private String buildUrl(String imageUrl, int height) {
            int width = metrics.widthPixels;
            return imageUrl + "/" + String.valueOf(width) + "/"
                    + String.valueOf(height) + "/fashion/";
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public OfferListAdapter(List<OfferStub> dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.offer_list_item, viewGroup, false);
        final int viewGroupWidh = viewGroup.getWidth();

        return new ViewHolder(v, viewGroupWidh, getMetrics());
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.loadData(mDataSet.get(position));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public int add(OfferStub item) {
        mDataSet.add(item);
        notifyItemInserted(mDataSet.size() - 1);
        return mDataSet.size();
    }

    public void remove(OfferStub item) {
        int position = mDataSet.indexOf(item);
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void setMetrics(DisplayMetrics metrics) {
        this.metrics = metrics;
    }

    public DisplayMetrics getMetrics() {
        return metrics;
    }


}
