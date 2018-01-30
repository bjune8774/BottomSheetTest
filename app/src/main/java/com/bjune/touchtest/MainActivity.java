package com.bjune.touchtest;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "[JUNE] BottomSheetTest";
    private Toolbar mToolbar;
    private Toolbar mBottomSheetToolbar;
    private Button mBtnCollapsed;
    private Button mBtnHide;
    private Button mBtnExpandHeight;

    private RelativeLayout mBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int mPrevBottomSheetState = 0;
    private int mPeekHeight = 200;
    private int mBtnDefaultY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnCollapsed = (Button) findViewById(R.id.btn_collapsed);
        mBtnCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mBtnHide = (Button) findViewById(R.id.btn_hide);
        mBtnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        mBottomSheetToolbar = (Toolbar) findViewById(R.id.bottom_sheet_toolbar);
        mBottomSheetToolbar.setVisibility(View.GONE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle(null);
        mToolbar.showOverflowMenu();
        mToolbar.bringToFront();

        mBottomSheet = (RelativeLayout) findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            float mSlideOffset = -2;
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (mPrevBottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {
                            mBottomSheetToolbar.setVisibility(View.GONE);
                            mToolbar.setVisibility(View.VISIBLE);
                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if(mSlideOffset == (float)1.0) {
                            mToolbar.setVisibility(View.GONE);
                            mBottomSheetToolbar.setVisibility(View.VISIBLE);
                            setSupportActionBar(mBottomSheetToolbar);
                            getSupportActionBar().setDisplayShowTitleEnabled(false);
                            if (mBottomSheetToolbar.isOverflowMenuShowing()) {
                                mBottomSheetToolbar.showOverflowMenu();
                                mBottomSheetToolbar.setTitle(null);
                            }
                        }

                        break;
                }
                mPrevBottomSheetState = newState;
                Log.d(TAG, "state : " + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d(TAG, "onSlide : " + slideOffset);
                float peekHeight = mBottomSheetBehavior.getPeekHeight();
                float fullHeight = mBottomSheet.getHeight();

                if(slideOffset <= 0) {
                    mBtnExpandHeight.setY(mBtnDefaultY + (peekHeight * slideOffset / 2));
                } else {
                    mBtnExpandHeight.setY(mBtnDefaultY + ((fullHeight - peekHeight) * slideOffset / 2));
                }
                mSlideOffset = slideOffset;

            }
        });

        mBtnExpandHeight = (Button) findViewById(R.id.btn_expand_height);
        mBtnExpandHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPeekHeight += 100;
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mBottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, mPeekHeight, getResources().getDisplayMetrics()));
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        mBtnDefaultY = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPeekHeight, getResources().getDisplayMetrics())) / 2;
        mBtnExpandHeight.setY(mBtnDefaultY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.si_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
