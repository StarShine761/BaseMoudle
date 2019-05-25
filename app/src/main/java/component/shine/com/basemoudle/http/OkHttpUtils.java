package component.shine.com.basemoudle.http;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cc
 * On 2019/5/24.
 * okhttp工具类
 */
public class OkHttpUtils {

    private final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

    private static volatile OkHttpUtils mInstance;//单利引用
    private OkHttpClient mOkHttpClient;//okHttpClient 实例

    /**
     * 获取单例引用
     *
     * @return
     */
    public static OkHttpUtils init() {
        OkHttpUtils instance = mInstance;
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new OkHttpUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }


    /**
     * 初始化RequestManager
     */
    private OkHttpUtils() {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(new LoggingInterceptor())//添加日志拦截器，查看请求的url、参数、返回的结果
                .build();
    }


    /**
     * @param url
     * @param map
     * @param reqCallBack post 提交表单的形式
     */
    public void doPost(final String url, final Map<String, String> map, final ReqCallBack<String> reqCallBack) {
        //创建表单
        FormBody.Builder builder = new FormBody.Builder();

        if (map != null) {
            for (String key : map.keySet()) {
                //添加键值对
                builder.add(key, map.get(key));
            }
        }
        //完成表单的创建
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                reqCallBack.onError(e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //保险
                if (response != null & response.isSuccessful()) {
                    final String strJson = response.body().string();
                    reqCallBack.OnSuccess(strJson);

                }
            }
        });


    }


}
