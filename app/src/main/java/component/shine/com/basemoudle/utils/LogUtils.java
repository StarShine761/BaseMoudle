package component.shine.com.basemoudle.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>方便在发布应用的时候,屏蔽所有不必要的log输出</h1> <br>
 * (防止别人通过观察log输出了解我们代码的实现细节,同时可以减少内存消耗) <br>
 * 输出log到SD文件部分,需要权限:<uses-permission android:name=
 * "android.permission.WRITE_EXTERNAL_STORAGE"/>
 *
 * <pre>
 * # 功能说明
 * 		1. 自己控制log输出级别
 * 		2. 自动获取调用所在类名及方法名,不用手动写了
 * 		3. 写出Log日志到包名文件夹下或SD卡中
 * </pre>
 *
 * <pre>
 * 增加功能 根据flag的过滤,把一些关键日志实时发送出去,用一个专门的客户端接收,并进行log日志的处理,将处理结果写入到excel中, flag
 * 该flag动态生成,及通过mqtt发送设定当前状态 接受到mqtt消息时候,改变当前的状态,并把实时的状态保存到本地中
 * 平时应用开启的时候,会先从本地尝试读取该状态 需要发送的日志数据 error级别以上,或者是特殊的某个activity的日志
 * 整个工程的日志输出->LogUtils里的某个方法->flag控制 以mqtt的一个单独topic携带数据的形式,发送该日志文件.
 */
public class LogUtils {

    // --------------------推荐使用! 自动获取并打印java类名和方法名 推荐使用-----------------------

    public static void logV(String message) {
        if (mDebuggable <= LEVEL_VERBOSE) {
            Log.v(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
        }
    }

    /**
     * 在LogCat中,以调用该方法的所在的类为Tag, 打印 调用该方法所在的方法+要打印的信息 <br>
     * 例如,在MainActivity的onCreate()方法里调用LogUtils.logV("abc") <br>
     * 打印后的结果,就是 标签"MainActivity.java",消息"onCreate()--abc"
     *
     * @param message 要打印的信息
     */
    public static void logD(String message) {
        if (mDebuggable <= LEVEL_DEBUG) {
            filterLogToFile(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
            Log.d(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
        }
    }

    public static void logW(String message) {
        if (mDebuggable <= LEVEL_WARN) {
            filterLogToFile(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
            Log.w(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
        }
    }

    public static void logI(String message) {
        if (mDebuggable <= LEVEL_INFO) {
            filterLogToFile(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
            Log.i(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
        }
    }

    public static void logE(String message) {
        if (mDebuggable <= LEVEL_ERROR) {
            filterLogToFile(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
            Log.e(getJavaFileName(), getJavaMethodName() + "()--" + avoidNullString(message));
        }
    }

    // ---------------------------变量定义,不用看-------------------------

    /**
     * 日志输出级别NONE
     */
    private static final int LEVEL_NONE = 6;

    /**
     * 日志输出级别V
     */
    private static final int LEVEL_VERBOSE = 1;

    /**
     * 日志输出级别D
     */
    private static final int LEVEL_DEBUG = 2;

    /**
     * 日志输出级别I
     */
    private static final int LEVEL_INFO = 3;

    /**
     * 日志输出级别W
     */
    private static final int LEVEL_WARN = 4;

    /**
     * 日志输出级别E
     */
    private static final int LEVEL_ERROR = 5;

    // ------------------------配置日志输出级别和输出log到SD卡中---------------------------

    /**
     * 是否允许输出log(即在什么级别及其之上,才可以输出log)
     */
    private static int mDebuggable = LEVEL_DEBUG;

    // 想把什么标签的Log输出到SD卡文件,可以在这里设置
    private static void filterLogToFile(String TAG, String message) {
        if (TAG.contains("MQTTConnect.java")) {
            // LogToSDFile(message);
        }
    }

    private static void LogToNet(String logText) {

    }

    // -----------------------下面的可以不看,不影响正常使用该Util类-----------------------------------
    // ----------------------------------------------------------------------------------

    private LogUtils() {
    }

    /**
     * verbose 黑色
     *
     * <p>
     * 这个方法用来打印那些最为琐碎的,意义最小的日志信息.对应界别verbose,是android日志里面级别最低的一种.
     *
     * @param message
     */
    public static void v(String TAG, String message) {
        if (mDebuggable <= LEVEL_VERBOSE) {
            Log.v(TAG, avoidNullString(message));
        }
    }

    /**
     * debug 蓝色
     *
     * <p>
     * 这个方法用来打印一些调试信息,这些信息对你调试程序和分析问题应该是有帮助的.对应级别debug,比verbose高一级.
     *
     * @param message
     */
    public static void d(String TAG, String message) {
        if (mDebuggable <= LEVEL_DEBUG) {
            filterLogToFile(TAG, message);
            Log.d(TAG, avoidNullString(message));
        }
    }

    /**
     * info 绿色
     *
     * <p>
     * 这个方法用于打印一些比较重要的数据,这些数据应该是你非常想看到的,可以帮你分析用户行为的那种.对应级别info,比debug高一级.
     *
     * @param message
     */
    public static void i(String TAG, String message) {
        if (mDebuggable <= LEVEL_INFO) {
            filterLogToFile(TAG, message);
            Log.i(TAG, avoidNullString(message));
        }
    }

    /**
     * warn 橙色
     *
     * <p>
     * 这个方法用于打印一些警告信息,提示程序在这个地方可能会有潜在的风险,最好去修复一下这些出现警告的地方.对应级别warn,比info高一级.
     *
     * @param message
     */
    public static void w(String TAG, String message) {
        if (mDebuggable <= LEVEL_WARN) {
            filterLogToFile(TAG, message);
            Log.w(TAG, avoidNullString(message));
        }
    }

    /**
     * error 红色
     *
     * <p>
     * 这个方法用于打印程序中的错误信息,比如程序进入到了catch语句当中. <br>
     * 当有错误信息打印出来的时候,一般都代表你的程序出现严重问题了,必须尽快修复.对应级别error,比warn高一级.
     *
     * @param message
     */
    public static void e(String TAG, String message) {
        if (mDebuggable <= LEVEL_ERROR) {
            filterLogToFile(TAG, message);
            Log.e(TAG, avoidNullString(message));
        }
    }

    public static void e(String TAG, String message, Throwable cause) {
        if (mDebuggable <= LEVEL_ERROR) {
            filterLogToFile(TAG, message);
            Log.e(TAG, getJavaMethodName() + "()--" + avoidNullString(message), cause);
        }
    }

    // ------------------------------------------------------

    private static StackTraceElement[] stackTraceElements;

    private static String getJavaFileName() {
        stackTraceElements = new Throwable().getStackTrace();
        // stackTraceElements[2] 这个2与调用的距离有关
        return stackTraceElements[2].getFileName();
    }

    private static String getJavaFileName(int distance) {
        stackTraceElements = new Throwable().getStackTrace();
        // stackTraceElements[distance] 这个distance与调用的距离有关
        return stackTraceElements[distance].getFileName();
    }

    private static String getJavaMethodName() {
        stackTraceElements = new Throwable().getStackTrace();
        return stackTraceElements[2].getMethodName();
    }

    private static int getJavaCodeLineNumber() {
        stackTraceElements = new Throwable().getStackTrace();
        return stackTraceElements[2].getLineNumber();
    }

    public static String getCrashLogPath() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(Environment.getExternalStorageDirectory())
                .append(File.separator)
                .append("ToolCar")
                .append(File.separator)
                .append("Log")
                .append(File.separator)
                .append(getCurrentDate())
                .append(File.separator)
                .append(LOG_FILE_NAME);
        String logFilePath =pathBuilder.toString();
        File file = new File(logFilePath);
        if (!file.exists()) {
            FileUtils.createFile(logFilePath);
        }
        return pathBuilder.toString();
    }
    public static String getDeleteLogPath() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(Environment.getExternalStorageDirectory())
                .append(File.separator)
                .append("ToolCar")
                .append(File.separator)
                .append("Log");
        return pathBuilder.toString();
    }


    public static String getAppPath() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(Environment.getExternalStorageDirectory())
                .append(File.separator)
                .append("ToolCar");
        String logFilePath =pathBuilder.toString();
        File file = new File(logFilePath);
        if (!file.exists()) {
            FileUtils.createFile(logFilePath);
        }
        return pathBuilder.toString();
    }
    // ---------------------(把Log日志输出到SD卡中)在SD卡写删Log文件----------------------------------

    /**
     * 单独输出一条两条到SD卡可以直接调用该方法,否则建议最好不要直接调用该方法,而是通过在filterLogToFile()方法加入筛选条件来进行SD卡的Log输出,方便统一管理
     */
    public static void LogToSDFile(String message) {
        // 判断是否有SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        String path = getCrashLogPath();
        logToFile(path,  "\n" + getCurrentTime() + "  " + getJavaFileName(4) + "--" + message);
    }

    public static void LogExceptionToSD(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("Exception", sw.toString());
            LogToSDFile(sw.toString());

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    /**
     * 删除所有日志文件
     */
    public static void deleteSDLogFile() {
        deleteDir(getDeleteLogPath());
    }

    // ----------------帮助方法----------------------
    private static String LOG_FILE_NAME = "ToolCar.log";

    private static void logToFile(String path, String message) {
        try {
            FileWriter writer = new FileWriter(path, true);
            writer.write(avoidNullString(message) + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteLogFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    private static String avoidNullString(String string) {
        if (string == null) {
            return "null";
        }
        return string;
    }

    private static SimpleDateFormat formatter;
    private static Date curDate;
    private static String str;

    /**
     * 得到当前时间,年月日时分秒
     */
    private static String getCurrentTime() {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());// 获取当前时间
        str = formatter.format(curDate);
        return str;
    }

    /**
     * 得到当前时间,年月日时分秒
     */
    private static String getCurrentDate() {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDate = new Date(System.currentTimeMillis());// 获取当前时间
        str = formatter.format(curDate);
        return str;
    }
    // ---------------------------------

    /**
     * 方便定位到此方法从哪里一路被调用过来
     */
    public static void whoCalled() {
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace(); // 我自己加的,可以直接打印堆栈信息

            // 或者用这种方式,不过感觉不如上面的方便
            // for(StackTraceElement ste : e.getStackTrace())
            // System.out.println(ste.getClassName()+":"+ste.getLineNumber()+"
            // --"+ste.getMethodName()+"()");
        }
    }



    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
}
