package com.test.co2sensor;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "Co2SensorApp";
    private static Co2SensorClient co2SensorClient;

    // 创建一个 Handler
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable dataFetcher;
    String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 Co2SensorClient 实例
        co2SensorClient = new Co2SensorClient();
        int baudrate = 9600;
        co2SensorClient.openSensor(baudrate);
        Button btn = (Button) findViewById(R.id.btn);
        Button end = (Button) findViewById(R.id.btn_end);
        Button ref = (Button) findViewById(R.id.btn_refresh);
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

            }
        });

        TextView dataTextView = (TextView) findViewById(R.id.textView);
        // 定义 Runnable，每秒获取一次数据
//        dataFetcher = new Runnable() {
//            @Override
//            public void run() {
//                // 获取数据并更新 UI
//                if (co2SensorClient != null){
//                     data = co2SensorClient.getData();
//                dataTextView.setText(data);
//                }
//       +         Log.d(TAG, "App: Response Data： " + data);
//
//                // 每 10 秒再次执行
//                handler.postDelayed(this, 10000);
//            }
//        };
//
//        // 启动定时任务
//        handler.post(dataFetcher);

        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取数据并更新 UI
                if (co2SensorClient != null){
                    data = co2SensorClient.getData();
                    dataTextView.setText(data);
                }
                Log.d(TAG, "--------NEW DATA----------");
                Log.d(TAG, "App: Response Data： " + data);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                co2SensorClient.closeSensor();
            }
        });
    }
}
