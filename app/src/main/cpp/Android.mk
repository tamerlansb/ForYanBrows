LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)
LOCAL_MODULE := Chilkat
LOCAL_SRC_FILES := libs/$(TARGET_ARCH_ABI)/libchilkatAndroid.a
LOCAL_EXPORT_C_INCLUDES := include/
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE   := native-lib
LOCAL_SRC_FILES := native-lib.cpp
LOCAL_STATIC_LIBRARIES := Chilkat
LOCAL_CPP_FEATURES += exceptions
LOCAL_CPPFLAGS += -fexceptions
include $(BUILD_SHARED_LIBRARY)