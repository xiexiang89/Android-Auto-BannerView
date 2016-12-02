package banner.edgar.com.banner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.edgar.banner.BannerItem;
import com.edgar.banner.IBannerPageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
/**
 * Created by Edgar on 2016/12/3.
 */
public class FrescoBannerPageView implements IBannerPageView{
    @Override
    public View createPageView(Context context, BannerItem bannerItem, int position) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        simpleDraweeView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return simpleDraweeView;
    }

    @Override
    public View createTitleView(Context context, BannerItem bannerItem, int position) {
        return null;
    }

    @Override
    public void destroyPageView(View pageView, int position) {}

    @Override
    public void finishInstantiateItem(View pageView, BannerItem bannerItem, int position) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder().setUri(bannerItem.getBannerUrl())
                .build();
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) pageView;
        simpleDraweeView.setController(draweeController);
    }
}