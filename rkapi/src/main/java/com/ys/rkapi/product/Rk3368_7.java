package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.ScreenUtils;
import com.ys.rkapi.Utils.Utils;

/**
 * Created by Administrator on 2018/11/6.
 */

public class Rk3368_7 extends RK {
    public final static Rk3368_7 INSTANCE = new Rk3368_7();
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
        Toast.makeText(context, "暂不支持此功能", Toast.LENGTH_LONG).show();
    }

    @Override
    public void rotateScreen(Context context, String degree) {
        ScreenUtils.rotationScreen("/sys/bus/i2c/devices/1-0054/displayrot",degree);
        Utils.reboot();
    }

    @Override
    public boolean getNavBarHideState(Context context) {
        return com.ys.rkapi.Utils.Utils.getValueFromProp(com.ys.rkapi.Constant.PROP_HIDE_STATUSBAR).equals("1");
    }

    @Override
    public boolean isSlideShowNavBarOpen() {
        return false;
    }

    @Override
    public void setSlideShowNavBar(Context context, boolean flag) {

    }

    @Override
    public boolean isSlideShowNotificationBarOpen() {
        return false;
    }

    @Override
    public void setSlideShowNotificationBar(Context context, boolean flag) {

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
