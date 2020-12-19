/**
 * Portions Copyright (c) 2017 m2049r
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Copyright (c) 2020 ivoryguru
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <inttypes.h>
#include "wallet_jni.h"
#include "wallet2_api.h"
#include <android/log.h>

#ifdef __cplusplus
extern "C"
{
#endif
static JavaVM *cachedJVM;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    cachedJVM = jvm;
    JNIEnv *jenv;
    if (jvm->GetEnv(reinterpret_cast<void **>(&jenv), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_6;
}
#ifdef __cplusplus
}
#endif

int attachJVM(JNIEnv **jenv) {
    int envStat = cachedJVM->GetEnv((void **) jenv, JNI_VERSION_1_6);
    if (envStat == JNI_EDETACHED) {
        if (cachedJVM->AttachCurrentThread(jenv, nullptr) != 0) {
            return JNI_ERR;
        }
    } else if (envStat == JNI_EVERSION) {
        return JNI_ERR;
    }
    return envStat;
}
void detachJVM(JNIEnv *jenv, int envStat) {
    if (jenv->ExceptionCheck()) {
        jenv->ExceptionDescribe();
    }

    if (envStat == JNI_EDETACHED) {
        cachedJVM->DetachCurrentThread();
    }
}

#ifdef __cplusplus
extern "C"
{
#endif

JNIEXPORT jint JNICALL
Java_com_aeon_app_models_TransactionInfo_getDirectionJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    if (transactionInfo == NULL) {
        return 2;
    } else {
        return transactionInfo->direction();
    }
}
JNIEXPORT jboolean JNICALL
Java_com_aeon_app_models_TransactionInfo_isPendingJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return transactionInfo->isPending();
}
JNIEXPORT jboolean JNICALL
Java_com_aeon_app_models_TransactionInfo_isFailedJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return transactionInfo->isFailed();
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_TransactionInfo_getAmountJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return transactionInfo->amount();
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_TransactionInfo_getFeeJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return transactionInfo->fee();
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_TransactionInfo_getHeightJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return transactionInfo->blockHeight();
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_TransactionInfo_getConfirmationsJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return transactionInfo->confirmations();
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_TransactionInfo_getUnlockTimeJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return transactionInfo->unlockTime();
}
JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_TransactionInfo_getHashJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env, instance);
    return env->NewStringUTF(transactionInfo->hash().c_str());
}
JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_TransactionInfo_getPaymentIdJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env,  instance);
    return env->NewStringUTF(transactionInfo->paymentId().c_str());
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_TransactionInfo_getTimestampJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::TransactionInfo *transactionInfo = getHandle<Aeon::TransactionInfo>(env,  instance);
    return transactionInfo->timestamp();
}

#ifdef __cplusplus
}
#endif