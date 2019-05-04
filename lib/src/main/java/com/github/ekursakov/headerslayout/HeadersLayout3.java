package com.github.ekursakov.headerslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.github.ekursakov.headerslayout.delegates.FixRecyclerNestedScrollDelegate;
import com.github.ekursakov.headerslayout.delegates.SnapDelegate;

import androidx.annotation.Px;

public class HeadersLayout3 extends DelegatedNestedScrollView {
    private final HeadersLayout3Wrap container;

    public HeadersLayout3(Context context) {
        this(context, null);
    }

    public HeadersLayout3(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadersLayout3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setFillViewport(true);

        container = new HeadersLayout3Wrap(context);

        addDelegate(new SnapDelegate(container.snapPoints));
        addDelegate(new FixRecyclerNestedScrollDelegate());

        super.addView(container);
    }

    //region addView
    @Override
    public void addView(View child) {
        if (child == container) {
            super.addView(container);
        } else {
            container.addView(child);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (child == container) {
            super.addView(child, index);
        } else {
            container.addView(child, index);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (child == container) {
            super.addView(child, params);
        } else {
            container.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child == container) {
            super.addView(child, index, params);
        } else {
            container.addView(child, index, params);
        }
    }
    //endregion

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        container.onScroll(this, t);
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
