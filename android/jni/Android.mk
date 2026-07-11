LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libcurve25519-donna
LOCAL_SRC_FILES := curve25519-donna.c

include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE     := libcurve25519-ref10
LOCAL_SRC_FILES  := $(wildcard $(LOCAL_PATH)/ed25519/*.c) $(wildcard $(LOCAL_PATH)/ed25519/additions/*.c) \
                    $(wildcard $(LOCAL_PATH)/ed25519/additions/generalized/*.c) $(wildcard $(LOCAL_PATH)/ed25519/nacl_sha512/*.c) \
		    $(LOCAL_PATH)/ed25519/tests/internal_fast_tests.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)/ed25519/nacl_includes $(LOCAL_PATH)/ed25519/additions $(LOCAL_PATH)/ed25519/additions/generalized $(LOCAL_PATH)/ed25519/tests $(LOCAL_PATH)/ed25519/sha512 $(LOCAL_PATH)/ed25519

include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE     := libcurve25519
LOCAL_SRC_FILES  := curve25519-jni.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)/ed25519/nacl_includes $(LOCAL_PATH)/ed25519/additions $(LOCAL_PATH)/ed25519/additions/generalized $(LOCAL_PATH)/ed25519/tests $(LOCAL_PATH)/ed25519/sha512 $(LOCAL_PATH)/ed25519

LOCAL_STATIC_LIBRARIES := libcurve25519-donna libcurve25519-ref10

include $(BUILD_SHARED_LIBRARY)

