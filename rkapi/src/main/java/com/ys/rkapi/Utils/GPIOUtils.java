package com.ys.rkapi.Utils;


import android.os.Build;
import android.util.Log;

import com.ys.rkapi.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by RYX on 2017/8/31.
 */

public class GPIOUtils {
    private static final String TAG = "GPIOUtils";

    public static void writeGpio(int num, int value) {
        if (num < Constant.IO_OUTPUTS.length) {
            File file = new File(Constant.IO_OUTPUTS[num]);
            file.setExecutable(true);
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限
            Utils.do_exec("busybox echo " + value + " > " + Constant.IO_OUTPUTS[num]);
        }
    }


    public static int readGpio(int num) {
        if (num < Constant.IO_INPUTS.length) {
            String str = "";
            File file = new File(Constant.IO_INPUTS[num]);
            try {
                FileInputStream in = new FileInputStream(file);
                byte[] buffer = new byte[1];
                in.read(buffer);
                in.close();
                str = new String(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (str.equals("0")) {
                return 0;
            } else {
                return 1;
            }
        }
        return 1;
    }

    public static boolean isHDMIOut() {
        boolean ishdmi = false;
        File file = null;
        String product = VersionUtils.getAndroidModle();
        if ("22".equals(Build.VERSION.SDK))
            file = new File(Constant.HDMI_STATUS_3288);
        else if (product.contains("rk3399"))
            file = new File(Constant.HDMI_STATUS_3399);
        String str = "";
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] by = new byte[1024];
            int len = -1;
            StringBuffer sb = new StringBuffer();
            while ((len = in.read(by))!=-1) {
                sb.append(new String(by, 0, len));
            }
            in.close();
             str = new String(sb).replace("\n","");
             Log.d("gpioutils","str=" + str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if  ("22".equals(Build.VERSION.SDK))
            ishdmi = str.equals("1");
        else if (product.contains("rk3399"))
            ishdmi = str.equals("connected");
        return ishdmi;
    }

    public static void writeIntFileUnder7(String str, String path) throws IOException, InterruptedException {
        File file = new File(path);
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);
        if (str.equals("0")) {
            do_exec("busybox echo 0 > " + path);
        } else {
            do_exec("busybox echo 1 > " + path);
        }
    }


    public static void writeIntFileFor7(String str,String path) throws IOException, InterruptedException {
        Log.d("chenhuan","path = " + path);
        File file = new File(path);
        file.setExecutable(true);
        file.setReadable(true);//设置可读权限
        file.setWritable(true);//设置可写权限
        if (str.equals("0")) {
            Utils.execFor7("busybox echo 0 > " + path);
        }  else {
            Utils.execFor7("busybox echo 1 > " + path);
        }
    }

    public static void writeStringFileFor7(File file, String content) {
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

    public static void writeScreenBrightFile(String str, String path) throws IOException, InterruptedException {
        File file = new File(path);
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);
        do_exec("busybox echo " + str +" > " + path);
    }

    public static void do_exec(String cmd) {
        try {
            /* Missing read/write permission, trying to chmod the file */
            Process su;
            su = Runtime.getRuntime().exec("su");
            String str = cmd + "\n" + "exit\n";
            su.getOutputStream().write(str.getBytes());

            Log.d(TAG, "cmd：" + cmd);

            if ((su.waitFor() != 0)) {
                Log.d(TAG, "cmd=" + cmd + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readGpioPG(String path) {
        String str = "";
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[1];
            fileInputStream.read(buffer);
            fileInputStream.close();
            str = new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String readGpioPGForLong(String path) {
        String str = "";
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            fileInputStream.read(buffer);
            fileInputStream.close();
            str = new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}


