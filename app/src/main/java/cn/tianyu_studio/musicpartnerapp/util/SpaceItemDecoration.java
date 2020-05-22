package cn.tianyu_studio.musicpartnerapp.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position % 4 == 0)
            outRect.left = mSpace;
        if ((position) + 1 % 4 == 0)
            outRect.right = mSpace;
        /*outRect.bottom = mSpace;
        outRect.top = mSpace;*/
    }
}
