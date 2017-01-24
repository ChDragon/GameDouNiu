#include "douniuclient_jni.h"
#include "DouniuClient.h"

jstring Java_com_game_douniu_jni_DouniuClient_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
    return env->NewStringUTF("Hello from JNI! WZJ!");
}

JNIEXPORT jint JNICALL Java_com_game_douniu_jni_DouniuClient_nativeConnectAndLogin( JNIEnv* env,
                                                  jobject thiz, jstring ipaddr,
                                                  jstring username, jstring password)
{
	char *ipaddrStr;
    char *usernameStr;
    char *passwordStr;

    ipaddrStr = (char*)env->GetStringUTFChars(ipaddr,NULL);
    LOGD("ipaddrStr:%s",ipaddrStr);

    usernameStr = (char*)env->GetStringUTFChars(username,NULL);
    LOGD("usernameStr:%s",usernameStr);

    passwordStr = (char*)env->GetStringUTFChars(password,NULL);
    LOGD("passwordStr:%s",passwordStr);

	return connectServerAndLogin(ipaddrStr, usernameStr, passwordStr);
}

