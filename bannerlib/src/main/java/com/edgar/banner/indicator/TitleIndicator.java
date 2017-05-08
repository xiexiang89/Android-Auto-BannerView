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
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edgar.banner.BannerItem;
import com.edgar.banner.R;

/**
 * Created by Edgar on 2017/5/8.
 * 标题指示器
 */
public class TitleIndicator extends LinearLayout implements BannerIndicator {
    private TextView mTitle;
    public TitleIndicator(Context context) {
        this(context,null);
    }

    public TitleIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.indicator_background)));
        inflate(context, R.layout.title_indicator,this);
        mTitle = (TextView) findViewById(R.id.title);
    }

    @Override
    public void onBannerSelected(BannerItem bannerItem, int position) {
        mTitle.setText(bannerItem.getBannerTitle());
    }

    @Override
    public void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onBannerScrollStateChanged(int state) {

    }

    @Override
    public void onBannerPageUpdate(int newCount) {}

    @Override
    public View getView() {
        return this;
    }
}
