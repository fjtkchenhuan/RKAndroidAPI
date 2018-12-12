package com.ys.rkapi.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import com.ys.rkapi.Constant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by RYX on 2017/8/30.
 */

public class ScreenUtils {

    // adb 修改system prop 属性旋转屏幕
    // value 为 0 、 90 、180 、270
    public static void rotateScreen(String value) {
        Process process = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");   // 这个地方用su会出现permission deny 的异常.
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.displayrot " + value + " \n");
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

    public static void rotationScreen(String path,String rot) {
        Utils.setValueToProp("persist.sys.displayrot",rot);
        Log.i("yuanhang",Utils.getValueFromProp("persist.sys.displayrot"));
        int rotate = Integer.parseInt(rot);
        File file = new File(path);
        if(file.exists()){
            GPIOUtils.writeStringFileFor7(file, (rotate / 90) +"");
        }
    }


    public static void rotationScreen(Context context, int rot) {
        Log.d("HHHHH","rotationScreen");
        Intent i = new Intent();
        i.setAction(Constant.ROTATION_ACTION);
        i.putExtra(Constant.ROTATION_INDEX, rot);
        context.sendBroadcast(i);
    }


    public static int getDisplayWith(Context context){
        WindowManager manager = ((Activity)context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getDisplayHeight(Context context){
        WindowManager manager = ((Activity)context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
