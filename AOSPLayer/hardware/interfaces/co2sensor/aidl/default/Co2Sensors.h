#pragma once

#ifndef ANDROID_HARDWARE_CO2SENSOR_CO2SENSOR_H
#define ANDROID_HARDWARE_CO2SENSOR_CO2SENSOR_H

#include <string>
#include <aidl/android/hardware/co2sensor/BnCo2Sensor.h>

namespace aidl::android::hardware::co2sensor {

// Co2Sensor 继承 AIDL 生成的 BnCo2Sensor
class Co2Sensor : public BnCo2Sensor { 
public:
    Co2Sensor();
    virtual ~Co2Sensor();

    // 打开CO2传感器，设置波特率
    ::ndk::ScopedAStatus openCo2Sensor(int32_t in_baudrate, bool* _aidl_return) override;

    // 关闭CO2传感器
    ::ndk::ScopedAStatus closeCo2Sensor(bool* _aidl_return) override;

    // 从CO2传感器获取数据
    ::ndk::ScopedAStatus getData(std::string* _aidl_return) override;

    // 向CO2传感器发送命令
    ::ndk::ScopedAStatus sendCommand(const std::string& in_command, bool* _aidl_return) override;
    
private:
    int mFd;  // 文件描述符，用于访问 CO2 传感器
};

}  // namespace android::hardware::co2sensor

#endif  // ANDROID_HARDWARE_CO2SENSOR_CO2SENSOR_H
