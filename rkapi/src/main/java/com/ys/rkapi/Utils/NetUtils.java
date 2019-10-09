package com.ys.rkapi.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;


import com.ys.rkapi.Constant;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by RYX on 2017/8/30.
 */

public class NetUtils {
    public static String staticIP;
    public static void setEthMAC(String val) {
        Class<?> classType;
        try {
            classType = Class.forName("android.os.Custom");
            Method method = classType.getDeclaredMethod("setMac", new Class[]{String.class});
            method.invoke(classType, new Object[]{val});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getEthMAC() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (macSerial != null && !macSerial.equals("") && macSerial.length() == 17) {
            return macSerial.toUpperCase();
        }
            return "";

    }

//    public static String getEthIPAddress(){
//        return ScreenUtils.getValueFromProp(Constant.ETH_IP_ADDRESS_PROP);
//    }
    public static String getDynamicEthIPAddress(Context context){
        if (VersionUtils.getAndroidModle().equals("rk3288") && Build.VERSION.SDK.equals("25")
                || VersionUtils.getAndroidModle().equals("rk3368") && Build.VERSION.SDK.equals("25")
                || VersionUtils.getAndroidModle().equals("rk3128")&& Build.VERSION.SDK.equals("25"))
//            return Utils.getValueFromProp("net.ppp0.local-ip");
            return Utils.getEthernet(context);
        else
            return Utils.getValueFromProp(Constant.ETH_IP_ADDRESS_PROP);
    }

    public static void setStaticIP(Context context, String ip, String gateWay, String mask, String dns1, String dns2){
        Log.d("MyManager", "setStaticIP 发送修改IP广播");
        Intent intent = new Intent(Constant.ETH_STATIC_IP_ACTION);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        intent.putExtra("useStaticIP",1);
        intent.putExtra(Constant.ETH_SET_IP, ip);//IP地址
        intent.putExtra(Constant.ETH_SET_GATEWAY, gateWay);//网关
        intent.putExtra(Constant.ETH_SET_MASK, mask);//子网掩码
        intent.putExtra(Constant.ETH_DNS1, dns1);//dns1
        intent.putExtra(Constant.ETH_DNS2, dns2);//dns2
        context.sendBroadcast(intent);
    }


    ///以太网为9
    ///wifi为1
    ///移动网络为0
    public static int getNetWorkType(Context context) {
        int netWorkType = -100;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            netWorkType = networkInfo.getType();
        }
        return netWorkType;
    }

    public static void setEthernetEnabled(Context context,boolean enable){
//        EthernetManager mEthManager;
        Log.d("Ethernet","发送Ethernet开关广播");
        Intent intent = new Intent(Constant.SET_ETH_ENABLE_ACTION);
        intent.putExtra(Constant.ETH_MODE, enable);
        context.sendBroadcast(intent);
    }

    public static int getEthStatus() {
        int value = -2;
        try {
            Class<?> classType = Class.forName("android.net.EthernetManager");
            Method getMethod = classType.getDeclaredMethod("getEthernetIfaceState", new Class<?>[]{String.class});
            value = (int) getMethod.invoke(classType);
        } catch (Exception e) {
        }
        return value;
    }

}
