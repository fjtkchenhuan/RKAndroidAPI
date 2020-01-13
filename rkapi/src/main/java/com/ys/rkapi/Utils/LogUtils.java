package com.ys.rkapi.Utils;

import android.os.Build;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by RYX on 2017/8/30.
 */

public class LogUtils {

    public static void startLog(final String path) {
        File file = new File(path);
        file.setExecutable(true);
        file.setReadable(true);//设置可读权限
        file.setWritable(true);//设置可写权限
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Utils.execFor7("logcat -v time  > " + path);
                }
            });
            t.start();
        }
    }

    public static void stopLog() {
           Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT>22)
                    Utils.execFor7("killall logcat");
                    else
                        Utils.execFor7("busybox1.11 killall logcat");
                }
            });
            t.start();
    }

    public static void saveToSDCard(String fileName, String content) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            file = new File(fileName);
        }
        writeFile(file, content);
    }

    private static void writeFile(File file, String content) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
