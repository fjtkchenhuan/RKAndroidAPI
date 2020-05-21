package com.ys.rkapi.product;

import android.content.Context;

import com.ys.rkapi.Constant;

import java.io.File;

/**
 * Created by Administrator on 2018/4/14.
 */

public abstract class YS {

    public abstract String getRtcPath();

    public abstract String getLedPath();

    public abstract void takeBrightness(Context context);

    public abstract void setEthMacAddress(Context context,String val);

    public abstract void rotateScreen(Context context, String degree);

    public abstract boolean getNavBarHideState(Context context);

    public abstract boolean isSlideShowNavBarOpen();

    public abstract void setSlideShowNavBar(Context context,boolean flag);

    public abstract boolean isSlideShowNotificationBarOpen();

    public abstract void setSlideShowNotificationBar(Context context,boolean flag);

    public abstract void turnOffBackLight();

    public abstract void turnOnBackLight();

    public abstract boolean isBackLightOn();

    public abstract void rebootRecovery(Context context);

    public abstract boolean silentInstallApk(String apkPath);

    public abstract void changeScreenLight(Context context,int value);

    public abstract void turnOnHDMI();

    public abstract void turnOffHDMI();

    public abstract void setSoftKeyboardHidden(boolean hidden);

    public abstract void setDormantInterval(Context context,long time);

    public abstract int getCPUTemperature();

    public abstract void setADBOpen(boolean open);

    public abstract void awaken();

    /**
     * 过滤文件路径
     *
     * @param filePaths
     * @return
     */
    protected String filterPath(String[] filePaths) {
        if (filePaths == null) return null;
        String filterPath = null;
        for (String path : filePaths) {
            if (new File(path).exists()) {
                filterPath = path;
                break;
            }
        }
        return filterPath;
    }
}
