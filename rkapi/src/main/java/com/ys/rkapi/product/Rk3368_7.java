package com.ys.rkapi.product;

import android.content.Context;

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

    }

    @Override
    public void rotateScreen(Context context, String degree) {

    }

    @Override
    public boolean getNavBarHideState(Context context) {
        return false;
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
}
