package com.nuttertools.utils;

/**
 * Created by GabdrakhmanovII on 08.02.2018.
 */

        import android.annotation.TargetApi;
        import android.content.Context;
        import android.os.Build;
        import android.util.AttributeSet;
        import android.widget.RelativeLayout;

/** A RelativeLayout that will always be square -- same width and height,
 * where the height is based off the width. */
public class SquareRelativeLayout extends RelativeLayout {

    public SquareRelativeLayout(Context context) {
        super(context);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareRelativeLayout(Context context, AttributeSet attrs,         int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

