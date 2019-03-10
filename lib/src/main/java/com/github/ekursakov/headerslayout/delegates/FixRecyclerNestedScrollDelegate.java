package com.github.ekursakov.headerslayout.delegates;

import android.view.View;

import com.github.ekursakov.headerslayout.DelegatedNestedScrollView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FixRecyclerNestedScrollDelegate implements DelegatedNestedScrollView.Delegate {
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
