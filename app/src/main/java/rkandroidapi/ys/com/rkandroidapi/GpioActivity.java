package rkandroidapi.ys.com.rkandroidapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ys.rkapi.MyManager;

public class GpioActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText gpioIndex;
    private TextView inValidText;

    private int index;
    private MyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpio);

        manager = MyManager.getInstance(this);
        manager.upgradeRootPermissionForExport();

        gpioIndex = findViewById(R.id.gpio_index);
        inValidText = findViewById(R.id.invalid);

        findViewById(R.id.get_io_status).setOnClickListener(this);
        findViewById(R.id.set_input).setOnClickListener(this);
        findViewById(R.id.set_output).setOnClickListener(this);
        findViewById(R.id.get_io_value).setOnClickListener(this);
        findViewById(R.id.set_highvalue).setOnClickListener(this);
        findViewById(R.id.set_lowvalue).setOnClickListener(this);
        findViewById(R.id.checkio).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkio:
                String indexText = gpioIndex.getText().toString();
                if ( !"".equals(indexText)) {
                    index = Integer.parseInt(indexText);
                    if (manager.exportGpio(index)) {
                        manager.upgradeRootPermissionForGpio(index);
                        String status = manager.getGpioDirection(index);
                        if ("".equals(status))
                            inValidText.setText("无效的GPIO");
                        else
                            inValidText.setText("有效的GPIO");
                    }
                }
                break;
            case R.id.get_io_status:
                Toast.makeText(this,"当前io的类型 = " + manager.getGpioDirection(index),Toast.LENGTH_LONG).show();
                break;
            case R.id.set_input:
                if (manager.setGpioDirection(index, 1))
                    Toast.makeText(this,"成功设置该io为输入口",Toast.LENGTH_LONG).show();
                break;
            case R.id.set_output:
                if (manager.setGpioDirection(index, 0))
                    Toast.makeText(this,"成功设置该io为输出口",Toast.LENGTH_LONG).show();
                break;
            case R.id.get_io_value:
                Toast.makeText(this,"当前io的电平 = " + manager.getGpioValue(index),Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_highvalue:
                if (manager.writeGpioValue(index,"1"))
                    Toast.makeText(this,"成功设置该io高电平",Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_lowvalue:
                if (manager.writeGpioValue(index,"0"))
                    Toast.makeText(this,"成功设置该io低电平",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }

    }
}
