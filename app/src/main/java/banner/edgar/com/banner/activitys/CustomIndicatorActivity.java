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

package banner.edgar.com.banner.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.edgar.banner.BannerPageViewAdapter;
import com.edgar.banner.ImageLoader;
import com.facebook.drawee.backends.pipeline.Fresco;

import banner.edgar.com.banner.FrescoBannerPageView;
import banner.edgar.com.banner.LinePageIndicator;
import banner.edgar.com.banner.R;

/**
 * Created by XieXiang on 2017/5/8.
 * 自定义Indicator演示
 */
public class CustomIndicatorActivity extends BannerControllActivity {
    private FrescoBannerPageView frescoBannerPageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fresco.initialize(getApplicationContext());
        frescoBannerPageView = new FrescoBannerPageView();
        super.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelOffset(R.dimen.underline_indicator_height));
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.indicator_margin);
        params.topMargin = getResources().getDimensionPixelOffset(R.dimen.indicator_margin);
        mBannerPagerView.setCustomIndicator(new LinePageIndicator(this),params);
        mBannerPagerView.setBanner(BANNER_ITEMS);
    }

    @Override
    protected ImageLoader getBannerImageLoader() {
        return frescoBannerPageView;
    }

    @Override
    protected BannerPageViewAdapter getBannerPageViewAdapter() {
        return frescoBannerPageView;
    }
}
