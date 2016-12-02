package banner.edgar.com.banner;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Edgar on 2016/12/3.
 */
public final class ToastUtils {

    private ToastUtils(){}

    public static void showToast(Context context,CharSequence message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}