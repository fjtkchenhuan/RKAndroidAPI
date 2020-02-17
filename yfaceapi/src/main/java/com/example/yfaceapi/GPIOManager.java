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

    private static final String USB1_8953_450 = "/sys/class/custom_class/custom_dev/host";
    private static final String USB2_8953_450 = "/sys/class/gpio/gpio20/value";
    private static final String USB3_8953_450 = "/sys/class/gpio/gpio21/value";

    //功放
    private static final String VOICE_3288 = "/sys/bus/i2c/devices/2-0010/spk";
    private static final String VOICE_3368 = "/sys/devices/platform/misc_power_en/spk_ctl";
    private static final String VOICE_3399 = "/sys/class/gpio/gpio1152/value";
    private static final String VOICE_8953_450 = "/sys/class/gpio/gpio89/value";

    //风扇控制
    private static final String FAN_CONTROL_3288 = "/sys/class/custom_class/custom_dev/fan";
    private static final String FAN_CONTROL_3368 = "/sys/devices/platform/misc_power_en/fan_ctl";
    private static final String FAN_CONTROL_3399 = "/sys/devices/platform/misc_power_en/fan_ctl";
    private static final String FAN_CONTROL_8953_450 = "/sys/class/custom_class/custom_dev/fan";

    //红外LED电源
    private static final String INFRARED_LED_3288 = "/sys/class/custom_class/custom_dev/ir";//  /sys/class/custom_class/custom_dev/ir
    private static final String INFRARED_LED_3368 = "/sys/devices/platform/misc_power_en/red_led"; ///sys/devices/platform/misc_power_en/red_led
    private static final String INFRARED_LED_3399 = "/sys/devices/platform/misc_power_en/ir_led";
    private static final String INFRARED_LED_8953_450 = "/sys/class/custom_class/custom_dev/ir";

    //继电器控制
    private static final String RELAY_3288 = "/sys/class/custom_class/custom_dev/relay";
    private static final String RELAY_3368 = "/sys/devices/platform/misc_power_en/rtl_ctl";
    private static final String RELAY_3399 = "/sys/devices/platform/misc_power_en/rtl_ctl";
    private static final String RELAY_8953_450 = "/sys/class/custom_class/custom_dev/relay";

    //绿色补光灯
    private static final String GREEN_LIGHT_3288 = "/sys/class/custom_class/custom_dev/green_led";
    private static final String GREEN_LIGHT_3368 = "/sys/devices/platform/misc_power_en/g_led";
    private static final String GREEN_LIGHT_3399 = "/sys/devices/platform/misc_power_en/g_led";
    private static final String GREEN_LIGHT_8953_450 = "/sys/class/custom_class/custom_dev/green_led";

    //红色补光灯
    private static final String RED_LIGHT_3288 = "/sys/class/custom_class/custom_dev/red_led";
    private static final String RED_LIGHT_3368 = "/sys/devices/platform/misc_power_en/r_led";
    private static final String RED_LIGHT_3399 = "/sys/devices/platform/misc_power_en/r_led";
    private static final String RED_LIGHT_8953_450 = "/sys/class/custom_class/custom_dev/red_led";

    //白色补光灯
    private static final String WHITE_LIGHT_3288 = "/sys/class/custom_class/custom_dev/white_led";
    private static final String WHITE_LIGHT_3368 = "/sys/devices/platform/misc_power_en/w_led";
    private static final String WHITE_LIGHT_3399 = "/sys/devices/platform/misc_power_en/w_led";
    private static final String WHITE_LIGHT_8953_450 = "/sys/class/custom_class/custom_dev/white_led";

    //CPU频率
    private static final String SCALING_MAX_FREQ_3288 = "/sys/devices/system/cpu/cpufreq/policy0/scaling_max_freq";
    private static final String SCALING_MIN_FREQ_3288 = "/sys/devices/system/cpu/cpufreq/policy0/scaling_min_freq";

    private static GPIOManager myManager;
    private boolean isRk3288;
    private boolean isRk3368;
    private boolean isRk3399;
    private boolean isGt8953;

    private Context mContext;


    private GPIOManager(Context context) {
        mContext = context;
        isRk3288 = isRk3288();
        isRk3399 = isRk3399();
        isRk3368 = isRk3368();
        isGt8953 = isGt8953();

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
        } else if (isGt8953) {
            rootGpio(45);
            rootGpio(43);
            rootGpio(44);
            rootGpio(66);
        }
    }

    public static synchronized GPIOManager getInstance(Context context) {
        if (myManager == null) {
            myManager = new GPIOManager(context);
        }
        return myManager;
    }

    /**
     * @method getUSBOTGStatus()
     * @description 获取当前USB-OTG的状态
     * @date  20190329
     * @author sky
     * @return 返回当前usb-otg的连接状态。返回0代表已连接，1代表未连接
    */
    public String getUSBOTGStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(USB_OTG_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB_OTG_3368);
        }else if (isRk3399) {
            String status = Utils.readGpioPG(USB_OTG_3399);
            Log.d("sky","status = " + status);
            if (status.contains("peripheral"))
                return "0";
            else if (status.contains("host"))
                return "1";
        }
        return "";
    }

    /**
     * @method pullUpUSBOTG()
     * @description 拉高当前USB-OTG的状态，即断开otg连接
     * @date  20190329
     * @author sky
    */
    public void pullUpUSBOTG() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB_OTG_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB_OTG_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB_OTG_3399),"host");
    }

    /**
     * @method pullDownUSBOTG()
     * @description 拉低当前USB-OTG的状态，即otg连接
     * @date  20190329
     * @author sky
     */
    public void pullDownUSBOTG() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB_OTG_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB_OTG_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB_OTG_3399),"peripheral");
    }

    /**
     * @method getUSB1Status()
     * @description 获取当前USB1的状态
     * @date  20190329
     * @author sky
     * @return 返回当前usb1的连接状态。返回1代表已连接，0代表未连接
     */
    public String getUSB1Status() {
        if (isRk3288) {
            return Utils.readGpioPG(USB1_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB1_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(USB1_3399);
        }else if (isGt8953) {
            return Utils.readGpioPG(USB1_8953_450);
        }
        return "";
    }

    /**
     * @method pullUpUSB1()
     * @description 拉高当前USB1的状态，即连接可用
     * @date  20190329
     * @author sky
     */
    public void pullUpUSB1() {
        if (isRk3288) {
            GpioUtils.writeGpioValue(257,"1");
        }
//           Utils.writeStringFileFor7(new File(USB1_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB1_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB1_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(USB1_8953_450),"1");
    }

    /**
     * @method pullDownUSB1()
     * @description 拉低当前USB1的状态，即断开连接
     * @date  20190329
     * @author sky
     */
    public void pullDownUSB1() {
        if (isRk3288)
            GpioUtils.writeGpioValue(257,"0");
//            Utils.writeStringFileFor7(new File(USB1_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB1_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB1_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(USB1_8953_450),"0");
    }

    /**
     * @method getUSB2Status()
     * @description 获取当前USB2的状态
     * @date  20190329
     * @author sky
     * @return 返回当前usb2的连接状态。返回1代表已连接，0代表未连接
     */
    public String getUSB2Status() {
        if (isRk3288) {
            return Utils.readGpioPG(USB2_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB2_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(USB2_3399);
        }else if (isGt8953) {
            return Utils.readGpioPG(USB2_8953_450);
        }
        return "";
    }

    /**
     * @method pullUpUSB2()
     * @description 拉高当前USB2的状态，即连接可用
     * @date  20190329
     * @author sky
     */
    public void pullUpUSB2() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB2_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB2_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB2_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(USB2_8953_450),"1");
    }

    /**
     * @method pullDownUSB2()
     * @description 拉低当前USB2的状态，即断开连接
     * @date  20190329
     * @author sky
     */
    public void pullDownUSB2() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB2_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB2_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB2_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(USB2_8953_450),"0");
    }

    /**
     * @method getUSB3Status()
     * @description 获取当前USB3的状态
     * @date  20190329
     * @author sky
     * @return 返回当前usb3的连接状态。返回1代表已连接，0代表未连接
     */
    public String getUSB3Status() {
        if (isRk3288) {
            return Utils.readGpioPG(USB3_3288);
//            return GpioUtils.getGpioValue(257);
        }else if (isRk3368) {
            return Utils.readGpioPG(USB3_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(USB3_3399);
        }else if (isGt8953) {
            return Utils.readGpioPG(USB3_8953_450);
        }
        return "";
    }

    /**
     * @method pullUpUSB3()
     * @description 拉高当前USB3的状态，即连接可用
     * @date  20190329
     * @author sky
     */
    public void pullUpUSB3() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB3_3288),"1");
//            GpioUtils.writeGpioValue(257,"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB3_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB3_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(USB3_8953_450),"1");
    }

    /**
     * @method pullDownUSB3()
     * @description 拉低当前USB3的状态，即断开连接
     * @date  20190329
     * @author sky
     */
    public void pullDownUSB3() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(USB3_3288),"0");
//            GpioUtils.writeGpioValue(257,"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(USB3_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(USB3_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(USB3_8953_450),"0");
    }

    /**
     * @method getUSB3Status()
     * @description 获取当前喇叭功放的状态
     * @date  20190329
     * @author sky
     * @return 返回当前喇叭功放的状态。返回1代表有声音输出，0代表没有声音
     */
    public String getVoiceStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(VOICE_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(VOICE_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(VOICE_3399);
        }else if (isGt8953) {
            return Utils.readGpioPG(VOICE_8953_450);
        }
        return "";
    }

    /**
     * @method pullUpUSB3()
     * @description 拉高当前喇叭功放的状态，即打开喇叭
     * @date  20190329
     * @author sky
     */
    public void pullUpVoice() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(VOICE_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(VOICE_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(VOICE_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(VOICE_8953_450),"1");
    }

    /**
     * @method pullDownVoice()
     * @description 拉低当前喇叭功放的状态，即关掉喇叭
     * @date  20190329
     * @author sky
     */
    public void pullDownVoice() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(VOICE_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(VOICE_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(VOICE_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(VOICE_8953_450),"0");
    }

    /**
     * @method getFanStatus()
     * @description 获取当前风扇的状态
     * @date  20190329
     * @author sky
     * @return 返回当前风扇的状态。返回1代表风扇打开，0代表关闭风扇
     */
    public String getFanStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(FAN_CONTROL_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(FAN_CONTROL_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(FAN_CONTROL_3399);
        }else if (isGt8953) {
            return Utils.readGpioPG(FAN_CONTROL_8953_450);
        }
        return "";
    }

    /**
     * @method pullUpFan()
     * @description 拉高当前风扇的状态，即打开风扇
     * @date  20190329
     * @author sky
     */
    public void pullUpFan() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_8953_450),"1");
    }

    /**
     * @method pullDownFan()
     * @description 拉低当前喇叭功放的状态，即关掉喇叭
     * @date  20190329
     * @author sky
     */
    public void pullDownFan() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(FAN_CONTROL_8953_450),"0");

    }

    /**
     * @method getInfraredLedStatus
     * @description 获取当前红外LED的状态
     * @date  20190329
     * @author sky
     * @return 返回当前红外LED的状态。返回1代表打开红外LED，0代表关闭
     */
    public String getInfraredLedStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(INFRARED_LED_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(INFRARED_LED_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(INFRARED_LED_3399);
        }else if (isGt8953) {
            return Utils.readGpioPG(INFRARED_LED_8953_450);
        }
        return "";
    }

    /**
     * @method pullUpInfraredLed()
     * @description 拉高当前红外LED的状态，即打开红外灯
     * @date  20190329
     * @author sky
     */
    public void pullUpInfraredLed() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(INFRARED_LED_8953_450),"1");
    }

    /**
     * @method pullDownInfraredLed()
     * @description 拉低当前红外LED的状态，即关掉红外灯
     * @date  20190329
     * @author sky
     */
    public void pullDownInfraredLed() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(INFRARED_LED_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(INFRARED_LED_8953_450),"0");
    }

    /**
     * @method getRelayStatus
     * @description 获取当前继电器的状态
     * @date  20190329
     * @author sky
     * @return 返回当前继电器的状态。返回1代表打开继电器，0代表关闭
     */
    public String getRelayStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(RELAY_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(RELAY_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(RELAY_3399);
        }else if (isGt8953)
            return Utils.readGpioPG(RELAY_8953_450);
        return "";
    }

    /**
     * @method pullUpRelay()
     * @description 拉高当前继电器的状态，即打开继电器
     * @date  20190329
     * @author sky
     */
    public void pullUpRelay() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RELAY_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RELAY_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RELAY_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(RELAY_8953_450),"1");
    }

    /**
     * @method pullDownRelay()
     * @description 拉低当前继电器的状态，即关掉继电器
     * @date  20190329
     * @author sky
     */
    public void pullDownRelay() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RELAY_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RELAY_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RELAY_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(RELAY_8953_450),"0");
    }

    /**
     * @method getGreenLightStatus
     * @description 获取当前绿色补光的状态
     * @date  20190329
     * @author sky
     * @return 返回当前绿色补光的状态。返回1代表打开绿色补光器，0代表关闭
     */
    public String getGreenLightStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(GREEN_LIGHT_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(GREEN_LIGHT_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(GREEN_LIGHT_3399);
        }else if (isGt8953)
            return Utils.readGpioPG(GREEN_LIGHT_8953_450);
        return "";
    }

    /**
     * @method pullUpGreenLight()
     * @description 拉高当前绿色补光的状态，即打开绿色补光
     * @date  20190329
     * @author sky
     */
    public void pullUpGreenLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_8953_450),"1");
    }

    /**
     * @method pullDownGreenLight()
     * @description 拉低当前绿色补光的状态，即关掉绿色补光
     * @date  20190329
     * @author sky
     */
    public void pullDownGreenLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(GREEN_LIGHT_8953_450),"0");
    }

    /**
     * @method getRedLightStatus()
     * @description 获取当前红色补光的状态
     * @date  20190329
     * @author sky
     * @return 返回当前红色补光的状态。返回1代表打开红色补光器，0代表关闭
     */
    public String getRedLightStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(RED_LIGHT_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(RED_LIGHT_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(RED_LIGHT_3399);
        }else if (isGt8953)
            return Utils.readGpioPG(RED_LIGHT_8953_450);
        return "";
    }

    /**
     * @method pullUpRedLight()
     * @description 拉高当前红色补光的状态，即打开红色补光
     * @date  20190329
     * @author sky
     */
    public void pullUpRedLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(RED_LIGHT_8953_450),"1");
    }

    /**
     * @method pullDownRedLight()
     * @description 拉低当前红色补光的状态，即关掉红色补光
     * @date  20190329
     * @author sky
     */
    public void pullDownRedLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(RED_LIGHT_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(RED_LIGHT_8953_450),"0");
    }

    /**
     * @method getWhiteLightStatus
     * @description 获取当前白色补光的状态
     * @date  20190329
     * @author sky
     * @return 返回当前白色补光的状态。返回1代表打开白色补光器，0代表关闭
     */
    public String getWhiteLightStatus() {
        if (isRk3288) {
            return Utils.readGpioPG(WHITE_LIGHT_3288);
        }else if (isRk3368) {
            return Utils.readGpioPG(WHITE_LIGHT_3368);
        }else if (isRk3399) {
            return Utils.readGpioPG(WHITE_LIGHT_3399);
        }else if (isGt8953)
            return Utils.readGpioPG(WHITE_LIGHT_8953_450);
        return "";
    }

    /**
     * @method pullUpWhiteLight()
     * @description 拉高当前白色补光的状态，即打开白色补光
     * @date  20190329
     * @author sky
     */
    public void pullUpWhiteLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3288),"1");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3368),"1");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3399),"1");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_8953_450),"1");
    }

    /**
     * @method pullDownWhiteLight()
     * @description 拉低当前白色补光的状态，即关掉白色补光
     * @date  20190329
     * @author sky
     */
    public void pullDownWhiteLight() {
        if (isRk3288)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3288),"0");
        else if (isRk3368)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3368),"0");
        else if (isRk3399)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_3399),"0");
        else if (isGt8953)
            Utils.writeStringFileFor7(new File(WHITE_LIGHT_8953_450),"0");
    }

    /**
     * @method getReservedIO1Status()
     * @description 获取预留io1的状态
     * @date  20190329
     * @author sky
     * @return 返回预留io1的状态。返回1代表高电平，0代表低电平
     */
    public String getReservedIO1Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(162);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(91);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1067);
        else if (isGt8953)
            status = GpioUtils.getGpioValue(45);
        return status;
    }

    /**
     * @method getReservedIO2Status()
     * @description 获取预留io2的状态
     * @date  20190329
     * @author sky
     * @return 返回预留io2的状态。返回1代表高电平，0代表低电平
     */
    public String getReservedIO2Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(163);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(90);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1066);
        else if(isGt8953)
            status = GpioUtils.getGpioValue(66);
        return status;
    }

    /**
     * @method getReservedIO3Status()
     * @description 获取预留io3的状态
     * @date  20190329
     * @author sky
     * @return 返回预留io3的状态。返回1代表高电平，0代表低电平
     */
    public String getReservedIO3Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(169);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(111);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1071);
        else if (isGt8953)
            status = GpioUtils.getGpioValue(44);
        return status;
    }

    /**
     * @method getReservedIO4Status()
     * @description 获取预留io4的状态
     * @date  20190329
     * @author sky
     * @return 返回预留io4的状态。返回1代表高电平，0代表低电平
     */
    public String getReservedIO4Status() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioValue(171);
        else if (isRk3368)
            status = GpioUtils.getGpioValue(109);
        else if (isRk3399)
            status = GpioUtils.getGpioValue(1072);
        else if (isGt8953)
            status = GpioUtils.getGpioValue(43);
        return status;
    }

    /**
     * @method getReservedIO4Direction()
     * @description 获取预留io4是输入口还是输出口
     * @date  20190329
     * @author sky
     * @return 返回预留io4的状态。返回out代表输出口，返回in代表输入口
     */
    public String getReservedIO4Direction() {
        String status = "";
        if (isRk3288)
            status = GpioUtils.getGpioDirection(171);
        else if (isRk3368)
            status = GpioUtils.getGpioDirection(109);
        else if (isRk3399)
            status = GpioUtils.getGpioDirection(1072);
        else if (isGt8953)
            status = GpioUtils.getGpioDirection(43);
        return status;
    }

    /**
     * @method setReservedIO4Out()
     * @description 设置io4为输出口
     * @date  20190329
     * @author sky
     */
    public void setReservedIO4Out() {
        if (isRk3288)
            GpioUtils.setGpioDirection(171,0);
        else if (isRk3368)
            GpioUtils.setGpioDirection(109,0);
        else if (isRk3399)
            GpioUtils.setGpioDirection(1072,0);
        else if (isGt8953)
            GpioUtils.setGpioDirection(43,0);
    }

    /**
     * @method setReservedIO4In()
     * @description 设置io4为输入口
     * @date  20190329
     * @author sky
     */
    public void setReservedIO4In() {
        if (isRk3288)
            GpioUtils.setGpioDirection(171,1);
        else if (isRk3368)
            GpioUtils.setGpioDirection(109,1);
        else if (isRk3399)
            GpioUtils.setGpioDirection(1072,1);
        else if (isGt8953)
            GpioUtils.setGpioDirection(43,1);
    }

    /**
     * @method isIO4DirectionOut
     * @description 获取预留io4是否是输出口
     * @date  20190329
     * @author sky
     * @return 返回预留io4的状态。返回true代表是输出口，返回false代表不是输出口
     */
    public boolean isIO4DirectionOut() {
        String direction = "out";
        String temp = "";
        if (isRk3288)
            temp = GpioUtils.getGpioDirection(171);
        else if (isRk3368)
            temp = GpioUtils.getGpioDirection(109);
        else if (isRk3399)
            temp = GpioUtils.getGpioDirection(1072);
        else if (isGt8953)
            temp = GpioUtils.getGpioDirection(43);
        return direction.equals(temp);
    }

    /**
     * @method pullUpIO4()
     * @description 拉高预留io4的状态，只有在io4是输出口时才可执行
     * @date  20190329
     * @author sky
     */
    public void pullUpIO4() {
        if (isRk3288)
            GpioUtils.writeGpioValue(171,"1");
        else if (isRk3368)
            GpioUtils.writeGpioValue(109,"1");
        else if (isRk3399)
            GpioUtils.writeGpioValue(1072,"1");
        else if (isGt8953)
            GpioUtils.writeGpioValue(43,"1");
    }

    /**
     * @method pullDownIO4()
     * @description 拉低预留io4的状态，只有在io4是输出口时才可执行
     * @date  20190329
     * @author sky
     */
    public void pullDownIO4() {
        if (isRk3288)
            GpioUtils.writeGpioValue(171,"0");
        else if (isRk3368)
            GpioUtils.writeGpioValue(109,"0");
        else if (isRk3399)
            GpioUtils.writeGpioValue(1072,"0");
        else if (isGt8953)
            GpioUtils.writeGpioValue(43,"0");
    }

    /**
     * @method setMaxFreq(int value)
     * @description 设置CPU最大频率
     * @date  20190417
     * @author sky
     * @param value，设置的CPU最大频率
     */
    public void setMaxFreq(int value) {
        if (isRk3288) {
            Utils.writeStringFileFor7(new File(SCALING_MAX_FREQ_3288),(value * 1000) + "");
        }
    }

    /**
     * @method setMinFreq(int value)
     * @description 设置CPU最小频率
     * @date  20190417
     * @author sky
     * @param value，设置的CPU最小频率
     */
    public void setMinFreq(int value) {
        if (isRk3288) {
            Utils.writeStringFileFor7(new File(SCALING_MIN_FREQ_3288),(value * 1000) + "");
        }
    }

    /**
     * @method getMaxFreq()
     * @description 获取当前CPU最大频率
     * @date  20190417
     * @author sky
     * @return 返回当前系统CPU的最大频率
     */
    public String getMaxFreq() {
        if (isRk3288) {
            return Utils.readGpioPG(SCALING_MAX_FREQ_3288);
        }
        return "";
    }

    /**
     * @method getMinFreq()
     * @description 获取当前CPU最小频率
     * @date  20190417
     * @author sky
     * @return 返回当前系统CPU的最小频率
     */
    public String getMinFreq() {
        if (isRk3288) {
           return Utils.readGpioPG(SCALING_MIN_FREQ_3288);
        }
        return "";
    }


    private  String getVersion() {
        return getValueFromProp("ro.build.description").substring(0,8);
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

    private boolean isGt8953() {
        return getVersion().contains("msm8953");
    }

    private void rootGpio(int index) {
        if (GpioUtils.exportGpio(index))
            GpioUtils.upgradeRootPermissionForGpio(index);
    }
}
