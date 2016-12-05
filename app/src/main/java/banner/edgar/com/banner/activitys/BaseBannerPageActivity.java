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

import banner.edgar.com.banner.R;
import banner.edgar.com.banner.ToastUtils;

/**
 * Created by Edgar on 2016/12/3.
 */
public abstract class BaseBannerPageActivity extends AppCompatActivity {

    private static final List<BannerItem> BANNER_ITEMS = new ArrayList<>();
    static {
        addBannerItems(new BannerItem().title("Banner1").url("http://f.hiphotos.baidu.com/image/h%3D360/sign=e105b9f1d61b0ef473e89e58edc651a1/b151f8198618367a9f738e022a738bd4b21ce573.jpg"));
        addBannerItems(new BannerItem().title("Banner2").url("http://img0.imgtn.bdimg.com/it/u=2425082484,2187620716&fm=11&gp=0.jpg"));
        addBannerItems(new BannerItem().title("Banner3").url("http://img3.imgtn.bdimg.com/it/u=678632234,3415842991&fm=11&gp=0.jpg"));
        addBannerItems(new BannerItem().title("Banner4").url("http://img1.imgtn.bdimg.com/it/u=2007297771,1800118672&fm=11&gp=0.jpg"));
        addBannerItems(new BannerItem().title("Banner5").url("http://desk.fd.zol-img.com.cn/t_s960x600c5/g5/M00/02/03/ChMkJlbKxo2Icle3ABK7R4H2DZMAALHnABaAUIAErtf905.jpg"));
        addBannerItems(new BannerItem().title("Banner6").url("http://pic15.nipic.com/20110620/6465749_141657320145_2.jpg"));
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
        mBannerPagerView.setBanner(BANNER_ITEMS);
    }

    protected abstract ImageLoader getBannerImageLoader();

    protected abstract BannerPageViewAdapter getBannerPageViewAdapter();
}