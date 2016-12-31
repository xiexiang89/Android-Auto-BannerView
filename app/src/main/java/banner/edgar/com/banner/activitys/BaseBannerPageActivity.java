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
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.edgar.banner.BannerItem;
import com.edgar.banner.BannerPageViewAdapter;
import com.edgar.banner.BannerPagerView;
import com.edgar.banner.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import banner.edgar.com.banner.CustomBannerTitleView;
import banner.edgar.com.banner.R;
import banner.edgar.com.banner.ToastUtils;

/**
 * Created by Edgar on 2016/12/3.
 */
public abstract class BaseBannerPageActivity extends AppCompatActivity {

    protected static final List<BannerItem> BANNER_ITEMS = new ArrayList<>();
    static {
        addBannerItems(createBanner("多喝开水 至少暖在心里",
                "http://p3.music.126.net/KNdGeLq-J4XyK6PC1uDofQ==/18567452859744456.jpg"));
        addBannerItems(createBanner("28岁未成年",
                "http://p3.music.126.net/UUNCMv9sMW6nAjepzayA4A==/18686200115549047.jpg"));
        addBannerItems(createBanner("我们的航母编队！辽宁号航母战斗群靓照大曝光",
                "http://n.sinaimg.cn/mil/transform/20161205/7HQf-fxyipxf7604307.jpg"));
        addBannerItems(createBanner("中国空军司令确认研制轰20 制造完毕明年将首飞",
                "http://n.sinaimg.cn/mil/20161205/O97X-fxyiayr9093409.jpg"));
        addBannerItems(createBanner("90后特警美女教官走红：教男队员打枪格斗",
                "http://n.sinaimg.cn/mil/20161205/csIL-fxyiayr9137844.jpg"));
        addBannerItems(createBanner("空中弹药库！F-15与中国版苏30实战挂载对比",
                "http://img3.duitang.com/uploads/item/201212/19/20121219172711_y5Pk5.thumb.700_0.jpeg"));
        addBannerItems(createBanner("红豆词-忘不了新愁与旧愁",
                "http://p4.music.126.net/DXi1EXNhX1cfjiVUn3HtpA==/18651015743458158.jpg"));
    }

    private static BannerItem createBanner(CharSequence title,String url){
        return new BannerItem().title(title).url(url);
    }

    private static void addBannerItems(BannerItem bannerItem){
        BANNER_ITEMS.add(bannerItem);
    }

    protected BannerPagerView mBannerPagerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mBannerPagerView = (BannerPagerView) findViewById(R.id.banner_pager);

        mBannerPagerView.setBannerPagerAdapter(getBannerPageViewAdapter());
        mBannerPagerView.setImageLoader(getBannerImageLoader());
        mBannerPagerView.setOnBannerClickListener(new BannerPagerView.OnBannerClickListener() {
            @Override
            public void onBannerClick(BannerItem bannerItem) {
                ToastUtils.showToast(BaseBannerPageActivity.this, bannerItem.getBannerTitle());
            }
        });
        // 取屏幕尺寸的1/3
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int bannerHeight = displayMetrics.heightPixels / 3;
        mBannerPagerView.getLayoutParams().height = bannerHeight;
        mBannerPagerView.setBannerTitleView(new CustomBannerTitleView());
        mBannerPagerView.setBanner(BANNER_ITEMS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerPagerView.pauseAutoPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBannerPagerView.startAutoPlay();
    }

    protected abstract ImageLoader getBannerImageLoader();

    protected abstract BannerPageViewAdapter getBannerPageViewAdapter();

    @LayoutRes
    protected abstract int getLayoutId();
}