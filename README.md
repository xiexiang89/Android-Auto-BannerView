#Banner
Android平台的轮播控件,使用方便,可以任意使用一种图片加载;
ImageLoader: 主要是负责Banner图片加载,内部没有任何实现,开发需要实现它来调用图片加载库加载Banner图;
IBannerPagerView: 负责自定义BannerView, 如果没有自定义，内部有一个默认的实现,默认是只创建ImageView，Banner标题暂未实现;

使用方式:
```
xml布局:
    <com.edgar.banner.BannerPagerView
        android:id="@+id/banner_pager"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

Java代码:
构建BannerItem对象:
   BannerItem banner = new  BannerItem().title("Banner2").url("http://img0.imgtn.bdimg.com/it/u=2425082484,2187620716&fm=11&gp=0.jpg")
List<BannerItem> banners = new ArrayList<BannerItem>();
banners.add(banner);

BannerPagerView bannerPagerView = (BannerPagerView) findViewById(R.id.banner_pager);
bannerPagerView.setBannerPagerView(new CustomBannerPagerView()); //自定义BannerItemView
bannerPagerView.setImageLoader(new PicassoImageLoader()); //设置一个ImageLoader,用于加载图片
bannerPagerView.setBanner(banners);
```