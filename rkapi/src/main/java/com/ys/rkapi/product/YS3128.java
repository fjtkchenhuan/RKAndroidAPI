package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.ScreenUtils;
import com.ys.rkapi.Utils.SilentInstallUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/13.
 */

public class YS3128 extends YS {
    static final String[] LED_PATH = new String[]{"/sys/devices/misc_power_en.19/out8", "/sys/devices/misc_power_en.18/out8"};
    static final String RTC_PATH = "/sys/devices/20072000.i2c/i2c-0/0-0051/rtc/rtc0/time";
    private final static String BACKLIGHT_IO_PATH = "/sys/devices/fb.9/graphics/fb0/pwr_bl";
    public static final YS3128 INSTANCE = new YS3128();

    private YS3128() {
    }

    @Override
    public String getRtcPath() {
        return RTC_PATH;
    }

    @Override
    public String getLedPath() {
        return filterPath(LED_PATH);
    }

    @Override
    public void takeBrightness(Context context) {
        Intent intent = new Intent("com.ys.show_brightness_dialog");
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.sendBroadcast(intent);
    }

    @Override
    public void setEthMacAddress(Context context, String val) {
        Toast.makeText(context, "暂不支持此功能", Toast.LENGTH_LONG).show();
    }

    @Override
    public void rotateScreen(Context context, String degree) {
        ScreenUtils.rotationScreen(context, getDisplayRot(degree));
    }

    @Override
    public boolean getNavBarHideState(Context context) {
        return (Settings.System.getInt(context.getContentResolver(), "hidden_state_bar", 0) == 1);
    }

    @Override
    public boolean isSlideShowNavBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_STATUSBAR_LU).equals("1");
    }

    @Override
    public void setSlideShowNavBar(Context context, boolean flag) {
        Intent intent = new Intent("com.ys.slide.systembar");
        intent.putExtra("barMode","navigationbar");
        intent.putExtra("isSlide",flag);
        context.sendBroadcast(intent);
    }

    @Override
    public boolean isSlideShowNotificationBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_NOTIFIBAR_LU).equals("0");
    }

    @Override
    public void setSlideShowNotificationBar(Context context, boolean flag) {
        Intent intent = new Intent("com.ys.slide.systembar");
        intent.putExtra("barMode","notificationbar");
        intent.putExtra("isSlide",flag);
        context.sendBroadcast(intent);
    }

    @Override
    public void turnOffBackLight() {
        try {
            GPIOUtils.writeIntFileUnder7("0",BACKLIGHT_IO_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOnBackLight() {
        try {
            GPIOUtils.writeIntFileUnder7("1",BACKLIGHT_IO_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBackLightOn() {
        return "1".equals(GPIOUtils.readGpioPG(BACKLIGHT_IO_PATH));
    }

    @Override
    public void rebootRecovery(Context context) {
        Utils.do_exec("reboot recovery");
    }

    @Override
    public boolean silentInstallApk(String apkPath) {
        return SilentInstallUtils.install(apkPath);
    }

    @Override
    public void changeScreenLight(Context context, int value) {
//        try {
            int i = value * 255 /100 ;
        GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/rk28_bl/brightness"),String.valueOf(i));
//            GPIOUtils.writeScreenBrightFile(String.valueOf(i),"/sys/class/backlight/rk28_bl/brightness");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent("com.ys.set_screen_bright");
        intent.putExtra("brightValue",value);
        context.sendBroadcast(intent);
        Log.i("yuanhang","brightValue");
    }

    @Override
    public void turnOnHDMI() {
        try {
            GPIOUtils.writeIntFileUnder7("1",Constant.HDMI_STATUS_3128);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOffHDMI() {
        try {
            GPIOUtils.writeIntFileUnder7("0 ",Constant.HDMI_STATUS_3128);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSoftKeyboardHidden(boolean hidden) {
        if (hidden)
            Utils.setValueToProp("persist.sys.softkeyboard", "0");
        else
            Utils.setValueToProp("persist.sys.softkeyboard", "1");
    }

    @Override
    public void setDormantInterval(Context context, long time) {
        Intent intent = new Intent(Constant.DORMANT_INTERVAL);
        intent.putExtra("time_interval",time);
        context.sendBroadcast(intent);
    }

    @Override
    public int getCPUTemperature() {
        return 0;
    }

    @Override
    public void setADBOpen(boolean open) {
        if (open) {
//            Utils.setValueToProp("persist.sys.usb.otg.mode","2");///sys/bus/platform/drivers/usb20_otg/force_usb_mode
//            GPIOUtils.writeStringFileFor7(new File("/sys/bus/platform/drivers/usb20_otg/force_usb_mode"),"2");
            try {
                GPIOUtils.writeIntFileUnder7("2","/sys/bus/platform/drivers/usb20_otg/force_usb_mode");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
//            Utils.setValueToProp("persist.sys.usb.otg.mode","1");
//            GPIOUtils.writeStringFileFor7(new File("/sys/bus/platform/drivers/usb20_otg/force_usb_mode"),"2");
            try {
                GPIOUtils.writeIntFileUnder7("1","/sys/bus/platform/drivers/usb20_otg/force_usb_mode");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void awaken() {
        if ("0".equals(GPIOUtils.readGpioPG(BACKLIGHT_IO_PATH))) {
            try {
                GPIOUtils.writeIntFileUnder7("1",Constant.HDMI_STATUS_3128);
                GPIOUtils.writeIntFileUnder7("1",BACKLIGHT_IO_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getDisplayRot(String value) {
        int index = 0;
        switch (value) {
            case "0":
                index = 0;
                break;
            case "90":
                index = 1;
                break;
            case "180":
                index = 2;
                break;
            case "270":
                index = 3;
                break;
        }
        return index;
    }

}
