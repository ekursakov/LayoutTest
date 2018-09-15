package com.github.ekursakov.headerslayout.delegates;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.ekursakov.headerslayout.DelegatedNestedScrollView;

public class FixRecyclerNestedScrollDelegate extends DelegatedNestedScrollView.DefaultDelegate {
    @Override
    public boolean onNestedPreScroll(@NonNull NestedScrollView view,
                                     @NonNull View target,
                                     int dx, int dy,
                                     @NonNull int[] consumed,
                                     @ViewCompat.NestedScrollType int type) {
        if ((dy < 0 && isTargetScrolledToTop(target)) || (dy > 0 && view.canScrollVertically(1))) {
            view.scrollBy(0, dy);
            consumed[1] = dy;
            return true;
        } else {
            return false;
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
        } else {
            return false;
        }
    }
}
