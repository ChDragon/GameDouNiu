#include "douniuclient_jni.h"
#include "DouniuClient.h"

static JavaVM* g_vm                   = NULL;
static JNIEnv* g_env                 = NULL;
static jclass  g_JniClass               = NULL;
static jobject g_JniObject              = NULL;
static const char *classPathName      = "com/game/douniu/jni/DouniuClientInterface";

jmethodID loginCb_Id       			= NULL;
jmethodID otherLoginCb_Id 			= NULL;
jmethodID logoutCb_Id        		= NULL;
jmethodID otherLogoutCb_Id        	= NULL;
jmethodID prepareCb_Id         		= NULL;
jmethodID otherUserPrepareCb_Id     = NULL;
jmethodID willBankerCb_Id       	= NULL;
jmethodID tryBankerCb_Id       		= NULL;

jmethodID willStakeCb_Id       		= NULL;
jmethodID stakeCb_Id 				= NULL;
jmethodID otherUserStakeValueCb_Id  = NULL;
jmethodID willStartCb_Id         	= NULL;
jmethodID playCb_Id     			= NULL;
jmethodID otherUserCardPatternCb_Id = NULL;
jmethodID gameResultCb_Id       	= NULL;

#define ATTACH_CURRENT_THREAD_IF_NEED  \
		int status;\
		JNIEnv *env;\
		int isAttached = 0;\
		status = (*g_vm)->GetEnv(g_vm, (void **)&env, JNI_VERSION_1_4);\
		if (status < 0)\
		{\
			LOGD("status:%d, is native thread",status);\
			status = (*g_vm)->AttachCurrentThread(g_vm, &env, NULL);\
			if (status < 0)\
			{\
				LOGE("status:%d, failed to attach current thread",status);\
				return;\
			}\
			isAttached = 1;\
			LOGD("IsAttached is 1");\
		}

#define DETACH_CURRENT_THREAD_IF_NEED  \
		if (isAttached)\
		{\
			(*g_vm)->DetachCurrentThread(g_vm);\
		}

#define CHECK_METHOD_ID(methodId)  \
		if (!methodId)\
		{\
			LOGD("methodId Init Failed. %s %s %d", __FILE__, __FUNCTION__, __LINE__);\
			return FALSE;\
		}

//======Callback to invoke Java=============
void loginCb(int userid, char *data, int datalen)
{
	LOGD("[loginCb]---userid[%d], data[%s], datalen[%d]", userid, data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, loginCb_Id, userid, jstr);
	//(*env)->ReleaseCharArrayElements(env, jarray, pArray, 0);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void otherLoginCb(char *data, int datalen)
{
	LOGD("[otherLoginCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, otherLoginCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void logoutCb(int value)
{
	LOGD("start %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	(*g_vm)->AttachCurrentThread(g_vm, &g_env, NULL); //获取当前线程的JNIEnv*

	(*g_env)->CallVoidMethod(g_env, g_JniObject, logoutCb_Id, value);

	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	(*g_vm)->DetachCurrentThread(g_vm); //释放当前线程的JNIEnv*
}

void otherLogoutCb(int value)
{
	LOGD("start %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	(*g_vm)->AttachCurrentThread(g_vm, &g_env, NULL);

	(*g_env)->CallVoidMethod(g_env, g_JniObject, otherLogoutCb_Id, value);

	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	(*g_vm)->DetachCurrentThread(g_vm);
}

void prepareCb(char *data, int datalen)
{
	LOGD("[%s %d]data[%s], datalen[%d]", __FUNCTION__, __LINE__, data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, prepareCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void otherUserPrepareCb(char *data, int datalen)
{
	LOGD("[otherUserPrepareCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, otherUserPrepareCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void willBankerCb(char *data, int datalen)
{
	LOGD("[willBankerCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, willBankerCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void tryBankerCb(char *data, int datalen)
{
	LOGD("[tryBankerCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, tryBankerCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void willStakeCb(char *data, int datalen)
{
	LOGD("[willStakeCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, willStakeCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void stakeCb(char *data, int datalen)
{
	LOGD("[stakeCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, stakeCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void otherUserStakeValueCb(char *data, int datalen)
{
	LOGD("[otherUserStakeValueCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, otherUserStakeValueCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void willStartCb(char *data, int datalen)
{
	LOGD("[willStartCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, willStartCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void playCb(char *data, int datalen)
{
	LOGD("[playCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, playCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void otherUserCardPatternCb(char *data, int datalen)
{
	LOGD("[otherUserCardPatternCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, otherUserCardPatternCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}

void gameResultCb(char *data, int datalen)
{
	LOGD("[gameResultCb]---data[%s], datalen[%d]", data, datalen);
	ATTACH_CURRENT_THREAD_IF_NEED;

	jstring jstr = (*env)->NewStringUTF(env, data);
	(*env)->CallVoidMethod(env, g_JniObject, gameResultCb_Id, jstr);

	DETACH_CURRENT_THREAD_IF_NEED;
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}
//==================================


//======Java invoke C===============
jboolean nativeInit(JNIEnv *env, jobject thiz)
{
	LOGD("%s %s %d", __FILE__, __FUNCTION__, __LINE__);
	g_env    = env;
	g_JniObject = (*env)->NewGlobalRef(env, thiz);

	// Get the Class.
	g_JniClass = (*g_env)->FindClass(g_env, classPathName);
	if (!g_JniClass)
	{
		LOGE("JniClass Init Failed.");
	    return FALSE;
	}

	loginCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "loginCb", "(ILjava/lang/String;)V");
	CHECK_METHOD_ID(loginCb_Id);

	otherLoginCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "otherLoginCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(otherLoginCb_Id);

	logoutCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "logoutCb", "(I)V");
	CHECK_METHOD_ID(logoutCb_Id);

	otherLogoutCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "otherLogoutCb", "(I)V");
	CHECK_METHOD_ID(otherLogoutCb_Id);

	prepareCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "prepareCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(prepareCb_Id);

	otherUserPrepareCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "otherUserPrepareCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(otherUserPrepareCb_Id);

	willBankerCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "willBankerCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(willBankerCb_Id);

	tryBankerCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "tryBankerCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(tryBankerCb_Id);


	willStakeCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "willStakeCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(willStakeCb_Id);

	stakeCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "stakeCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(stakeCb_Id);

	otherUserStakeValueCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "otherUserStakeValueCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(otherUserStakeValueCb_Id);

	willStartCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "willStartCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(willStartCb_Id);

	playCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "playCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(playCb_Id);

	otherUserCardPatternCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "otherUserCardPatternCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(otherUserCardPatternCb_Id);

	gameResultCb_Id = (*g_env)->GetMethodID(g_env, g_JniClass, "gameResultCb", "(Ljava/lang/String;)V");
	CHECK_METHOD_ID(gameResultCb_Id);

	return TRUE;
}

jboolean nativeStop(JNIEnv *env, jobject thiz)
{
	(*g_env)->DeleteGlobalRef(g_env, g_JniObject);
	g_env = NULL;
	g_JniClass = NULL;
	g_JniObject = NULL;
	loginCb_Id = NULL;
	otherLoginCb_Id = NULL;
	logoutCb_Id        		= NULL;
	otherLogoutCb_Id        = NULL;
	prepareCb_Id         		= NULL;
	otherUserPrepareCb_Id     = NULL;
	willBankerCb_Id       	= NULL;
	tryBankerCb_Id       		= NULL;
	willStakeCb_Id       		= NULL;
	stakeCb_Id 				= NULL;
	otherUserStakeValueCb_Id  = NULL;
	willStartCb_Id         	= NULL;
	playCb_Id     			= NULL;
	otherUserCardPatternCb_Id = NULL;
	gameResultCb_Id       	= NULL;
	return TRUE;
}

jstring native_hello(JNIEnv* env, jobject thiz )
{
    return (*env)->NewStringUTF(env, "Hello from JNI! WZJ!");
}

jstring nativeConnectAndLogin(JNIEnv* env,
                            jobject thiz, jstring ipaddr,
                            jstring username, jstring password)
{
	char *ipaddrStr;
    char *usernameStr;
    char *passwordStr;

    ipaddrStr = (char*)(*env)->GetStringUTFChars(env, ipaddr,NULL);
    LOGD("ipaddrStr:%s",ipaddrStr);

    usernameStr = (char*)(*env)->GetStringUTFChars(env, username,NULL);
    LOGD("usernameStr:%s",usernameStr);

    passwordStr = (char*)(*env)->GetStringUTFChars(env, password,NULL);
    LOGD("passwordStr:%s",passwordStr);

    char *pstr = connectServerAndLogin(ipaddrStr, usernameStr, passwordStr);
    jstring jstr = (*env)->NewStringUTF(env, pstr);

    return jstr;
}

jint nativeLogoutAndExit(JNIEnv* env, jobject thiz, jstring username)
{
    char *usernameStr = (char*)(*env)->GetStringUTFChars(env, username,NULL);
    LOGD("usernameStr:%s",usernameStr);

    jint ret = logoutAndExit(usernameStr);
    return ret;
}

void nativePrepareCMD(JNIEnv* env, jobject thiz)
{
	LOGD("nativePrepareCMD start");
    prepareCMD();
}

void nativeTryingBankerCMD(JNIEnv* env, jobject thiz, jint value)
{
	LOGD("nativeTryingBankerCMD start");
	tryingBankerCMD(value);
}

void nativeStakeCMD(JNIEnv* env, jobject thiz, jint stakeValue)
{
	LOGD("nativeStakeCMD start");
	stakeCMD(stakeValue);
}

void nativePlayCMD(JNIEnv* env, jobject thiz, jint niuValue)
{
	LOGD("nativePlayCMD start");
    playCMD(niuValue);
}
//==================================


/**
* 方法对应表
*/
static JNINativeMethod gMethods[] = {
    {"stringFromJNI", "()Ljava/lang/String;", (void*)native_hello},
    {"nativeInit", "()Z", (void*)nativeInit},
    {"nativeStop", "()Z", (void*)nativeStop},
    {"nativeConnectAndLogin", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void*)nativeConnectAndLogin},
    {"nativeLogoutAndExit", "(Ljava/lang/String;)I", (void*)nativeLogoutAndExit},
    {"nativePrepareCMD", "()V", (void*)nativePrepareCMD},
    {"nativeTryingBankerCMD", "(I)V", (void*)nativeTryingBankerCMD},
    {"nativeStakeCMD", "(I)V", (void*)nativeStakeCMD},
    {"nativePlayCMD", "(I)V", (void*)nativePlayCMD},
};


//======以下不需修改================
/*
* 为某一个类注册本地方法
*/
static int registerNativeMethods(JNIEnv* env
        , const char* className
        , JNINativeMethod* gMethods, int numMethods) {
    jclass clazz;
    clazz = (*env)->FindClass(env, className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if ((*env)->RegisterNatives(env, clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
* 为所有类注册本地方法
*/
static int registerNatives(JNIEnv* env) {
    return registerNativeMethods(env, classPathName, gMethods,
            sizeof(gMethods) / sizeof(gMethods[0]));
}

/*
* System.loadLibrary("lib")时调用
* 如果成功返回JNI版本, 失败返回-1
*/
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = -1;

    if ((*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);

    if (!registerNatives(env)) {//注册
        return -1;
    }
    //成功
    result = JNI_VERSION_1_4;
    g_vm = vm;
    LOGD("JNI_OnLoad:%s",(g_vm!=NULL)?"NotNull":"Null");

    return result;
}
//==================================

//backup
/*void loginCb(char *data, int datalen)
{
	int status;
	int isAttached = 0;
	JNIEnv *env;

	LOGD("start %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	LOGD("+++data[%s], datalen[%d]", data, datalen);
	status = (*g_vm)->GetEnv(g_vm, (void **)&env, JNI_VERSION_1_4);
	if (status < 0)
	{
		LOGD("status:%d, is native thread",status);
		status = (*g_vm)->AttachCurrentThread(g_vm, &env, NULL); //获取当前线程的JNIEnv*
		if (status < 0)
		{
			LOGE("status:%d, failed to attach current thread",status);
			return;
		}
		isAttached = 1;
		LOGD("IsAttached is 1");
	}

	LOGD("11 %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	jbyteArray jarray = (*env)->NewByteArray(env, datalen);
	if (jarray == NULL)
	{
		LOGE("%s %s %d", __FILE__, __FUNCTION__, __LINE__);
		if (isAttached)
		{
			(*g_vm)->DetachCurrentThread(g_vm);
		}
		return;
	}

	LOGD("22 %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	jbyte* bytes = (*env)->GetByteArrayElements(env, jarray, NULL);
	if (bytes == NULL)
	{
		LOGE("%s %s %d", __FILE__, __FUNCTION__, __LINE__);
		if (isAttached)
		{
			(*g_vm)->DetachCurrentThread(g_vm);
		}
		return;
	}
	LOGD("+++ bytes:%s", bytes);

	LOGD("33 %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	memcpy(bytes, data, datalen);//strcpy(bytes, data);//
	LOGD("+++ end bytes:%s", bytes);
	(*env)->CallVoidMethod(env, g_JniObject, loginCb_Id, jarray, datalen); //调用java层相关方法
	(*env)->ReleaseByteArrayElements(env, jarray, bytes, 0);

	LOGD("444 %s %s %d", __FILE__, __FUNCTION__, __LINE__);
	if (isAttached)
	{
		(*g_vm)->DetachCurrentThread(g_vm);//释放当前线程的JNIEnv*
	}
	LOGD("end %s %s %d", __FILE__, __FUNCTION__, __LINE__);
}*/
