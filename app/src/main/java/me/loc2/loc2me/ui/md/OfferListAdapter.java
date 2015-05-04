package me.loc2.loc2me.ui.md;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.loc2.loc2me.R;
import me.loc2.loc2me.util.Ln;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private static final String TAG = "OfferListAdapter";

    private List<OfferStub> mDataSet;
    private DisplayMetrics metrics;
    private final DisplayImageOptions imageLoadingOptions;


    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mOfferItemImage;
        private View mOfferListButtons;
        private ProgressBar mSpinner;
        private final Context context;
        private final DisplayMetrics metrics;
        private final DisplayImageOptions imageLoadingOptions;
        private boolean imageLoaded = false;

        public ViewHolder(View v, int viewGroupWidh, DisplayMetrics metrics, DisplayImageOptions imageLoadingOptions) {
            super(v);
            this.metrics = metrics;
            this.imageLoadingOptions = imageLoadingOptions;
            // Define click listener for the ViewHolder's View.
            mOfferItemImage = (ImageView) v.findViewById(R.id.offer_list_image);
            mOfferListButtons = v.findViewById(R.id.offer_action_buttons);
            mSpinner = (ProgressBar)v.findViewById(R.id.loading);
            context = v.getContext();
        }

        public void loadData(OfferStub offerStub) {
            if (!imageLoaded) {
                mSpinner.setVisibility(View.VISIBLE);
                String url = buildUrl(offerStub.getImageUrl(), offerStub.getHeight());
                ImageLoader.getInstance().displayImage(url, mOfferItemImage, imageLoadingOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        Ln.i("On loading started");
                        mSpinner.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Ln.i("On loading failed");
                        mSpinner.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Ln.i("On loading complete");
                        mSpinner.setVisibility(View.GONE);
                        imageLoaded = true;
                    }
                });
            }
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
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
        imageLoadingOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.offer_list_item, viewGroup, false);
        final int viewGroupWidh = viewGroup.getWidth();

        return new ViewHolder(v, viewGroupWidh, getMetrics(), imageLoadingOptions);
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
