package im.wise.wiseim.ui;

import android.view.LayoutInflater;

import im.wise.wiseim.R;
import im.wise.wiseim.core.models.Wise;

import java.util.List;

public class WisesListAdapter extends AlternatingColorListAdapter<Wise> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public WisesListAdapter(final LayoutInflater inflater, final List<Wise> items,
                            final boolean selectable) {
        super(R.layout.wise_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public WisesListAdapter(final LayoutInflater inflater, final List<Wise> items) {
        super(R.layout.wise_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_title, R.id.tv_summary,
                R.id.tv_date};
    }

    @Override
    protected void update(final int position, final Wise item) {
        super.update(position, item);

        setText(0, item.getName());
        setText(1, item.getMessage());
        //setNumber(R.id.tv_date, item.getCreatedAt());
    }
}
