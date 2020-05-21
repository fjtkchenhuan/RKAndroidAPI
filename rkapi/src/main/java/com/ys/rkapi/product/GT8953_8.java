package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/11/6.
 */

public class GT8953_8 extends YS {
    public final static GT8953_8 INSTANCE = new GT8953_8();
    private static final String BACKLIGHT_IO_PATH = "/sys/devices/platform/backlight/backlight/backlight/bl_power";

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
        Utils.setValueToProp("persist.sys.displayrot", degree);
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
        if (flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "0");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "1");
    }

    @Override
    public void turnOffBackLight() {
        try {
            GPIOUtils.writeIntFileFor7("0", "/sys/class/backlight/backlight/bl_power");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOnBackLight() {
        try {
            GPIOUtils.writeIntFileFor7("1", "/sys/class/backlight/backlight/bl_power");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBackLightOn() {
        return "1".equals(GPIOUtils.readGpioPG("/sys/class/backlight/backlight/bl_power"));
    }

    @Override
    public void rebootRecovery(Context context) {
        Intent intent = new Intent("com.ys.recovery_system");
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        context.sendBroadcast(intent);
    }

    @Override
    public boolean silentInstallApk(String apkPath) {
        return Utils.execFor7("pm install -r " + apkPath);
    }

    @Override
    public void changeScreenLight(Context context, int value) {
        Intent intent = new Intent("com.ys.set_screen_bright");
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        intent.putExtra("brightValue", value);
        context.sendBroadcast(intent);
    }

    @Override
    public void turnOnHDMI() {
        try {
            GPIOUtils.writeIntFileFor7("1", "/sys/class/hdmi/hdmi/status");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOffHDMI() {
        try {
            GPIOUtils.writeIntFileFor7("0", "/sys/class/hdmi/hdmi/status");
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
        Intent intent = new Intent(Constant.DORMANT_INTERVAL);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        intent.putExtra("time_interval",time);
        context.sendBroadcast(intent);
    }

    @Override
    public int getCPUTemperature() {
        ///sys/class/thermal/thermal_zone0/temp
        String s = GPIOUtils.readGpioPGForLong("/sys/class/thermal/thermal_zone0/temp");
        int temp = Integer.parseInt(s.substring(0,5));
        return (int) (temp/1000);
    }

    @Override
    public void setADBOpen(boolean open) {
        if (open) {
            Utils.setValueToProp("persist.sys.usb.otg.mode","1");
            try {
                GPIOUtils.writeIntFileFor7("1","/sys/devices/soc/soc:misc_power_en/otg");
                SystemClock.sleep(1000);
                GPIOUtils.writeIntFileFor7("1","/sys/devices/soc/soc:misc_power_en/otg_pwr");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                GPIOUtils.writeIntFileFor7("0","/sys/devices/soc/soc:misc_power_en/otg_pwr");
                GPIOUtils.writeIntFileFor7("0","/sys/devices/soc/soc:misc_power_en/otg");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Utils.setValueToProp("persist.sys.usb.otg.mode","0");
        }
    }

    @Override
    public void awaken() {
        if ("0".equals(GPIOUtils.readGpioPG("/sys/class/backlight/backlight/bl_power"))) {
            try {
                GPIOUtils.writeIntFileFor7("1", "/sys/class/hdmi/hdmi/status");
                GPIOUtils.writeIntFileFor7("1", "/sys/class/backlight/backlight/bl_power");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
