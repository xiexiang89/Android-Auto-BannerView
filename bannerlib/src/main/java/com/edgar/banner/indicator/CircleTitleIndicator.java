/*
 * Copyright (C) 2017 The Edgar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edgar.banner.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edgar.banner.BannerItem;
import com.edgar.banner.R;

/**
 * Created by Edgar on 2017/5/1.
 * 圆点标题
 */
public class CircleTitleIndicator extends RelativeLayout implements BannerIndicator,IndicatorAttributeSet{
    private TextView mTitle;
    private LinearLayout mIndicatorView;

    private Drawable mUnSelectedDrawable;
    private Drawable mSelectedDrawable;

    private int mPointMargin;
    private int mCurItem;

    public CircleTitleIndicator(Context context) {
        this(context,null);
    }

    public CircleTitleIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleTitleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources resources = getResources();
        setBackgroundDrawable(new ColorDrawable(resources.getColor(R.color.indicator_background)));
        inflate(context,R.layout.circle_title_indicator,this);
        mTitle = (TextView) findViewById(R.id.title);
        mIndicatorView = (LinearLayout) findViewById(R.id.indicator_view);

        int horPadding = resources.getDimensionPixelOffset(R.dimen.indicator_hor_padding);
        int verPadding = resources.getDimensionPixelOffset(R.dimen.indicator_ver_padding);
        setPadding(horPadding,verPadding,horPadding,verPadding);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void onBannerSelected(BannerItem bannerItem, int position) {
        mTitle.setText(bannerItem.getBannerTitle());
        mIndicatorView.getChildAt(mCurItem).setBackgroundDrawable(mUnSelectedDrawable);
        mCurItem = position;
        mIndicatorView.getChildAt(mCurItem).setBackgroundDrawable(mSelectedDrawable);
    }

    @Override
    public void onBannerPageUpdate(int newCount) {
        mIndicatorView.removeAllViews();
        if (newCount > 0){
            for (int i = 0; i < newCount; i++) {
                mIndicatorView.addView(createPointView());
            }
            mCurItem = 0;
            mIndicatorView.getChildAt(mCurItem).setBackgroundDrawable(mSelectedDrawable);
        }
    }

    @Override
    public ViewGroup getView() {
        return this;
    }

    private ImageView createPointView(){
        Resources resources = getResources();
        ImageView pointView = new ImageView(getContext());
        pointView.setBackgroundDrawable(mUnSelectedDrawable);
        int pointSize = resources.getDimensionPixelSize(R.dimen.point_size);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pointSize,pointSize);
        params.leftMargin = mPointMargin;
        pointView.setLayoutParams(params);
        return pointView;
    }

    @Override
    public void setIndicatorGravity(int indicatorGravity) {
        if (indicatorGravity == IndicatorGravity.LEFT){
            LayoutParams params = (LayoutParams) mIndicatorView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            params = (LayoutParams) mTitle.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF,0);
            params.addRule(RelativeLayout.RIGHT_OF,R.id.indicator_view);
            mTitle.setGravity(Gravity.RIGHT);
        } else if (indicatorGravity == IndicatorGravity.RIGHT){
            LayoutParams params = (LayoutParams) mIndicatorView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params = (LayoutParams) mTitle.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF,0);
            params.addRule(RelativeLayout.LEFT_OF,R.id.indicator_view);
            mTitle.setGravity(Gravity.LEFT);
        } else {
            LayoutParams params = (LayoutParams) mIndicatorView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params = (LayoutParams) mTitle.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF,0);
            params.addRule(RelativeLayout.LEFT_OF,R.id.indicator_view);
            mTitle.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public void setPointPadding(int pointPadding) {
        mPointMargin = pointPadding;
    }

    @Override
    public void setPointDrawable(Drawable unSelectDrawable, Drawable selectDrawable) {
        mUnSelectedDrawable = unSelectDrawable;
        mSelectedDrawable = selectDrawable;
    }
}