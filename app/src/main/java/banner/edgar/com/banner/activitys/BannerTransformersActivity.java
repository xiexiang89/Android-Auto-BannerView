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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.edgar.banner.BannerPageViewAdapter;
import com.edgar.banner.ImageLoader;
import com.edgar.transformers.TransformerType;
import com.facebook.drawee.backends.pipeline.Fresco;

import banner.edgar.com.banner.FrescoBannerPageView;
import banner.edgar.com.banner.PicassoImageLoader;
import banner.edgar.com.banner.R;

/**
 * Created by Edgar on 2016/12/16.
 */
public class BannerTransformersActivity extends BaseBannerPageActivity {
    private FrescoBannerPageView frescoBannerPageView;
    private ListView mTransformerListView;
    private ArrayAdapter<TransformerType> mTransformerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fresco.initialize(getApplicationContext());
        frescoBannerPageView = new FrescoBannerPageView();
        super.onCreate(savedInstanceState);
        mTransformerListView = (ListView) findViewById(R.id.listview);
        mTransformerAdapter = new ArrayAdapter<TransformerType>(this,android.R.layout.simple_list_item_1,TransformerType.values());
        mTransformerListView.setAdapter(mTransformerAdapter);
        mTransformerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBannerPagerView.setBannerPageTransformer(true,mTransformerAdapter.getItem(position));
            }
        });
    }

    @Override
    protected ImageLoader getBannerImageLoader() {
        return frescoBannerPageView;
    }

    @Override
    protected BannerPageViewAdapter getBannerPageViewAdapter() {
        return frescoBannerPageView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.banner_transformer;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBannerPagerView.startAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerPagerView.pauseAutoPlay();
    }
}
