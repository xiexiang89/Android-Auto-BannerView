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

import android.graphics.drawable.Drawable;

/**
 * Created by Edgar on 2017/5/7.
 */
public interface IndicatorAttributeSet {

    void setIndicatorGravity(int indicatorGravity);

    void setPointPadding(int pointPadding);

    void setPointDrawable(Drawable unSelectDrawable,Drawable selectDrawable);
}