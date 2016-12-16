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

package com.edgar.transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Edgar on 2016/12/15.
 * 感谢https://github.com/daimajia/AndroidImageSlider
 */
public class FlipHorizontalTransformer extends BaseTransformer {
    @Override
    protected void onTransform(View page, float position) {
        final float rotation = 180f * position;
        ViewHelper.setAlpha(page,rotation > 90f || rotation < -90f ? 0 : 1);
        ViewHelper.setPivotY(page,page.getHeight()*0.5f);
        ViewHelper.setPivotX(page,page.getWidth() * 0.5f);
        ViewHelper.setRotationY(page,rotation);
    }
}
