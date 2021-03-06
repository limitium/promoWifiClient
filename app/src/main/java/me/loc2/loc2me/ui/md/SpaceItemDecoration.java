package me.loc2.loc2me.ui.md;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.loc2.loc2me.util.Ln;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildPosition(view);

        if (position % 2 == 1) {
            outRect.left = space;
            outRect.right = space * 2;
        } else {
            outRect.right = space;
            outRect.left = space * 2;
        }

        outRect.top = space;
        outRect.bottom = space;

        if (position == 1 || position == 0) {
            outRect.top = space * 2;
        }

        if (position == parent.getChildCount() - 1|| position == parent.getChildCount() - 2) {
            outRect.bottom = space * 2;
        }
    }
}