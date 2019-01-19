package com.ys.rkapi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;


import com.ys.myapi.IgetMessage;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.LogUtils;
import com.ys.rkapi.Utils.NetUtils;
import com.ys.rkapi.Utils.StorageUtils;
import com.ys.rkapi.Utils.TimeUtils;
import com.ys.rkapi.Utils.VersionUtils;
import com.ys.rkapi.Utils.Utils;
import com.ys.rkapi.product.RkFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RYX on 2017/8/30.
 */

public class MyManager {
    private static final String TAG = "MyManager";
    private static MyManager myManager;

    private Context mContext;

    private DisplayManager mDisplayManager;

    private MyManager(Context context) {
        mContext = context;
    }

    public static synchronized MyManager getInstance(Context context) {
        if (myManager == null) {
            myManager = new MyManager(context);
        }
        return myManager;
    }

    private void sendMyBroadcast(String action) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWithExtra(String action, String key, String value) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key, value);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWith2Extras(String action, String key1, String value1, String key2, String value2) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key1, value1);
            intent.putExtra(key2, value2);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWithLongExtra(String action, String key, long value) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key, value);
            mContext.sendBroadcast(intent);
        }
    }


    //获取目前API平台-版本-日期信息，如果API发生修改就需要修改此处
    public String getApiVersion() {///ok
        String verName = "";
        try {
            verName = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    //获取目前设备的型号
    public String getAndroidModle() {//ok
        return VersionUtils.getAndroidModle();
    }

    //获取目前设备的android系统的版本
    public String getAndroidVersion() {  //ok
        return Build.VERSION.SDK;
    }

    //获取设备的硬件内存大小容量。
    public String getRunningMemory() {//ok
        return StorageUtils.getRealMeoSize();
    }

    //获取设备的硬件内部存储大小容量
    public String getInternalStorageMemory() {//ok
        return StorageUtils.getRealSizeOfNand();
    }

    //获取设备的固件SDK版本。
    public String getFirmwareVersion() { // ok
        return "1.0";
    }

    //获取设备的固件内核版本。
    public String getKernelVersion() { // ok
        return VersionUtils.getKernelVersion();
    }

    //获取设备的固件系统版本和编译日期。
    public String getAndroidDisplay() {  //ok
        return VersionUtils.getSystemVersionInfo();
    }

    //获取设备CPU型号
    public String getCPUType() {
        return VersionUtils.getCpuInfo()[0];
    }

    //获取固件编译的时间
    public String getFirmwareDate() {  // ok
        return VersionUtils.getFirmwareDate();
    }


    //关机
    public void shutdown() {  // ok
        sendMyBroadcast(Constant.SHUTDOWN_ACTION);
    }

    //重启
    public void reboot() {  // ok
        sendMyBroadcast(Constant.REBOOT_ACTION);
    }

    // 截屏
    // path  存储的绝对路径 如：/mnt/internal_sd/001.jpg
    public boolean takeScreenshot(String path) { // ok
//        sendMyBroadcastWithExtra(Constant.SCREENSHOOT_ACTION, Constant.SCREENSHOOT_KEY, path);
        boolean flag = false;
        if (igetMessage != null) {
            try {
                flag = igetMessage.isSuccess(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    // 旋转屏幕
    public void rotateScreen(Context context, String degree) {  /// error
        RkFactory.getRK().rotateScreen(context,degree);
    }

    public void setPowerOn(Context context,long powerOnTime) {
        Intent intent = new Intent("com.ys.setPowerOnTime");
        intent.putExtra("powerOnTime",powerOnTime);
        Log.d("chenhuan","powerOnTime = " + powerOnTime);
        context.sendBroadcast(intent);
    }



    public boolean getNavBarHideState() {
        return RkFactory.getRK().getNavBarHideState(mContext);
    }

    //设置显示或隐藏导航
    public void hideNavBar(boolean hide) {  // ok
            Intent intent = new Intent();
            if (!hide) {
                intent.setAction("android.action.adtv.showNavigationBar");
                mContext.sendBroadcast(intent);
            } else {
                intent.setAction("android.action.adtv.hideNavigationBar");
                mContext.sendBroadcast(intent);
            }

    }

    public boolean isSlideShowNavBarOpen() {
        return RkFactory.getRK().isSlideShowNavBarOpen();
    }

    public void setSlideShowNavBar(boolean flag) {
        RkFactory.getRK().setSlideShowNavBar(mContext,flag);
    }

    public boolean isSlideShowNotificationBarOpen() {
        return RkFactory.getRK().isSlideShowNotificationBarOpen();
    }

    public void setSlideShowNotificationBar(boolean flag) {
        RkFactory.getRK().setSlideShowNotificationBar(mContext,flag);
    }

    public  int getDisplayWidth(Context context){
        WindowManager manager = ((Activity)context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public  int getDisplayHeight(Context context){
        WindowManager manager = ((Activity)context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    //关背光
    public void turnOffBackLight() {
        RkFactory.getRK().turnOffBackLight();
    }

    //开背光
    public void turnOnBackLight() {
        RkFactory.getRK().turnOnBackLight();
    }

    public boolean isBacklightOn(){
        return RkFactory.getRK().isBackLightOn();
    }

    public int getSystemBrightness() {
        int systemBrightness;
        int value = 0;
        try {
            systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            value =  systemBrightness * 100 / 255;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }


    public void turnOnHDMI() {
//        Utils.writeFile(new File(Constant.HDMI_STATUS_3399),"on");
        RkFactory.getRK().turnOnHDMI();
//        try {
//            GPIOUtils.writeIntFileUnder7("1",Constant.HDMI_STATUS_3288);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    public void turnOffHDMI() {
//        Utils.writeFile(new File(Constant.HDMI_STATUS_3399),"off");
        RkFactory.getRK().turnOffHDMI();
//        try {
//            GPIOUtils.writeIntFileUnder7("0",Constant.HDMI_STATUS_3288);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    //升级固件
    // 固件存放的绝对路径
    public void upgradeSystem(String absolutePath) {//  ok
        sendMyBroadcastWithExtra(Constant.FIRMWARE_UPGRADE_ACTION, Constant.FIRMWARE_UPGRADE_KEY, absolutePath);
    }

    //重启进入Recovery模式
    public void rebootRecovery() { // ok
        RkFactory.getRK().rebootRecovery();
    }

    //静默安装APK
    //安装成功返回true 否则返回false
    public boolean silentInstallApk(String apkPath) {//////  ok
        return RkFactory.getRK().silentInstallApk(apkPath);
    }

    //设置以太网Mac地址
    public void setEthMacAddress(String val) {
        RkFactory.getRK().setEthMacAddress(mContext,val);
    }

    //获取以太网MAC地址
    public String getEthMacAddress() {////////  ok
        return NetUtils.getEthMAC();
    }

    //获取以太网静态IP地址
    public void bindAIDLService(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GET_ETH_STATIC_IP");
        intent.setComponent(new ComponentName("com.ys.ys_receiver", "com.ys.ys_receiver.AIDLService"));
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBindAIDLService(Context context) {
        context.unbindService(serviceConnection);
    }


    private IgetMessage igetMessage;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            igetMessage = IgetMessage.Stub.asInterface(iBinder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            igetMessage = null;
        }
    };

    //获取当前以太网模式
    public String getEthMode() {
        Log.d(TAG, "获取以太网模式");
        String ethMode = "";
        if (igetMessage != null) {
            try {
                ethMode = igetMessage.getEthMode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ethMode;
    }

    public boolean getEthStatus() {
        boolean flag = false;
        if (igetMessage != null) {
            try {
                flag = igetMessage.getEthStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public boolean isAutoSyncTime() {
        boolean flag = false;
        if (igetMessage != null) {
            try {
                flag = igetMessage.isAutoSyncTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public String getGateway() {
        String gateway = "";
        if (igetMessage != null) {
            try {
                gateway = igetMessage.getGateway();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return gateway;
    }

    public String getNetMask() {
        String mask = "";
        if (igetMessage != null) {
            try {
                mask = igetMessage.getNetMask();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return mask;
    }

    public String getEthDns1() {
        String dns1 = "";
        if (igetMessage != null) {
            try {
                dns1 = igetMessage.getEthDns1();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return dns1;
    }

    public String getEthDns2() {
        String dns2 = "";
        if (igetMessage != null) {
            try {
                dns2 = igetMessage.getEthDns2();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return dns2;
    }


    //设置静态IP
    public void setStaticEthIPAddress(String IPaddr, String gateWay, String mask, String dns1, String dns2) {//ok
        Log.d(TAG, "setEthIPAddress 修改以太网IP");
        NetUtils.setStaticIP(mContext, IPaddr, gateWay, mask, dns1, dns2);
    }

    //获取以太网静态IP
    public String getStaticEthIPAddress() {//ok
        Log.d(TAG, "获取静态IP");
        String staticEthIP = "";
        if (igetMessage != null) {
            try {
                staticEthIP = igetMessage.getStaticIP();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return staticEthIP;
    }

    //设置动态获取IP
    public void setDhcpIpAddress(Context context) {
        Intent intent = new Intent("com.ys.set_dhcp");
        intent.putExtra("useStaticIP",0);
        context.sendBroadcast(intent);
    }

    //获取以太网动态IP地址
    public String getDhcpIpAddress() {//ok
        return NetUtils.getDynamicEthIPAddress(mContext);
    }

    //以太网使能 OnOff true 连接 false断开
    public void ethEnabled(boolean enable) {
        NetUtils.setEthernetEnabled(mContext, enable);
        Log.d(TAG, "mContext == null:::" + (mContext == null));
    }

    public void setSoftKeyboardHidden(boolean hidden) {
        RkFactory.getRK().setSoftKeyboardHidden(hidden);
    }

    // 获取外置SD卡的路径
    public String getSDcardPath() {/////  ok
        return Constant.SD_PATH;
    }

    // 获取外置U盘的路径
    // mum 表示第几个U盘，从0开始。
    // 注意要插入U盘后才能有U盘路径！！！！！
    public String getUSBStoragePath(int num) {  // ok
        if (num < StorageUtils.getAllUSBStorageLocations().size())
            return StorageUtils.getAllUSBStorageLocations().get(num);
        return "";
    }

    // 卸载外部存储，如果外部存储存在的话。
    // path 表示存储路径。
    // !!!!!!!!!!!!注意path 不能错，类似于"/mnt/usb_storage/USB_DISK3"
    public void unmountVolume(String path) {  // ok
        sendMyBroadcastWith2Extras(Constant.MOUNT_USB_ACTION, Constant.MOUNT_ENABLE_KEY, "0", Constant.MOUNT_POINT_KEY, path);
    }

    // 挂载外部存储，如果外部存储存在的话。
    // path 表示存储路径
    // !!!!!!!!!!!!注意path 不能错，类似于"/mnt/usb_storage/USB_DISK3"
    public void mountVolume(String path) {  //ok
        sendMyBroadcastWith2Extras(Constant.MOUNT_USB_ACTION, Constant.MOUNT_ENABLE_KEY, "1", Constant.MOUNT_POINT_KEY, path);
    }

    //读EEPRom
    public String readEEPRom() {
        String s = "";
        if (isRk3128())
            Toast.makeText(mContext, "rk3128暂未实现该功能", Toast.LENGTH_LONG).show();
        else
            s = StorageUtils.getValueFromEEPROM();
        return s;
    }

    //写EEPROM
    public void writeEEPRom(String buffer) {
        if (isRk3128())
            Toast.makeText(mContext, "rk3128暂未实现该功能", Toast.LENGTH_LONG).show();
        else
            StorageUtils.setValueToEEPROM(buffer);
    }

    //获取串口的绝对路径
    public String getUartPath(String uart) {
        if (!TextUtils.isEmpty(uart)) {
            if (uart.toUpperCase().equals("TTYS0")) {
                return "/dev/ttyS0";
            }

            if (uart.toUpperCase().equals("TTYS1")) {
                return "/dev/ttyS1";
            }

            if (uart.toUpperCase().equals("TTYS2")) {
                return "/dev/ttyS2";
            }

            if (uart.toUpperCase().equals("TTYS3")) {
                return "/dev/ttyS3";
            }

            if (uart.toUpperCase().equals("TTYS4")) {
                return "/dev/ttyS4";
            }

            if (uart.toUpperCase().equals("TTYS5")) {
                return "/dev/ttyS5";
            }
        }
        return "";
    }

    //设置USB 的电源， 返回true表示设置成功, 目前只是针对OTG口起作用
    //value 0 表示关
    //value 1 表示开
    public boolean setUsbPower(int type, int num, int value) {
        Utils.do_exec("busybox echo " + value + " > " + Constant.USB_OTG_IO);
        return true;
    }

    public String getGPIOStatus(String path) {
         return GPIOUtils.readGpioPG("/sys/devices/misc_power_en.23/" + path);
    }

    public void setGPIOStatus(String value,String path) {
        try {
            GPIOUtils.writeIntFileUnder7(value,"/sys/devices/misc_power_en.23/" + path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isRk3128() {
        return "rk3128".endsWith(getAndroidModle());
    }

    // 读取IO口的状态，返回0或者1
    public int readGpioValue(int io) {
        return GPIOUtils.readGpio(io);
    }

    //设置IO口的状态， value为0或者1
    public void setGpioValue(int io, int value) {
        // 外部传入的IO值只能是0或者1
        if (value == 0 || value == 1) {
            GPIOUtils.writeGpio(io, value);
        }
    }

    // 开关耳机麦克风, value 0 关， value 1 开
    public void setHeadsetMicOnOff(int value) {

    }

    // 设置时间  24小时制
    public void setTime(int year, int month, int day, int hour, int minute,int sec) {////// ok
        sendMyBroadcastWithLongExtra(Constant.UPDATE_TIME_ACTION, Constant.UPDATE_TIME_KEY, TimeUtils.getTimeMills(year, month, day, hour, minute,sec));
    }

    public void setTime(long currentTimeMillis) {///////ok
        sendMyBroadcastWithExtra(Constant.UPDATE_TIME_ACTION, Constant.UPDATE_TIME_KEY, currentTimeMillis + "");
    }

    // 执行su 命令
    public void execSuCmd(String command) {///// ok
        Utils.execFor7(command);
    }

    // 获取android logcat
    // path 指定输出logcat 的绝对路径，如 /mnt/internal_sd/logcat.txt
    public void getAndroidLogcat(String path) {//// ok
        LogUtils.startLog(path);
    }

    public void stopAndroidLogcat() {//// ok
        LogUtils.stopLog();
    }


    //获取当前的上网类型
    //返回值 0表示以太网
    //返回值 1表示WIFI
    //返回值 2表示移动网络
    //返回值 -100表示未知网络
    public int getCurrentNetType() {  // ok
        int realNetType = NetUtils.getNetWorkType(mContext);
        if (realNetType == 9)
            return Constant.NET_TYPE_ETH;
        if (realNetType == 1)
            return Constant.NET_TYPE_WIFI;
        if (realNetType == 0)
            return Constant.NET_TYPE_MOBILE;
        return Constant.NETWORK_UNKNOW;
    }

    //开关一些外设的电源IO口
    // type外设类型
    public void setDevicePower(Device device, int values) {
        switch (device) {
            case HDMI:
                break;
            case LAN:
                break;
            case SPEAKER:
                break;
            case WIFI:
                break;
            case _3G: //3G模块
                break;
            case SD:
                break;
            case LED:
                break;
            default:
                break;
        }

    }

    // 获取当前屏幕数，默认返回1，有双屏时返回2
    public int getScreenNumber() {
        mDisplayManager = (DisplayManager) mContext.getSystemService(
                Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();
        return displays.length;
    }

    // 获取HDMI IN 的状态
    // 返回0没有HDMI输入， 1有HDMI输入
    public boolean getHdmiinStatus() {
        return GPIOUtils.isHDMIOut();
    }

    //setPowerOnTime
    public void setSystemPowerOnTime(int year, int month, int day, int hourOfDay, int minute) {
        TimeUtils.setPowerOnTime(mContext, year, month, day, hourOfDay, minute);
    }

    //清除系统时间
    public void clearSystemPowerOnTime() {
        TimeUtils.clearPowerOnTime(mContext);
    }

    //设置开关机模式
    public void setSystemPowerOnOffMode(int value) {
        TimeUtils.setPowerOnMode(value);
    }

    //看门狗使能
//    public void setWatchDogEnable(int enable) {
//        TimeUtils.WatchDogEnable(enable);
//    }

    //喂狗
//    public void watchDogFeedTime() {
//        TimeUtils.WatchDogFeed();
//    }

    //控制自动确定日期和时间的开关
    public void switchAutoTime(boolean open) {
        Intent intent = new Intent("com.ys.switch_auto_set_time");
        intent.putExtra("switch_auto_time",open);
        mContext.sendBroadcast(intent);
    }

    public void getKmsgLog(String path) {
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        String lineText = null;
        List<String> txtLists = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec("dmesg");
            reader = new InputStreamReader(process.getInputStream());
            bufferedReader = new BufferedReader(reader);
            while ((lineText = bufferedReader.readLine()) != null) {
                txtLists.add(lineText);
            }
            StringBuffer buffer = new StringBuffer();
            for (String s : txtLists) {
                buffer.append(s + "\n");
            }
            LogUtils.saveToSDCard(path,buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeScreenLight(int value) {
        RkFactory.getRK().changeScreenLight(mContext,value);

    }

    public void setDormantInterval(Context context,long time) {
        RkFactory.getRK().setDormantInterval(context,time);
    }

//获取以太网的IP地址
//    public String getEthIPAddress() {//ok
//        return NetUtils.getEthIPAddress();
//    }

    //设置静态IP
    //返回以太网IP地址
//    public String setEthIPAddress(String IPaddr, String mask, String gateWay, String dns1, String dns2) {//ok
//        NetUtils.setStaticIP(mContext, IPaddr, gateWay, mask, dns1, dns2);
//        return NetUtils.getEthIPAddress();
//    }
}
