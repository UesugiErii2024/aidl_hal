import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.hardware.co2sensor.ICo2Sensor;

public class Co2SensorClient {
    private static final String TAG = "Co2SensorClient";
    private ICo2Sensor mCo2SensorService;

    public Co2SensorClient() {
        try {
            IBinder binder = android.os.ServiceManager.getService("android.hardware.co2sensor.ICo2Sensor/default");

            if (binder != null) {
                mCo2SensorService = ICo2Sensor.Stub.asInterface(binder);
                Log.d(TAG, "CO2 Sensor Service connected successfully.");
            } else {
                Log.e(TAG, "Failed to connect to CO2 Sensor Service.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting CO2 Sensor Service", e);
        }
    }

    public boolean openSensor(int baudrate) {
        try {
            boolean result = mCo2SensorService.openCo2Sensor(baudrate);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false; // 或者你可以根据实际情况处理异常
        }
    }
    
    public boolean closeSensor() {
        try {
            boolean result = mCo2SensorService.closeCo2Sensor();
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String getData() {
        try {
            String data = mCo2SensorService.getData();
            return data;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean sendCommand(String command) {
        try {
            boolean result = mCo2SensorService.sendCommand(command);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
