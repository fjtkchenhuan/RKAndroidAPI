package com.example.yfaceapi;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

public class GPIOManager {
    private static final String TAG = "GPIOManager";
    private static final String USB_OTG_3288 = "/sys/class/gpio/gpio12/value";
    private static final String USB_OTG_3368 = "/sys/devices/platform/misc_power_en/otg_vbus";
    private static final String USB_OTG_3399 = "/sys/kernel/debug/usb@fe800000/rk_usb_force_mode";

    private static final String USB1_3288 = "/sys/class/gpio/gpio257/value";
    private static final String USB2_3288 = "/sys/class/gpio/gpio170/value";
    private static final String USB3_3288 = "/sys/class/gpio/gpio14/value";

    private static final String USB1_3368 = "/sys/devices/platform/misc_power_en/vbus_drv5";
    private static final String USB2_3368 = "/sys/devices/platform/misc_power_en/vbus_drv3";
    private static final String USB3_3368 = "/sys/devices/platform/misc_power_en/vbus_drv4";

    private static final String USB1_3399 = "/sys/devices/platform/misc_power_en/vbus_drv5";
    private static final String USB2_3399 = "/sys/devices/platform/misc_power_en/vbus_drv4";
    private static final String USB3_3399 = "/sys/devices/platform/vcc5v0-sys/usb30_5v_en";// /sys/devices/platform/misc_power_en/usb30_5v_en

    //功放
    private static final String VOICE_3288 = "/sys/bus/i2c/devices/2-0010/spk";
    private static final String VOICE_3368 = "/sys/devices/platform/misc_power_en/spk_ctl";
    private static final String VOICE_3399 = "/sys/class/gpio/gpio1152/value";

    //风扇控制
    private static final String FAN_CONTROL_3288 = "/sys/class/custom_class/custom_dev/fan";
    private static final String FAN_CONTROL_3368 = "/sys/devices/platform/misc_power_en/fan_ctl";
    private static final String FAN_CONTROL_3399 = "/sys/devices/platform/misc_power_en/fan_ctl";

    //红外LED电源
    private static final String INFRARED_LED_3288 = "/sys/class/custom_class/custom_dev/ir";//  /sys/class/custom_class/custom_dev/ir
    private static final String INFRARED_LED_3368 = "/sys/devices/platform/misc_power_en/red_led"; ///sys/devices/platform/misc_power_en/red_led
    private static final String INFRARED_LED_3399 = "/sys/devices/platform/misc_power_en/ir_led";

    //继电器控制
    private static final String RELAY_3288 = "/sys/class/custom_class/custom_dev/relay";
    private static final String RELAY_3368 = "/sys/devices/platform/misc_power_en/rtl_ctl";
    private static final String RELAY_3399 = "/sys/devices/platform/misc_power_en/rtl_ctl";

    //绿色补光灯
    private static final String GREEN_LIGHT_3288 = "/sys/class/custom_class/custom_dev/green_led";
    private static final String GREEN_LIGHT_3368 = "/sys/devices/platform/misc_power_en/g_led";
    private static final String GREEN_LIGHT_3399 = "/sys/devices/platform/misc_power_en/g_led";

    //红色补光灯
    private static final String RED_LIGHT_3288 = "/sys/class/custom_class/custom_dev/red_led";
    private static final String RED_LIGHT_3368 = "/sys/devices/platform/misc_power_en/r_led";
    private static final String RED_LIGHT_3399 = "/sys/devices/platform/misc_power_en/r_led";

    //白色补光灯
    private static final String WHITE_LIGHT_3288 = "/sys/class/custom_class/custom_dev/white_led";
    private static final String WHITE_LIGHT_3368 = "/sys/devices/platform/misc_power_en/w_led";
    private static final String WHITE_LIGHT_3399 = "/sys/devices/platform/misc_power_en/w_led";

    //CPU频率
    private static final String SCALING_MAX_FREQ_3288 = "/sys/devices/system/cpu/cpufreq/policy0/scaling_max_freq";
    private static final String SCALING_MIN_FREQ_3288 = "/sys/devices/system/cpu/cpufreq/policy0/scaling_min_freq";

    private static GPIOManager myManager;
    private boolean isRk3288;
    private boolean isRk3368;
    private boolean isRk3399;

    private Context mContext;


    private GPIOManager(Context context) {
        mContext = context;
        isRk3288 = isRk3288();
        isRk3399 = isRk3399();
        isRk3368 = isRk3368();

        GpioUtils.upgradeRootPermissionForExport();
        if (isRk3288()) {
            rootGpio(162);
            rootGpio(163);
            rootGpio(169);
            rootGpio(171);

            GpioUtils.upgradeRootPermissionForGpioValue(257);
            GpioUtils.upgradeRootPermissionForGpioValue(170);
            GpioUtils.upgradeRootPermissionForGpioValue(14);

            GpioUtils.upgradeRootPermission(SCALING_MAX_FREQ_3288);
            GpioUtils.upgradeRootPermission(SCALING_MIN_FREQ_3288);
        }else if (isRk3399()) {
            rootGpio(1067);
            rootGpio(1066);
            rootGpio(1071);
            rootGpio(1072);

            GpioUtils.upgradeRootPermission(USB3_3399);
        }
    }

    public static synchronized GPIOManager getInstance(Context context) {
        if (myManager == null) {
            myManager = new GPIOManager(context);
        }
        return myManager;
    }

    public String getUSBOTGStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(USB_OTG_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB_OTG_3368);
        }else if (isRk3399) {
            String status = Utils.readGpioPG(USB_OTG_3399);
            Log.d("chenhuan","status = " + status);
            if (status.contains("peripheral"))
                return "0";
            else if (status.contains("host"))
                return "1";
        }
        return "";
    }

    public void pullUpUSBOTG() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB_OTG_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB_OTG_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB_OTG_3399),"host");
    }

    public void pullDownUSBOTG() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB_OTG_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB_OTG_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB_OTG_3399),"peripheral");
    }

    public String getUSB1Status() {
        if (isRk3288) {
            return Utils.readGpioPG(USB1_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB1_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(USB1_3399);
        }
        return "";
    }

    public void pullUpUSB1() {
        if (isRk3288) {
            GpioUtils.writeGpioValue(257,"1");
        }
//           Utils.writeStringFileFor7(new File(USB1_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB1_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB1_3399),"1");
    }

    public void pullDownUSB1() {
        if (isRk3288)
            GpioUtils.writeGpioValue(257,"0");
//            Utils.writeStringFileFor7(new File(USB1_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB1_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB1_3399),"0");
    }

    public String getUSB2Status() {
        if (isRk3288) {
            return Utils.readGpioPG(USB2_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB2_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(USB2_3399);
        }
        return "";
    }

    public void pullUpUSB2() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB2_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB2_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB2_3399),"1");
    }

    public void pullDownUSB2() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB2_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB2_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB2_3399),"0");
    }

    public String getUSB3Status() {
        if (isRk3288) {
            return Utils.readGpioPG(USB3_3288);
//            return GpioUtils.getGpioValue(257);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB3_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(USB3_3399);
        }
        return "";
    }

    public void pullUpUSB3() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB3_3288),"1");
//            GpioUtils.writeGpioValue(257,"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB3_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB3_3399),"1");
    }

    public void pullDownUSB3() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB3_3288),"0");
//            GpioUtils.writeGpioValue(257,"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB3_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB3_3399),"0");
    }

    public String getVoiceStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(VOICE_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(VOICE_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(VOICE_3399);
        }
        return "";
    }

    public void pullUpVoice() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(VOICE_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(VOICE_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(VOICE_3399),"1");
    }

    public void pullDownVoice() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(VOICE_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(VOICE_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(VOICE_3399),"0");
    }

    public String getFanStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(FAN_CONTROL_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(FAN_CONTROL_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(FAN_CONTROL_3399);
        }
        return "";
    }

    public void pullUpFan() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3399),"1");
    }

    public void pullDownFan() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3399),"0");
    }

    public String getInfraredLedStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(INFRARED_LED_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(INFRARED_LED_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(INFRARED_LED_3399);
        }
        return "";
    }

    public void pullUpInfraredLed() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3399),"1");
    }

    public void pullDownInfraredLed() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3399),"0");
    }

    public String getRelayStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(RELAY_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(RELAY_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(RELAY_3399);
        }
        return "";
    }

    public void pullUpRelay() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RELAY_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RELAY_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RELAY_3399),"1");
    }

    public void pullDownRelay() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RELAY_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RELAY_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RELAY_3399),"0");
    }

    public String getGreenLightStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(GREEN_LIGHT_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(GREEN_LIGHT_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(GREEN_LIGHT_3399);
        }
        return "";
    }

    public void pullUpGreenLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3399),"1");
    }

    public void pullDownGreenLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3399),"0");
    }

    public String getRedLightStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(RED_LIGHT_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(RED_LIGHT_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(RED_LIGHT_3399);
        }
        return "";
    }

    public void pullUpRedLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3399),"1");
    }

    public void pullDownRedLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3399),"0");
    }

    public String getWhiteLightStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(WHITE_LIGHT_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(WHITE_LIGHT_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(WHITE_LIGHT_3399);
        }
        return "";
    }

    public void pullUpWhiteLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3399),"1");
    }

    public void pullDownWhiteLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3399),"0");
    }

    public String getReservedIO1Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(162);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(91);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1067);
        return status;
    }

    public String getReservedIO2Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(163);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(90);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1066);
        return status;
    }

    public String getReservedIO3Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(169);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(111);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1071);
        return status;
    }

    public String getReservedIO4Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(171);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(109);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1072);
        return status;
    }

    public String getReservedIO4Direction() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioDirection(171);
        else if (isRk3368)
            status = GpioUtils.getGpioDirection(109);
        else if (isRk3399)
            status = GpioUtils.getGpioDirection(1072);
        return status;
    }

    public void setReservedIO4Out() {
        if (isRk3288)
            GpioUtils.setGpioDirection(171,0);
        else if (isRk3368)
            GpioUtils.setGpioDirection(109,0);
        else if (isRk3399)
            GpioUtils.setGpioDirection(1072,0);
    }

    public void setReservedIO4In() {
        if (isRk3288)
            GpioUtils.setGpioDirection(171,1);
        else if (isRk3368)
            GpioUtils.setGpioDirection(109,1);
        else if (isRk3399)
            GpioUtils.setGpioDirection(1072,1);
    }

    public boolean isIO4DirectionOut() {
        String direction = "out";
        String temp = "";
        if (isRk3288)
            temp = GpioUtils.getGpioDirection(171);
        else if (isRk3368)
            temp = GpioUtils.getGpioDirection(109);
        else if (isRk3399)
            temp = GpioUtils.getGpioDirection(1072);
        return direction.equals(temp);
    }

    public void pullUpIO4() {
        if (isRk3288)
            GpioUtils.writeGpioValue(171,"1");
        else if (isRk3368)
            GpioUtils.writeGpioValue(109,"1");
        else if (isRk3399)
            GpioUtils.writeGpioValue(1072,"1");
    }

    public void pullDownIO4() {
        if (isRk3288)
            GpioUtils.writeGpioValue(171,"0");
        else if (isRk3368)
            GpioUtils.writeGpioValue(109,"0");
        else if (isRk3399)
            GpioUtils.writeGpioValue(1072,"0");
    }

    public void setMaxFreq(int value) {
        if (isRk3288) {
            Utils.writeStringFileFor7(new File(SCALING_MAX_FREQ_3288),(value * 1000) + "");
        }
    }

    public void setMinFreq(int value) {
        if (isRk3288) {
            Utils.writeStringFileFor7(new File(SCALING_MIN_FREQ_3288),(value * 1000) + "");
        }
    }

    public String getMaxFreq() {
        if (isRk3288) {
            return Utils.readGpioPG(SCALING_MAX_FREQ_3288);
        }
        return "";
    }

    public String getMinFreq() {
        if (isRk3288) {
           return Utils.readGpioPG(SCALING_MIN_FREQ_3288);
        }
        return "";
    }


    private  String getVersion() {
        return getValueFromProp("ro.build.description").substring(0,6);
    }

    public  String getValueFromProp(String key) {
        String value = "";
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            value = (String) getMethod.invoke(classType, new Object[]{key});
        } catch (Exception e) {
        }
        return value;
    }



    private boolean isRk3288() {
        return getVersion().contains("rk3288");
    }

    private boolean isRk3368() {
        return getVersion().contains("rk3368");
    }

    private boolean isRk3399() {
        return getVersion().contains("rk3399");
    }

    private void rootGpio(int index) {
        if (GpioUtils.exportGpio(index))
            GpioUtils.upgradeRootPermissionForGpio(index);
    }
}
