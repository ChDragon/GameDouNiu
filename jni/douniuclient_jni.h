#include <string.h>
#include <jni.h>
#include <android/log.h>

#define TAG "[wzj]douniuclient_jni"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG , TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO  , TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN  , TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , TAG, __VA_ARGS__)

#ifndef _Included_DOUNIUCLIENT_H
#define _Included_DOUNIUCLIENT_H

#ifdef __cplusplus
extern "C" {
#endif

jstring Java_com_game_douniu_jni_DouniuClient_stringFromJNI( JNIEnv* env,
                                                  jobject thiz );
JNIEXPORT jint JNICALL Java_com_game_douniu_jni_DouniuClient_nativeConnectAndLogin( JNIEnv* env,
                                                  jobject thiz,
                                                  jstring ipaddr,
                                                  jstring username,
                                                  jstring password);
extern int connectServerAndLogin(char *ipaddr, char *username, char *password);

#ifdef __cplusplus
}
#endif

#endif //_Included_DOUNIUCLIENT_H
