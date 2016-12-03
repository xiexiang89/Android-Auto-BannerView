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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.edgar.banner.BannerItem;
import com.edgar.banner.BannerPagerView;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import banner.edgar.com.banner.FrescoBannerPageView;
import banner.edgar.com.banner.R;
import banner.edgar.com.banner.ToastUtils;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITYS_NAME = "banner.edgar.com.banner.activitys.%sActivity";
    private BannerPagerView mBannerPagerView;
    private ListView mSampleListView;
    private ArrayAdapter<String> mSampleListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSampleListView = (ListView) findViewById(R.id.listview);
        String[] array = getResources().getStringArray(R.array.banner_demo);
        mSampleListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
        mSampleListView.setAdapter(mSampleListAdapter);
        mSampleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Class<?> clazz = getClassLoader().loadClass(generateActivityClassName(mSampleListAdapter.getItem(position)));
                    Intent intent = new Intent(MainActivity.this,clazz);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
//        mBannerPagerView = (BannerPagerView) findViewById(R.id.banner_pager);
//        mBannerPagerView.setBannerPagerView(new FrescoBannerPageView());
//        mBannerPagerView.setOnBannerClickListener(new BannerPagerView.OnBannerClickListener() {
//            @Override
//            public void onBannerClick(BannerItem bannerItem) {
//                ToastUtils.showToast(MainActivity.this,bannerItem.getBannerTitle());
//            }
//        });
//        mBannerPagerView.setBanner(BANNER_ITEMS);
    }

    private static String generateActivityClassName(String prefixName){
        return String.format(ACTIVITYS_NAME,prefixName);
    }
}