package component.shine.com.basemoudle.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Process;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import component.shine.com.basemoudle.MainActivity;

/**
 * 异常崩溃处理
 *
 * 需在程序的入口进行初始化
 *          CrashHandler.getInstance().init(getCarLockContext());
 *          Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
 *
 *
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultHanlder;
    // 程序的Context对象
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return CrashHandlerHolder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        defaultHanlder = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleCrash(e)) {
            defaultHanlder.uncaughtException(t, e);
        } else {
            //TODO:仅测试时开放
          defaultHanlder.uncaughtException(t, e);
            try {
                Thread.sleep(1000);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleCrash(Throwable e) {
        if (null == e) return false;
        StringBuilder builder = new StringBuilder();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (null != cause) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String msg = writer.toString();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        builder.append(time)
                .append("#")
                .append(msg);

        saveErrorFile(builder.toString());

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        return true;
    }

    private void saveErrorFile(String msg) {
//        String logFilePath = LogUtils.getCrashLogPath();
//        File file = new File(logFilePath);
//        if (!file.exists()) {
//            FileUtils.createFile(logFilePath);
//        } else {
//            if (file.length() >= 5 * 1024 * 1024) {//大于5M就删除旧文件，重新生成新文件，一般不会超出大小
//                file.delete();
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        BufferedWriter out = null;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
//            out.write(msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private final static class CrashHandlerHolder {
        private final static CrashHandler INSTANCE = new CrashHandler();
    }
}
