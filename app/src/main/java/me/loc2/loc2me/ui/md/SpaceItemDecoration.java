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
        Ln.i("Position: " + position);
        if (position % 2 == 1) {
            outRect.left = space;
        } else {
            outRect.right = space;
        }
        if (position != 1 && position != 0) {
            outRect.top = space;
        }
        if (position != parent.getChildCount() && position != (parent.getChildCount() - 1) ) {
            outRect.bottom = space;
        }
    }
}