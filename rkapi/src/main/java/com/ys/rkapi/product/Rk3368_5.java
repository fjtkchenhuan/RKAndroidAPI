package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.NetUtils;
import com.ys.rkapi.Utils.ScreenUtils;
import com.ys.rkapi.Utils.SilentInstallUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.IOException;

/**
 * Created by Administrator on 2018/4/13.
 */

public class Rk3368_5 extends RK {
    static final String RTC_PATH = "/sys/devices/ff150000.i2c/i2c-3/3-0051/rtc/rtc0/time";
    static final String[] LED_PATH = new String[]{"/sys/devices/misc_power_en.22/green_led", "/sys/devices/misc_power_en.23/green_led"};
    private static final String BACKLIGHT_IO_PATH = "/sys/class/graphics/fb0/pwr_bl";
    public static final Rk3368_5 INSTANCE =  new Rk3368_5();
    private Rk3368_5(){}
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
        context.startActivity(new Intent("android.intent.action.SHOW_BRIGHTNESS_DIALOG"));
    }

    @Override
    public void setEthMacAddress(Context context, String val) {
        NetUtils.setEthMAC(val);
    }

    @Override
    public void rotateScreen(Context context, String degree) {
        if (!TextUtils.isEmpty(degree)) {
            if (degree.equals("0") || degree.equals("90")
                    || degree.equals("180") || degree.equals("270")) {
                ScreenUtils.rotateScreen(degree);
                Utils.reboot();
            }
        }
    }

    @Override
    public boolean getNavBarHideState(Context context) {
        return Utils.getValueFromProp(Constant.PROP_HIDE_STATUSBAR).equals("1");
    }

    @Override
    public boolean isSlideShowNavBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_STATUSBAR).equals("1");
    }

    @Override
    public void setSlideShowNavBar(Context context, boolean flag) {
        if (!flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR, "0");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR, "1");
    }

    @Override
    public boolean isSlideShowNotificationBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_NOTIFIBAR).equals("1");
    }

    @Override
    public void setSlideShowNotificationBar(Context context, boolean flag) {
        if (flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR, "1");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR, "0");
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
    public void rebootRecovery() {
        Utils.do_exec("reboot recovery");
    }

    @Override
    public boolean silentInstallApk(String apkPath) {
        return SilentInstallUtils.install(apkPath);
    }

    @Override
    public void changeScreenLight(Context context, int value) {
        Intent intent = new Intent("com.ys.set_screen_bright");
        intent.putExtra("brightValue",value);
        context.sendBroadcast(intent);
    }

    @Override
    public void turnOnHDMI() {
        try {
            GPIOUtils.writeIntFileUnder7("1",Constant.HDMI_STATUS_3288);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOffHDMI() {
        try {
            GPIOUtils.writeIntFileUnder7("0",Constant.HDMI_STATUS_3288);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSoftKeyboardHidden(boolean hidden) {

    }

    @Override
    public void setDormantInterval(Context context,long time) {

    }

    @Override
    public int getCPUTemperature() {
        return 0;
    }

    @Override
    public void setADBOpen(boolean open) {

    }
}
