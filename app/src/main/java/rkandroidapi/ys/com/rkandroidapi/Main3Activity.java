package rkandroidapi.ys.com.rkandroidapi;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Main3Activity";
    private PowerManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        ((Chronometer)findViewById(R.id.chronomter)).start();

    }

    @Override
    public void onClick(View v) {
        String i = readGpioPG("/sys/hdmiin/hdmiin_reset");
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn1:
                intent.setAction("com.ys.slide.navigationbar");
                sendBroadcast(intent);
                break;
            case R.id.btn2:
                intent.setAction("com.ys.slide.notificationbar");
                sendBroadcast(intent);
            case R.id.btn3:
                intent.setAction("com.ys.slide.systembar");
                intent.putExtra("barMode","navigationbar");
                intent.putExtra("isSlide",true);
                sendBroadcast(intent);
                break;
            case R.id.btn4:
                intent.setAction("com.ys.slide.systembar");
                intent.putExtra("barMode","navigationbar");
                intent.putExtra("isSlide",false);
                sendBroadcast(intent);
                break;
            case R.id.btn5:
                intent.setAction("com.ys.slide.systembar");
                intent.putExtra("barMode","notificationbar");
                intent.putExtra("isSlide",true);
                sendBroadcast(intent);
                break;
            case R.id.btn6:
                intent.setAction("com.ys.slide.systembar");
                intent.putExtra("barMode","notificationbar");
                intent.putExtra("isSlide",false);
                sendBroadcast(intent);
                break;
            case R.id.btn7:
                intent.setAction("android.action.adtv.showNavigationBar");
                sendBroadcast(intent);
                break;
            case R.id.btn8:
                intent.setAction("android.action.adtv.hideNavigationBar");
                sendBroadcast(intent);
                break;

                default:
                    break;
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

    private Runnable dddd = new Runnable() {
        @Override
        public void run() {
            Intent intent1 = new Intent("com.ys.setPowerSleep");
            sendBroadcast(intent1);
        }
    };

    private void writeIOFile(String str, String path) throws IOException, InterruptedException {
        File file = new File(path);
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);
        if (str.equals("0")) {
            do_exec("busybox echo 0 > " + path);
        } else {
            do_exec("busybox echo 1 > " + path);
        }
    }

    public static void do_exec(String cmd) {
        try {
            /* Missing read/write permission, trying to chmod the file */
            Process su;
            su = Runtime.getRuntime().exec("su");
            String str = cmd + "\n" + "exit\n";
            su.getOutputStream().write(str.getBytes());

            Log.d(TAG, "cmd：" + cmd);

            if ((su.waitFor() != 0)) {
                Log.d(TAG, "cmd=" + cmd + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String readGpioPG(String path) {
        String str = "";
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[1];
            fileInputStream.read(buffer);
            fileInputStream.close();
            str = new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
