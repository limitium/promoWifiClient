package me.loc2.loc2me.ui.md;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import me.loc2.loc2me.R;
import me.loc2.loc2me.core.models.Offer;
import me.loc2.loc2me.core.services.ImageLoaderService;
import me.loc2.loc2me.util.Ln;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private static final String TAG = "OfferListAdapter";

    private List<Offer> mDataSet;
    private DisplayMetrics metrics;
    private final DisplayImageOptions imageLoadingOptions;
    private ImageLoaderService imageLoaderService;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final long ANIM_DURATION = 300;
        private static final int AVATAR_HEIGHT = 60;
        private RelativeLayout mOfferItemHolder;
        private Context context;
        private ImageLoaderService imageLoaderService;
        private ImageView mOfferItemImage;
        private View mOfferAvatar;
        private ImageView mOfferImageAvatar;
        private TextView mOfferCompanyName;
        private TextView mOfferPromoActionName;
        private View mOfferDescription;
        private ProgressBar mSpinner;

        private final DisplayMetrics metrics;
        private final DisplayImageOptions imageLoadingOptions;
        private boolean imageLoaded = false;

        public ViewHolder(View v, DisplayMetrics metrics, DisplayImageOptions imageLoadingOptions, ImageLoaderService imageLoaderService) {
            super(v);
            this.metrics = metrics;
            this.imageLoadingOptions = imageLoadingOptions;
            this.imageLoaderService = imageLoaderService;
            this.context = v.getContext();
            // Define click listener for the ViewHolder's View.
            mOfferItemHolder = (RelativeLayout) v.findViewById(R.id.relaGrid);
            mOfferItemImage = (ImageView) v.findViewById(R.id.offer_list_image);
            mOfferDescription = v.findViewById(R.id.offer_description_layout);
            mOfferCompanyName = (TextView) mOfferDescription.findViewById(R.id.offer_company_name);
            mOfferPromoActionName = (TextView) mOfferDescription.findViewById(R.id.offer_promo_name);
            mOfferAvatar = v.findViewById(R.id.avatar);
            mOfferImageAvatar = (ImageView)v.findViewById(R.id.avatar_image);
            mSpinner = (ProgressBar)v.findViewById(R.id.loading);
        }

        public void loadData(final Offer offer) {
//            if (!imageLoaded) {
            mSpinner.setVisibility(View.VISIBLE);
            imageLoaderService.loadImage(offer.getImage(), mOfferItemImage, new SimpleImageLoadingListener() {
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
                    loadAvatar(offer);
                    imageLoaded = true;
                }
            });
//            }
            mOfferPromoActionName.setText(offer.getName());
            mOfferCompanyName.setText(offer.getOrganization_name());
            mOfferCompanyName.setTextColor(offer.getTextColor());
            mOfferItemHolder.setBackgroundColor(offer.getBackgroundColor());
            mOfferDescription.setBackgroundColor(offer.getBackgroundColor());
        }

        private void loadAvatar(Offer offer) {
            imageLoaderService.loadAvatar(offer.getAvatar(), mOfferImageAvatar);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.avatar_in_list_scale);
            animation.setDuration(ANIM_DURATION);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mOfferAvatar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mOfferAvatar.startAnimation(animation);

        }

        private int dpToPx(int dp) {
            return Math.round((float) dp * metrics.density);
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public OfferListAdapter(List<Offer> dataSet, ImageLoaderService imageLoaderService) {
        mDataSet = dataSet;
        this.imageLoaderService = imageLoaderService;
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

        return new ViewHolder(v, getMetrics(), imageLoadingOptions, imageLoaderService);
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
