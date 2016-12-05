
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.edgar.banner.indicator.CirclePageIndicator;
import com.edgar.banner.indicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edgar on 2016/1/6.
 * Banner pager
 */
public class BannerPagerView extends FrameLayout {

    public interface OnBannerClickListener{
        void onBannerClick(BannerItem bannerItem);
    }

    private static final String TAG = BannerPagerView.class.getSimpleName();
    private static final int INIT_POSITION = 0;
    private static final long DEFAULT_DELAY_TIME = 10*10_00;
    //Banner auto loop play state
    private static final int STATE_STOP = 0x00000000;
    private static final int STATE_RUNNING = 0x00000001;
    private static final int STATE_MARK = 0x00000001;

    private static final int AUTO_PLAY_DISABLE = 0x00000020;
    private static final int AUTO_PLAY_ENABLE = 0x00000000;
    private static final int AUTO_PLAY_MARK = 0x00000020;

    private static final int INVALID_POINTER = -1;
    private static final int MAX_TOUCH_SLOP = 10;
    /**
     * 最大Banner数量,少于4条,将会在原有数据上放大2倍,保证加载更换的体验.
     */
    private static final int MI_BANNER_COUNT = 4;

    private float mLastMotionX;
    private float mLastMotionY;
    private int mTouchSlop = MAX_TOUCH_SLOP;
    private int mActivePointerId = INVALID_POINTER;

    /**
     * Banner list
     */
    private final List<BannerItem> mBannerList = new ArrayList<>();
    private CirclePageIndicator mBottomIndicator;
    private LoopViewPager mViewPage;
    private BannerPageAdapter mPageAdapter;

    private long mIntervalTime = DEFAULT_DELAY_TIME;
    //Banner flags
    private int mBannerFlags;
    private final BannerPageListener mCarousePageListener = new BannerPageListener();
    private HandlerThread mLooperThread;
    private Handler mLooperHandler;

    private BannerPageViewAdapter mBannerPageAdapter;
    private ImageLoader mImageLoader;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    private OnBannerClickListener mOnBannerClickListener;
    private final View.OnClickListener mBannerClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnBannerClickListener != null){
                mOnBannerClickListener.onBannerClick((BannerItem) v.getTag());
            }
        }
    };

    private final Runnable mBannerUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (isEnableAutoPlay() && isBannerLoopRunning()){
                updateViewPage();
                mLooperHandler.postDelayed(mBannerLooperRunnable,mIntervalTime);
            }
        }

        private void updateViewPage(){
            int currentItem = mViewPage.getCurrentItem();
            int newItem = currentItem+1;
            mViewPage.setCurrentItem(newItem,true);
        }
    };
    private final Runnable mBannerLooperRunnable = new Runnable() {
        @Override
        public void run() {
            if (isEnableAutoPlay() && isBannerLoopRunning()){
                post(mBannerUpdateRunnable);
            }
        }
    };

    public BannerPagerView(Context context) {
        super(context);
        init(null,0);
    }

    public BannerPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public BannerPagerView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        init(attrs,defStyle);
    }

    private void init(AttributeSet attrs,int defStyle){
        Context context = getContext();
        Resources resources = getResources();
        LayoutInflater.from(getContext()).inflate(R.layout.banner_layout,this,true);
        mBottomIndicator = (CirclePageIndicator) findViewById(R.id.indicator_view);
        mViewPage = (LoopViewPager) findViewById(R.id.carouse_viewpager);
        mViewPage.setScroller(new BannerScroller(getContext()));
        mViewPage.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mBottomIndicator.setOnPageChangeListener(mCarousePageListener);
        mBannerPageAdapter = new DefaultBannerPageViewAdapter();

        final float defaultSelectRadius = resources.getDimension(R.dimen.default_circle_selector_radius);
        final float defaultNormalRadius = resources.getDimension(R.dimen.default_circle_normal_radius);
        final int defaultPageColor = resources.getColor(R.color.default_circle_indicator_page_color);
        final int defaultFillColor = resources.getColor(R.color.default_circle_indicator_fill_color);
        final int defaultStrokeColor = resources.getColor(R.color.default_circle_indicator_stroke_color);
        final float defaultStrokeWidth = resources.getDimension(R.dimen.default_circle_indicator_stroke_width);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerPagerView, defStyle, 0);
        float indicatorSelectorRadius = a.getDimension(R.styleable.BannerPagerView_bannerIndicatorSelectorRadius,
                defaultSelectRadius);
        float indicatorNormalRadius = a.getDimension(R.styleable.BannerPagerView_bannerIndicatorNormalRadius,
                defaultNormalRadius);
        int indicatorSelectorColor = a.getColor(R.styleable.BannerPagerView_bannerIndicatorSelectorColor,defaultFillColor);
        int indicatorNormalColor = a.getColor(R.styleable.BannerPagerView_bannerIndicatorNormalColor,defaultPageColor);
        int strokeColor = a.getColor(R.styleable.BannerPagerView_bannerIndicatorStrokeColor,defaultStrokeColor);
        float strokeWidth = a.getDimension(R.styleable.BannerPagerView_bannerIndicatorStrokeWidth,defaultStrokeWidth);
        boolean enableAutoPlay = a.getBoolean(R.styleable.BannerPagerView_enableAutoPlayer,false);

        mBottomIndicator.setSelectedRadius(indicatorSelectorRadius);
        mBottomIndicator.setNormalRadius(indicatorNormalRadius);
        mBottomIndicator.setFillColor(indicatorSelectorColor);
        mBottomIndicator.setPageColor(indicatorNormalColor);
        mBottomIndicator.setStrokeColor(strokeColor);
        mBottomIndicator.setStrokeWidth(strokeWidth);
        setEnableAutoPlay(enableAutoPlay);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewPage.getLayoutParams().height = getLayoutParams().height;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        mOnPageChangeListener = listener;
    }

    public void setBannerPagerAdapter(BannerPageViewAdapter bannerPageAdapter){
        if (bannerPageAdapter == null) return;
        mBannerPageAdapter = bannerPageAdapter;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setBanner(List<BannerItem> bannerItems){
        if (bannerItems != null && bannerItems.size() > 0){
            pauseAutoPlay();
            clearAllData();
            clearAllView();
            mBannerList.addAll(bannerItems);
            final int originCount = mBannerList.size();
            int bannerCount = originCount;
            mViewPage.setLoopEnable(true);
            View indicator = (View) mBottomIndicator;
            indicator.setVisibility(View.VISIBLE);
            //之所以判断这个,是因为ViewPager是预加载,但是需要循环滚动,
            //so,在原有的数据项增加2倍,保证在后面的item不会加载失败.
            if (bannerCount < MI_BANNER_COUNT){
                //banner 数量如果是1,那么就不让他循环.
                //自动轮播也会停止.
                if (bannerCount == 1){
                    indicator.setVisibility(GONE);
                    mViewPage.setLoopEnable(false);
                } else {
                    bannerCount = 2*bannerCount;
                }
            }
            mPageAdapter = new BannerPageAdapter();
            mPageAdapter.setBannerCount(bannerCount);
            mViewPage.setAdapter(mPageAdapter);
            mBottomIndicator.setViewPager(mViewPage);
            //底部指示条需要以真实数据size为准
            mBottomIndicator.setPageCount(originCount);
            mBottomIndicator.setCurrentItem(INIT_POSITION);
            startAutoPlay();
        }
    }

    private void clearAllData(){
        mBannerList.clear();
    }

    private void clearAllView(){
        mViewPage.removeAllViews();
        if (mPageAdapter != null) mPageAdapter.clearViewCache();
    }

    /**
     * set intervalTime.
     * @param intervalTime Play interval time
     */
    public void setIntervalTime(long intervalTime){
        this.mIntervalTime = intervalTime;
        if (isEnableAutoPlay()){
            pauseAutoPlay();
            startAutoPlay();
        }
    }

    public void setEnableAutoPlay(boolean enableAutoPlay){
        setBannerFlags(enableAutoPlay ? AUTO_PLAY_ENABLE:AUTO_PLAY_DISABLE,AUTO_PLAY_MARK);
        if (enableAutoPlay){
            startAutoPlay();
        } else {
            pauseAutoPlay();
        }
    }

    public boolean isEnableAutoPlay(){
        return AUTO_PLAY_ENABLE == (mBannerFlags & AUTO_PLAY_MARK);
    }

    private boolean isBannerLessThanOne(){
        return mBannerList.size() <= 1;
    }

    private boolean isBannerLoopRunning(){
        return (mBannerFlags & STATE_RUNNING) == STATE_RUNNING;
    }

    private void setBannerFlags(int flags, int mark){
        mBannerFlags = (mBannerFlags & ~mark) | (flags & mark);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerScreenReceiver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(mScreenReceiver);
        //View离开窗体时,需要停止滚动
        stopAutoPlay();
    }

    private void registerScreenReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        getContext().registerReceiver(mScreenReceiver, intentFilter);
    }

    public void pauseAutoPlay(){
        setBannerFlags(STATE_STOP,STATE_MARK);
        if (mLooperHandler != null){
            mLooperHandler.removeCallbacks(mBannerLooperRunnable);
            removeCallbacks(mBannerUpdateRunnable);
        }
    }

    /**
     * 停止轮询播放Banner
     */
    public void stopAutoPlay(){
        pauseAutoPlay();
        if (mLooperThread != null){
            mLooperThread.quit();
        }
        clearAllData();
        clearAllView();
    }

    /**
     * 开始轮询播放Banner
     */
    public void startAutoPlay(){
        if (!isEnableAutoPlay()) return;
        if (isBannerLoopRunning()) return;
        if (isBannerLessThanOne()) return;
        if(mPageAdapter == null) return;
        if (mLooperThread == null){
            mLooperThread = new HandlerThread(TAG);
            mLooperThread.start();
            mLooperHandler = new Handler(mLooperThread.getLooper());
        }
        mLooperHandler.postDelayed(mBannerLooperRunnable,mIntervalTime);
        setBannerFlags(STATE_RUNNING,STATE_MARK);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                pauseAutoPlay();
                Log.i(TAG,"Pause carousel.");
                break;
            case MotionEvent.ACTION_MOVE:
                if (isBannerLessThanOne()) break;
                //代码摘自ViewPager.
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = Math.abs(y - mLastMotionY);
                if (BuildConfig.DEBUG) Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
                if (xDiff > mTouchSlop && xDiff * 0.5f > yDiff) {
                    ViewParent parent = getParent();
                    if (parent != null){
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startAutoPlay();
                Log.i(TAG,"Resume carousel");
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private class BannerPageListener implements LoopViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mOnPageChangeListener != null){
                mOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mOnPageChangeListener != null){
                mOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_IDLE){
                startAutoPlay();
            }
            if (mOnPageChangeListener != null){
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_SCREEN_ON.equals(action)){
                pauseAutoPlay();
                startAutoPlay();
            } else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                pauseAutoPlay();
            }
        }
    };

    private class BannerPageAdapter extends PagerAdapter {
        private SparseArray<View> mBannerViewCache = new SparseArray<>();
        private int mBannerCount;

        private void setBannerCount(int bannerCount) {
            this.mBannerCount = bannerCount;
        }

        @Override
        public int getCount() {
            return mBannerCount;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG,"destroyItem item position:"+position);
            if (object instanceof View){
                View bannerView = (View) object;
                container.removeView(bannerView);
                int realPosition = position % mBannerList.size();
                mBannerPageAdapter.destroyPageView(bannerView,realPosition);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View bannerView = null;
            Log.i(TAG,"instantiateItem item position:"+position);
            if(position >= 0 && position <= getCount()-1){
                int realPosition = position % mBannerList.size();
                BannerItem bannerItem = mBannerList.get(realPosition);
                bannerView = mBannerViewCache.get(position);
                if (bannerView == null){
                    bannerView = mBannerPageAdapter.createPageView(getContext(),bannerItem,realPosition);
                    bannerView.setTag(bannerItem);
                    bannerView.setOnClickListener(mBannerClickListener);
                    mBannerViewCache.put(position,bannerView);
                }
                if (bannerView.getParent() != null){
                    ViewGroup viewGroup = (ViewGroup)bannerView.getParent();
                    viewGroup.removeView(bannerView);
                }
                try {
                    container.addView(bannerView);
                } catch (Exception e){
                    Log.e(TAG,"Banner view already exists parent",e);
                }
                mBannerPageAdapter.finishInstantiateItem(bannerView,bannerItem,realPosition);
                if (mImageLoader != null) mImageLoader.displayBannerImage(getContext(),bannerView,bannerItem);
            }
            return bannerView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private void clearViewCache(){
            mBannerViewCache.clear();
        }
    }
}