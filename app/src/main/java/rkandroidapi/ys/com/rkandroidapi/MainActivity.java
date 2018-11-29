package rkandroidapi.ys.com.rkandroidapi;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.ys.rkapi.MyManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MyManager manager;
    private String STATICIP = "StaticIp";
    private String DHCP = "DHCP";
    private String IPADDR = "192.168.1.125";
    private String MASK = "255.255.255.0";
    private String GATEWAY = "192.168.1.1";
    private String DNS1 = "192.168.1.1";
    private String DNS2 = "0.0.0.0";
    private String TFPath;
    private String USBPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = MyManager.getInstance(this);
        manager.bindAIDLService(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mountReceiver,filter);

        findViewById(R.id.get_api_version).setOnClickListener(this);
        findViewById(R.id.get_device_type).setOnClickListener(this);
        findViewById(R.id.get_android_version).setOnClickListener(this);
        findViewById(R.id.get_firmware_size).setOnClickListener(this);
        findViewById(R.id.get_in_store_size).setOnClickListener(this);
        findViewById(R.id.get_firmware_sdk_version).setOnClickListener(this);
        findViewById(R.id.get_firmware_kernel_version).setOnClickListener(this);
        findViewById(R.id.get_device_firmware_version).setOnClickListener(this);
        findViewById(R.id.get_firmware_date).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.shutdown).setOnClickListener(this);
        findViewById(R.id.reboot).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.nav_bar_state).setOnClickListener(this);
        findViewById(R.id.nav_bar_hide_show).setOnClickListener(this);
        findViewById(R.id.nav_bar_slide_state).setOnClickListener(this);
        findViewById(R.id.nav_bar_slide).setOnClickListener(this);
        findViewById(R.id.notification_bar_slide_state).setOnClickListener(this);
        findViewById(R.id.notification_bar_slide).setOnClickListener(this);
        findViewById(R.id.screen_shot).setOnClickListener(this);
        findViewById(R.id.get_width_height).setOnClickListener(this);
        findViewById(R.id.change_screen_bright).setOnClickListener(this);
        findViewById(R.id.get_brightness).setOnClickListener(this);
        findViewById(R.id.open_screen_bright).setOnClickListener(this);
        findViewById(R.id.close_screen_bright).setOnClickListener(this);
        findViewById(R.id.is_screen_light_on).setOnClickListener(this);
        findViewById(R.id.open_hdmi).setOnClickListener(this);
        findViewById(R.id.close_hdmi).setOnClickListener(this);
        findViewById(R.id.rotate_90).setOnClickListener(this);
        findViewById(R.id.rotate_0).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.update_img).setOnClickListener(this);
        findViewById(R.id.recovery).setOnClickListener(this);
        findViewById(R.id.silent_install).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.get_eth_status).setOnClickListener(this);
        findViewById(R.id.get_eth_mode).setOnClickListener(this);
        findViewById(R.id.get_eth_mac).setOnClickListener(this);
        findViewById(R.id.set_eth_mac).setOnClickListener(this);
        findViewById(R.id.get_eth_ip).setOnClickListener(this);
        findViewById(R.id.set_eth_ip).setOnClickListener(this);
        findViewById(R.id.open_eth).setOnClickListener(this);
        findViewById(R.id.close_eth).setOnClickListener(this);
        findViewById(R.id.set_dhcp_eth).setOnClickListener(this);
        findViewById(R.id.get_mask).setOnClickListener(this);
        findViewById(R.id.get_gateway).setOnClickListener(this);
        findViewById(R.id.get_dns1).setOnClickListener(this);
        findViewById(R.id.get_dns2).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.externel_sd_path).setOnClickListener(this);
        findViewById(R.id.externel_u_path).setOnClickListener(this);
        findViewById(R.id.uninstall_externel).setOnClickListener(this);
        findViewById(R.id.mount_externel).setOnClickListener(this);
        findViewById(R.id.read_eeprom).setOnClickListener(this);
        findViewById(R.id.write_eeprom).setOnClickListener(this);
        findViewById(R.id.get_uartPath).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.set_time).setOnClickListener(this);
        findViewById(R.id.su_order).setOnClickListener(this);
        findViewById(R.id.net_type).setOnClickListener(this);
        findViewById(R.id.screen_num).setOnClickListener(this);
        findViewById(R.id.hdmi).setOnClickListener(this);
        //------------------------------------------------------------------

        findViewById(R.id.switch_on_time).setOnClickListener(this);
        findViewById(R.id.switch_off_time).setOnClickListener(this);
        findViewById(R.id.is_auto_time).setOnClickListener(this);
        findViewById(R.id.get_kernel_log).setOnClickListener(this);
        findViewById(R.id.get_android_log).setOnClickListener(this);
        findViewById(R.id.stop_android_log).setOnClickListener(this);
        findViewById(R.id.open_softkeyboard).setOnClickListener(this);
        findViewById(R.id.close_softkeyboard).setOnClickListener(this);
//        findViewById(R.id.set_volume).setOnClickListener(this);
    }

    private BroadcastReceiver mountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.MEDIA_MOUNTED".equals(intent.getAction())) {
                String path = intent.getData().getPath();
                Log.d("chenhuan","path=" + path);
                if (path.contains("usb"))
                    USBPath = path;
                else if (path.contains("other"))///storage/0000-0000
                    TFPath  = path;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_api_version :
                ToastUtils.showToast(this,manager.getApiVersion());
                break;
            case R.id.get_device_type :
                ToastUtils.showToast(this,manager.getAndroidModle());
                break;
            case R.id.get_android_version :
                ToastUtils.showToast(this,manager.getAndroidVersion());
                break;
            case R.id.get_firmware_size :
                ToastUtils.showToast(this,manager.getRunningMemory());
                break;
            case R.id.get_in_store_size :
                ToastUtils.showToast(this,manager.getInternalStorageMemory());
                break;
            case R.id.get_firmware_sdk_version :
                ToastUtils.showToast(this,manager.getFirmwareVersion());
                break;
            case R.id.get_firmware_kernel_version :
                ToastUtils.showToast(this,manager.getKernelVersion());
                break;
            case R.id.get_device_firmware_version :
                ToastUtils.showToast(this,manager.getAndroidDisplay());
                break;
            case R.id.get_firmware_date :
                ToastUtils.showToast(this,manager.getFirmwareDate());
                break;
            case R.id.shutdown:
                manager.shutdown();
                break;
            case R.id.reboot:
                manager.reboot();
                break;
            case R.id.nav_bar_state:
                ToastUtils.showToast(this,"导航栏是否隐藏："+manager.getNavBarHideState());
                break;
            case R.id.nav_bar_hide_show:
                manager.hideNavBar(manager.getNavBarHideState());
                break;
            case R.id.nav_bar_slide_state:
                ToastUtils.showToast(this,"滑出导航栏是否打开："+manager.isSlideShowNavBarOpen());
                break;
            case R.id.nav_bar_slide:
                manager.setSlideShowNavBar(manager.isSlideShowNavBarOpen());
                break;
            case R.id.notification_bar_slide_state:
                ToastUtils.showToast(this,"滑出通知栏是否打开："+manager.isSlideShowNotificationBarOpen());
                break;
            case R.id.notification_bar_slide:
                manager.setSlideShowNotificationBar(manager.isSlideShowNotificationBarOpen());
                break;
            case R.id.screen_shot:
                manager.takeScreenshot(Environment.getExternalStorageDirectory().getPath() +"/001.jpg");
                ToastUtils.showToast(this,"截图存储在 /mnt/sdcard/001.jpg");
                break;
            case R.id.get_width_height:
                int width = manager.getDisplayWidth(this);
                int height = manager.getDisplayHeight(this);
                ToastUtils.showToast(this,"Width = " + width + " height = " + height);
                break;
            case R.id.change_screen_bright:
                manager.changeScreenLight(50);
                break;
            case R.id.open_screen_bright:
                manager.turnOnBackLight();
                break;
            case R.id.close_screen_bright:
                manager.turnOffBackLight();
                break;
            case R.id.is_screen_light_on:
                ToastUtils.showToast(this,"背光是否打开 = " + manager.isBacklightOn());
                break;
            case R.id.get_brightness:
                ToastUtils.showToast(this,"背光亮度 = " + manager.getSystemBrightness());
            case R.id.open_hdmi:
                manager.turnOnHDMI();
                break;
            case R.id.close_hdmi:
                manager.turnOffHDMI();
                break;
            case R.id.rotate_90:
                manager.rotateScreen(this,"90");
                break;
            case R.id.rotate_0:
                manager.rotateScreen(this,"0");
                break;
            case R.id.update_img:
                manager.upgradeSystem("/mnt/media_rw/10C0-C930/Android/update.zip"); ///mnt/media_rw/10C0-C930/update.zip
                Log.d("chenhuan","path = " +Environment.getExternalStorageDirectory().getPath() +"/Download/update.zip");
//                ToastUtils.showToast(this,"3399和3328，暂未实现");
                break;
            case R.id.recovery:
                manager.rebootRecovery();
                break;
            case R.id.silent_install:
                Log.d("chenhuan","ff = " + manager.silentInstallApk(Environment.getExternalStorageDirectory().getPath() +"/weixt.apk"));// /mnt/sdcard/Download
//                Log.d("chenhuan","path = " + Environment.getExternalStorageDirectory().getPath() +"/test.apk");
//                manager.silentInstallApk("/mnt/sdcard/Download/sougoushurufa_831.apk");
                break;
            case R.id.get_eth_status:
                ToastUtils.showToast(this,"以太网是否打开=" + manager.getEthStatus());
                break;
            case R.id.get_eth_mode:
                ToastUtils.showToast(this,"以太网模式为=" +manager.getEthMode());
                break;
            case R.id.get_eth_mac:
                ToastUtils.showToast(this,"Mac地址 = " + manager.getEthMacAddress());
//                Toast.makeText(getApplicationContext(), "Mac地址 = " + manager.getEthMacAddress(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_eth_mac:
                manager.setEthMacAddress("ee4e592090cf");
                ToastUtils.showToast(this,"设置以太网mac地址为ee4e592090cf");
                break;
            case R.id.get_eth_ip:
//                ToastUtils.showToast(this,"动态IP地址：" + manager.getDhcpIpAddress());
                if (DHCP.equals(manager.getEthMode()))
                    ToastUtils.showToast(this,"动态IP地址：" + manager.getDhcpIpAddress());
                else if (STATICIP.equals(manager.getEthMode()))
                    ToastUtils.showToast(this,"静态IP地址：" + manager.getStaticEthIPAddress());
                break;
            case R.id.set_eth_ip:
                manager.setStaticEthIPAddress(IPADDR, GATEWAY, MASK, DNS1, DNS2);
                break;
            case R.id.open_eth:
                manager.ethEnabled(true);
                break;
            case R.id.close_eth:
                manager.ethEnabled(false);
                break;
            case R.id.set_dhcp_eth:
                manager.setDhcpIpAddress(this);
                break;
            case R.id.get_mask:
                ToastUtils.showToast(this,"以太网子网掩码 = " + manager.getNetMask());
                break;
            case R.id.get_gateway:
                ToastUtils.showToast(this,"以太网网关 = " + manager.getGateway());
                break;
            case R.id.get_dns1:
                ToastUtils.showToast(this,"dns1 = " + manager.getEthDns1());
                break;
            case R.id.get_dns2:
                ToastUtils.showToast(this,"dns2 = " + manager.getEthDns2());
                break;
            case R.id.externel_sd_path:
                ToastUtils.showToast(this,"外置SD卡路径 = " + manager.getSDcardPath());
//                if ("".equals(TFPath) || TFPath == null)
//                    Toast.makeText(getApplicationContext(), "请插入TF卡", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(), "外置TF卡路径 = " + TFPath, Toast.LENGTH_SHORT).show();
                break;
            case R.id.externel_u_path:
                ToastUtils.showToast(this,"U 盘路径 = " + manager.getUSBStoragePath(0));
//                if ("".equals(USBPath) || USBPath == null)
//                    Toast.makeText(getApplicationContext(), "请插入U盘", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(), "外置U盘路径 = " + USBPath, Toast.LENGTH_SHORT).show();
                break;
            case R.id.uninstall_externel:
                String USBStorage = "/mnt/usb_storage/USB_DISK1";
                manager.unmountVolume(USBStorage);
                Toast.makeText(getApplicationContext(), "卸载" + USBStorage, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mount_externel:
                String USBStorage1 = "/mnt/usb_storage/USB_DISK1";
                manager.mountVolume(USBStorage1);
                Toast.makeText(getApplicationContext(), "挂载" + USBStorage1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.read_eeprom:
                Toast.makeText(getApplicationContext(), "读到的EEPROM数据：" + manager.readEEPRom(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.write_eeprom:
                manager.writeEEPRom("12345678无是谁dmd");
                break;
            case R.id.get_uartPath:
                ToastUtils.showToast(this,"串口0的绝对路径 = " + manager.getUartPath("TTYS0"));
                break;
            case R.id.set_time:
                manager.setTime(2017, 3, 16, 17, 44,55);
                break;
            case R.id.su_order:
                manager.execSuCmd("reboot");
                break;
            case R.id.net_type:
                ToastUtils.showToast(this,"上网类型 =" + manager.getCurrentNetType());
                break;
            case R.id.screen_num:
                ToastUtils.showToast(this,"屏幕数" + manager.getScreenNumber());
                break;
            case R.id.hdmi:
                ToastUtils.showToast(this,"HDMI =" + manager.getHdmiinStatus());
                break;
            case R.id.switch_on_time:
                manager.switchAutoTime(true);
                ToastUtils.showToast(this,"打开自动确定日期和时间" );
                break;
            case R.id.switch_off_time:
                manager.switchAutoTime(false);
                ToastUtils.showToast(this,"关闭自动确定日期和时间" );
                break;
            case R.id.is_auto_time:
                ToastUtils.showToast(this,"是否打开自动更新时间开关 = " + manager.isAutoSyncTime());
                break;
            case R.id.open_softkeyboard:
                manager.setSoftKeyboardHidden(false);
                break;
            case R.id.close_softkeyboard:
                manager.setSoftKeyboardHidden(true);
                break;
            case R.id.get_kernel_log:
                manager.getKmsgLog("/mnt/sdcard/kernelLog.txt");
                break;
            case R.id.get_android_log:
                String path = Environment.getExternalStorageDirectory().getPath();
                manager.getAndroidLogcat(path +"/androidLog.txt");
                break;
            case R.id.stop_android_log:
                manager.stopAndroidLogcat();
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.TetherSettings"));
                startActivity(intent);
                break;
            default:
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("chenhuan","keycode = " + keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        manager.unBindAIDLService(this);
        unregisterReceiver(mountReceiver);
        super.onDestroy();
    }


}
