package component.shine.com.basemoudle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Timer;

import component.shine.com.basemoudle.http.OkHttpUtils;
import component.shine.com.basemoudle.http.ReqCallBack;
import component.shine.com.basemoudle.utils.DaoManager;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //测试git
        //测试啦
        DaoManager.getInstance().getDaoSession().getUserDao().insert(new User(null, "1", "2", "3", "4"));
        Timber.d("测试");


        OkHttpUtils.init().doPost("test", new HashMap<>(), new ReqCallBack<String>() {
            @Override
            public void OnSuccess(String result) {

            }

            @Override
            public void onError(String error) {

            }
        });


    }
}
