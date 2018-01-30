package com.bjune.touchtest;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by bjune.jeong on 2018-01-25.
 */

public class ImageViewBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private static final String TAG = "ImageViewBehavior";
    private int mPeekHeight = 200;

    private int mToolbarDp = 40;
    private int mImageMarginDp = 6;

    private int mImageTopPx;
    private Object mImageSynchronize = new Object();

    private boolean mIsInit = false;

    public ImageViewBehavior() {
    }

    public ImageViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        AppCompatActivity activity = (AppCompatActivity) context;
        mImageTopPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mToolbarDp + mImageMarginDp, activity.getResources().getDisplayMetrics());

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof RelativeLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {

        if (dependency instanceof RelativeLayout) {
            RelativeLayout rl = (RelativeLayout) dependency;
            ImageView iv = (ImageView) child;

            if (!mIsInit) {
                iv.setImageResource(R.drawable.jisoo_vertical);

                iv.setX(0);
                iv.setY(mImageTopPx);
                mIsInit = true;
            }

            float height = rl.getHeight();

            synchronized (mImageSynchronize) {
                if (rl.getY() > 500) {
                    ViewGroup.LayoutParams lp = iv.getLayoutParams();
                    lp.height = (int) (rl.getY() - iv.getY());

                    Log.d(TAG, "width = " + lp.width + ", height = " + lp.height);
                    iv.setLayoutParams(lp);

                    if (rl.getY() > 1500) {
                        float alpha = (height - rl.getY()) / (height - 1500);
                        iv.setAlpha(alpha);
                    }
                }
            }
        }
        return true;
    }
}
