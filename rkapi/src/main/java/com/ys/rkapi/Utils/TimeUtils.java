package com.ys.rkapi.Utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import com.ys.rkapi.Constant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Calendar;

//import android.app.AlarmManager;

/**
 * Created by RYX on 2017/8/30.
 */

public class TimeUtils {
    public static long getTimeMills(int year, int month, int day, int hour, int minute,int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute,sec);
        return calendar.getTimeInMillis();
    }


    public static void setPowerOnTime(Context context, int year, int month, int day, int hourOfDay, int minute) {
//        boolean dateFlag = false;
//        if ((year <= 2099) && (year >= 2017) && ){
//
//        }
        if (validate(year, month, day, hourOfDay, minute) == true) {
            Log.d("H22222H", "1222222222222222222222");
            setPowerOnMode(1);
            Intent intent = new Intent(Constant.POWER_ON_ACTION);
            intent.putExtra(Constant.POWER_ON_YEAR, year);//IP地址
            intent.putExtra(Constant.POWER_ON_Month, month);//网关
            intent.putExtra(Constant.POWER_ON_DAY, day);//子网掩码
            intent.putExtra(Constant.POWER_ON_HOUR, hourOfDay);//dns1
            intent.putExtra(Constant.POWER_ON_MINUTE, minute);//dns2
            context.sendBroadcast(intent);
        } else {
            Toast.makeText(context.getApplicationContext(), "输入时间有误请检查", Toast.LENGTH_SHORT).show();
        }

    }

    public static void clearPowerOnTime(Context context) {
        Intent intent = new Intent(Constant.POWER_ON_CLEAR_ACTION);
        context.sendBroadcast(intent);
    }

    //0-normalmode 1-usermode
    public static void setPowerOnMode(int mode) {
        Process process = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");   // 这个地方用su会出现permission deny 的异常.
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.poweronmode " + mode + " \n");
            //os.writeBytes("reboot \n");
            os.writeBytes("exit\n");
            os.flush();
            int aa = process.waitFor();
            is = new DataInputStream(process.getInputStream());
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String out = new String(buffer);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }

    //1-enable 0-disable
    public static void WatchDogEnable(int enable) {
        Process process = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");   // 这个地方用su会出现permission deny 的异常.
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.watchdogen " + enable + " \n");
            //os.writeBytes("reboot \n");
            os.writeBytes("exit\n");
            os.flush();
            int aa = process.waitFor();
            is = new DataInputStream(process.getInputStream());
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String out = new String(buffer);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }

    public static void WatchDogFeed() {
        Process process = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");   // 这个地方用su会出现permission deny 的异常.
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.watchdogfeed " + 60 + " \n");
            //os.writeBytes("reboot \n");
            os.writeBytes("exit\n");
            os.flush();
            int aa = process.waitFor();
            is = new DataInputStream(process.getInputStream());
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String out = new String(buffer);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }

    public static boolean validate(int year, int month, int day, int hourOfDay, int minute) {

        if (year > 2099 && year < 2017) {
            return false;
        }
        /*Android月份从0开始*/
        if (month < 0 || month > 11) {
            return false;
        }
        int[] monthLengths = new int[]{31, 31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30};
        if (isLeapYear(year)) {
            monthLengths[2] = 29;
        } else {
            monthLengths[2] = 28;
        }
        int monthLength = monthLengths[month];
        if (day < 1 || day > monthLength) {
            return false;
        }
        if (hourOfDay < 0 || hourOfDay > 23) {
            return false;
        }
        if (minute < 0 || minute > 59) {
            return false;
        }
        return true;
    }

    /*是否是闰年*/
    private static boolean isLeapYear(int year) {
        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
    }
}
