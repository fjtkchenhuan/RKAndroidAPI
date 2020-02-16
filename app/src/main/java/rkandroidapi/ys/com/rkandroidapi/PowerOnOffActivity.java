package rkandroidapi.ys.com.rkandroidapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ys.rkapi.MyManager;

import java.util.Arrays;


public class PowerOnOffActivity extends AppCompatActivity implements View.OnClickListener {
    MyManager powerOnOffManager;
    private static final String TAG = "---MainActivity---";
    private EditText powerOffHour;
    private EditText powerOffMinute;
    private EditText poweronHour;
    private EditText poweronMinute;
    private EditText poweronoff_onYear;
    private EditText poweronoff_onMonth;
    private EditText poweronoff_onDate;
    private EditText poweronoff_onHour;
    private EditText poweronoff_onMinute;
    private EditText poweronoff_offYear;
    private EditText poweronoff_offMonth;
    private EditText poweronoff_offDate;
    private EditText poweronoff_offHour;
    private EditText poweronoff_offMinute;
    private CheckBox mon;
    private CheckBox tue;
    private CheckBox wen;
    private CheckBox thr;
    private CheckBox fri;
    private CheckBox sat;
    private CheckBox sun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_on_off);
        powerOnOffManager = MyManager.getInstance(this);

        findViewById(R.id.set_power_on_time).setOnClickListener(this);
        findViewById(R.id.shutdown).setOnClickListener(this);
        findViewById(R.id.set_power_on_off).setOnClickListener(this);
        findViewById(R.id.getPowerOnTime).setOnClickListener(this);
        findViewById(R.id.getPowerOffTime).setOnClickListener(this);
        findViewById(R.id.getPrePowerOnTime).setOnClickListener(this);
        findViewById(R.id.getPrePowerOffTime).setOnClickListener(this);
        findViewById(R.id.getPowerOnMode).setOnClickListener(this);
        findViewById(R.id.clearTime).setOnClickListener(this);
        findViewById(R.id.getVersion).setOnClickListener(this);
        findViewById(R.id.isSetPowerOnOff).setOnClickListener(this);

        poweronHour = findViewById(R.id.poweron_hour);
        poweronMinute = findViewById(R.id.poweron_minute);
        powerOffHour = findViewById(R.id.poweroff_hour);
        powerOffMinute = findViewById(R.id.poweroff_minute);

        poweronoff_onYear = findViewById(R.id.poweronoff_on_year);
        poweronoff_onMonth = findViewById(R.id.poweronoff_on_month);
        poweronoff_onDate = findViewById(R.id.poweronoff_on_date);
        poweronoff_onHour = findViewById(R.id.poweronoff_on_hour);
        poweronoff_onMinute = findViewById(R.id.poweronoff_on_minute);

        poweronoff_offYear = findViewById(R.id.poweronoff_off_year);
        poweronoff_offMonth = findViewById(R.id.poweronoff_off_month);
        poweronoff_offDate = findViewById(R.id.poweronoff_off_date);
        poweronoff_offHour = findViewById(R.id.poweronoff_off_hour);
        poweronoff_offMinute = findViewById(R.id.poweronoff_off_minute);

        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wen = findViewById(R.id.wen);
        thr = findViewById(R.id.thr);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_power_on_time:
                int onMinute1 = Integer.parseInt(poweronMinute.getText().toString().trim());
                int onHour1 = Integer.parseInt(poweronHour.getText().toString().trim());
                int offMinute1 = Integer.parseInt(powerOffMinute.getText().toString().trim());
                int offHour1 = Integer.parseInt(powerOffHour.getText().toString().trim());

                int[] timeoffArray1 = new int[2];
                int[] timeonArray1 = new int[2];
                timeonArray1[0] = onHour1;
                timeonArray1[1] = onMinute1;
                timeoffArray1[0] = offHour1;
                timeoffArray1[1] = offMinute1;

                int[] wkdays = new int[7];
                wkdays[0] = getWeekState(mon.isChecked());
                wkdays[1] = getWeekState(tue.isChecked());
                wkdays[2] = getWeekState(wen.isChecked());
                wkdays[3] = getWeekState(thr.isChecked());
                wkdays[4] = getWeekState(fri.isChecked());
                wkdays[5] = getWeekState(sat.isChecked());
                wkdays[6] = getWeekState(sun.isChecked());
                Log.d("dddd", Arrays.toString(wkdays));
                Log.d("ddddd", "timeoffArray:" + Arrays.toString(timeonArray1) + "timeoffArray:" + Arrays.toString(timeoffArray1));
                powerOnOffManager.setPowerOnOffWithWeekly(timeonArray1, timeoffArray1, wkdays);

                break;
            case R.id.shutdown:
                powerOnOffManager.shutdown();
                break;
            case R.id.set_power_on_off:
                int offYear = Integer.parseInt(poweronoff_offYear.getText().toString().trim());
                int offMonth = Integer.parseInt(poweronoff_offMonth.getText().toString().trim());
                int offDate = Integer.parseInt(poweronoff_offDate.getText().toString().trim());
                int offHour = Integer.parseInt(poweronoff_offHour.getText().toString().trim());
                int offMinute = Integer.parseInt(poweronoff_offMinute.getText().toString().trim());

                int onYear = Integer.parseInt(poweronoff_onYear.getText().toString().trim());
                int onMonth = Integer.parseInt(poweronoff_onMonth.getText().toString().trim());
                int onDate = Integer.parseInt(poweronoff_onDate.getText().toString().trim());
                int onHour = Integer.parseInt(poweronoff_onHour.getText().toString().trim());
                int onMinute = Integer.parseInt(poweronoff_onMinute.getText().toString().trim());
                int[] timeoffArray = new int[5];
                timeoffArray[0] = offYear;
                timeoffArray[1] = offMonth;
                timeoffArray[2] = offDate;
                timeoffArray[3] = offHour;
                timeoffArray[4] = offMinute;

                int[] timeonArray = new int[5];
                timeonArray[0] = onYear;
                timeonArray[1] = onMonth;
                timeonArray[2] = onDate;
                timeonArray[3] = onHour;
                timeonArray[4] = onMinute;

                Log.d(TAG, "关机时间：" + Arrays.toString(timeoffArray) + "，开机时间：" + Arrays.toString(timeonArray));
                powerOnOffManager.setPowerOnOff(timeonArray, timeoffArray);
                break;
            case R.id.isSetPowerOnOff:
                Toast.makeText(this,"是否设置了定时开关机"+powerOnOffManager.isSetPowerOnTime(),Toast.LENGTH_LONG).show();
                break;
            case R.id.getPowerOnTime:
                Toast.makeText(this, "当前开机时间=" + powerOnOffManager.getPowerOnTime(), Toast.LENGTH_LONG).show();
                break;
            case R.id.getPowerOffTime:
                Toast.makeText(this, "当前关机时间=" + powerOnOffManager.getPowerOffTime(), Toast.LENGTH_LONG).show();
                break;
            case R.id.getPrePowerOnTime:
                Toast.makeText(this, "上次开机时间=" + powerOnOffManager.getLastestPowerOnTime(), Toast.LENGTH_LONG).show();
                break;
            case R.id.getPrePowerOffTime:
                Toast.makeText(this, "上次关机时间=" + powerOnOffManager.getLastestPowerOffTime(), Toast.LENGTH_LONG).show();
                break;
            case R.id.getPowerOnMode:
                Toast.makeText(this, "当前开关机模式=" + powerOnOffManager.getPowerOnMode(), Toast.LENGTH_LONG).show();
                break;
            case R.id.clearTime:
//                Intent intent1 = new Intent("android.intent.ClearOnOffTime");
//                sendBroadcast(intent1);
                powerOnOffManager.clearPowerOnOffTime();
                break;
            case R.id.getVersion:
                Toast.makeText(this, "定时开关机版本号 = " + powerOnOffManager.getVersion(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }


    private int getWeekState(boolean flag) {
        int i;
        if (flag)
            i = 1;
        else
            i = 0;
        return i;
    }
}