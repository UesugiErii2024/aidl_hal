#define LOG_TAG "Co2HalService"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#include "Co2Sensors.h"
#include <android/binder_manager.h>
#include <android/binder_process.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>
#include <android/log.h>

using namespace aidl::android::hardware::co2sensor;

int main() {
    LOGD("Starting CO2 sensor HAL service");

    // Enable vndbinder to allow vendor-to-venfor binder call
    android::ProcessState::initWithDriver("/dev/vndbinder");
    // Initialize the binder thread pool
    ABinderProcess_setThreadPoolMaxThreadCount(0);
    ABinderProcess_startThreadPool();

    // Create an instance of the CO2 sensor service
    std::shared_ptr<Co2Sensor> co2SensorService = ndk::SharedRefBase::make<Co2Sensor>();
    const std::string instanceName = std::string("android.hardware.co2sensor.ICo2Sensor/default");

    if(co2SensorService != nullptr){
        // Register the service with the AIDL NDK backend
        if (AServiceManager_addService(co2SensorService->asBinder().get(), instanceName.c_str()) != STATUS_OK) {
            LOGD("Failed to register CO2 sensor HAL service");
            return -1;
        }
    } else {
        LOGD("Failed to get ICo2Sensor instance");
        return -1;
    }
    

    LOGD("CO2 sensor HAL service registered successfully");

    // Start the binder processing loop
    ABinderProcess_joinThreadPool();

    return EXIT_FAILURE;  // Should never reach here
}
