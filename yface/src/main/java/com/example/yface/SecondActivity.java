package com.example.yface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.yfaceapi.GPIOManager;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText maxEdit;
    private EditText minEdit;
    private GPIOManager gpioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        gpioManager = GPIOManager.getInstance(this);

        maxEdit = findViewById(R.id.max_freq);
        minEdit = findViewById(R.id.min_freq);

        findViewById(R.id.btn46).setOnClickListener(this);
        findViewById(R.id.btn47).setOnClickListener(this);
        findViewById(R.id.btn48).setOnClickListener(this);
        findViewById(R.id.btn49).setOnClickListener(this);
        findViewById(R.id.btn50).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.btn50:
                sync();
                break;
                default:
                    break;
        }

    }

    private void sync() {
        try {
            Process process = Runtime.getRuntime().exec("sync");
            process.waitFor();
        } catch (Exception e) {
            Log.e("exect", e.getMessage(), e);
        } finally {
            Log.e("exect", "finally");
        }
    }

}
