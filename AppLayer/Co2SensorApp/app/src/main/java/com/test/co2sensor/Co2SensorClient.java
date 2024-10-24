package com.test.co2sensor;

import android.annotation.SuppressLint;
import android.os.IBinder;
//import android.os.ServiceManager;
import java.lang.reflect.Method;
import android.hardware.co2sensor.ICo2Sensor;
import android.os.RemoteException;
import android.util.Log;

public class Co2SensorClient {
    private static final String TAG = "Co2SensorClient";
    private ICo2Sensor mCo2SensorService;

    @SuppressLint("PrivateApi")
    public Co2SensorClient() {
        Method method = null;
        try {
            // 使用反射获取 ServiceManager 类
            method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, "android.hardware.co2sensor.ICo2Sensor/default");
            if (binder != null) {
                mCo2SensorService = ICo2Sensor.Stub.asInterface(binder);
                Log.d(TAG, "CO2 Sensor Service connected successfully.");
            } else {
                Log.e(TAG, "Failed to connect to CO2 Sensor Service.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error accessing CO2 Sensor Service", e);
        }
    }

    // 打开传感器的方法
    public void openSensor(int baudrate) {
        if (mCo2SensorService != null) {
            try {
                boolean result = mCo2SensorService.openCo2Sensor(baudrate);
                Log.d(TAG, "打开CO2传感器结果: " + result);
            } catch (RemoteException e) {
                Log.e(TAG, "打开CO2传感器失败", e);
            }
        } else {
            Log.e(TAG, "服务未绑定，无法打开传感器");
        }
    }

    // 关闭传感器的方法
    public void closeSensor() {
        if (mCo2SensorService != null) {
            try {
                boolean result = mCo2SensorService.closeCo2Sensor();
                Log.d(TAG, "关闭CO2传感器结果: " + result);
            } catch (RemoteException e) {
                Log.e(TAG, "关闭CO2传感器失败", e);
            }
        } else {
            Log.e(TAG, "服务未绑定，无法关闭传感器");
        }
    }

    // 获取传感器数据的方法
    public String getData() {
        if (mCo2SensorService != null) {
            try {
                String data = mCo2SensorService.getData();
                while(data.equals("\n")){
                    data = mCo2SensorService.getData();
                }
                Log.d(TAG, "CO2传感器数据: " + data);
                return data;
            } catch (RemoteException e) {
                Log.e(TAG, "获取CO2传感器数据失败", e);
                return null;
            }
        } else {
            Log.e(TAG, "服务未绑定，无法获取数据");
        }
        return null;
    }

    // 发送命令的方法
    public void sendCommand(String command) {
        if (mCo2SensorService != null) {
            try {
                boolean result = mCo2SensorService.sendCommand(command);
                Log.d(TAG, "发送命令结果: " + result);
            } catch (RemoteException e) {
                Log.e(TAG, "发送命令失败", e);
            }
        } else {
            Log.e(TAG, "服务未绑定，无法发送命令");
        }
    }

}
