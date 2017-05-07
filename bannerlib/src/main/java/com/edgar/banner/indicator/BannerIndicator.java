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

import android.view.View;
import android.view.ViewGroup;

import com.edgar.banner.BannerItem;

/**
 * Created by Edgar on 2017/4/30.
 */
public interface BannerIndicator{

    /**
     * page selected
     * @param bannerItem banner item
     * @param position page index
     */
    void onBannerSelected(BannerItem bannerItem,int position);

    /**
     * Banner 更新,需要更新indicator
     * @param newCount banner新的数量
     */
    void onBannerPageUpdate(int newCount);

    ViewGroup getView();
}