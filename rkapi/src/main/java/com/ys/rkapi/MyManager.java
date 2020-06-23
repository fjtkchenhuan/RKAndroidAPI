package com.ys.rkapi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
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
import com.ys.rkapi.product.YsFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                 intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWithExtra(String action, String key, String value) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key, value);
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWith2Extras(String action, String key1, String value1, String key2, String value2) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key1, value1);
            intent.putExtra(key2, value2);
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWithLongExtra(String action, String key, long value) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key, value);
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * @method getApiVersion()
     * @description 获取目前API平台-版本-日期信息，如果API发生修改就需要修改此处
     * @date  20180602
     * @author sky
     * @return 当前API的版本信息
    */
    public String getApiVersion() {
        return "V5.0-20200529";
    }

    /**
     * @method getAndroidModle()
     * @description 获取目前设备的型号，例如rk3288、rk3399
     * @date  20180602
     * @author sky
     * @return 设备型号
     *
    */
    public String getAndroidModle() {
        return VersionUtils.getAndroidModle();
    }

    //
    /**
     * @method getAndroidVersion()
     * @description 获取目前设备的android系统的版本，例如7.1就返回25
     * @date  20180602
     * @author sky
     * @return android系统的版本
    */
    public String getAndroidVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * @method getRunningMemory()
     * @description 获取设备的硬件内存大小容量
     * @date  20180602
     * @author sky
     * @return 内存大小
    */
    public String getRunningMemory() {//ok
        return StorageUtils.getRealMeoSize();
    }

    //获取设备的硬件内部存储大小容量
    /**
     * @method getInternalStorageMemory()
     * @description 获取设备的存储大小容量
     * @date 20180602
     * @author sky
     * @return 设备内部存储大小
    */
    public String getInternalStorageMemory() {//ok
        return StorageUtils.getRealSizeOfNand();
    }

    /**
     * @method getFirmwareVersion()
     * @description 获取设备的固件SDK版本。
     * @date  20180602
     * @author sky
     * @return 设备SDK版本
    */
    public String getFirmwareVersion() {
        return "1.0";
    }

    //获取设备的固件内核版本。
    /**
     * @method getKernelVersion()
     * @description 获取设备的固件内核版本
     * @date  20180602
     * @author sky
     * @return 内核版本
    */
    public String getKernelVersion() { // ok
        return VersionUtils.getKernelVersion();
    }

    //获取设备的固件系统版本和编译日期。
    /**
     * @method  getAndroidDisplay()
     * @description  获取固件版本号
     * @date  20180602
     * @author sky
     * @return  设备的固件版本号
    */
    public String getAndroidDisplay() {  //ok
        return VersionUtils.getSystemVersionInfo();
    }

    //获取设备CPU型号
    /**
     * @method getCPUType()
     * @description 获取设备CPU型号
     * @date  20190118
     * @author sky
     * @return 设备CPU型号
    */
    public String getCPUType() {
        return VersionUtils.getCpuInfo()[0];
    }

    //获取固件编译的时间
    /**
     * @method getFirmwareDate()
     * @description 固件的编译时间
     * @date  20180602
     * @author sky
     * @return 固件编译的时间，比如20180602
    */
    public String getFirmwareDate() {  // ok
        return VersionUtils.getFirmwareDate();
    }


    //关机
    /**
     * @method shutdown()
     * @description 执行关机操作，走安卓标准关机流程
     * @date  20180602
     * @author sky
    */
    public void shutdown() {  // ok
        sendMyBroadcast(Constant.SHUTDOWN_ACTION);
    }

    //重启
    /**
     * @method reboot()
     * @description 执行重启操作，走安卓标准重启流程
     * @date  20180602
     * @author sky
    */
    public void reboot() {  // ok
        sendMyBroadcast(Constant.REBOOT_ACTION);
    }

    // 截屏
    // path  存储的绝对路径 如：/mnt/internal_sd/001.jpg
    /**
     * @method takeScreenshot
     * @description 截屏，执行此方法可将系统当前显示画面截图保存在指定路径
     * @date  20180602
     * @author sky
     * @param path，保存图片的路径。例如/mnt/internal_sd/001.jpg
     * @return 是否截图成功，true成功，false失败
     *
    */
    public boolean takeScreenshot(String path) {
//        sendMyBroadcastWithExtra(Constant.SCREENSHOOT_ACTION, Constant.SCREENSHOOT_KEY, path);
        Log.d("sky","takeScreenshot takeScreenshot");
        boolean flag = false;
        if (igetMessage != null) {
            try {
                flag = igetMessage.isSuccessScreenshot(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * @method rotateScreen(Context context, String degree)
     * @description 旋转屏幕方向，0度、90度、180度和270度
     * @date 20180602
     * @author sky
     * @param context，上下文对象。degree，旋转的角度（0/90/180/270）
    */
    public void rotateScreen(Context context, String degree) {
        YsFactory.getRK().rotateScreen(context, degree);
    }

    public void setPowerOn(Context context, long powerOnTime) {
        Intent intent = new Intent("com.ys.setPowerOnTime");
        intent.putExtra("powerOnTime", powerOnTime);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        Log.d("chenhuan", "powerOnTime = " + powerOnTime);
        context.sendBroadcast(intent);
    }


    /**
     * @method getNavBarHideState()
     * @description 获取导航栏的状态
     * @date  20180602
     * @author sky
     * @return 导航栏隐藏返回true，显示则返回false
    */
    public boolean getNavBarHideState() {
        return YsFactory.getRK().getNavBarHideState(mContext);
    }

    /**
     * @method hideNavBar(boolean hide)
     * @description 设置显示或隐藏导航
     * @date  20180602
     * @author sky
     * @param hide，隐藏导航栏传入true，显示传入false
    */
    public void hideNavBar(boolean hide) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT>19)
            intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        if (!hide) {
            intent.setAction("android.action.adtv.showNavigationBar");
            mContext.sendBroadcast(intent);
        } else {
            intent.setAction("android.action.adtv.hideNavigationBar");
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * @method isSlideShowNavBarOpen()
     * @description 查询滑出导航栏选项是否打开
     * @date  20180602
     * @author sky
     * @return 滑出导航栏打开返回true，关闭返回false
    */
    public boolean isSlideShowNavBarOpen() {
        return YsFactory.getRK().isSlideShowNavBarOpen();
    }

    /**
     * @method setSlideShowNavBar(boolean flag)
     * @description 打开或关闭滑出导航栏
     * @date 20180602
     * @author sky
     * @param flag，打开滑出导航栏传入true，关闭传入false
    */
    public void setSlideShowNavBar(boolean flag) {
        YsFactory.getRK().setSlideShowNavBar(mContext, flag);
    }

    /**
     * @method isSlideShowNotificationBarOpen()
     * @description 查询下拉通知栏是否打开
     * @date  20180602
     * @author sky
     * @return 下拉通知栏打开返回true，否则返回false
    */
    public boolean isSlideShowNotificationBarOpen() {
        return YsFactory.getRK().isSlideShowNotificationBarOpen();
    }

    /**
     * @method setSlideShowNotificationBar(boolean flag)
     * @description 设置 打开或关闭下拉通知栏
     * @date 20180602
     * @author sky
     * @param flag，打开下拉通知栏传入true，禁止下拉通知栏传入false
    */
    public void setSlideShowNotificationBar(boolean flag) {
        YsFactory.getRK().setSlideShowNotificationBar(mContext, flag);
    }

    /**
     * @method getDisplayWidth(Context context)
     * @description 获取屏幕宽
     * @date 20180602
     * @author sky
     * @param context，上下文对象
     * @return 屏幕宽度
    */
    public int getDisplayWidth(Activity context) {
//        WindowManager manager = ((Activity) context).getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        return outMetrics.widthPixels;
        Point point = new Point();
        context.getWindowManager().getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    /**
     * @method getDisplayHeight(Context context)
     * @description 获取屏幕高
     * @date  20180602
     * @author sky
     * @param context，上下文对象
     * @return 屏幕高度
    */
    public int getDisplayHeight(Activity context) {
//        WindowManager manager = ((Activity) context).getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        return outMetrics.heightPixels;
        Point point = new Point();
        context.getWindowManager().getDefaultDisplay().getRealSize(point);
        return point.y;
    }


    /**
     * @method turnOffBackLight()
     * @description 关闭屏幕背光，仅仅是关闭背光，其他系统功能不影响
     * @date  20180602
     * @author sky
    */
    public void turnOffBackLight() {
        YsFactory.getRK().turnOffBackLight();
    }

    /**
     * @method turnOnBackLight()
     * @description 打开屏幕背光，跟turnOffBackLight()方法相对应
     * @date  20180602
     * @author sky
     */
    public void turnOnBackLight() {
        YsFactory.getRK().turnOnBackLight();
    }

    /**
     * @method isBacklightOn()
     * @description 获取背光是否打开
     * @date  20181122
     * @author sky
     * @return 当前背光是开返回true，否则返回false
    */
    public boolean isBacklightOn() {
        return YsFactory.getRK().isBackLightOn();
    }

    /**
     * @method getSystemBrightness() 
     * @description 获取当前设备背光亮度
     * @date  20181122
     * @author sky
     * @return int，返回的亮度范围是0-100
    */
    public int getSystemBrightness() {
        int systemBrightness;
        int value = 0;
        try {
            systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            value = systemBrightness * 100 / 255;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * @method turnOnHDMI()
     * @description 打开HDMI输出，与turnOffHDMI()相对应
     * @date 20180602
     * @author sky
    */
    public void turnOnHDMI() {
        YsFactory.getRK().turnOnHDMI();
    }

    /**
     * @method turnOffHDMI()
     * @description 关闭HDMI输出，仅仅关闭信号输出，其他系统功能不影响
     * @date 20180602
     * @author sky
     */
    public void turnOffHDMI() {
        YsFactory.getRK().turnOffHDMI();
    }

    //升级固件
    // 固件存放的绝对路径
    /**
     * @method upgradeSystem(String absolutePath)
     * @description 升级固件
     * @date 20180602
     * @author sky
     * @param absolutePath，待升级固件放置的绝对路径
    */
    public void upgradeSystem(String absolutePath) {
        if (Build.VERSION.SDK.equals("27")) {
            Intent intent = new Intent();
            intent.setAction(Constant.FIRMWARE_UPGRADE_ACTION);
            intent.putExtra(Constant.FIRMWARE_UPGRADE_KEY, absolutePath);
            intent.setPackage("com.ys.gtupdatezip");
            mContext.sendBroadcast(intent);
        }else {
            sendMyBroadcastWithExtra(Constant.FIRMWARE_UPGRADE_ACTION, Constant.FIRMWARE_UPGRADE_KEY, absolutePath);
        }
    }

    /**
     * @method setUpdateSystemWithDialog(boolean flag)
     * @description 设置升级固件时是否弹窗提示
     * @date  20200327
     * @author sky
     * @param flag，true，弹窗提示，false，不弹窗直接升级
    */
    public void setUpdateSystemWithDialog(boolean flag) {
        Utils.setValueToProp("persist.sys.ota.customdefine","true");
        if (flag)
            Utils.setValueToProp("persist.sys.ota.noclick","0");
        else
            Utils.setValueToProp("persist.sys.ota.noclick","1");
    }

    /**
     * @method setUpdateSystemDelete(boolean flag)
     * @description 设置升级固件时后，固件包是否从文件中删除
     * @date  20200327
     * @author sky
     * @param flag，true，删除，false，不删除
     */
    private void setUpdateSystemDelete(boolean flag) {
        Utils.setValueToProp("persist.sys.ota.customdefine","true");
        if (flag)
            Utils.setValueToProp("persist.sys.ota.delete","1");
        else
            Utils.setValueToProp("persist.sys.ota.delete","0");
    }

    //重启进入Recovery模式
    /**
     * @method rebootRecovery()
     * @description 恢复出厂设置
     * @date 20180602
     * @author sky
    */
    public void rebootRecovery() { // ok
        if (Build.VERSION.SDK_INT >= 25) {
            Intent intent = new Intent("com.ys.recovery_system");
            intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        } else
            YsFactory.getRK().rebootRecovery(mContext);
    }

    //静默安装APK
    //安装成功返回true 否则返回false
    /**
     * @method silentInstallApk(String apkPath)
     * @description 静默安装apk，采用的方法是pm install -r
     * @date 20180602
     * @author sky
     * @param apkPath，需要安装的apk的绝对路径
     * @return 静默安装成功返回true，否则返回false
    */
    public boolean silentInstallApk(String apkPath) {//////  ok
        if (Utils.isRoot())
            return YsFactory.getRK().silentInstallApk(apkPath);
        else {
            Intent intent = new Intent("com.ys.silent_install");
            intent.putExtra("isStartApk", false);
            intent.putExtra("path", apkPath);
            mContext.sendBroadcast(intent);
            return true;
        }
    }

    //设置以太网Mac地址
    public void setEthMacAddress(String val) {
        YsFactory.getRK().setEthMacAddress(mContext, val);
    }

    //获取以太网MAC地址
    /**
     * @method getEthMacAddress()
     * @description 获取以太网mac地址
     * @date 20180602
     * @author sky
     * @return 返回以太网mac地址，例如30:1F:9A:61:BA:8F
    */
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

    /**
     * @method  getEthMode()
     * @description 获取以太网的模式，静态或动态
     * @date 20180602
     * @author sky
     * @return 静态模式返回StaticIp，动态模式返回DHCP
    */
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

    /**
     * @method getEthStatus()
     * @description 获取以太网的开关状态
     * @date  20180806
     * @author sky
     * @return 以太网开关打开返回true，开关关闭返回false
    */
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

    /**
     * @method isAutoSyncTime() 
     * @description 获取自动确定网络时间的开关状态
     * @date  20180806
     * @author sky
     * @return 返回true说明开启了自动确定网络时间，返回false说明关闭了此开关
    */
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

    /**
     * @method getGateway()
     * @description 获取以太网的网关
     * @date  20180806
     * @author sky
     * @return 返回当前设备以太网的网关，例如192.168.1.1
    */
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

    /**
     * @method getNetMask()
     * @description 获取以太网的子网掩码
     * @date  20180806
     * @author sky
     * @return 返回当前设备以太网的子网掩码，例如255.255.255.0
    */
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

    /**
     * @method getEthDns1()
     * @description 获取以太网的DNS1
     * @date  20180806
     * @author sky
     * @return 返回当前设备以太网的DNS1，例如192.168.1.1
     */
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

    /**
     * @method getEthDns2()
     * @description 获取以太网的DNS2
     * @date  20180806
     * @author sky
     * @return 返回当前设备以太网的DNS2，例如192.168.1.1
     */
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
    /**
     * @method setStaticEthIPAddress(String IPaddr, String gateWay, String mask, String dns1, String dns2)
     * @description 设置以太网的模式为静态，并指定相关参数
     * @date 20180602
     * @author sky
     * @param  IPaddr，设置的IP地址。gateWay，设置的网关。mask，设置的子网掩码。dns1和dns2是设置的dns值
    */
    public void setStaticEthIPAddress(String IPaddr, String gateWay, String mask, String dns1, String dns2) {//ok
        NetUtils.setStaticIP(mContext, IPaddr, gateWay, mask, dns1, dns2);
    }

    //获取以太网静态IP
    /**
     * @method getStaticEthIPAddress()
     * @description 获取以太网的静态IP
     * @date  20180602
     * @author sky
     * @return 返回以太网的静态ip地址
    */
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
    /**
     * @method setDhcpIpAddress(Context context)
     * @description 设置以太网的模式为动态
     * @date 20180602
     * @author sky
     * @param context，上下文对象
    */
    public void setDhcpIpAddress(Context context) {
        Intent intent = new Intent("com.ys.set_dhcp");
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        intent.putExtra("useStaticIP", 0);
        context.sendBroadcast(intent);
    }

    public void setPppoeDial(Context context,String userName,String password) {
        Intent intent = new Intent("com.ys.set_pppoe_dial");
        intent.putExtra("userName",userName);
        intent.putExtra("password",password);
        context.sendBroadcast(intent);
    }

    //获取以太网动态IP地址
    /**
     * @method getDhcpIpAddress()
     * @description 获取以太网的动态IP地址
     * @date  20180602
     * @author sky
     * @return 返回以太网动态ip地址
    */
    public String getDhcpIpAddress() {
        String address = "";
        if (Build.VERSION.SDK_INT >= 27) {
            if (igetMessage != null) {
                try {
                    address = igetMessage.getDhcpIpAddress();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else
            address = NetUtils.getDynamicEthIPAddress(mContext);
        return address;
    }

    //以太网使能 OnOff true 连接 false断开
    /**
     * @method ethEnabled(boolean enable)
     * @description 控制以太网开关
     * @date 20180602
     * @author sky
     * @param enable，打开以太网开关传入true，关闭以太网开关传入false
    */
    public void ethEnabled(boolean enable) {
        if (Build.VERSION.SDK_INT >= 27) {
            Intent intent = new Intent(Constant.SET_ETH_ENABLE_ACTION);
            intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            intent.putExtra(Constant.ETH_MODE, enable);
            mContext.sendBroadcast(intent);
        } else
            NetUtils.setEthernetEnabled(mContext, enable);
    }

    /**
     * @method setSoftKeyboardHidden(boolean hidden)
     * @description 控制软键盘是否弹出（主要是EditText控件）
     * @date 20181126
     * @author sky
     * @param hidden，传入true，说明隐藏软键盘，传入false就是显示软键盘
    */
    public void setSoftKeyboardHidden(boolean hidden) {
        YsFactory.getRK().setSoftKeyboardHidden(hidden);
    }

    // 获取外置SD卡的路径
    /**
     * @method getSDcardPath()
     * @description 获取外置SD卡路径
     * @date 20180602
     * @author sky
     * @return 返回外置SD卡路径
    */
    public String getSDcardPath() {/////  ok
        return Constant.SD_PATH;
    }

    // 获取外置U盘的路径
    // mum 表示第几个U盘，从0开始。
    // 注意要插入U盘后才能有U盘路径！！！！！
    /**
     * @method getUSBStoragePath(int num)
     * @description 获取外置U盘路径
     * @date 20180602
     * @author sky
     * @param num，从0开始，表示第几个U盘
     * @return u盘的路径
    */
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
    /**
     * @method getUartPath(String uart)
     * @description 获取串口的绝对路径
     * @date 20180602
     * @author sky
     * @param uart，传入的是串口的序列号，比如串口0就传入TTYS0
     * @return 返回指定串口的绝对路径
    */
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

    public void setGPIOStatus(String value, String path) {
        try {
            GPIOUtils.writeIntFileUnder7(value, "/sys/devices/misc_power_en.23/" + path);
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
    /**
     * @method setTime(int year, int month, int day, int hour, int minute, int sec)
     * @description 设置系统时间
     * @date  20180602
     * @author sky
     * @param year,传入需要设置的年月日时分秒，其中月份从1-12，时间按照24小时制
    */
    public void setTime(int year, int month, int day, int hour, int minute, int sec) {////// ok
        sendMyBroadcastWithLongExtra(Constant.UPDATE_TIME_ACTION, Constant.UPDATE_TIME_KEY, TimeUtils.getTimeMills(year, month, day, hour, minute, sec));
    }

    public void setTime(long currentTimeMillis) {///////ok
        sendMyBroadcastWithExtra(Constant.UPDATE_TIME_ACTION, Constant.UPDATE_TIME_KEY, currentTimeMillis + "");
    }


    // 执行su 命令
    /**
     * @method execSuCmd(String command)
     * @description 在root权限下运行shell命令
     * @date 20180602
     * @author sky
     * @param command，传入需要执行的shell命令，比如reboot
    */
    public void execSuCmd(String command) {///// ok
        Utils.execFor7(command);
    }

    // 获取android logcat
    // path 指定输出logcat 的绝对路径，如 /mnt/internal_sd/logcat.txt
    /**
     * @method getAndroidLogcat(String path)
     * @description 获取安卓层日志
     * @date 20180602
     * @author sky
     * @param path，日志保存的路径
    */
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
    /**
     * @method getCurrentNetType()
     * @description 获取当前网络类型
     * @date 20180602
     * @author sky
     * @return 返回int值，0表示以太网，1表示WIFI，2表示移动网络，-100表示未知网络
    */
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
    /**
     * @method getScreenNumber()
     * @description 获取当前设备连接的屏幕数
     * @date  20180602
     * @author sky
     * @return 设备连接几个屏就会返回几
    */
//    public int getScreenNumber() {
//        mDisplayManager = (DisplayManager) mContext.getSystemService(
//                Context.DISPLAY_SERVICE);
//        Display[] displays = mDisplayManager.getDisplays();
//        return displays.length;
//    }

    // 获取HDMI IN 的状态
    // 返回0没有HDMI输入， 1有HDMI输入
    /**
     * @method getHdmiinStatus()
     * @description 获取HDMI连接情况
     * @date  20180602
     * @author sky
     * @return  HDMI有输出返回true，否则返回false
    */
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
    /**
     * @method switchAutoTime(boolean open)
     * @description 控制自动确定日期和时间的开关
     * @date 20180602
     * @author sky
     * @param open，传入true就是打开开关，false关闭开关
    */
    public void switchAutoTime(boolean open) {
        Intent intent = new Intent("com.ys.switch_auto_set_time");
        intent.putExtra("switch_auto_time", open);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        mContext.sendBroadcast(intent);
    }

    /**
     * @method setLanguage(String language, String country)
     * @description 设置当前系统默认语言
     * @date  20190513
     * @author sky
     * @param language，语言，如zh。country，国家，如CN
    */
    public void setLanguage(String language, String country) {
        Intent intent = new Intent("com.ys.set_language");
        intent.putExtra("language", language);
        intent.putExtra("country", country);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        mContext.sendBroadcast(intent);
    }

    /**
     * @method getKmsgLog(String path)
     * @description 获取kernel层日志
     * @date 20180602
     * @author sky
     * @param path，日志保存的路径
    */
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
            LogUtils.saveToSDCard(path, buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @method changeScreenLight(int value)
     * @description 设置屏幕亮度
     * @date 20180828
     * @author sky
     * @param value，是按照1到100设置的，设置100即代表最大亮度，1代表最小亮度
    */
    public void changeScreenLight(int value) {
        YsFactory.getRK().changeScreenLight(mContext, value);
    }

    /**
     * @method setDormantInterval(Context context, long time)
     * @description 休眠时间的控制
     * @date 20181129
     * @author sky
     * @param context，上下文对象。time，间隔时间（在间隔时间内没操作系统进入休眠）
    */
    public void setDormantInterval(Context context, long time) {
        YsFactory.getRK().setDormantInterval(context, time);
    }

    public void awaken() {
        YsFactory.getRK().awaken();
    }

    //获取设置默认输入法是否成功
    /**
     * @method isSetDefaultInputMethodSuccess(String defaultInputMethod)
     * @description 设置默认输入法，并判断是否设置成功
     * @date 20190228
     * @author sky
     * @param defaultInputMethod，需要设置的输入法的包名，例如谷歌拼音输入法"com.google.android.inputmethod.pinyin/.PinyinIME"
     * @return 设置成功返回true，失败返回false
    */
    public boolean isSetDefaultInputMethodSuccess(String defaultInputMethod) {//ok
        Log.d(TAG, "isSetDefaultInputMethodSuccess isSetDefaultInputMethodSuccess");
        boolean isSuccess = false;
        if (igetMessage != null) {
            try {
                isSuccess = igetMessage.isSetDefaultInputMethodSuccess(defaultInputMethod);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    //获取当前的输入法
    /**
     * @method getDefaultInputMethod()
     * @description 获取当前系统输入法
     * @date 20190228
     * @author sky
     * @return 返回当前输入法的包名，例如com.google.android.inputmethod.pinyin/.PinyinIME
    */
    public String getDefaultInputMethod() {
        String defaultInputMethod = "";
        if (igetMessage != null) {
            try {
                defaultInputMethod = igetMessage.getDefaultInputMethod();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return defaultInputMethod;
    }

    //获取CPU温度
    /**
     * @method getCPUTemperature()
     * @description 获取当前系统CPU温度
     * @date  20190619
     * @author sky
     * @return 返回int值，单位是摄氏度
    */
    public int getCPUTemperature() {
        if (Build.MODEL.contains("312")||Build.VERSION.SDK_INT==22) {
            Toast.makeText(mContext, "暂不支持该功能", Toast.LENGTH_SHORT).show();
        }
        return YsFactory.getRK().getCPUTemperature();
    }

    /**
     * @method setADBOpen(boolean open)
     * @description 打开或关闭adb连接
     * @date 20190628
     * @author sky
     * @param open，true，打开adb连接开关。false，关闭adb连接
    */
    public void setADBOpen(boolean open) {
        YsFactory.getRK().setADBOpen(open);
    }

    /**
     * @method replaceBootanimation(String path)
     * @description 替换开机动画
     * @date  20190628
     * @author sky
     * @param path，开机动画bootanimation.zip所在的绝对路径
    */
    public void replaceBootanimation(String path) {
        if (Build.VERSION.SDK_INT <= 27) {
            String[] commands = new String[7];
            commands[0] = "mount -o rw,remount -t ext4 /system";
            commands[1] = "rm -rf system/media/bootanimation.zip";
            commands[2] = "cp  " + path + " system/media/bootanimation.zip";
            commands[3] = "chmod 644 system/media/bootanimation.zip";
            commands[4] = "sync";
            commands[5] = "mount -o ro,remount -t ext4 /system";
            commands[6] = "reboot";
            for (int i = 0; i < commands.length; i++)
                Utils.execFor7(commands[i]);
        } else {
            String[] commands = new String[7];
            commands[0] = "mount -o rw,remount -t ext4 /oem";
            commands[1] = "rm -rf oem/media/bootanimation.zip";
            commands[2] = "cp  " + path + " oem/media/bootanimation.zip";
            commands[3] = "chmod 644 oem/media/bootanimation.zip";
            commands[4] = "sync";
            commands[5] = "mount -o ro,remount -t ext4 /oem";
            commands[6] = "reboot";
            for (int i = 0; i < commands.length; i++)
                Utils.execFor7(commands[i]);
        }
    }

    private void setScreenAndVoiceOpen(boolean open) {
        if ("25".equals(Build.VERSION.SDK)) {
            if (open) {
                GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "0");
//            GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"),"1");
            } else {
                GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "1");
                GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"), "1");
            }
        } else {
            if (open) {
                try {
                    GPIOUtils.writeIntFileUnder7("1", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    GPIOUtils.writeIntFileUnder7("0", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
                    GPIOUtils.writeIntFileUnder7("1", "/sys/bus/i2c/devices/2-0010/spkmode");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setStandByMode() {
        if ("25".equals(Build.VERSION.SDK)) {
            GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "1");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/green_led"), "0");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/red_led"), "0");
            GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"), "1");
        } else {
            try {
                GPIOUtils.writeIntFileUnder7("0", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
                GPIOUtils.writeIntFileUnder7("1", "/sys/bus/i2c/devices/2-0010/spkmode");
                GPIOUtils.writeIntFileUnder7("0", "/sys/devices/misc_power_en.23/green_led");
                GPIOUtils.writeIntFileUnder7("0", "/sys/devices/misc_power_en.23/red_led");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setNormalMode() {
        if ("25".equals(Build.VERSION.SDK)) {
            GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "0");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/green_led"), "1");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/red_led"), "1");
//        GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"),"1");
        } else {
            try {
                GPIOUtils.writeIntFileUnder7("1", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
//                GPIOUtils.writeIntFileUnder7("1","/sys/bus/i2c/devices/2-0010/spkmode");
                GPIOUtils.writeIntFileUnder7("1", "/sys/devices/misc_power_en.23/green_led");
                GPIOUtils.writeIntFileUnder7("1", "/sys/devices/misc_power_en.23/red_led");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @method setDefaultLauncher(String packageAndClassName)
     * @description 设置系统默认Launcher
     * @date  20190719
     * @author sky
     * @param packageAndClassName，设置的Launcher的包名和启动类名，例如"com.android.launcher3/com.android.launcher3.Launcher"
    */
    public void setDefaultLauncher(String packageAndClassName) {
        Intent intent = new Intent("com.ys.setDefaultLauncher");
        intent.putExtra("defaultLauncher", packageAndClassName);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        mContext.sendBroadcast(intent);
    }

    /**
     * @method setPowerOnOffWithWeekly(int[] powerOnTime,int[] powerOffTime,int[] weekdays)
     * @description 周模式设置定时开关机，一天只有一组开关机时间
     * @date 20200113
     * @author sky
     * @param powerOnTime，开机时间，例如{8,30}。powerOffTime，关机时间，例如{18,30}。
     *        weekdays，周一到周日工作状态,1为开机，0为不开机。例如{1,1,1,1,1,0,0}，是指周一到周五执行开关机
    */
    public void setPowerOnOffWithWeekly(int[] powerOnTime,int[] powerOffTime,int[] weekdays) {
        Intent intent = new Intent("android.intent.action.setyspoweronoff");
        intent.putExtra("timeon", powerOnTime);
        intent.putExtra("timeoff", powerOffTime);
        intent.putExtra("wkdays", weekdays);
        intent.putExtra("enable", true); //使能开关机功能，设为 false,则为关闭
        intent.setPackage(Constant.POWER_ON_OFF_PACKAGENAME);
        mContext.sendBroadcast(intent);
        Log.d(TAG,"poweron:" + Arrays.toString(powerOnTime) + "/ poweroff:" +Arrays.toString(powerOffTime) + "/weekday:" + Arrays.toString(weekdays));
    }

    /**
     * @method setPowerOnOff(int[] powerOnTime,int[] powerOffTime)
     * @description 设置一组定时开关机时间数据，需要传入年月日时分
     * @date 20200113
     * @author sky
     * @param powerOnTime，开机时间，例如{2020,1,10,20,48}，powerOffTime，关机时间，例如{2020,1,10,20,38}。
     */
    public void setPowerOnOff(int[] powerOnTime,int[] powerOffTime) {
        Intent intent = new Intent(Constant.INTENT_ACTION_POWERONOFF);
        intent.putExtra("timeon", powerOnTime);
        intent.putExtra("timeoff", powerOffTime);
        intent.putExtra("enable", true); //使能开关机功能，设为 false,则为关闭
        intent.setPackage(Constant.POWER_ON_OFF_PACKAGENAME);
        mContext.sendBroadcast(intent);
        Log.d(TAG,"poweron:" + Arrays.toString(powerOnTime) + "/ poweroff:" +Arrays.toString(powerOffTime));
    }

    /**
     * @method getPowerOnMode()
     * @description 获取定时开关机的模式
     * @date  20200113
     * @author sky
     * @return  "本地模式"是指在定时开关机本地设置的开关机时间。"网络周模式"是指用广播的方式调用setPowerOnOffWithWeekly方法。
     *          "网络每组模式"是指用广播的方式调用setPowerOnOff方法。
    */
    public String getPowerOnMode() {
        String mode = Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONMODE);
        return mode;
//        if ("2".equals(mode))
//            return "网络周模式";
//        else if ("0".equals(mode))
//            return "本地模式";
//        else
//            return "网络每组模式";
    }

    /**
     * @method getPowerOnTime()
     * @description 获取当前设备的开机时间
     * @date 20200113
     * @author sky
     * @return 返回当前设置的开机时间，例如202001132025，是指2020年1月13号20:25开机
    */
    public String getPowerOnTime() {
        String mode = Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONMODE);
        if ("2".equals(mode))
            return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONTIME_2);
        else
            return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONTIME);
    }

    /**
     * @method getPowerOffTime()
     * @description 获取当前设备的关机时间
     * @date 20200113
     * @author sky
     * @return 返回当前设置的关机时间，例如202001132020，是指2020年1月13号20:20关机
     */
    public String getPowerOffTime() {
        String mode = Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONMODE);
        if ("2".equals(mode))
            return Utils.getValueFromProp(Constant.PERSIST_SYS_POWEROFFTIME_2);
        else
            return Utils.getValueFromProp(Constant.PERSIST_SYS_POWEROFFTIME);
    }

    /**
     * @method getLastestPowerOnTime()
     * @description 获取设备上一次执行过的开机时间
     * @date 20200113
     * @author sky
     * @return 返回设备上一次的开机时间，例如202001132025，是指在2020年1月13号20:25执行了开机操作
    */
    public String getLastestPowerOnTime() {
        return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONTIMEPER);
    }

    /**
     * @method getLastestPowerOffTime()
     * @description 获取设备上一次执行过的关机时间
     * @date 20200113
     * @author sky
     * @return 返回设备上一次的关机时间，例如202001132020，是指在2020年1月13号20:20执行了关机操作
     */
    public String getLastestPowerOffTime() {
        return Utils.getValueFromProp(Constant.PERSIST_SYS_POWEROFFTIMEPER);
    }

    /**
     * @method clearPowerOnOffTime()
     * @description 清除定时开关机时间
     * @date  20200113
     * @author sky
    */
    public void clearPowerOnOffTime() {
        Intent intent = new Intent(Constant.INTENT_ACTION_CLEARONTIME);
        intent.setPackage(Constant.POWER_ON_OFF_PACKAGENAME);
        mContext.sendBroadcast(intent);
    }

    /**
     * @method isSetPowerOnTime()
     * @description 获取设备是否设置了定时开关机
     * @date  20200113
     * @author sky
     * @return  设置了定时开关机返回true，否则返回false
    */
    public boolean isSetPowerOnTime() {
        return !"0".equals(getPowerOnTime());
    }

    /**
     * @method getVersion()
     * @description 获取定时开关机版本号
     * @date  20200113
     * @author sky
     * @return 返回定时开关机版本号，例如YS_1.0_20190217
    */
    public String getVersion() {
        return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONOFF_VERSION);
    }

    /**
     * @method upgradeRootPermissionForExport()
     * @description 给export申请权限，建议在开机广播或应用启动时就执行
     * @date 20200213
     * @author sky
     */
    public void upgradeRootPermissionForExport() {
        GPIOUtils.upgradeRootPermissionForExport();
    }

    /**
     * @method exportGpio(int gpio)
     * @description 根据具体的索引值配置export路径，建议在开机广播或应用启动时就执行
     * @date 20200213
     * @author sky
     * @return 配置成功返回true，配置失败返回false
     */
    public boolean exportGpio(int gpio) {
        return GPIOUtils.exportGpio(gpio);
    }

    /**
     * @method upgradeRootPermissionForGpio(int gpio)
     * @description 给配置好的gpio路径申请权限，建议在开机广播或应用启动时就执行，在exportGpio(int gpio)方法后执行
     * @date 20200213
     * @author sky
     */
    public void upgradeRootPermissionForGpio(int gpio) {
        GPIOUtils.upgradeRootPermissionForGpio(gpio);
    }

    /**
     * @method setGpioDirection(int gpio, int arg)
     * @description 设置io口的状态是输入或输出
     * @date 20200213
     * @author sky
     * @param gpio，所要操作的gpio索引值。arg，1代表设置为输入口，0代表设置为输出口。
     * @return 设置成功返回true，失败返回false
     */
    public boolean setGpioDirection(int gpio, int arg) {
        return GPIOUtils.setGpioDirection(gpio,arg);
    }

    /**
     * @method getGpioDirection(int gpio)
     * @description 根据具体的gpio索引值获取当前gpio的状态是输入还是输出
     * @date 20200213
     * @author sky
     * @param gpio，gpio的索引值
     * @return 输入口返回in，输出口返回out
     */
    public String getGpioDirection(int gpio) {
        return GPIOUtils.getGpioDirection(gpio);
    }

    /**
     * @method writeGpioValue(int gpio, String arg)
     * @description 控制gpio电平，只针对输出口
     * @date 20200213
     * @author sky
     * @param gpio，gpio的索引值。Arg，高电平--1，低电平--0
     * @return 写入成功返回true，失败返回false
     */
    public boolean writeGpioValue(int gpio, String arg) {
        return GPIOUtils.writeGpioValue(gpio,arg);
    }

    /**
     * @method getGpioValue(int gpio)
     * @description 获取当前gpio的电平
     * @date 20200213
     * @author sky
     * @param gpio，gpio的索引值
     * @return 高电平返回1，低电平返回0
     */
    public String getGpioValue(int gpio) {
        return GPIOUtils.getGpioValue(gpio);
    }

//    private void controlMainScreenBright(boolean isOpen) {
////        upgradeRootPermission("/sys/class/gpio/gpio13/value");
//        if (isOpen)
//            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio13/value"),"1");
//        else
//            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio13/value"),"0");
//    }
//
//    private void controlSecondScreenBright(boolean isOpen) {
////        upgradeRootPermission("/sys/class/gpio/gpio230/value");
//        if (isOpen)
//            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio230/value"),"1");
//        else
//            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio230/value"),"0");
//    }
//
//    private void controlVoice(boolean isOpen) {
//        if (isOpen)
//            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio226/value"),"0");
//        else
//            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio226/value"),"1");
//    }

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
