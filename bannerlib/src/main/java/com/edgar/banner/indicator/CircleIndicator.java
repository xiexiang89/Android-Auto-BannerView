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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edgar.banner.BannerItem;
import com.edgar.banner.R;

/**
 * Created by Edgar on 2017/5/1.
 * 不带标题的圆点指示器
 * @hide
 */
public class CircleIndicator extends LinearLayout implements BannerIndicator,IndicatorAttributeSet{
    private Drawable mUnSelectedDrawable;
    private Drawable mSelectedDrawable;
    private int mPointPadding;
    private int mCurItem;
    private int mIndicatorGravity = IndicatorGravity.LEFT;

    public CircleIndicator(Context context) {
        this(context,null);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources resources = getResources();
        setBackgroundDrawable(new ColorDrawable(resources.getColor(R.color.indicator_background)));
        setOrientation(HORIZONTAL);
        int horPadding = resources.getDimensionPixelOffset(R.dimen.indicator_hor_padding);
        int verPadding = resources.getDimensionPixelOffset(R.dimen.indicator_ver_padding);
        setPadding(horPadding,verPadding,horPadding,verPadding);
    }

    @Override
    public void onBannerSelected(BannerItem bannerItem, int position) {
        getChildAt(mCurItem).setBackgroundDrawable(mUnSelectedDrawable);
        mCurItem = position;
        getChildAt(mCurItem).setBackgroundDrawable(mSelectedDrawable);
    }

    @Override
    public void onBannerPageUpdate(int newCount) {
        removeAllViews();
        if (newCount > 0){
            for (int i = 0; i < newCount; i++) {
                addView(createPointView());
            }
            mCurItem = 0;
            getChildAt(mCurItem).setBackgroundDrawable(mSelectedDrawable);
        }
    }

    @Override
    public void setPointPadding(int pointPadding){
        mPointPadding = pointPadding;
    }

    @Override
    public void setPointDrawable(Drawable unSelectDrawable, Drawable selectDrawable) {
        mUnSelectedDrawable = unSelectDrawable;
        mSelectedDrawable = selectDrawable;
    }

    public void setIndicatorGravity(int indicatorGravity){
        mIndicatorGravity = indicatorGravity;
        if (mIndicatorGravity == IndicatorGravity.LEFT){
            setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        } else if (mIndicatorGravity == IndicatorGravity.RIGHT){
            setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        } else {
            setGravity(Gravity.CENTER);
        }
    }

    private ImageView createPointView(){
        Resources resources = getResources();
        ImageView pointView = new ImageView(getContext());
        pointView.setBackgroundDrawable(mUnSelectedDrawable);
        int pointSize = resources.getDimensionPixelSize(R.dimen.point_size);
        LayoutParams params = new LayoutParams(pointSize,pointSize);
        if (mIndicatorGravity== IndicatorGravity.LEFT){
            params.rightMargin = mPointPadding;
        } else {
            params.leftMargin = mPointPadding;
        }
        pointView.setLayoutParams(params);
        return pointView;
    }

    @Override
    public ViewGroup getView() {
        return this;
    }
}