package com.example.co2;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.hardware.co2sensor.ICo2Sensor;

public class Co2SensorClient {
    private static final String TAG = "Co2SensorClient";
    private ICo2Sensor mCo2SensorService;

    public Co2SensorClient() {
        // 获取服务实例
        IBinder binder = ServiceManager.getService("android.hardware.co2sensor.ICo2Sensor/default");
        if (binder != null) {
            mCo2SensorService = ICo2Sensor.Stub.asInterface(binder);
            Log.d(TAG, "CO2 Sensor Service connected successfully.");
        } else {
            Log.e(TAG, "Failed to connect to CO2 Sensor Service.");
        }
    }

    public void openSensor(int baudrate) {
        try {
            boolean result = mCo2SensorService.openCo2Sensor(baudrate);
            Log.d(TAG, "Open CO2 Sensor result: " + result);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to open CO2 Sensor", e);
        }
    }

    public void closeSensor() {
        try {
            boolean result = mCo2SensorService.closeCo2Sensor();
            Log.d(TAG, "Close CO2 Sensor result: " + result);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to close CO2 Sensor", e);
        }
    }

    public String getData() {
        try {
            String data = mCo2SensorService.getData();
            Log.d(TAG, "CO2 Sensor Data: " + data);
            return data;
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get data from CO2 Sensor", e);
            return null;
        }
    }

    public void sendCommand(String command) {
        try {
            boolean result = mCo2SensorService.sendCommand(command);
            Log.d(TAG, "Send command result: " + result);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send command to CO2 Sensor", e);
        }
    }
}
