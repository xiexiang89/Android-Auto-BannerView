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

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.edgar.banner.BannerItem;

/**
 * Created by Edgar on 2017/4/30.
 * 这个接口,可以用Activity实现,也可以通过自定义View实现
 */
public interface BannerIndicator{

    /**
     * page selected
     * @param bannerItem banner item
     * @param position page index
     */
    void onBannerSelected(BannerItem bannerItem,int position);

    /**
     * Banner page scrolled.
     * @param position banner item
     * @param positionOffset position offset
     * @param positionOffsetPixels positionOffsetPixels
     */
    void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels);

    /**
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    void onBannerScrollStateChanged(int state);

    /**
     * Banner 更新,需要更新indicator
     * @param pageCount banner page count
     */
    void onBannerPageUpdate(int pageCount);

    /**
     * 返回indicator view
     */
    View getView();
}