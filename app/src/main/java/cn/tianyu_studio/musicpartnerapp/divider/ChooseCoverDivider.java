package cn.tianyu_studio.musicpartnerapp.divider;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ChooseCoverDivider extends RecyclerView.ItemDecoration {

    private int divideHeight;
    private int spanCount;

    public ChooseCoverDivider(int divideHeight, int spanCount) {
        this.divideHeight = divideHeight;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 每个都有左边距和下边距
        outRect.left = divideHeight;
        outRect.bottom = divideHeight;
        // 最后一行有右边距
        if ((parent.getChildLayoutPosition(view) + 1) % spanCount == 0) {
            outRect.right = divideHeight;
        }
        // 第一行有上边距
        if (parent.getChildLayoutPosition(view) < spanCount) {
            outRect.top = divideHeight;
        }
    }

}
