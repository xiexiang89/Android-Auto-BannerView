
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

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.edgar.banner.indicator.BannerIndicator;
import com.edgar.banner.indicator.IndicatorAttributeSet;
import com.edgar.banner.indicator.IndicatorFactory;
import com.edgar.banner.indicator.IndicatorGravity;
import com.edgar.banner.indicator.IndicatorStyle;
import com.edgar.transformers.PageTransformerFactory;
import com.edgar.transformers.TransformerType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by edgar on 2016/1/6.
 * Banner pager
 * 修改: 删除属性,设计indicator,提供开放扩展,
 * 将标题和指示合并为indicator
 */
public class BannerPagerView extends FrameLayout {

    public interface OnBannerClickListener {
        void onBannerClick(BannerItem bannerItem);
    }

    private static final String TAG = BannerPagerView.class.getSimpleName();
    private final LayoutParams mIndicatorParams =
            new LayoutParams(MATCH_PARENT,WRAP_CONTENT);
    private static final int INIT_POSITION = 0;
    private static final long DEFAULT_DELAY_TIME = 8 * 10_00;

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
    private BannerIndicator mBannerIndicator;
    private ViewGroup mIndicatorView;
    private LoopViewPager mViewPage;
    private BannerPageAdapter mPageAdapter;

    private long mIntervalTime = DEFAULT_DELAY_TIME;
    //Banner flags
    private int mBannerFlags;
    private boolean mTouchDown;
    private final BannerPageListener mCarousePageListener = new BannerPageListener();

    private BannerPageViewAdapter mBannerPageAdapter;
    private ImageLoader mImageLoader;
    private Scroller mBannerScroller;
    private Field mScrollerField;
    private List<ViewPager.OnPageChangeListener> mOnPageChangeListeners;

    private Drawable mIndicatorBackground;
    private Drawable mUnSelectedDrawable;
    private Drawable mSelectedDrawable;
    private int mIndicatorStyle = IndicatorStyle.CIRCLE_INDICATOR;
    private int mIndicatorGravity = IndicatorGravity.CENTER;
    private int mPointPadding;

    private OnBannerClickListener mOnBannerClickListener;
    private final View.OnClickListener mBannerClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnBannerClickListener != null) {
                mOnBannerClickListener.onBannerClick((BannerItem) v.getTag());
            }
        }
    };

    private static final int LOOPER = 0x1000;
    private final Handler mBannerLooperHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOOPER:
                    if (isEnableAutoPlay() && !mTouchDown) {
                        updateViewPage();
                        sendEmptyMessageDelayed(LOOPER, mIntervalTime);
                    } else {
                        removeMessages(LOOPER);
                    }
                    Log.e(TAG, "Banner looper");
                    break;
            }
        }

        private void updateViewPage() {
            int currentItem = mViewPage.getCurrentItem();
            int newItem = currentItem + 1;
            mViewPage.setCurrentItem(newItem, true);
        }
    };

    public BannerPagerView(Context context) {
        super(context);
        init(null, 0);
    }

    public BannerPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BannerPagerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        Context context = getContext();
        Resources resource = context.getResources();
        LayoutInflater.from(getContext()).inflate(R.layout.banner_layout, this, true);
        mViewPage = (LoopViewPager) findViewById(R.id.carouse_viewpager);
        setBannerScroller(new BannerScroller(getContext()));
        mViewPage.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPage.setOnPageChangeListener(mCarousePageListener);
        mBannerPageAdapter = new DefaultBannerPageViewAdapter();
        mIndicatorParams.gravity = Gravity.BOTTOM;
        mPointPadding = resource.getDimensionPixelOffset(R.dimen.point_margin);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerPagerView, defStyle, 0);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.BannerPagerView_bannerAnimation){
                int transformerType = a.getInt(attr,0);
                setBannerPageTransformer(true, TransformerType.convert(transformerType));
            } else if (attr == R.styleable.BannerPagerView_enableAutoPlayer){
                setEnableAutoPlay(a.getBoolean(attr,false));
            } else if (attr == R.styleable.BannerPagerView_indicatorStyle){
                mIndicatorStyle = a.getInt(attr, IndicatorStyle.CIRCLE_INDICATOR);
            } else if (attr == R.styleable.BannerPagerView_bannerIndicatorLayout){
                int resId = a.getResourceId(attr,0);
                if (resId != 0){
                    ViewGroup indicatorView = (ViewGroup) LayoutInflater.from(context).inflate(resId,this,false);
                    if (!(indicatorView instanceof BannerIndicator)){
                        throw new IllegalArgumentException("Your indicator must implements BannerIndicator.");
                    }
                    mIndicatorView = indicatorView;
                    mBannerIndicator = (BannerIndicator) indicatorView;
                    addView(mIndicatorView,mIndicatorParams);
                }
            } else if (attr == R.styleable.BannerPagerView_unSelectDrawable){
                mUnSelectedDrawable = a.getDrawable(attr);
            } else if (attr == R.styleable.BannerPagerView_selectedDrawable){
                mSelectedDrawable = a.getDrawable(attr);
            } else if (attr == R.styleable.BannerPagerView_indicatorGravity){
                mIndicatorGravity = a.getInt(attr, Gravity.CENTER);
            } else if (attr == R.styleable.BannerPagerView_pointPadding){
                mPointPadding = a.getDimensionPixelOffset(attr,mPointPadding);
            } else if (attr == R.styleable.BannerPagerView_indicatorBackground){
                mIndicatorBackground = a.getDrawable(attr);
                if (mIndicatorBackground == null){
                    mIndicatorBackground = new ColorDrawable(resource.getColor(R.color.indicator_background));
                }
            }
        }

        if (mUnSelectedDrawable == null){
            mUnSelectedDrawable = ContextCompat.getDrawable(context,R.drawable.def_circle_normal_background);
        }
        if (mSelectedDrawable == null){
            mSelectedDrawable = ContextCompat.getDrawable(context,R.drawable.def_circle_selected_background);
        }
        if (mBannerIndicator == null){
            setIndicatorStyle(mIndicatorStyle);
        }

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewPage.getLayoutParams().height = getLayoutParams().height;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = new ArrayList<>();
        }
        mOnPageChangeListeners.add(listener);
    }

    public void removeOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) return;
        mOnPageChangeListeners.remove(listener);
    }

    private void dispatchOnPageScrolled(int position, float offset, int offsetPixels) {
        if (mOnPageChangeListeners != null) {
            for (int i = 0, z = mOnPageChangeListeners.size(); i < z; i++) {
                ViewPager.OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrolled(position, offset, offsetPixels);
                }
            }
        }
    }

    private void dispatchOnPageSelected(int position) {
        if (mOnPageChangeListeners != null) {
            for (int i = 0, z = mOnPageChangeListeners.size(); i < z; i++) {
                ViewPager.OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }
    }

    private void dispatchOnScrollStateChanged(int state) {
        if (mOnPageChangeListeners != null) {
            for (int i = 0, z = mOnPageChangeListeners.size(); i < z; i++) {
                ViewPager.OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrollStateChanged(state);
                }
            }
        }
    }

    /**
     * 设置自定义indicator,默认使用内部的LayoutParams
     * 如果想使用自定义LayoutParams,请调用{@link BannerPagerView#setCustomIndicator(BannerIndicator, LayoutParams)}
     * @param indicator see {@link BannerIndicator}
     */
    public void setCustomIndicator(BannerIndicator indicator){
        setCustomIndicator(indicator,mIndicatorParams);
    }

    /**
     * 设置自定义indicator,如果有特殊的样式,可以使用这个方法
     * xml方式请使用 bannerIndicatorLayout 属性
     * @param indicator see {@link BannerIndicator}
     * @param params Indicator params
     */
    public void setCustomIndicator(BannerIndicator indicator,LayoutParams params){
        if (indicator == null) return;
        if (mBannerIndicator != indicator){
            removeView(mIndicatorView);
            mBannerIndicator = indicator;
            mIndicatorView = indicator.getView();
            addView(mIndicatorView,params);
        }
    }

    /**
     * 设置指示条的位置
     * @param indicatorStyle see {@link IndicatorGravity}
     */
    public void setIndicatorStyle(int indicatorStyle){
        mBannerIndicator = IndicatorFactory.createBannerIndicator(getContext(),indicatorStyle);
        if (mBannerIndicator != null){
            mIndicatorView = mBannerIndicator.getView();
            addView(mIndicatorView,mIndicatorParams);
            applyIndicatorAttribute();
        }
    }

    private void applyIndicatorAttribute(){
        if (mIndicatorView instanceof IndicatorAttributeSet){
            IndicatorAttributeSet indicatorAttributeSet = (IndicatorAttributeSet) mIndicatorView;
            indicatorAttributeSet.setIndicatorGravity(mIndicatorGravity);
            indicatorAttributeSet.setPointDrawable(mUnSelectedDrawable,mSelectedDrawable);
            indicatorAttributeSet.setPointPadding(mPointPadding);
        }
    }

    /**
     * Set BannerPageTransformer type {@link BannerPagerView#setBannerPageTransformer(boolean, TransformerType)}
     *
     * @param transformerType PageTransformer that will modify each page's animation properties
     * @see TransformerType
     */
    public void setBannerPageTransformer(TransformerType transformerType) {
        setBannerPageTransformer(true, transformerType);
    }

    /**
     * Set BannerPageTransformer type {@link TransformerType}
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformerType     PageTransformer that will modify each page's animation properties
     */
    public void setBannerPageTransformer(boolean reverseDrawingOrder, TransformerType transformerType) {
        setBannerPageTransformer(reverseDrawingOrder, PageTransformerFactory.createPageTransformer(transformerType));
    }

    /**
     * Set a {@link ViewPager.PageTransformer} that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     * <p>
     * <p><em>Note:</em> Prior to Android 3.0 the property animation APIs did not exist.
     * As a result, setting a PageTransformer prior to Android 3.0 (API 11) will have no effect.</p>
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param pageTransformer     PageTransformer that will modify each page's animation properties
     */
    public void setBannerPageTransformer(boolean reverseDrawingOrder, final ViewPager.PageTransformer pageTransformer) {
        if (pageTransformer == null) return;
        mViewPage.setPageTransformer(reverseDrawingOrder, new LoopViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                pageTransformer.transformPage(page, position);
            }
        });
    }

    public void setBannerScroller(Scroller scroller) {
        try {
            //只获取一次字段
            if (mScrollerField == null){
                mScrollerField = LoopViewPager.class.getDeclaredField("mScroller");
                mScrollerField.setAccessible(true);
            }
            mScrollerField.set(mViewPage, scroller);
            mBannerScroller = scroller;
        } catch (Exception e) {
            Log.e(TAG, "Set ViewPager scroller fail", e);
        }
    }

    /**
     * If the default scroller is used, it will be valid
     *
     * @param duration Duration of the scroll in milliseconds.
     */
    public void setBannerScrollerDuration(int duration) {
        if (mBannerScroller instanceof BannerScroller) {
            ((BannerScroller) mBannerScroller).setDuration(duration);
        }
    }

    /**
     * This method only sets the animation interpolator, the internal use of BannerScroller
     *
     * @param interpolator {@link Interpolator}
     * @param duration     Duration of the scroll in milliseconds.
     */
    public void setBannerScrollerInterpolator(Interpolator interpolator, int duration) {
        BannerScroller scroller = new BannerScroller(getContext(), interpolator);
        if (duration > 0)
            scroller.setDuration(duration);
        setBannerScroller(scroller);
    }

//    /**
//     * <p>设置Banner title view,title view的位置取决与indicator view的位置.
//     * 如果indicator是在left,title会添加到right;反之,则添加到left.
//     * 如果你需要一个title,则必须设置bannerIndicatorGravity属性</p>
//     *
//     * @param bannerTitleView {@link BannerTitleView}
//     */
//    public void setBannerTitleView(BannerTitleView bannerTitleView) {
//        mBannerTitleView = bannerTitleView;
//        int layoutId = mBannerTitleView.getTitleLayoutId();
//        View titleView = LayoutInflater.from(getContext()).inflate(layoutId, mBottomLayout, false);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleView.getLayoutParams();
//        params.weight = 1;
//        params.gravity = Gravity.CENTER_VERTICAL;
//        bannerTitleView.onFinishInflater(titleView);
//        if (mIndicatorGravity == Gravity.LEFT) {
//            //indicator 如果是在left,则直接addView
//            mBottomLayout.addView(titleView);
//        } else if (mIndicatorGravity == Gravity.RIGHT) {
//            //indicator 如果是在right,就addView第一个index
//            mBottomLayout.addView(titleView, 0);
//        }
//    }

    public void setBannerPagerAdapter(BannerPageViewAdapter bannerPageAdapter) {
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

    public void setBanner(List<BannerItem> bannerItems) {
        if (bannerItems != null && bannerItems.size() > 0) {
            pauseAutoPlay();
            clearAllData();
            clearAllView();
            mBannerList.addAll(bannerItems);
            final int originCount = mBannerList.size();
            int bannerCount = originCount;
            mViewPage.setLoopEnable(true);
//            View indicator = (View) mBottomIndicator;
            mIndicatorView.setVisibility(VISIBLE);
            //之所以判断这个,是因为ViewPager是预加载,但是需要循环滚动,
            //so,在原有的数据项增加2倍,保证在后面的item不会加载失败.
            if (bannerCount < MI_BANNER_COUNT) {
                //banner 数量如果是1,那么就不让他循环.
                //自动轮播也会停止.
                if (bannerCount == 1) {
                    mIndicatorView.setVisibility(GONE);
                    mViewPage.setLoopEnable(false);
                } else {
                    bannerCount = 2 * bannerCount;
                }
            }
            mPageAdapter = new BannerPageAdapter();
            mPageAdapter.setBannerCount(bannerCount);
            mViewPage.setAdapter(mPageAdapter);
//            mBottomIndicator.setViewPager(mViewPage);
            //底部指示条需要以真实数据size为准
            mBannerIndicator.onBannerPageUpdate(originCount);
            mBannerIndicator.onBannerSelected(mBannerList.get(INIT_POSITION),INIT_POSITION);
            mViewPage.setCurrentItem(INIT_POSITION);
            startAutoPlay();
        }
    }

    private void clearAllData() {
        mBannerList.clear();
    }

    private void clearAllView() {
        mViewPage.removeAllViews();
        if (mPageAdapter != null) mPageAdapter.clearViewCache();
    }

    /**
     * set intervalTime.
     *
     * @param intervalTime Play interval time
     */
    public void setIntervalTime(long intervalTime) {
        this.mIntervalTime = intervalTime;
        if (isEnableAutoPlay()) {
            pauseAutoPlay();
            startAutoPlay();
        }
    }

    public void setEnableAutoPlay(boolean enableAutoPlay) {
        if (enableAutoPlay) {
            startAutoPlay();
        } else {
            pauseAutoPlay();
        }
    }

    public boolean isEnableAutoPlay() {
        return AUTO_PLAY_ENABLE == (mBannerFlags & AUTO_PLAY_MARK);
    }

    private boolean isBannerLessThanOne() {
        return mBannerList.size() <= 1;
    }

    private void setBannerFlags(int flags, int mark) {
        mBannerFlags = (mBannerFlags & ~mark) | (flags & mark);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //View离开窗体时,需要停止滚动
        destroy();
    }

    /**
     * Pause polling banner play
     * <p>If your page is not visible, it is recommended that you call this method to reduce unnecessary polling</p>
     */
    public void pauseAutoPlay() {
        setBannerFlags(AUTO_PLAY_DISABLE, AUTO_PLAY_MARK);
        mBannerLooperHandler.removeMessages(LOOPER);
    }

    /**
     * If Activity or Fragment is closed, you need to call this method, timely clear memory
     */
    public void destroy() {
        pauseAutoPlay();
        clearAllData();
        clearAllView();
    }

    /**
     * Start polling play banner
     */
    public void startAutoPlay() {
        if (isEnableAutoPlay()) return;
        if (isBannerLessThanOne()) return;
        if (mPageAdapter == null) return;
        Log.i(TAG,"Start auto play banner");
        setBannerFlags(AUTO_PLAY_ENABLE, AUTO_PLAY_MARK);
        mBannerLooperHandler.sendEmptyMessageDelayed(LOOPER, mIntervalTime);
    }

    private void downPauseAutoPlay(){
        mTouchDown = true;
        mBannerLooperHandler.removeMessages(LOOPER);
        Log.i(TAG, "Touch down Pause auto play.");
    }

    private void upResumeAutoPlay(){
        //检查用户是否启用自动播放
        mTouchDown = false;
        if (isEnableAutoPlay()){
            mBannerLooperHandler.sendEmptyMessageDelayed(LOOPER, mIntervalTime);
            Log.i(TAG,"Touch up resume auto play.");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                upResumeAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                downPauseAutoPlay();
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
                if (BuildConfig.DEBUG)
                    Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
                if (xDiff > mTouchSlop && xDiff * 0.5f > yDiff) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private class BannerPageListener implements LoopViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            dispatchOnPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if (checkIndexOutOfBounds(position)){
                mBannerIndicator.onBannerSelected(mBannerList.get(position),position);
                position = realPosition(position);
                dispatchOnPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            dispatchOnScrollStateChanged(state);
        }
    }

    private int realPosition(int position) {
        return position % mBannerList.size();
    }

    /**
     * 检查下标越界
     */
    private boolean checkIndexOutOfBounds(int position){
        return position >= 0 && position <= mPageAdapter.getCount() - 1;
    }

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
            Log.i(TAG, "destroyItem item position:" + position);
            if (checkIndexOutOfBounds(position)){
                View bannerView = mBannerViewCache.get(position);
                if (bannerView == null) return;
                container.removeView(bannerView);
                int realPosition = position % mBannerList.size();
                mBannerPageAdapter.destroyPageView(bannerView, realPosition);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View bannerView = null;
            Log.i(TAG, "instantiateItem item position:" + position);
            if (checkIndexOutOfBounds(position)) {
                int realPosition = realPosition(position);
                BannerItem bannerItem = mBannerList.get(realPosition);
                bannerView = mBannerViewCache.get(position);
                if (bannerView == null){
                    bannerView = mBannerPageAdapter.createPageView(getContext(),bannerItem,realPosition);
                    bannerView.setTag(bannerItem);
                    bannerView.setOnClickListener(mBannerClickListener);
                    mBannerViewCache.put(position,bannerView);
                }
                boolean addSuccess = false;
                try {
                    container.addView(bannerView);
                    addSuccess = true;
                } catch (Exception e) {
                    Log.e(TAG, "Banner view already exists parent", e);
                }
                if (addSuccess){
                    mBannerPageAdapter.finishInstantiateItem(bannerView, bannerItem, realPosition);
                    if (mImageLoader != null)
                        mImageLoader.displayBannerImage(getContext(), bannerView, bannerItem);
                }
            }
            return bannerView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private void clearViewCache() {
            mBannerViewCache.clear();
        }
    }
}