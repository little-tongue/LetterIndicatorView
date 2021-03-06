package com.fxyan.letterindicatorview.adapter.wechat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.fxyan.letterindicatorview.Tools;
import com.fxyan.letterindicatorview.adapter.BaseRecyclerAdapter;

/**
 * @author fxYan
 */
public final class WeChatItemDecoration extends RecyclerView.ItemDecoration {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<String> array;
    private int dp64;

    public WeChatItemDecoration(Context context, SparseArray<String> array) {
        this.array = array;
        this.paint.setColor(Color.parseColor("#ebebeb"));
        dp64 = (int) Tools.dp2px(context, 64);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int childAdapterPosition = parent.getChildAdapterPosition(child);
            int itemViewType = parent.getAdapter().getItemViewType(childAdapterPosition);
            if (itemViewType == BaseRecyclerAdapter.ITEM_TYPE
                    && array.indexOfKey(childAdapterPosition) < 0) {
                c.drawRect(dp64, child.getTop(), child.getRight(), child.getTop() - 1, paint);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        int itemViewType = parent.getAdapter().getItemViewType(childAdapterPosition);
        if (itemViewType == BaseRecyclerAdapter.ITEM_TYPE
                && array.indexOfKey(childAdapterPosition) < 0) {
            outRect.top = 1;
        }
    }
}
