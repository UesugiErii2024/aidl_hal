public class Co2SensorAidlTest {
    // 将成员变量声明为静态，以便在静态上下文中访问
    private static Co2SensorClient mCo2SensorClient;

    public static void main(String[] args) {
        // 初始化 Co2SensorClient 实例
        mCo2SensorClient = new Co2SensorClient();

        // 打开 CO2 传感器
        boolean openSuccess = mCo2SensorClient.openSensor(9600);
        if (openSuccess) {
            System.out.println("CO2 Sensor opened successfully.");
        } else {
            System.out.println("Failed to open CO2 Sensor.");
        }

        // 发送命令给传感器
        boolean commandSuccess = mCo2SensorClient.sendCommand("This is a test command: app");
        if (commandSuccess) {
            System.out.println("Command sent successfully.");
        } else {
            System.out.println("Failed to send command.");
        }

        // 获取传感器数据
        String data = mCo2SensorClient.getData();
        System.out.println("Sensor data: " + data);

        // 关闭 CO2 传感器
        boolean closeSuccess = mCo2SensorClient.closeSensor();
        if (closeSuccess) {
            System.out.println("CO2 Sensor closed successfully.");
        } else {
            System.out.println("Failed to close CO2 Sensor.");
        }
    }
}
