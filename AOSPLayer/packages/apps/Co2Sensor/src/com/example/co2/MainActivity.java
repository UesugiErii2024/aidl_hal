package com.example.co2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "Co2SensorApp";
    private static Co2SensorClient co2SensorClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 初始化 Co2SensorClient 实例
        co2SensorClient = new Co2SensorClient();
        int baudrate = 9600;
        co2SensorClient.openSensor(baudrate);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText);
                String txt = editText.getText().toString();
                Log.d(TAG, "App: Request = " + txt);

                // 发送命令到 CO2 传感器
                if (co2SensorClient != null) {
                    co2SensorClient.sendCommand(txt);
                }

                // 获取传感器数据
                String ret = "";
                if (co2SensorClient != null) {
                    ret = co2SensorClient.getData();
                }

                Log.d(TAG, "App: Response = " + ret);

                TextView tv = (TextView) findViewById(R.id.textView);
                tv.setText(ret);
            }
        });
        co2SensorClient.closeSensor();
    }
}
