package component.shine.com.basemoudle.http;

import android.content.Context;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import component.shine.com.basemoudle.utils.LogUtils;
import fi.iki.elonen.NanoHTTPD;


/**
 * Created by cc
 * On 2019/6/18.
 * 依赖于    implementation 'org.nanohttpd:nanohttpd:2.2.0'
 * http server端 轻量级
 */
public class WebServer extends NanoHTTPD {
    private static final String TAG = "WebServer";


    private final static int PORT = 8080;
    private Context _mainContext;

    /*
    主构造函数，也用来启动http服务
    */
    public WebServer(Context context) throws IOException {
        super(PORT);
        _mainContext = context;
        start();
        LogUtils.e(TAG, "web server is Running");
    }

    /*
    解析的主入口函数，所有请求从这里进，也从这里出
    */
    @Override
    public Response serve(IHTTPSession session) {

        Map<String, String> files = new HashMap<String, String>();
        Method method = session.getMethod();
        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException ioe) {
                return newFixedLengthResponse("</body></html>");
            } catch (ResponseException re) {
                return newFixedLengthResponse("</body></html>");
            }
        }
        // get the POST body
        String postBody = session.getQueryParameterString();
        // or you can access the POST request's parameters

        String postParameter = files.get("postData");//接收的json数据
        LogUtils.e(TAG, "recive msg ===" + postParameter);


//        return newFixedLengthResponse(Response.Status.OK,NanoHTTPD.MIME_PLAINTEXT,"hhh");

//        byte[] resBytes = new byte[] {(byte)0x00, (byte)0x64, (byte)0xFF, (byte)0xFF, (byte)0x30, (byte)0x14, (byte)0x01, (byte)0xBB, (byte)0xB6, (byte)0xD3, (byte)0xAD, (byte)0xB9, (byte)0xE2, (byte)0xC1, (byte)0xD9, (byte)0x2C, (byte)0xC7, (byte)0xEB, (byte)0xC8, (byte)0xEB, (byte)0xB3, (byte)0xA1, (byte)0xCD, (byte)0xA3, (byte)0xB3, (byte)0xB5, (byte)0xA9, (byte)0x36};
        byte[] resBytes = new byte[]{(byte) 0x00, (byte) 0x64, (byte) 0xFF, (byte) 0xFF, (byte) 0x3F, (byte) 0x10, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x20, (byte) 0x77, (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64, (byte) 0x21, (byte) 0xEA, (byte) 0x28};

        String dataStr = android.util.Base64.encodeToString(resBytes, android.util.Base64.DEFAULT);


        return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "{" +
                "\"Response_AlarmInfoPlate\": {" +
                "\"info\": \"ok\"," +
                "\"content\": \"retransfer_stop\"," +
                "\"is_pay\": \"true\"," +
                "\"serialData\": [" +
                "{" +
                "\"serialChannel\": 1," +
                "\"data\": \"" + dataStr + "\"," +
                "\"dataLen\": " + resBytes.length +
                "}" +
                "," +
                "{" +
                "\"serialChannel\": 1," +
                "\"data\": \"00 64 FF FF 3F 10 40 00 00 00 48 65 6C 6C 6F 20 77 6F 72 6C 64 21 EA 28\"," +
                "\"dataLen\": 24" +
                "}" +
                "]" +
                "}" +
                "}");
    }

}
