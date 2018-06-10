package ek.layouttest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class UberScrollLayout extends ViewGroup {

    public UberScrollLayout(Context context) {
        this(context, null);
    }

    public UberScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UberScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.layout(l, 0, r, child.getMeasuredHeight());
            child.setTranslationY(childTop);
            childTop += child.getMeasuredHeight();
        }
    }


    public static class LayoutParams extends ViewGroup.LayoutParams {
        private final boolean collapsible;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            collapsible = false;
        }

        public boolean isCollapsible() {
            return collapsible;
        }
    }
}
