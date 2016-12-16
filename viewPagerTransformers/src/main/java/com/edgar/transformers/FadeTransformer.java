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
public class FadeTransformer extends BaseTransformer {
    @Override
    protected void onTransform(View page, float position) {
        // Page is not an immediate sibling, just make transparent
        if(position < -1 || position > 1) {
            ViewHelper.setAlpha(page,0.6f);
        } else if (position <= 0 || position <= 1) {
            // Page is sibling to left or right
            // Calculate alpha.  Position is decimal in [-1,0] or [0,1]
            float alpha = (position <= 0) ? position + 1 : 1 - position;
            ViewHelper.setAlpha(page,alpha);
        } else if (position == 0) {
            // Page is active, make fully visible
            ViewHelper.setAlpha(page,1);
        }
    }
}
