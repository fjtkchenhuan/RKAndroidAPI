package easypos.android.cslc.com.api3399;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText brightValue;
    private EditText rotationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.set_bright).setOnClickListener(this);
        findViewById(R.id.set_rotation).setOnClickListener(this);
        findViewById(R.id.ratio0).setOnClickListener(this);
        findViewById(R.id.ratio1).setOnClickListener(this);
        findViewById(R.id.ratio2).setOnClickListener(this);
        findViewById(R.id.ratio3).setOnClickListener(this);
        findViewById(R.id.hide_show_nav).setOnClickListener(this);
        findViewById(R.id.reboot).setOnClickListener(this);
        findViewById(R.id.get_net_level).setOnClickListener(this);
        findViewById(R.id.hide_soft_keyboard).setOnClickListener(this);
        findViewById(R.id.show_soft_keyboard).setOnClickListener(this);
        findViewById(R.id.install_apk).setOnClickListener(this);
        findViewById(R.id.uninstall_apk).setOnClickListener(this);

        brightValue = findViewById(R.id.bright_value);
        rotationValue = findViewById(R.id.rotation_value);

    }

    @Override
    public void onClick(View v) {
        File ratioFile = new File("/sys/devices/platform/ff150000.i2c/i2c-6/6-0050/usrbuf");
        switch (v.getId()) {
            case R.id.set_bright:
                int value = Integer.parseInt(brightValue.getText().toString());
                Intent intent = new Intent("com.ys.set_screen_bright");
                intent.putExtra("brightValue", value);
                sendBroadcast(intent);
                break;
            case R.id.set_rotation:
                String degree = rotationValue.getText().toString();
                if (!"".equals(degree)) {
                    setValueToProp("persist.sys.displayrot", degree);
                    File file = new File("/sys/devices/platform/ff150000.i2c/i2c-6/6-0050/rotate");
                    if (file.exists()) {
                        writeFile(file, degree); //real display rotate
                    }
                    reboot();
                }

                break;
            case R.id.ratio0:
                if (ratioFile.exists())
                    writeFile(ratioFile, "0");
                Toast.makeText(this, "设置分辨率为1920*1080", Toast.LENGTH_LONG).show();
                reboot();
                break;
            case R.id.ratio1:
                if (ratioFile.exists())
                    writeFile(ratioFile, "1");
                Toast.makeText(this, "设置分辨率为1280*800", Toast.LENGTH_LONG).show();
                reboot();
                break;
            case R.id.ratio2:
                if (ratioFile.exists())
                    writeFile(ratioFile, "2");
                Toast.makeText(this, "设置分辨率为1366*768", Toast.LENGTH_LONG).show();
                reboot();
                break;
            case R.id.ratio3:
                if (ratioFile.exists())
                    writeFile(ratioFile, "3");
                Toast.makeText(this, "设置分辨率为1440*900", Toast.LENGTH_LONG).show();
                reboot();
                break;
            case R.id.hide_show_nav:
                hideNavBar(getValueFromProp("persist.sys.statebarstate").equals("0"));
                break;
            case R.id.reboot:
                reboot();
                break;
            case R.id.get_net_level:
               getCurrentNetDBM(this);
                break;
            case R.id.hide_soft_keyboard:
                setValueToProp("persist.sys.softkeyboard","0");
                break;
            case R.id.show_soft_keyboard:
                setValueToProp("persist.sys.softkeyboard","1");
                break;
            case R.id.install_apk:
                installApk(this, Environment.getExternalStorageDirectory().getPath() +"/test.apk");
                break;
            case R.id.uninstall_apk:
                uninstallApk(this,"com.bjw.ComAssistant");
                break;
            default:
                break;
        }
    }

   private void installApk(Context context, String path) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Log.d("level5","path = " + Environment.getExternalStorageDirectory().getParent() + "/test.apk");
            intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getParent() + "/test.apk"),"application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    public void hideNavBar(boolean hide) {  // ok
        Intent intent = new Intent();
        if (hide) {
            intent.setAction("android.action.adtv.showNavigationBar");
            sendBroadcast(intent);
        } else {
            intent.setAction("android.action.adtv.hideNavigationBar");
            sendBroadcast(intent);
        }

    }

    private void reboot() {
        Intent intent = new Intent("android.intent.action.reboot");
        sendBroadcast(intent);
    }

    private void setValueToProp(String key, String val) {
        Class<?> classType;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method method = classType.getDeclaredMethod("set", new Class[]{String.class, String.class});
            method.invoke(classType, new Object[]{key, val});
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

    public static String getValueFromProp(String key) {
        String value = "";
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            value = (String) getMethod.invoke(classType, new Object[]{key});
        } catch (Exception e) {
        }
        return value;
    }

    public static void writeFile(File file, String content) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getCurrentNetDBM(Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener mylistener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                String signalInfo = signalStrength.toString();
                String[] params = signalInfo.split(" ");

                if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    //4G网络 最佳范围   >-90dBm 越大越好
                    int Itedbm = Integer.parseInt(params[9]);
                    Log.d("level1",Itedbm + "");
                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    //3G网络最佳范围  >-90dBm  越大越好  ps:中国移动3G获取不到  返回的无效dbm值是正数（85dbm）
                    //在这个范围的已经确定是3G，但不同运营商的3G有不同的获取方法，故在此需做判断 判断运营商与网络类型的工具类在最下方
                    String yys = getOperator(MainActivity.this);//获取当前运营商
                    if (yys == "中国移动") {
                        Log.d("level2",0 + "");//中国移动3G不可获取，故在此返回0
                    } else if (yys == "中国联通") {
                        int cdmaDbm = signalStrength.getCdmaDbm();
                        Log.d("level3",cdmaDbm + "");
                    } else if (yys == "中国电信") {
                        int evdoDbm = signalStrength.getEvdoDbm();
                        Log.d("level4",evdoDbm + "");
                    }

                } else {
                    //2G网络最佳范围>-90dBm 越大越好
                    int asu = signalStrength.getGsmSignalStrength();
                    int dbm = -113 + 2 * asu;
                    Log.d("level5",dbm + "");
                }
            }
        };
        //开始监听
        tm.listen(mylistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public  String getOperator(Context context) {
        String ProvidersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        String IMSI = telephonyManager.getSubscriberId();
        Log.i("qweqwes", "运营商代码" + IMSI);
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")  || IMSI.startsWith("46006")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
            return ProvidersName;
        } else {
            return "没有获取到sim卡信息";
        }
    }
}
