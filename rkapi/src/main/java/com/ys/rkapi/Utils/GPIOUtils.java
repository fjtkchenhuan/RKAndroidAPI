package com.ys.rkapi.Utils;


import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.ys.rkapi.Constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
        if ("22".equals(Build.VERSION.SDK)||product.contains("rk3328")||product.contains("rk3128"))
            file = new File(Constant.HDMI_STATUS_3288);
        else if ("25".equals(Build.VERSION.SDK) || product.contains("rk3399"))//product.contains("rk3399")||product.contains("rk3368")&&
            file = new File(Constant.HDMI_STATUS_3399);
        else if (product.contains("rk3128")&&"25".equals(Build.VERSION.SDK))
            file = new File(Constant.HDMI_STATUS_3128);
        else if("27".equals(Build.VERSION.SDK)) {
            ishdmi = "1".equals(readGpioPG("/sys/class/graphics/fb1/connected"));
            return ishdmi;
        }
        String str = "";
        try {
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                byte[] by = new byte[1024];
                int len = -1;
                StringBuffer sb = new StringBuffer();
                while ((len = in.read(by)) != -1) {
                    sb.append(new String(by, 0, len));
                }
                in.close();
                str = new String(sb).replace("\n", "");
                Log.d("gpioutils", "str=" + str);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if  ("22".equals(Build.VERSION.SDK)||product.contains("rk3328")||product.contains("rk3128"))
            ishdmi = str.equals("1");
        else if ("25".equals(Build.VERSION.SDK) || product.contains("rk3399"))//product.contains("rk3399")
            ishdmi = str.equals("connected");
        return ishdmi;
    }

    public static void writeIntFileUnder7(String str, String path) throws IOException, InterruptedException {
        File file = new File(path);
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);
        do_exec("busybox echo " + str + " >" + path);
//        if (str.equals("0")) {
//            do_exec("busybox echo 0 > " + path);
//        } else {
//            do_exec("busybox echo 1 > " + path);
//        }
    }


    public static void writeIntFileFor7(String str,String path) throws IOException, InterruptedException {
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

    /*
    给export申请权限
     */
    public static void upgradeRootPermissionForExport(){
        upgradeRootPermission("/sys/class/gpio/export");
    }

    /*
   配置一个gpio路径
    */
    public static boolean exportGpio(int gpio) {
        return writeNode("/sys/class/gpio/export", ""+gpio);
    }

    /*
    给获取io口的状态的路径申请权限,该方法需要在exportGpio后调用
     */
    public static void upgradeRootPermissionForGpio(int gpio){
        upgradeRootPermission("/sys/class/gpio/gpio" + gpio + "/direction");
        upgradeRootPermission("/sys/class/gpio/gpio" + gpio + "/value");
    }


    /*
    设置io口为输入或输出
     */
    public static boolean setGpioDirection(int gpio, int arg){
        String gpioDirection = "";
        if(arg == 0) gpioDirection = "out";
        else if(arg == 1) gpioDirection = "in";
        else return false;
        return writeNode("/sys/class/gpio/gpio" + gpio + "/direction", gpioDirection);
    }

    /*
    获取io口的状态为输出还是输入
     */
    public static String getGpioDirection(int gpio){
        String gpioDirection ="";
        gpioDirection = readNode("/sys/class/gpio/gpio" + gpio + "/direction");
        return  gpioDirection;
    }

    /*
    给输出io口写值，高电平或低电平
     */
    public static boolean writeGpioValue(int gpio, String arg){
        return writeNode("/sys/class/gpio/gpio" + gpio + "/value", arg);
    }

    //获取当前gpio是高还是低
    public static String getGpioValue(int gpio) {
        return readNode("/sys/class/gpio/gpio" + gpio + "/value");
    }


    private static boolean upgradeRootPermission(String path) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd="chmod 777 " + path;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        try {
            return process.waitFor()==0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean writeNode(String path, String arg) {
        Log.d(TAG, "Gpio_test set node path: " + path + " arg: " + arg);
        if (path == null || arg == null) {
            Log.e(TAG, "set node error");
            return false;
        }
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(path);
            fileWriter.write(arg);
        } catch (Exception e) {
            Log.e(TAG, "Gpio_test write node error!! path" + path + " arg: " + arg);
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static long mTime = 0;
    private static int mFailTimes = 0;
    private static String readNode(String path) {
        String result = "";

        FileReader fread = null;
        BufferedReader buffer = null;
        try {
            fread = new FileReader(path);
            buffer = new BufferedReader(fread);
            String str = null;
            while ((str = buffer.readLine()) != null) {
                result = str;
                break;
            }
            mFailTimes = 0;
        } catch (IOException e) {
            Log.e(TAG, "IO Exception");
            e.printStackTrace();
            if (mTime == 0 || SystemClock.uptimeMillis() - mTime < 1000) {
                mFailTimes++;
            }
            if (mFailTimes >= 3) {
                Log.d(TAG, "read format node continuous failed three times, exist thread");
            }
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
                if (fread != null) {
                    fread.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}


