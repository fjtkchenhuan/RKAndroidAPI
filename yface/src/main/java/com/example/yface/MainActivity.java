package com.example.yface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yfaceapi.GPIOManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private GPIOManager gpioManager;
    private EditText maxEdit;
    private EditText minEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpioManager = GPIOManager.getInstance(this);

        initView();
    }

    private void initView() {
        maxEdit = findViewById(R.id.max_freq);
        minEdit = findViewById(R.id.min_freq);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        findViewById(R.id.btn10).setOnClickListener(this);
        findViewById(R.id.btn11).setOnClickListener(this);
        findViewById(R.id.btn12).setOnClickListener(this);
        findViewById(R.id.btn13).setOnClickListener(this);
        findViewById(R.id.btn14).setOnClickListener(this);
        findViewById(R.id.btn15).setOnClickListener(this);
        findViewById(R.id.btn16).setOnClickListener(this);
        findViewById(R.id.btn17).setOnClickListener(this);
//        findViewById(R.id.btn18).setOnClickListener(this);
        findViewById(R.id.btn19).setOnClickListener(this);
        findViewById(R.id.btn20).setOnClickListener(this);
        findViewById(R.id.btn21).setOnClickListener(this);
        findViewById(R.id.btn22).setOnClickListener(this);
//        findViewById(R.id.btn23).setOnClickListener(this);
//        findViewById(R.id.btn24).setOnClickListener(this);
        findViewById(R.id.btn25).setOnClickListener(this);
        findViewById(R.id.btn26).setOnClickListener(this);
        findViewById(R.id.btn27).setOnClickListener(this);
        findViewById(R.id.btn28).setOnClickListener(this);
        findViewById(R.id.btn29).setOnClickListener(this);
        findViewById(R.id.btn30).setOnClickListener(this);
        findViewById(R.id.btn31).setOnClickListener(this);
        findViewById(R.id.btn32).setOnClickListener(this);
        findViewById(R.id.btn33).setOnClickListener(this);
        findViewById(R.id.btn34).setOnClickListener(this);
        findViewById(R.id.btn35).setOnClickListener(this);
        findViewById(R.id.btn36).setOnClickListener(this);
        findViewById(R.id.btn37).setOnClickListener(this);
        findViewById(R.id.btn38).setOnClickListener(this);
        findViewById(R.id.btn39).setOnClickListener(this);
        findViewById(R.id.btn40).setOnClickListener(this);
        findViewById(R.id.btn41).setOnClickListener(this);
        findViewById(R.id.btn42).setOnClickListener(this);
        findViewById(R.id.btn43).setOnClickListener(this);
        findViewById(R.id.btn44).setOnClickListener(this);
        findViewById(R.id.btn45).setOnClickListener(this);
        findViewById(R.id.btn46).setOnClickListener(this);
        findViewById(R.id.btn47).setOnClickListener(this);
        findViewById(R.id.btn48).setOnClickListener(this);
        findViewById(R.id.btn49).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                ToastUtils.showToast(this,"USB_OTG的状态是: " + gpioManager.getUSBOTGStatus());
                break;
            case R.id.btn2:
                gpioManager.pullUpUSBOTG();
                break;
            case R.id.btn3:
                gpioManager.pullDownUSBOTG();
                break;
            case R.id.btn4:
                ToastUtils.showToast(this,"USB1的状态是: " + gpioManager.getUSB1Status());
                break;
            case R.id.btn5:
                gpioManager.pullUpUSB1();
                break;
            case R.id.btn6:
               gpioManager.pullDownUSB1();
                break;
            case R.id.btn7:
                ToastUtils.showToast(this,"USB2的状态是: " + gpioManager.getUSB2Status());
                break;
            case R.id.btn8:
                gpioManager.pullUpUSB2();
                break;
            case R.id.btn9:
                gpioManager.pullDownUSB2();
                break;
            case R.id.btn10:
                ToastUtils.showToast(this,"USB3的状态是: " + gpioManager.getUSB3Status());
                break;
            case R.id.btn11:
                gpioManager.pullUpUSB3();
                break;
            case R.id.btn12:
                gpioManager.pullDownUSB3();
                break;
            case R.id.btn13:
                ToastUtils.showToast(this,"功放io的状态: " + gpioManager.getVoiceStatus());
                break;
            case R.id.btn14:
                gpioManager.pullUpVoice();
                break;
            case R.id.btn15:
                gpioManager.pullDownVoice();
                break;
            case R.id.btn16:
                ToastUtils.showToast(this,"io1的状态: " + gpioManager.getReservedIO1Status());
                break;
            case R.id.btn19:
                ToastUtils.showToast(this,"io2的状态: " + gpioManager.getReservedIO2Status());
                break;
            case R.id.btn22:
                ToastUtils.showToast(this,"io3的状态: " + gpioManager.getReservedIO3Status());
                break;
            case R.id.btn25:
                ToastUtils.showToast(this,"io4的输入输出状态: " + gpioManager.getReservedIO4Direction());
                break;
            case R.id.btn17:
                ToastUtils.showToast(this,"io4的状态: " + gpioManager.getReservedIO4Status());
                break;
            case R.id.btn20:
                gpioManager.setReservedIO4Out();
                break;
            case R.id.btn21:
                gpioManager.setReservedIO4In();
                break;
            case R.id.btn26:
                if (gpioManager.isIO4DirectionOut())
                    gpioManager.pullUpIO4();
                else
                    ToastUtils.showToast(this,"输入口不能拉高");
                break;
            case R.id.btn27:
                if (gpioManager.isIO4DirectionOut())
                    gpioManager.pullDownIO4();
                else
                    ToastUtils.showToast(this,"输入口不能拉低");
                break;
            case R.id.btn28:
                ToastUtils.showToast(this,"风扇的状态: " + gpioManager.getFanStatus());
                break;
            case R.id.btn29:
                gpioManager.pullUpFan();
                break;
            case R.id.btn30:
                gpioManager.pullDownFan();
                break;
            case R.id.btn31:
                ToastUtils.showToast(this,"红外Led的状态: " + gpioManager.getInfraredLedStatus());
                break;
            case R.id.btn32:
                gpioManager.pullUpInfraredLed();
                break;
            case R.id.btn33:
                gpioManager.pullDownInfraredLed();
                break;
            case R.id.btn34:
                ToastUtils.showToast(this,"继电器的状态: " + gpioManager.getRelayStatus());
                break;
            case R.id.btn35:
                gpioManager.pullUpRelay();
                break;
            case R.id.btn36:
                gpioManager.pullDownRelay();
                break;
            case R.id.btn37:
                ToastUtils.showToast(this,"绿色补光灯的状态: " + gpioManager.getGreenLightStatus());
                break;
            case R.id.btn38:
                gpioManager.pullUpGreenLight();
                break;
            case R.id.btn39:
                gpioManager.pullDownGreenLight();
                break;
            case R.id.btn40:
                Toast.makeText(this,"",Toast.LENGTH_LONG).show();
                ToastUtils.showToast(this,"红色补光灯的状态: " + gpioManager.getRedLightStatus());
                break;
            case R.id.btn41:
                gpioManager.pullUpRedLight();
                break;
            case R.id.btn42:
                gpioManager.pullDownRedLight();
                break;
            case R.id.btn43:
                ToastUtils.showToast(this,"白色补光灯的状态: " + gpioManager.getWhiteLightStatus());
                break;
            case R.id.btn44:
                gpioManager.pullUpWhiteLight();
                break;
            case R.id.btn45:
                gpioManager.pullDownWhiteLight();
                break;
            case R.id.btn46:
                int max = Integer.parseInt(maxEdit.getText().toString().trim());
                gpioManager.setMaxFreq(max);
                break;
            case R.id.btn47:
                ToastUtils.showToast(this,"CPU最大频率是 = " + gpioManager.getMaxFreq());
                break;
            case R.id.btn48:
                int min = Integer.parseInt(minEdit.getText().toString().trim());
                gpioManager.setMinFreq(min);
                break;
            case R.id.btn49:
                ToastUtils.showToast(this,"CPU最小频率是 = " + gpioManager.getMinFreq());
                break;
                default:
                    break;

        }
    }
}
