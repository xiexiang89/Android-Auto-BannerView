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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edgar.banner.BannerItem;
import com.edgar.banner.BannerPagerView;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final List<BannerItem> BANNER_ITEMS = new ArrayList<>();
    static {
        addBannerItems(new BannerItem().title("Banner1").url("http://f.hiphotos.baidu.com/image/h%3D360/sign=e105b9f1d61b0ef473e89e58edc651a1/b151f8198618367a9f738e022a738bd4b21ce573.jpg"));
        addBannerItems(new BannerItem().title("Banner2").url("http://img0.imgtn.bdimg.com/it/u=2425082484,2187620716&fm=11&gp=0.jpg"));
        addBannerItems(new BannerItem().title("Banner3").url("http://img3.imgtn.bdimg.com/it/u=678632234,3415842991&fm=11&gp=0.jpg"));
        addBannerItems(new BannerItem().title("Banner4").url("http://img1.imgtn.bdimg.com/it/u=2007297771,1800118672&fm=11&gp=0.jpg"));
    }

    private static void addBannerItems(BannerItem bannerItem){
        BANNER_ITEMS.add(bannerItem);
    }

    private BannerPagerView mBannerPagerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mBannerPagerView = (BannerPagerView) findViewById(R.id.banner_pager);
        mBannerPagerView.setBannerPagerView(new FrescoBannerPageView());
        mBannerPagerView.setOnBannerClickListener(new BannerPagerView.OnBannerClickListener() {
            @Override
            public void onBannerClick(BannerItem bannerItem) {
                ToastUtils.showToast(MainActivity.this,bannerItem.getBannerTitle());
            }
        });
        mBannerPagerView.setBanner(BANNER_ITEMS);
    }
}