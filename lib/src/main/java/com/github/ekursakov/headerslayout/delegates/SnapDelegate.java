package com.github.ekursakov.headerslayout.delegates;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.github.ekursakov.headerslayout.DelegatedNestedScrollView;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

public class SnapDelegate extends DelegatedNestedScrollView.DefaultDelegate {

    private final List<Integer> snapPoints;

    private boolean flingStarted = false;
    private boolean scrollStopped = true;
    private float lastVelocity;
    private boolean pressed;
    private ValueAnimator expandingAnimator;

    public SnapDelegate(List<Integer> snapPoints) {
        this.snapPoints = snapPoints;
    }

    @Override
    public boolean onNestedPreScroll(@NonNull NestedScrollView view, @NonNull View target, int dx, int dy,
                                     @NonNull int[] consumed, int type) {
        if (dy != 0) {
            lastVelocity = dy;
            scrollStopped = false;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull NestedScrollView view, @NonNull View target,
                                    float velocityX, float velocityY) {
        flingStarted = true;
        lastVelocity = velocityY;

        return animateToClosestSnapPoint(view);
    }


    @Override
    public boolean onStopNestedScroll(@NonNull NestedScrollView view, @NonNull View target, int type) {
        if (scrollStopped) {
            flingStarted = false;
            return true;
        }
        scrollStopped = true;
        switch (type) {
            case ViewCompat.TYPE_TOUCH:
                if (!flingStarted && !pressed) {
                    animateToClosestSnapPoint(view);
                }
                break;
            case ViewCompat.TYPE_NON_TOUCH:
                animateToClosestSnapPoint(view);
                break;
        }
        flingStarted = false;
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull NestedScrollView view, @NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            pressed = true;
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            pressed = false;
        }
        return false;
    }

    private boolean animateToClosestSnapPoint(NestedScrollView view) {
        int scrollY = view.getScrollY();
        int snapPoint = getClosestSnapPoint(scrollY);

        // TODO: don't interrupt fling if finalY is greater than last snap point
        if (snapPoint != scrollY) {
            if (expandingAnimator != null) {
                expandingAnimator.cancel();
            }
            expandingAnimator = ValueAnimator.ofInt(scrollY, snapPoint);
            expandingAnimator.addUpdateListener(valueAnimator ->
                    view.scrollTo(0, (int) valueAnimator.getAnimatedValue())
            );
            expandingAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    expandingAnimator = null;
                }
            });
            expandingAnimator.setInterpolator(new DecelerateInterpolator(2));
            // TODO: use dp/sec instead of px
            expandingAnimator.setDuration(200 * Math.abs(scrollY - snapPoint) / 100);
            expandingAnimator.start();
            return true;
        }
        return false;
    }

    private int getClosestSnapPoint(int scrollY) {
        if (snapPoints.size() == 1) {
            return scrollY;
        }

        int result = Collections.binarySearch(snapPoints, scrollY);
        if (result >= 0) {
            return scrollY;
        } else {
            int insertionPoint = -result - 1;
            if (lastVelocity > 0) {
                return snapPoints.get(Math.min(snapPoints.size() - 1, insertionPoint));
            } else {
                return snapPoints.get(Math.max(0, insertionPoint - 1));
            }
        }
    }
}
