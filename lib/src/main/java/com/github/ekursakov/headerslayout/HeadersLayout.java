package com.github.ekursakov.headerslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Px;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.github.ekursakov.headerslayout.delegates.FixRecyclerNestedScrollDelegate;
import com.github.ekursakov.headerslayout.delegates.SnapDelegate;

import java.util.ArrayList;
import java.util.List;

public class HeadersLayout extends ViewGroup {

    @ViewDebug.ExportedProperty(prefix = "scrolling")
    private final List<Integer> snapPoints = new ArrayList<>();

    public HeadersLayout(Context context) {
        this(context, null);
    }

    public HeadersLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadersLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() instanceof DelegatedNestedScrollView) {
            DelegatedNestedScrollView parent = (DelegatedNestedScrollView) getParent();

            parent.setOverScrollMode(OVER_SCROLL_NEVER);

            parent.addDelegate(new SnapDelegate(snapPoints));
            parent.addDelegate(new FixRecyclerNestedScrollDelegate());

            parent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> onScroll(v, scrollY)
            );
        } else {
            throw new IllegalStateException(getClass().getSimpleName() + " parent must be "
                                            + DelegatedNestedScrollView.class.getSimpleName());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        if (height == 0) {
            // NestedScrollView always pass zero height at first measure pass
            setMeasuredDimension(width, height);
        } else {
            snapPoints.clear();
            snapPoints.add(0);

            int availableHeight = height;
            int collapsedHeight = 0;
            int expandedHeight = 0;
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (child.getVisibility() == GONE) {
                    continue;
                }

                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (lp.height == LayoutParams.MATCH_PARENT) {
                    measureChild(child, widthMeasureSpec,
                            MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.EXACTLY));
                } else {
                    measureChild(child, widthMeasureSpec,
                            MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.AT_MOST));
                }

                int childCollapsedHeight = getChildCollapsedHeight(child);
                availableHeight -= childCollapsedHeight;
                collapsedHeight += childCollapsedHeight;
                expandedHeight += child.getMeasuredHeight();

                if (lp.collapsible && lp.snap) {
                    snapPoints.add(expandedHeight - collapsedHeight);
                }
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
            if (child.getVisibility() == GONE) {
                continue;
            }
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
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, false, 0, false);
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
        int previousHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

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

        @ViewDebug.ExportedProperty(category = "layout")
        public boolean collapsible;
        @ViewDebug.ExportedProperty(category = "layout")
        @Px
        public int collapsedHeight;
        @ViewDebug.ExportedProperty(category = "layout")
        public boolean snap;

        public LayoutParams(int height, boolean collapsible, @Px int collapsedHeight, boolean snap) {
            super(MATCH_PARENT, height);
            this.collapsible = collapsible;
            this.collapsedHeight = collapsedHeight;
            this.snap = snap;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.HeadersLayout_Layout);
            collapsible = a.getBoolean(R.styleable.HeadersLayout_Layout_layout_collapsible, false);
            collapsedHeight = a.getDimensionPixelSize(R.styleable.HeadersLayout_Layout_layout_collapsedHeight, 0);
            snap = a.getBoolean(R.styleable.HeadersLayout_Layout_layout_snap, false);
            a.recycle();
        }
    }

}
