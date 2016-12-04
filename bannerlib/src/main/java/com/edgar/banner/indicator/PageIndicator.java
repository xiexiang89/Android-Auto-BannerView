/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edgar.banner.indicator;

import com.edgar.banner.LoopViewPager;

/**
 * A PageIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.
 */
public interface PageIndicator extends LoopViewPager.OnPageChangeListener {
    /**
     * Bind the indicator to a ViewPager.
     * @param view Should ViewPager
     */
    void setViewPager(LoopViewPager view);

    /**
     * Bind the indicator to a ViewPager.
     * @param view Should viewpager
     * @param initialPosition Initial position
     */
    void setViewPager(LoopViewPager view, int initialPosition);

    /**
     * <p>Set the current page of both the ViewPager and indicator.</p>
     *
     * <p>This <strong>must</strong> be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     * @param item Item index to select
     */
    void setCurrentItem(int item);

    /**
     * Set the currently selected page.
     *
     * @param item Item index to select
     * @param smoothScroll True to smoothly scroll to the new item, false to transition immediately
     */
    void setCurrentItem(int item, boolean smoothScroll);

    /**
     * Set a page change listener which will receive forwarded events.
     * @param listener Listener to set
     */
    void setOnPageChangeListener(LoopViewPager.OnPageChangeListener listener);

    /**
     * Set page count
     * @param pageCount Page count
     */
    void setPageCount(int pageCount);

    /**
     * Notify the indicator that the fragment list has changed.
     */
    void notifyDataSetChanged();
}
