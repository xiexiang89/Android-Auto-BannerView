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

banner的属性:
        app:bannerIndicatorNormalColor="@android:color/white"    //设置指示条圆点的默认颜色
        app:bannerIndicatorSelectorColor="#04c05a"   //设置指示条圆点的选择颜色
        app:bannerIndicatorNormalRadius="1.5dp"      //设置指示条圆点的默认半径大小
        app:bannerIndicatorSelectorRadius="2dp"      //设置指示条圆点的选择半径大小
        app:bannerIndicatorStrokeColor="@android:color/transparent"  //设置指示条的描边，如果bannerIndicatorNormalColor属性设置为透明，建议设置下该属性，体验会更加。只作用在默认的时候。
        app:bannerBottomBackground="#850d0d0d"    //banner底部有个指示条，设置指示条的背景
        app:indicatorPaddingBottom="10dp"         //指示条的底部边距，可以调整高度。
        app:indicatorPaddingTop="10dp"            //指示条的顶部边距，可以调整高度
        app:bannerIndicatorGravity="right"        //指示条的位置，如果该属性设置为center，那么banner title将不起作用
        app:enableAutoPlayer="true"               //是否开启自动轮播。
```
gradle中使用:
抱歉，因为该项目一直未添加到公用仓库，maven引用如下
请在你的module下的gradle.build文件中添加以下代码:
repositories {
    maven {
        url 'https://dl.bintray.com/edgar/maven/'
    }
}

dependencies{
    compile 'com.edgar.android:bannerlib:1.0.3'
}