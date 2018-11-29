package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.ScreenUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.IOException;

/**
 * Created by Administrator on 2018/4/13.
 */

public class Rk3328 extends RK {
    static final String RTC_PATH = "/sys/devices/ff160000.i2c/i2c-1/1-0051/rtc/rtc0/time";
    static final String[] LED_PATH = new String[]{"/sys/devices/misc_power_en.3/led"};
    private static final String BACKLIGHT_IO_PATH = "/sys/class/graphics/fb0/pwr_bl";
    public final static Rk3328 INSTANCE = new Rk3328();
    private Rk3328(){}
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
        Toast.makeText(context, "暂不支持此功能", Toast.LENGTH_LONG).show();
    }

    @Override
    public void rotateScreen(Context context, String degree) {
        ScreenUtils.rotationScreen("/sys/bus/i2c/devices/1-0054/displayrot",degree);
        Utils.reboot();
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
        if (flag)
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
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR, "0");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR, "1");
    }

    @Override
    public void turnOffBackLight() {
        try {
            GPIOUtils.writeIntFileFor7("0",BACKLIGHT_IO_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOnBackLight() {
        try {
            GPIOUtils.writeIntFileFor7("1",BACKLIGHT_IO_PATH);
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
        Utils.execFor7("reboot recovery");
    }

    @Override
    public boolean silentInstallApk(String apkPath) {
        return Utils.execFor7("pm install -r " + apkPath);
    }

    @Override
    public void changeScreenLight(Context context, int value) {
        Intent intent = new Intent("com.ys.set_screen_bright");
        intent.putExtra("brightValue",value);
        context.sendBroadcast(intent);
    }

    @Override
    public void turnOnHDMI() {

    }

    @Override
    public void turnOffHDMI() {

    }

    @Override
    public void setSoftKeyboardHidden(boolean hidden) {

    }

    @Override
    public void setDormantInterval(Context context,long time) {

    }
}
