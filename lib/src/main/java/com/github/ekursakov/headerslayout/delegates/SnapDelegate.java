package com.github.ekursakov.headerslayout.delegates;

import android.view.MotionEvent;
import android.view.View;

import com.github.ekursakov.headerslayout.DelegatedNestedScrollView;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class SnapDelegate extends DelegatedNestedScrollView.DefaultDelegate {

    private final List<Integer> snapPoints;

    private boolean flingStarted = false;
    private boolean scrollStopped = true;
    private float lastVelocity;
    private boolean pressed;
    private SpringAnimation springAnimation;

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
        int snapPoint = getClosestSnapPoint((int) (scrollY + lastVelocity / 2));

        // TODO: don't interrupt fling if finalY is greater than last snap point
        if (snapPoint != scrollY) {
            if (springAnimation != null) {
                springAnimation.cancel();
            }

            springAnimation = new SpringAnimation(view, new FloatPropertyCompat<NestedScrollView>("scrollY") {
                @Override
                public float getValue(NestedScrollView object) {
                    return object.getScrollY();
                }

                @Override
                public void setValue(NestedScrollView object, float value) {
                    object.setScrollY((int) value);
                }
            });
            SpringForce force = new SpringForce();
            force.setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
            force.setStiffness(SpringForce.STIFFNESS_LOW);
            springAnimation.setSpring(force);
            springAnimation.setStartVelocity(lastVelocity);
            if (snapPoint > scrollY) {
                springAnimation.setMaxValue(snapPoint);
            } else {
                springAnimation.setMinValue(snapPoint);
            }

            springAnimation.animateToFinalPosition(snapPoint);
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
