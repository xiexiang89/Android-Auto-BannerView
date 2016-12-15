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

package com.edgar.transformers;

import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by Edgar on 2016/12/15.
 */
public final class PageTransformerFactory {

    private static final String TAG = "PageTransformerFactory";
    private static final String PACKAGE_NAME = "com.edgar.transformers";
    public static ViewPager.PageTransformer createPageTransformer(TransformerType transformerType){
        if (transformerType == null) return null;
        String className = PACKAGE_NAME+"."+ transformerType.name()+"Transformer";
        try {
            Class<?> clazz = Class.forName(className);
            return (ViewPager.PageTransformer) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            Log.e(TAG,String.format("Transformer class failed:%s",className),e);
        } catch (InstantiationException e) {
            Log.e(TAG,String.format("Instantiation error:%s",className),e);
        } catch (IllegalAccessException e) {
            Log.e(TAG,String.format("IllegalAccess error:%s",className),e);
        }
        return null;
    }


}