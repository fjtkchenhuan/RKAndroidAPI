package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.File;

public class Rk3128_7 extends RK {

    public final static Rk3128_7 INSTANCE = new Rk3128_7();
    @Override
    public String getRtcPath() {
        return null;
    }

    @Override
    public String getLedPath() {
        return null;
    }

    @Override
    public void takeBrightness(Context context) {

    }

    @Override
    public void setEthMacAddress(Context context, String val) {

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
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR_LU, "1");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR_LU, "0");
    }

    @Override
    public boolean isSlideShowNotificationBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_NOTIFIBAR_LU).equals("0");
    }

    @Override
    public void setSlideShowNotificationBar(Context context, boolean flag) {
        Log.d("chenhuan","setSlideShowNotificationBar");
        if (flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "0");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "1");
    }

    @Override
    public void turnOffBackLight() {

    }

    @Override
    public void turnOnBackLight() {

    }

    @Override
    public boolean isBackLightOn() {
        return false;
    }

    @Override
    public void rebootRecovery() {

    }

    @Override
    public boolean silentInstallApk(String apkPath) {
        return false;
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
    public void setDormantInterval(Context context, long time) {

    }

    @Override
    public int getCPUTemperature() {
        return 0;
    }

    @Override
    public void setADBOpen(boolean open) {

    }


}
