package component.shine.com.basemoudle;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import component.shine.com.basemoudle.utils.DaoManager;
import okhttp3.OkHttpClient;

/**
 * Created by cc
 * On 2019/6/19.
 * 测试用
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        setDatabase();
        //Stetho 测试用
        DaoManager.getInstance().init(this);
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }


}
