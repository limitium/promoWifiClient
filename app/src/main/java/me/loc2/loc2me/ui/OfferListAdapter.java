package me.loc2.loc2me.ui;

import android.view.LayoutInflater;

import me.loc2.loc2me.R;
import me.loc2.loc2me.core.models.Offer;

import java.util.List;

public class OfferListAdapter extends AlternatingColorListAdapter<Offer> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public OfferListAdapter(final LayoutInflater inflater, final List<Offer> items,
                            final boolean selectable) {
        super(R.layout.wise_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public OfferListAdapter(final LayoutInflater inflater, final List<Offer> items) {
        super(R.layout.wise_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_title, R.id.tv_summary,
                R.id.tv_date};
    }

    @Override
    protected void update(final int position, final Offer item) {
        super.update(position, item);

        setText(0, item.getName());
        setText(1, item.getMessage());
        //setNumber(R.id.tv_date, item.getCreatedAt());
    }
}
