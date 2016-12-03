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

package banner.edgar.com.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.edgar.banner.BannerItem;
import com.edgar.banner.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Created by Edgar on 2016/12/3.
 */
public class PicassoImageLoader implements ImageLoader{

    @Override
    public void displayBannerImage(Context context,View pageView, BannerItem banner) {
        Picasso.with(context).load(banner.getBannerUrl()).placeholder(R.drawable.img_loading).error(R.drawable.img_loading)
                .into((ImageView) pageView);
    }
}