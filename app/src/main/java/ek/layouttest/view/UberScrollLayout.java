package ek.layouttest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import ek.layouttest.R;

public class UberScrollLayout extends ViewGroup {

    public UberScrollLayout(Context context) {
        this(context, null);
    }

    public UberScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UberScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int availableHeight = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.AT_MOST));

            if (lp.collapsible) {
                availableHeight -= lp.collapsedHeight;
            } else {
                availableHeight -= child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;

        int childTop = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            child.layout(0, childTop, width, childTop + child.getMeasuredHeight());

            childTop += child.getMeasuredHeight();
        }
    }

    @Override
    protected int computeVerticalScrollRange() {
        // TODO: cache after layout
        int totalHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            totalHeight += getChildCollapsedHeight(child);
        }
        return totalHeight;
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
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.UberScrollLayout_Layout);
            collapsible = a.getBoolean(R.styleable.UberScrollLayout_Layout_layout_collapsible, false);
            collapsedHeight = a.getLayoutDimension(R.styleable.UberScrollLayout_Layout_layout_collapsedHeight, 0);
            a.recycle();
        }
    }
}
