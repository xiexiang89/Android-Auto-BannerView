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
 * Created by Edgar on 2016/12/14.
 */
public class DepthPageTransformer extends BaseTransformer {
    private static final float MIN_SCALE = 0.75f;

    @Override
    protected void onTransform(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setAlpha(view,0);
        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            ViewHelper.setAlpha(view,1);
            ViewHelper.setTranslationX(view,0);
            ViewHelper.setScaleX(view,1);
            ViewHelper.setScaleY(view,1);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            ViewHelper.setAlpha(view,1-position);

            // Counteract the default slide transition
            ViewHelper.setTranslationX(view,pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            ViewHelper.setScaleX(view,scaleFactor);
            ViewHelper.setScaleY(view,scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setAlpha(view,0);
        }
    }

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }
}
