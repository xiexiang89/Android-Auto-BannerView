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

/**
 * Created by Edgar on 2016/12/2.
 */
public interface IBannerPageView {

    /**
     * 创建Banner page view
     * 该方法只会在第一次创建page的时候才会调用,后面将不会再调用.
     * @see IBannerPageView#finishInstantiateItem(View, BannerItem, int)
     * @param context 上下文
     * @param bannerItem banner对象
     * @param position banner位置
     */
    View createPageView(Context context,BannerItem bannerItem, int position);

    /**
     * 创建Banner标题View
     * @param context 上下文
     * @param bannerItem banner对象
     * @param position banner位置
     */
    View createTitleView(Context context,BannerItem bannerItem,int position);

    /**
     * 销毁View时会调用
     * @param pageView banner page view
     * @param position banner 位置
     */
    void destroyPageView(View pageView,int position);

    /**
     * 该方法,每次都会调用,pageView可能是缓存view.
     * @param pageView banner page view
     * @param bannerItem banner对象
     * @param position banner位置
     */
    void finishInstantiateItem(View pageView,BannerItem bannerItem,int position);
}