/*
 * Copyright (C) 2016 The Edgar
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

package com.edgar.banner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Edgar on 2016/12/2.
 * 默认的Banner page view
 */
class DefaultBannerPageView implements IBannerPageView {

    @Override
    public View createPageView(Context context,BannerItem bannerItem, int position) {
        return createImageView(context,bannerItem);
    }

    @Override
    public View createTitleView(Context context,BannerItem bannerItem, int position) {
        return null;
    }

    @Override
    public void destroyPageView(View pageView,int position) {
        if (pageView instanceof ImageView){
            ((ImageView) pageView).setImageBitmap(null);
        }
    }

    @Override
    public void finishInstantiateItem(View pageView, BannerItem bannerItem, int position) {}

    private ImageView createImageView(Context context,BannerItem bannerItem){
        final ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}