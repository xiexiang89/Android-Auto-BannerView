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
import android.view.ViewGroup;

import com.edgar.banner.BannerItem;
import com.edgar.banner.IBannerPageView;
import com.edgar.banner.ImageLoader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
/**
 * Created by Edgar on 2016/12/3.
 */
public class FrescoBannerPageView implements IBannerPageView,ImageLoader{
    @Override
    public View createPageView(Context context, BannerItem bannerItem, int position) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        simpleDraweeView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return simpleDraweeView;
    }

    @Override
    public View createTitleView(Context context, BannerItem bannerItem, int position) {
        return null;
    }

    @Override
    public void destroyPageView(View pageView, int position) {}

    @Override
    public void finishInstantiateItem(View pageView, BannerItem bannerItem, int position) {}

    @Override
    public void displayBannerImage(Context context, View view, BannerItem banner) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder().setUri(banner.getBannerUrl())
                .build();
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view;
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(R.drawable.img_loading)
                .setFailureImage(R.drawable.img_loading)
                .build();
        draweeController.setHierarchy(hierarchy);
        simpleDraweeView.setController(draweeController);
    }
}