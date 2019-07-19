package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.File;

/**
 * Created by Administrator on 2018/4/13.
 */

public class Rk3399 extends RK {
    static final String RTC_PATH = "/sys/devices/platform/ff120000.i2c/i2c-2/2-0051/rtc/rtc0/time";
    static final String[] LED_PATH = new String[]{"/sys/devices/platform/misc_power_en/red_led"};
    private static final String BACKLIGHT_IO_PATH = "/sys/devices/platform/backlight/backlight/backlight/bl_power";
    public static final Rk3399 INSTANCE = new Rk3399();
    private Rk3399(){}
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
        Utils.setValueToProp("persist.sys.displayrot",degree);
        File file = new File("/sys/devices/platform/ff150000.i2c/i2c-6/6-0050/rotate");
        if(file.exists()){
            GPIOUtils.writeStringFileFor7(file, degree);
        }
        Utils.reboot();
    }

    @Override
    public boolean getNavBarHideState(Context context) {
        return Utils.getValueFromProp(Constant.PROP_STATUSBAR_STATE_LU).equals("0");
    }

    @Override
    public boolean isSlideShowNavBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_STATUSBAR_LU).equals("1");
    }

    @Override
    public void setSlideShowNavBar(Context context, boolean flag) {
        if (flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR_LU, "0");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR_LU, "1");
    }

    @Override
    public boolean isSlideShowNotificationBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_NOTIFIBAR_LU).equals("0");
    }

    @Override
    public void setSlideShowNotificationBar(Context context, boolean flag) {
        if (flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "1");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "0");
    }

    @Override
    public void turnOffBackLight() {
        GPIOUtils.writeStringFileFor7(new File(BACKLIGHT_IO_PATH),"1");
    }

    @Override
    public void turnOnBackLight() {
        GPIOUtils.writeStringFileFor7(new File(BACKLIGHT_IO_PATH),"0");
    }

    @Override
    public boolean isBackLightOn() {
        return "0".equals(GPIOUtils.readGpioPG(BACKLIGHT_IO_PATH));
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

    @Override
    public int getCPUTemperature() {
        return 0;
    }

    @Override
    public void setADBOpen(boolean open) {

    }
}
