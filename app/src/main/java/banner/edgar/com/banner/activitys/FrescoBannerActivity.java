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

package banner.edgar.com.banner.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.edgar.banner.IBannerPageView;
import com.edgar.banner.ImageLoader;
import com.facebook.drawee.backends.pipeline.Fresco;

import banner.edgar.com.banner.FrescoBannerPageView;

/**
 * Created by Edgar on 2016/12/3.
 */
public class FrescoBannerActivity extends BaseBannerPageActivity{

    private FrescoBannerPageView frescoBannerPageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fresco.initialize(getApplicationContext());
        frescoBannerPageView = new FrescoBannerPageView();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ImageLoader getBannerImageLoader() {
        return frescoBannerPageView;
    }

    @Override
    protected IBannerPageView getBannerPageView() {
        return frescoBannerPageView;
    }
}