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
 * Created by Edgar on 2016/12/16.
 */
public class CubeInTransformer extends BaseTransformer {
    @Override
    protected void onTransform(View page, float position) {
        // Rotate the fragment on the left or right edge
        ViewHelper.setPivotX(page,position > 0 ? 0 : page.getWidth());
        ViewHelper.setPivotY(page,0);
        ViewHelper.setRotationY(page,-90f * position);
    }

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }
}