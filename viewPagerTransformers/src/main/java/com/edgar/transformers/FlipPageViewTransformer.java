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

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Edgar on 2016/12/15.
 * 感谢https://github.com/daimajia/AndroidImageSlider
 */
public class FlipPageViewTransformer extends BaseTransformer{

    private void setVisibility(View page, float position) {
        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }

    private void setTranslation(View view) {
        ViewGroup viewPager = (ViewGroup) view.getParent();
        int scroll = viewPager.getScrollX() - view.getLeft();
        ViewHelper.setTranslationX(view,scroll);
    }

    private void setSize(View view, float position, float percentage) {
        ViewHelper.setScaleX(view,(position != 0 && position != 1) ? percentage : 1);
        ViewHelper.setScaleY(view,(position != 0 && position != 1) ? percentage : 1);
    }

    private void setRotation(View view, float position, float percentage) {
        if (position > 0) {
            ViewHelper.setRotationY(view,-180 * (percentage + 1));
        } else {
            ViewHelper.setRotationY(view,180 * (percentage + 1));
        }
    }

    @Override
    protected void onTransform(View view, float position) {
        float percentage = 1 - Math.abs(position);
        if(Build.VERSION.SDK_INT >= 13){
            view.setCameraDistance(12000);
        }
        setVisibility(view, position);
        setTranslation(view);
        setSize(view, position, percentage);
        setRotation(view, position, percentage);
    }
}
