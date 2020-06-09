package component.shine.com.basemoudle.http;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier((hostname, session) -> true)
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

    /**
     * @param url
     * @param json
     * @param reqCallBack post 提交json的形式
     */
    public void doPostJson(final String url, final String json, final ReqCallBack<String> reqCallBack) {


        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);

        Request request = new Request.Builder()
                .post(requestBody)
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

    static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }



}
