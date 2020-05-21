package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.File;
import java.io.IOException;

public class YS3128_7 extends YS {

    public final static YS3128_7 INSTANCE = new YS3128_7();
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
        Utils.setValueToProp("persist.sys.displayrot", degree);
//        File file = new File("sys/devices/20072000.i2c/i2c-0/0-0054/rotate");
//        if (file.exists()) {
//            GPIOUtils.writeStringFileFor7(file, degree);
//        }
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
        try {
            GPIOUtils.writeIntFileFor7("0","/sys/class/graphics/fb0/pwr_bl");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOnBackLight() {
        try {
            GPIOUtils.writeIntFileFor7("1","/sys/class/graphics/fb0/pwr_bl");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBackLightOn() {
        return "1".equals(GPIOUtils.readGpioPG("/sys/class/graphics/fb0/pwr_bl"));
    }

    @Override
    public void rebootRecovery(Context context) {
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
    public void turnOnHDMI() {///sys/class/display/HDMI/enable
        try {
            GPIOUtils.writeIntFileFor7("1","sys/class/display/HDMI/enable");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOffHDMI() {
        try {
            GPIOUtils.writeIntFileFor7("0","sys/class/display/HDMI/enable");
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
        intent.putExtra("time_interval", time);
        context.sendBroadcast(intent);
    }

    @Override
    public int getCPUTemperature() {
        return 0;
    }

    @Override
    public void setADBOpen(boolean open) {
        if (open) {
            Utils.setValueToProp("persist.sys.usb.otg.mode", "2");
            Utils.execFor7("busybox echo 2 > " + "/sys/bus/platform/drivers/usb20_otg/force_usb_mode");
        } else {
            Utils.setValueToProp("persist.sys.usb.otg.mode", "1");
            Utils.execFor7("busybox echo 1 > " + "/sys/bus/platform/drivers/usb20_otg/force_usb_mode");
        }
    }

    @Override
    public void awaken() {
        if ("1".equals(GPIOUtils.readGpioPG("/sys/class/graphics/fb0/pwr_bl"))) {
            try {
                GPIOUtils.writeIntFileFor7("1","sys/class/display/HDMI/enable");
                GPIOUtils.writeIntFileFor7("0","/sys/class/graphics/fb0/pwr_bl");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
