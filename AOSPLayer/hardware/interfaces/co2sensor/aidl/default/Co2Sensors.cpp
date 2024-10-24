#define LOG_TAG "Co2SensorHAL"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#include "Co2Sensors.h"
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <android/log.h>
#include <cstring>  // For memset

namespace aidl::android::hardware::co2sensor {

// Constructor: Initialize file descriptor
Co2Sensor::Co2Sensor() : mFd(-1) {
    LOGD("Co2Sensor initialized");
}

// Destructor: Close the sensor and release resources
Co2Sensor::~Co2Sensor() {
    if (mFd != -1) {
        close(mFd);
        LOGD("Co2Sensor closed in destructor");
    }
}

::ndk::ScopedAStatus Co2Sensor::openCo2Sensor(int32_t in_baudrate, bool* _aidl_return) {
    mFd = open("/dev/ttyAMA0", O_RDWR | O_NOCTTY | O_NDELAY);
    if (mFd == -1) {
        LOGD("Failed to open CO2 sensor (HAL)");
        *_aidl_return = false;
        return ::ndk::ScopedAStatus::fromExceptionCode(EX_ILLEGAL_ARGUMENT);
    }

    struct termios options;
    memset(&options, 0, sizeof(options));  // Ensure options are zeroed
    tcgetattr(mFd, &options);  // Get the current configuration

    cfsetispeed(&options, in_baudrate);
    cfsetospeed(&options, in_baudrate);

    options.c_cflag |= (CLOCAL | CREAD);  // Enable the receiver and set local mode
    options.c_cflag &= ~PARENB;  // No parity
    options.c_cflag &= ~CSTOPB;  // 1 stop bit
    options.c_cflag &= ~CSIZE;   // Clear size mask
    options.c_cflag |= CS8;      // 8 data bits

    if (tcsetattr(mFd, TCSANOW, &options) != 0) {
        LOGD("Failed to configure CO2 sensor");
        close(mFd);
        mFd = -1;
        *_aidl_return = false;
        return ::ndk::ScopedAStatus::fromServiceSpecificError(errno);
    }

    LOGD("CO2 sensor opened with baudrate %d", in_baudrate);
    *_aidl_return = true;
    return ::ndk::ScopedAStatus::ok();
}

::ndk::ScopedAStatus Co2Sensor::closeCo2Sensor(bool* _aidl_return) {
    if (mFd != -1) {
        close(mFd);
        mFd = -1;
        LOGD("CO2 sensor closed");
    }
    *_aidl_return = true;
    return ::ndk::ScopedAStatus::ok();
}

::ndk::ScopedAStatus Co2Sensor::getData(std::string* _aidl_return) {
    if (mFd == -1) {
        LOGD("CO2 sensor not opened");
        return ::ndk::ScopedAStatus::fromExceptionCode(EX_ILLEGAL_ARGUMENT);
    }

    char buffer[256] = {0};
    ssize_t bytesRead = read(mFd, buffer, sizeof(buffer) - 1);  // Leave space for null terminator

    if (bytesRead > 0) {
        *_aidl_return = std::string(buffer, bytesRead);
        LOGD("CO2 sensor data: %s", _aidl_return->c_str());
    } else {
        *_aidl_return = "";
        LOGD("Failed to read data from CO2 sensor");
    }

    return ::ndk::ScopedAStatus::ok();
}

::ndk::ScopedAStatus Co2Sensor::sendCommand(const std::string& in_command, bool* _aidl_return) {
    if (mFd == -1) {
        LOGD("CO2 sensor not opened");
        return ::ndk::ScopedAStatus::fromExceptionCode(EX_ILLEGAL_ARGUMENT);
    }
     // 清空输出缓冲区，确保发送的是最新命令
    tcflush(mFd, TCOFLUSH);

    ssize_t bytesWritten = write(mFd, in_command.c_str(), in_command.size());
    if (bytesWritten == -1) {
        LOGD("Failed to send command to CO2 sensor");
        *_aidl_return = false;
        return ::ndk::ScopedAStatus::fromServiceSpecificError(errno);
    }

    LOGD("Sent command to CO2 sensor: %s", in_command.c_str());
    *_aidl_return = true;
    return ::ndk::ScopedAStatus::ok();
}

}  // namespace aidl::android::hardware::co2sensor
