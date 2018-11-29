package com.ys.rkapi;

import android.os.Environment;

/**
 * Created by RYX on 2017/8/30.
 */

public class Constant {
    public final static String NAND_PATH = Environment.getExternalStorageDirectory().getPath();//"/mnt/internal_sd";
    public final static String SD_PATH = "/mnt/external_sd";

    public final static String SHUTDOWN_ACTION = "android.intent.action.shutdown";
    public final static String REBOOT_ACTION = "android.intent.action.reboot";
    public final static String DORMANT_INTERVAL = "com.ys.dormant_interval";

    // 背光IO
    public final static String A_BACKLIGHT_IO_PATH = "/sys/devices/fb.9/graphics/fb0/pwr_bl";
    //public final static String B_BACKLIGHT_IO_PATH = "/sys/devices/fb.8/graphics/fb0/pwr_bl";
    public final static String B_BACKLIGHT_IO_PATH = "/sys/devices/fb.8/graphics/fb0/pwr_bl";

    public static final String C_BACKLIGHT_IO_PATH = "/sys/class/graphics/fb0/pwr_bl";

    public static final String D_BACKLIGHT_IO_PATH = "/sys/devices/platform/backlight/backlight/backlight/bl_power";

    //3399hdmi
    public static final String HDMI_STATUS_3399 = "/sys/class/drm/card0-HDMI-A-1/status";
    public static final String HDMI_STATUS_3288 = "/sys/class/display/HDMI/enable";


    public final static String FIRMWARE_UPGRADE_ACTION = "android.intent.action.YS_UPDATE_FIRMWARE";
    public final static String FIRMWARE_UPGRADE_KEY = "img_path";

    public static final String PROP_HIDE_STATUSBAR = "persist.sys.sb.hide";
    public static final String PROP_SWIPE_STATUSBAR = "persist.sys.swipe.sb";
    public static final String PROP_SWIPE_NOTIFIBAR = "persist.sys.swipe.nb";

    public static final String PROP_STATUSBAR_STATE_LU = "persist.sys.statebarstate";
    public static final String PROP_SWIPE_STATUSBAR_LU = "persist.sys.statebarslide";
    public static final String PROP_SWIPE_NOTIFIBAR_LU = "persist.sys.disexpandbar";

    //截屏
    public final static String SCREENSHOOT_ACTION = "com.ys.screenshoot";
    public final static String SCREENSHOOT_KEY = "path";

    //更新时间
    public final static String UPDATE_TIME_ACTION = "com.ys.update_time";
    public final static String UPDATE_TIME_KEY = "current_time";

    //输入IO口，后续如果有添加只需在此处加入
    public final static String[] IO_INPUTS = {"/sys/devices/misc_power_en.22/key","/sys/class/display/HDMI/enable"};
    //输出类型的IO口，后续如果有添加只需在此处加入
    public final static String[] IO_OUTPUTS = {};

    //USB-OTG  IO
    public final static String USB_OTG_IO = "/sys/bus/platform/drivers/usb20_otg/force_usb_mode";

    ///在此属性中提取固件生成日期
    public final static String DATE_PROP = "ro.build.version.incremental";
    public final static String SYSTEM_VERSION_INFO = "ro.build.description";

    //ETH IP address prop
    public final static String ETH_IP_ADDRESS_PROP = "dhcp.eth0.ipaddress";

    //设置以太网静态IP
    public final static String ETH_STATIC_IP_ACTION = "com.ys.set_static_ip";
    public final static String ETH_DHCP_ACTION = "com.ys.set_dhcp";
    public final static String ETH_SET_IP = "ip";
    public final static String ETH_SET_GATEWAY = "gateway";
    public final static String ETH_SET_MASK = "netmask";
    public final static String ETH_DNS1 = "dns1";
    public final static String ETH_DNS2 = "dns2";

    //获取以太网静态IP
    public final static String GET_ETH_STATIC_IP_ACTION = "com.ys.get_static_ip";
    //连接以太网
    public final static String SET_ETH_ENABLE_ACTION = "com.ys.set_eth_enabled";
    public final static String ETH_MODE = "eth_mode";
    //挂载存储
    public final static String MOUNT_USB_ACTION = "com.ys.set_mount_usb";
    public final static String MOUNT_ENABLE_KEY = "mount";
    public final static String MOUNT_POINT_KEY = "mountPoint";

    //EEPROM
    public final static String EEPROM_FILE_NAME = "/sys/devices/ff140000.i2c/i2c-1/1-0050/usrbuf";
    // 网络类型定义
    public final static int NET_TYPE_ETH = 0;
    public final static int NET_TYPE_WIFI = 1;
    public final static int NET_TYPE_MOBILE = 2;
    public final static int NETWORK_UNKNOW = -100;

    //设置开机时间年月日
    public final static String POWER_ON_ACTION = "android.intent.PowerOnTime";
    public final static String POWER_ON_YEAR = "year";
    public final static String POWER_ON_Month = "month";
    public final static String POWER_ON_DAY = "day";
    public final static String POWER_ON_HOUR = "hour";
    public final static String POWER_ON_MINUTE = "minute";
    //清零开机时间
    public final static String POWER_ON_CLEAR_ACTION = "android.intent.ClearOnTime";

    //3128屏幕旋转广播接口
    public final static String ROTATION_ACTION = "com.ys.rotation_screen";
    public final static String ROTATION_INDEX = "rotIndex";

    public static final String SUCOMMAND_ACTION = "com.ys.execSuCmd";
    public static final String SUCOMMAND_KEY = "execSuCmd";
}
