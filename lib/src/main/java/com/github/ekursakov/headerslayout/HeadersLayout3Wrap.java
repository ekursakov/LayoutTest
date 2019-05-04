package com.github.ekursakov.headerslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.NestedScrollView;

public class HeadersLayout3Wrap extends ViewGroup {

    @ViewDebug.ExportedProperty(prefix = "scrolling")
    final List<Integer> snapPoints = new ArrayList<>();

    public HeadersLayout3Wrap(Context context) {
        this(context, null);
    }

    public HeadersLayout3Wrap(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadersLayout3Wrap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
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

                final HeadersLayout3.LayoutParams lp = (HeadersLayout3.LayoutParams) child.getLayoutParams();

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
        return layoutParams instanceof HeadersLayout3.LayoutParams;
    }

    @Override
    protected HeadersLayout3.LayoutParams generateDefaultLayoutParams() {
        return new HeadersLayout3.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, false, 0, false);
    }

    @Override
    public HeadersLayout3.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new HeadersLayout3.LayoutParams(getContext(), attrs);
    }

    @Override
    protected HeadersLayout3.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }
    //endregion

    void onScroll(NestedScrollView v, int scrollY) {
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
    }

    private int getChildCollapsedHeight(View child) {
        final HeadersLayout3.LayoutParams lp = (HeadersLayout3.LayoutParams) child.getLayoutParams();
        if (lp.collapsible) {
            return lp.collapsedHeight;
        } else {
            return child.getMeasuredHeight();
        }
    }

}