package component.shine.com.basemoudle.http;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


/**
 * Created by cc
 * On 2019/5/25.
 * 日志拦截器，查看请求的url、参数、返回的结果
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        //Chain 里包含了request和response
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String body = null;
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            body = buffer.readString(charset);
        }

        long t1 = System.nanoTime();//请求发起的时间
        Log.d(TAG,
                "请求url：" + request.url()
                        + "\n请求参数: " + body);
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        //不能直接使用response.body（）.string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，
        // 我们需要创建出一个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        Log.d(TAG,
                "响应code:" + response.code()
                        + "\n响应时间：" + (t2 - t1) / 1e6d + " ms"
                        + "\n响应url：" + response.request().url()
                        + "\n响应body：" + responseBody.string());

        return response;
    }
}
