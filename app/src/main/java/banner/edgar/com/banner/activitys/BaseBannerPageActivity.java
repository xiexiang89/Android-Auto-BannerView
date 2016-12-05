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

    private static final List<BannerItem> BANNER_ITEMS = new ArrayList<>();
    static {
        addBannerItems(new BannerItem().title("001A航母卫星照曝光！与辽宁舰一个模子舰岛略小").url("http://n.sinaimg.cn/mil/20161205/V6Di-fxyiayr9101767.jpg"));
        addBannerItems(new BannerItem().title("我们的航母编队！辽宁号航母战斗群靓照大曝光").url("http://n.sinaimg.cn/mil/transform/20161205/7HQf-fxyipxf7604307.jpg"));
        addBannerItems(new BannerItem().title("中国空军司令确认研制轰20 制造完毕明年将首飞").url("http://n.sinaimg.cn/mil/20161205/O97X-fxyiayr9093409.jpg"));
        addBannerItems(new BannerItem().title("90后特警美女教官走红：教男队员打枪格斗").url("http://n.sinaimg.cn/mil/20161205/csIL-fxyiayr9137844.jpg"));
        addBannerItems(new BannerItem().title("空中弹药库！F-15与中国版苏30实战挂载对比").url("http://n.sinaimg.cn/mil/20161205/hMHP-fxyipxf7621257.jpg"));
        addBannerItems(new BannerItem().title("我又不是人民币，没必要所有人都喜欢").url("http://imgsize.ph.126.net/?imgurl=http://cms-bucket.nosdn.127.net/f4cf66965c514dcca50ca69ab4ad4f1220161202120101.png_600x250x1x85.jpg"));
    }

    private static void addBannerItems(BannerItem bannerItem){
        BANNER_ITEMS.add(bannerItem);
    }

    private BannerPagerView mBannerPagerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner);
        mBannerPagerView = (BannerPagerView) findViewById(R.id.banner_pager);
        final Button button = (Button) findViewById(R.id.banner_enable_auto_play);
        Button slowView = (Button) findViewById(R.id.slow);
        slowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBannerPagerView.setIntervalTime(8*1000);
            }
        });
        Button fastView = (Button) findViewById(R.id.fast);
        fastView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBannerPagerView.setIntervalTime(1200);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerPagerView.isEnableAutoPlay()){
                    mBannerPagerView.setEnableAutoPlay(false);
                    button.setText("启用自动播放");
                } else {
                    mBannerPagerView.setEnableAutoPlay(true);
                    button.setText("禁用自动播放");
                }
            }
        });
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
        int bannerHeight = displayMetrics.heightPixels / 4;
        mBannerPagerView.getLayoutParams().height = bannerHeight;
        mBannerPagerView.setBannerTitleView(new CustomBannerTitleView());
        mBannerPagerView.setBanner(BANNER_ITEMS);
    }

    protected abstract ImageLoader getBannerImageLoader();

    protected abstract BannerPageViewAdapter getBannerPageViewAdapter();
}