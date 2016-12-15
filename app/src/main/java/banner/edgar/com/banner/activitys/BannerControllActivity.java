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
import android.view.View;
import android.widget.Button;

import banner.edgar.com.banner.R;

/**
 * Created by Edgar on 2016/12/16.
 */
public abstract class BannerControllActivity extends BaseBannerPageActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.banner;
    }
}