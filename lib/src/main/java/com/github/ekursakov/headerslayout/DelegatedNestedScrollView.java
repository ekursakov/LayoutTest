package com.github.ekursakov.headerslayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DelegatedNestedScrollView extends NestedScrollView {

    private final List<Delegate> delegates = new ArrayList<>();

    public DelegatedNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public DelegatedNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DelegatedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addDelegate(Delegate delegate) {
        delegates.add(delegate);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        for (Delegate delegate : delegates) {
            if (delegate.onStartNestedScroll(this, child, target, axes, type)) {
                return true;
            }
        }
        return super.onStartNestedScroll(child, target, axes, type);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        for (Delegate delegate : delegates) {
            if (delegate.onNestedScrollAccepted(this, child, target, axes, type)) {
                return;
            }
        }
        super.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        for (Delegate delegate : delegates) {
            if (delegate.onNestedPreScroll(this, target, dx, dy, consumed, type)) {
                return;
            }
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        for (Delegate delegate : delegates) {
            if (delegate.onNestedScroll(this, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)) {
                return;
            }
        }
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        for (Delegate delegate : delegates) {
            if (delegate.onNestedPreFling(this, target, velocityX, velocityY)) {
                return true;
            }
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        for (Delegate delegate : delegates) {
            if (delegate.onNestedFling(this, target, velocityX, velocityY, consumed)) {
                return true;
            }
        }
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        for (Delegate delegate : delegates) {
            if (delegate.onStopNestedScroll(this, target, type)) {
                return;
            }
        }
        super.onStopNestedScroll(target, type);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        for (Delegate delegate : delegates) {
            if (delegate.onInterceptTouchEvent(this, ev)) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface Delegate {
        boolean onStartNestedScroll(@NonNull NestedScrollView view, @NonNull View child, @NonNull View target,
                                    int axes, int type);

        boolean onNestedScrollAccepted(@NonNull NestedScrollView view, @NonNull View child, @NonNull View target,
                                       int axes, int type);

        boolean onNestedPreScroll(@NonNull NestedScrollView view, @NonNull View target, int dx, int dy,
                                  @NonNull int[] consumed, int type);

        boolean onNestedScroll(@NonNull NestedScrollView view, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type);

        boolean onNestedPreFling(@NonNull NestedScrollView view, @NonNull View target,
                                 float velocityX, float velocityY);

        boolean onNestedFling(@NonNull NestedScrollView view, @NonNull View target,
                              float velocityX, float velocityY, boolean consumed);

        boolean onStopNestedScroll(@NonNull NestedScrollView view, @NonNull View target, int type);

        boolean onInterceptTouchEvent(@NonNull NestedScrollView view, @NonNull MotionEvent ev);
    }

    public static class DefaultDelegate implements Delegate {
        @Override
        public boolean onStartNestedScroll(@NonNull NestedScrollView view, @NonNull View child, @NonNull View target,
                                           int axes, int type) {
            return false;
        }

        @Override
        public boolean onNestedScrollAccepted(@NonNull NestedScrollView view, @NonNull View child, @NonNull View target,
                                              int axes, int type) {
            return false;
        }

        @Override
        public boolean onNestedPreScroll(@NonNull NestedScrollView view, @NonNull View target, int dx, int dy,
                                         @NonNull int[] consumed, int type) {
            return false;
        }

        @Override
        public boolean onNestedScroll(@NonNull NestedScrollView view, @NonNull View target,
                                      int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
            return false;
        }

        @Override
        public boolean onNestedPreFling(@NonNull NestedScrollView view, @NonNull View target,
                                        float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onNestedFling(@NonNull NestedScrollView view, @NonNull View target,
                                     float velocityX, float velocityY, boolean consumed) {
            return false;
        }

        @Override
        public boolean onStopNestedScroll(@NonNull NestedScrollView view, @NonNull View target, int type) {
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull NestedScrollView view, @NonNull MotionEvent ev) {
            return false;
        }
    }
}
