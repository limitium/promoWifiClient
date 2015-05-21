package me.loc2.loc2me.ui.md;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.ocpsoft.pretty.time.PrettyTime;

import java.util.Date;
import java.util.List;

import me.loc2.loc2me.R;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.models.OfferImage;
import me.loc2.loc2me.util.Ln;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private static final String TAG = "OfferListAdapter";

    private List<Offer> mDataSet;
    private DisplayMetrics metrics;
    private final DisplayImageOptions imageLoadingOptions;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mOfferItemImage;
        private TextView mOfferDateCreated;
        private ProgressBar mSpinner;

        private final DisplayMetrics metrics;
        private final DisplayImageOptions imageLoadingOptions;
        private boolean imageLoaded = false;

        public ViewHolder(View v, int viewGroupWidh, DisplayMetrics metrics, DisplayImageOptions imageLoadingOptions) {
            super(v);
            this.metrics = metrics;
            this.imageLoadingOptions = imageLoadingOptions;
            // Define click listener for the ViewHolder's View.
            mOfferItemImage = (ImageView) v.findViewById(R.id.offer_list_image);
            mOfferDateCreated = (TextView)v.findViewById(R.id.offer_date_created);
            mSpinner = (ProgressBar)v.findViewById(R.id.loading);
        }

        public void loadData(final Offer offer) {
            if (!imageLoaded) {
                mOfferDateCreated.setVisibility(View.INVISIBLE);
                mSpinner.setVisibility(View.VISIBLE);
                String url = buildUrl(offer.get_image());
                ImageLoader.getInstance().displayImage("http://s27.postimg.org/i689ms769/olympus.png", mOfferItemImage, imageLoadingOptions, new SimpleImageLoadingListener() {
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
                        mOfferDateCreated.setVisibility(View.VISIBLE);
                        imageLoaded = true;
                    }
                });
            }
            PrettyTime prettyTime = new PrettyTime(new Date());
            mOfferDateCreated.setText("Added " + prettyTime.format(new Date(offer.getAddedAt())));
        }

        private String buildUrl(OfferImage image) {
            return image.getBaseUrl() + "/" + String.valueOf(image.getWidth()) + "/"
                    + String.valueOf(image.getHeight()) + "/animals/";
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public OfferListAdapter(List<Offer> dataSet) {
        mDataSet = dataSet;
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
        imageLoadingOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.NONE_SAFE)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.offer_list_item, viewGroup, false);
        final int viewGroupWidth = viewGroup.getWidth();

        return new ViewHolder(v, viewGroupWidth, getMetrics(), imageLoadingOptions);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        Ln.i("Position: " + position);
        viewHolder.loadData(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public int add(Offer item) {
        mDataSet.add(item);
        notifyItemInserted(mDataSet.size() - 1);
        return mDataSet.size();
    }

    public Offer remove(Offer item) {
        return remove(mDataSet.indexOf(item));
    }

    public Offer remove(int position) {
        Ln.i("Removing element on position: ");
        Offer removed = mDataSet.remove(position);
        notifyDataSetChanged();
        return removed;
    }

    public void setMetrics(DisplayMetrics metrics) {
        this.metrics = metrics;
    }

    public boolean hasNoOffers() {
        return mDataSet.isEmpty();
    }

    public DisplayMetrics getMetrics() {
        return metrics;
    }
}
