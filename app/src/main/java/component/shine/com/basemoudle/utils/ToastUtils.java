package component.shine.com.basemoudle.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 * 吐司工具类
 */
public class ToastUtils {


    private static Handler sMainThreadHandler;
    private static Toast mToast;

    public static Handler getMainThreadHandler() {
        if (sMainThreadHandler == null) {
            synchronized (ToastUtils.class) {
                if (sMainThreadHandler == null) {
                    sMainThreadHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sMainThreadHandler;
    }


    public static void showToast(final Context context, final String message, final int duration) {
        if (mToast != null) {
            mToast.cancel();
        }

        getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                mToast = Toast.makeText(context.getApplicationContext(), message, duration);
                mToast.show();
            }
        });
    }

    /**
     * @param message 长时间
     */
    public static void showToastLong(final Context context, final String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    /**
     * @param message 短时间
     */
    public static void showToastShort(final Context context, final String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }


}
