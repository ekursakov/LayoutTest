package ek.layouttest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Px;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ek.layouttest.R;

public class UberScrollLayout2 extends ViewGroup {

    private static final String TAG = "UberScrollLayout2";

    public UberScrollLayout2(Context context) {
        this(context, null);
    }

    public UberScrollLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UberScrollLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() instanceof NestedScrollView) {
            ((NestedScrollView) getParent()).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    onScroll(v, scrollY);
                }
            });
        } else {
            throw new IllegalStateException("UberScrollLayout2 parent must be NestedScrollView");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        if (height == 0) {
            setMeasuredDimension(width, height);
        } else {
            int availableHeight = height;
            int expandedHeight = 0;
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (child.getVisibility() == GONE) {
                    continue;
                }

                if (child.getLayoutParams().height == LayoutParams.MATCH_PARENT) {
                    measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.EXACTLY));
                } else {
                    measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.AT_MOST));
                }

                availableHeight -= getChildCollapsedHeight(child);
                expandedHeight += child.getMeasuredHeight();
            }

            setMeasuredDimension(width, expandedHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;

        int childTop = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.layout(0, childTop, width, childTop + child.getMeasuredHeight());
            childTop += child.getMeasuredHeight();
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return childCount - i - 1;
    }

    //region LayoutParams
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, false, 0);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }
    //endregion

    private void onScroll(NestedScrollView v, int scrollY) {
        Log.d(TAG, "onScroll(): [" + v.getScrollY() + "] scrollY = [" + scrollY + "]");

        int previousHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            int collapsedHeight = getChildCollapsedHeight(child);
            int topWhenCollapsed = child.getBottom() - previousHeight - collapsedHeight;

            if (collapsedHeight > 0 && scrollY > topWhenCollapsed) {
                child.setTranslationY(scrollY - topWhenCollapsed);
            } else {
                child.setTranslationY(0);
            }
            previousHeight += collapsedHeight;
        }
        v.invalidate();
    }

    private int getChildCollapsedHeight(View child) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp.collapsible) {
            return lp.collapsedHeight;
        } else {
            return child.getMeasuredHeight();
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public boolean collapsible;
        @Px
        public int collapsedHeight;

        public LayoutParams(int height, boolean collapsible, int collapsedHeight) {
            super(MATCH_PARENT, height);
            this.collapsible = collapsible;
            this.collapsedHeight = collapsedHeight;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.UberScrollLayout2_Layout);
            collapsible = a.getBoolean(R.styleable.UberScrollLayout2_Layout_layout_2collapsible, false);
            collapsedHeight = a.getDimensionPixelSize(R.styleable.UberScrollLayout2_Layout_layout_2collapsedHeight, 0);
            a.recycle();
        }
    }
}
