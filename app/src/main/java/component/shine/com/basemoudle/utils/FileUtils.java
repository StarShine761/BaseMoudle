package component.shine.com.basemoudle.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */

public class FileUtils {

    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        return file.exists();
    }

    public static boolean createFile(String path) {
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        if (file.isDirectory()) {
            if (file.exists()) {
                file.delete();
            }
            return file.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FileUtils", "create file error :" + e.getMessage());
        }
        return false;
    }

    public static boolean createFileNotExist(String path) {
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        if (file.isDirectory()) {
            if (file.exists()) {
                return true;
            }
            return file.mkdirs();
        }
        if (file.exists()) {
            return true;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FileUtils", "create file error :" + e.getMessage());
        }
        return false;
    }

    public static List<File> getFileList(String path) {
        if (TextUtils.isEmpty(path)) return null;
        File file = new File(path);
        if (!file.exists()) return null;
        List<File> files = new ArrayList<>();
        if (file.isFile()) {
            files.add(file);
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (null != listFiles && listFiles.length > 0) {
                for (File file1 : listFiles) {
                    files.add(file1);
                }
            }
        }
        return files;
    }

    public static void deleteFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFiles(f);
            }
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }
}
