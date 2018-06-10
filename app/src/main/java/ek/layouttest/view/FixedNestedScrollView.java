package ek.layouttest.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FixedNestedScrollView extends NestedScrollView2 {
    public FixedNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public FixedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, int[] consumed, int type) {
        if ((dy < 0 && isTargetScrolledToTop(target)) || (dy > 0 && canScrollVertically(1))) {
            scrollBy(0, dy);
            consumed[1] = dy;
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed, type);
        }
    }

    private boolean isTargetScrolledToTop(View target) {
        if (target instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) target;
            final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            return lm != null
                   && lm.findFirstVisibleItemPosition() == 0
                   && lm.findViewByPosition(0).getTop() == 0;
        } else if (target instanceof NestedScrollView) {
            return !target.canScrollVertically(-1);
        }
        return false;
    }
}
