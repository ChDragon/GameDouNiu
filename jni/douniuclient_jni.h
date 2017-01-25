#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

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

jstring native_hello( JNIEnv* env, jobject thiz );
jboolean nativeInit(JNIEnv *env, jobject thiz);
jboolean nativeStop(JNIEnv *env, jobject thiz);
jint nativeConnectAndLogin( JNIEnv* env,
                            jobject thiz, jstring ipaddr,
                            jstring username, jstring password);

extern int connectServerAndLogin(char *ipaddr, char *username, char *password);

extern void loginCb(int userid, char *data, int datalen);
extern void otherLoginCb(char *data, int datalen);
extern void logoutCb(int value);
extern void prepareCb(char *data, int datalen);
extern void otherUserPrepareCb(char *data, int datalen);
extern void willBankerCb(char *data, int datalen);
extern void tryBankerCb(char *data, int datalen);
extern void willStakeCb(char *data, int datalen);
extern void stakeCb(char *data, int datalen);
extern void otherUserStakeValueCb(char *data, int datalen);
extern void willStartCb(char *data, int datalen);
extern void playCb(char *data, int datalen);
extern void otherUserCardPatternCb(char *data, int datalen);
extern void gameResultCb(char *data, int datalen);

#ifdef __cplusplus
}
#endif

#endif //_Included_DOUNIUCLIENT_H
