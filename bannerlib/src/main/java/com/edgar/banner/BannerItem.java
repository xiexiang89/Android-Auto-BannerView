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

package com.edgar.banner;

/**
 * Created by Edgar on 2016/12/2.
 */
public class BannerItem {
    private String bannerUrl;
    private CharSequence bannerTitle;
    private Object bannerTag;

    public BannerItem tag(Object bannerTag) {
        this.bannerTag = bannerTag;
        return this;
    }

    public BannerItem url(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        return this;
    }

    public BannerItem title(CharSequence bannerTitle){
        this.bannerTitle = bannerTitle;
        return this;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public Object getBannerTag() {
        return bannerTag;
    }

    public CharSequence getBannerTitle() {
        return bannerTitle;
    }
}