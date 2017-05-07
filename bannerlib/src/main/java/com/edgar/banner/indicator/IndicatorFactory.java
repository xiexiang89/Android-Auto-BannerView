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

/**
 * Created by Edgar on 2017/5/1.
 */
public final class IndicatorFactory {

    private IndicatorFactory(){}

    public static BannerIndicator createBannerIndicator(Context context,int indicatorStyle){
        if (indicatorStyle == IndicatorStyle.CIRCLE_INDICATOR){
            return new CircleIndicator(context);
        } else if (indicatorStyle == IndicatorStyle.CIRCLE_TITLE_INDICATOR){
            return new CircleTitleIndicator(context);
        } else if (indicatorStyle == IndicatorStyle.TITLE_INDICATOR){
            return new TitleIndicator(context);
        } else {
            return null;
        }
    }
}