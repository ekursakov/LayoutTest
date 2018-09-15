package ek.layouttest.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class FixedNestedScrollView extends NestedScrollView2 {
    private boolean flingStarted = false;
    private boolean scrollStopped = true;
    private float lastVelocity;
    private boolean pressed;
    private ValueAnimator expandingAnimator;

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
        if (dy != 0) {
            lastVelocity = dy;
            scrollStopped = false;
        }

        if ((dy < 0 && isTargetScrolledToTop(target)) || (dy > 0 && canScrollVertically(1))) {
            scrollBy(0, dy);
            consumed[1] = dy;
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed, type);
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        flingStarted = true;
        lastVelocity = velocityY;

        return animateToClosestSnap() || super.onNestedPreFling(target, velocityX, velocityY);
    }


    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        if (scrollStopped) {
            flingStarted = false;
            return;
        }
        scrollStopped = true;
        switch (type) {
            case ViewCompat.TYPE_TOUCH:
                if (!flingStarted && !pressed) {
                    animateToClosestSnap();
                }
                break;
            case ViewCompat.TYPE_NON_TOUCH:
                animateToClosestSnap();
                break;
        }
        flingStarted = false;
        super.onStopNestedScroll(target, type);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            pressed = true;
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            pressed = false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean animateToClosestSnap() {
        int scrollY = getScrollY();
        int snapPoint = ((UberScrollLayout2) getChildAt(0)).getClosestSnapPoint(scrollY, lastVelocity);

        if (snapPoint != scrollY) {
            if (expandingAnimator != null) {
                expandingAnimator.cancel();
            }
            expandingAnimator = ValueAnimator.ofInt(scrollY, snapPoint);
            expandingAnimator.addUpdateListener(valueAnimator ->
                    scrollTo(0, (int) valueAnimator.getAnimatedValue())
            );
            expandingAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    expandingAnimator = null;
                }
            });
            expandingAnimator.setInterpolator(new DecelerateInterpolator(2));
            expandingAnimator.setDuration(200 * Math.abs(scrollY - snapPoint) / 100);
            expandingAnimator.start();
            return true;
        }
        return false;
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
