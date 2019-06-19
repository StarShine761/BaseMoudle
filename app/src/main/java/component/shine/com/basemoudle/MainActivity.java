package component.shine.com.basemoudle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import component.shine.com.basemoudle.utils.DaoManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //测试git
        //测试啦
        DaoManager.getInstance().getDaoSession().getUserDao().insert(new User(null, "1", "2", "3", "4"));
    }
}
