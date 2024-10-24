package android.hardware.co2sensor;
@VintfStability
interface ICo2Sensor {
    boolean openCo2Sensor(in int baudrate);
    boolean closeCo2Sensor();
    String getData();
    boolean sendCommand(in String command);
} 