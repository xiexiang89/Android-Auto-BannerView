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

import com.edgar.banner.BannerPageViewAdapter;
import com.edgar.banner.ImageLoader;

import banner.edgar.com.banner.PicassoImageLoader;

/**
 * Created by Edgar on 2016/12/3.
 */
public class PicassoBannerActivity extends BaseBannerPageActivity{

    private PicassoImageLoader picassoImageLoader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        picassoImageLoader = new PicassoImageLoader();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ImageLoader getBannerImageLoader() {
        return picassoImageLoader;
    }

    @Override
    protected BannerPageViewAdapter getBannerPageViewAdapter() {
        return null;
    }
}