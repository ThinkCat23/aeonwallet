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

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_createJNI(
        JNIEnv *env,
        jobject instance,
        jstring path,
        jstring password,
        jstring language
    ) {
    const char *_path = env->GetStringUTFChars(path, nullptr);
    const char *_password = env->GetStringUTFChars(password, nullptr);
    const char *_language = env->GetStringUTFChars(language, nullptr);

    Aeon::Wallet *wallet =
            Aeon::WalletManagerFactory::getWalletManager()->createWallet(
                    std::string(_path),
                    std::string(_password),
                    std::string(_language)
            );

    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    env->ReleaseStringUTFChars(language, _language);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_createFromSeedJNI(
        JNIEnv *env,
        jobject instance,
        jstring path,
        jstring password,
        jstring seed,
        jlong restoreHeight
) {
    const char *_path = env->GetStringUTFChars(path, nullptr);
    const char *_password = env->GetStringUTFChars(password, nullptr);
    const char *_seed = env->GetStringUTFChars(seed, nullptr);
    Aeon::NetworkType _networkType = static_cast<Aeon::NetworkType>(0);
    Aeon::Wallet *wallet =
            Aeon::WalletManagerFactory::getWalletManager()->recoveryWallet(
                    std::string(_path),
                    std::string(_password),
                    std::string(_seed),
                    _networkType,
                    restoreHeight
            );
    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    env->ReleaseStringUTFChars(seed, _seed);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_createFromKeysJNI(
        JNIEnv *env,
        jobject instance,
        jstring path,
        jstring password,
        jstring address,
        jstring view,
        jstring spend,
        jlong restoreHeight
) {
    const char *_path = env->GetStringUTFChars(path, nullptr);
    const char *_password = env->GetStringUTFChars(password, nullptr);
    const char *_address = env->GetStringUTFChars(address, nullptr);
    const char *_view = env->GetStringUTFChars(view, nullptr);
    const char *_spend = env->GetStringUTFChars(spend, nullptr);
    Aeon::NetworkType _networkType = static_cast<Aeon::NetworkType>(0);
    Aeon::Wallet *wallet =
            Aeon::WalletManagerFactory::getWalletManager()->createWalletFromKeys(
                    std::string(_path),
                    std::string(_password),
                    _networkType,
                    restoreHeight,
                    std::string(_address),
                    std::string(_view),
                    std::string(_spend)
            );
    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    env->ReleaseStringUTFChars(address, _address);
    env->ReleaseStringUTFChars(view, _view);
    env->ReleaseStringUTFChars(spend, _spend);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_openWalletJNI(
        JNIEnv *env,
        jobject instance,
        jstring path,
        jstring password
    ) {
    const char *_path = env->GetStringUTFChars(path, nullptr);
    const char *_password = env->GetStringUTFChars(password, nullptr);
    Aeon::NetworkType _networkType = static_cast<Aeon::NetworkType>(0);
    Aeon::Wallet *wallet =
        Aeon::WalletManagerFactory::getWalletManager()->openWallet(
            std::string(_path),
            std::string(_password),
            _networkType
    );
    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jboolean JNICALL
Java_com_aeon_app_models_Wallet_isExistsJNI(
        JNIEnv *env,
        jobject instance,
        jstring path
    ) {
    const char *_path = env->GetStringUTFChars(path, nullptr);
    bool exists =
            Aeon::WalletManagerFactory::getWalletManager()->walletExists(std::string(_path));
    env->ReleaseStringUTFChars(path, _path);
    return static_cast<jboolean>(exists);
}

JNIEXPORT void JNICALL
Java_com_aeon_app_models_Wallet_setDaemonAddressJNI(
        JNIEnv *env,
        jobject instance,
        jstring address
) {
    const char *_address = env->GetStringUTFChars(address, nullptr);
    Aeon::WalletManagerFactory::getWalletManager()->setDaemonAddress(std::string(_address));
    env->ReleaseStringUTFChars(address, _address);
}

JNIEXPORT jint JNICALL
Java_com_aeon_app_models_Wallet_getDaemonVersionJNI(
        JNIEnv *env,
        jobject instance
    ) {
    uint32_t version;
    bool isConnected =
            Aeon::WalletManagerFactory::getWalletManager()->connected(&version);
    if (!isConnected) version = 0;
    return version;
}

JNIEXPORT jint JNICALL
Java_com_aeon_app_models_Wallet_getStatusJNI(
        JNIEnv *env,
        jobject instance
    ) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->status();
}

JNIEXPORT jint JNICALL
Java_com_aeon_app_models_Wallet_getConnectionStatusJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->connected();
}


JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_Wallet_getPathJNI(
        JNIEnv *env,
        jobject instance
    ) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return env->NewStringUTF(wallet->path().c_str());
}

JNIEXPORT jboolean JNICALL
Java_com_aeon_app_models_Wallet_initJNI(
        JNIEnv *env,
        jobject instance,
        jstring daemon_address,
        jlong upper_transaction_size_limit,
        jstring daemon_username,
        jstring daemon_password
    ) {
    const char *_daemon_address = env->GetStringUTFChars(daemon_address, nullptr);
    const char *_daemon_username = env->GetStringUTFChars(daemon_username, nullptr);
    const char *_daemon_password = env->GetStringUTFChars(daemon_password, nullptr);
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    bool status = wallet->init(_daemon_address,
        (uint64_t) upper_transaction_size_limit,
        _daemon_username,
        _daemon_password
    );
    env->ReleaseStringUTFChars(daemon_address, _daemon_address);
    env->ReleaseStringUTFChars(daemon_username, _daemon_username);
    env->ReleaseStringUTFChars(daemon_password, _daemon_password);
    return static_cast<jboolean>(status);
}

JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_Wallet_getSeedJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return env->NewStringUTF(wallet->seed().c_str());
}
JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_Wallet_getSecretViewKeyJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return env->NewStringUTF(wallet->secretViewKey().c_str());
}
JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_Wallet_getPublicViewKeyJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return env->NewStringUTF(wallet->publicViewKey().c_str());
}

JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_Wallet_getSecretSpendKeyJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return env->NewStringUTF(wallet->secretSpendKey().c_str());
}
JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_Wallet_getPublicSpendKeyJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return env->NewStringUTF(wallet->publicSpendKey().c_str());
}
JNIEXPORT jstring JNICALL
Java_com_aeon_app_models_Wallet_getAddressJNI(
        JNIEnv *env,
        jobject instance,
        jint accountIndex,
        jint addressIndex
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return env->NewStringUTF(
            wallet->address((uint32_t) accountIndex, (uint32_t) addressIndex).c_str());
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getBalanceJNI(
        JNIEnv *env,
        jobject instance,
        jint accountIndex
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->balance((uint32_t) accountIndex);
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getUnlockedBalanceJNI(
        JNIEnv *env,
        jobject instance,
        jint accountIndex
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->unlockedBalance((uint32_t) accountIndex);
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getDaemonBlockChainHeightJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->daemonBlockChainHeight();
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getDaemonBlockChainTargetHeightJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->daemonBlockChainTargetHeight();
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getBlockChainHeightJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->blockChainHeight();
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getBlockChainHeightEstimateJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->approximateBlockChainHeight();
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getRefreshFromBlockHeightJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->getRefreshFromBlockHeight();
}
JNIEXPORT jboolean JNICALL
Java_com_aeon_app_models_Wallet_isSynchronizedJNI(
        JNIEnv *env,
        jobject instance
    ) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return static_cast<jboolean>(wallet->synchronized());
}

JNIEXPORT void JNICALL
Java_com_aeon_app_models_Wallet_startRefreshJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    wallet->startRefresh();
}
JNIEXPORT void JNICALL
Java_com_aeon_app_models_Wallet_pauseRefreshJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    wallet->pauseRefresh();
}
JNIEXPORT void JNICALL
Java_com_aeon_app_models_Wallet_setRefreshHeightJNI(
        JNIEnv *env,
        jobject instance,
        jlong height
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    wallet->setRefreshFromBlockHeight(height);
}

JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getRefreshHeightJNI(
        JNIEnv *env,
        jobject instance
) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return wallet->getRefreshFromBlockHeight();
}

JNIEXPORT void JNICALL
Java_com_aeon_app_models_Wallet_storeJNI(
        JNIEnv *env,
        jobject instance,
        jstring path
) {
    const char *_path = env->GetStringUTFChars(path, nullptr);
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    wallet->store(_path);
    env->ReleaseStringUTFChars(path, _path);
}


JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_getTransactionHistoryJNI(
        JNIEnv *env,
        jobject instance
    ) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    return reinterpret_cast<jlong>(wallet->history());
}

JNIEXPORT void JNICALL
Java_com_aeon_app_models_Wallet_disposeTransactionJNI(JNIEnv *env, jobject instance,
                                           jobject pendingTransaction) {
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    Aeon::PendingTransaction *_pendingTransaction =
            getHandle<Aeon::PendingTransaction>(env, pendingTransaction);
    wallet->disposeTransaction(_pendingTransaction);
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_createTransactionJNI(
        JNIEnv *env,
        jobject instance,
        jstring dst_address,
        jstring payment_id,
        jlong amount,
        jint ring_size,
        jint priority
) {
    const char *_dst_address = env->GetStringUTFChars(dst_address, nullptr);
    const char *_payment_id = env->GetStringUTFChars(payment_id, nullptr);
    Aeon::PendingTransaction::Priority _priority = static_cast<Aeon::PendingTransaction::Priority>(priority);
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    Aeon::PendingTransaction *pendingTransaction = wallet->createTransaction(
            _dst_address,
            _payment_id,
            amount,
            ring_size,
            _priority
    );
    env->ReleaseStringUTFChars(dst_address, _dst_address);
    env->ReleaseStringUTFChars(payment_id, _payment_id);
    return reinterpret_cast<jlong>(pendingTransaction);
}
JNIEXPORT jlong JNICALL
Java_com_aeon_app_models_Wallet_createSweepAllJNI(
        JNIEnv *env,
        jobject instance,
        jstring dst_address,
        jstring payment_id,
        jint ring_size,
        jint priority
) {
    const char *_dst_address = env->GetStringUTFChars(dst_address, nullptr);
    const char *_payment_id = env->GetStringUTFChars(payment_id, nullptr);
    Aeon::PendingTransaction::Priority _priority = static_cast<Aeon::PendingTransaction::Priority>(priority);
    Aeon::Wallet *wallet = getHandle<Aeon::Wallet>(env, instance);
    Aeon::optional<uint64_t> empty;
    Aeon::PendingTransaction *pendingTransaction = wallet->createTransaction(
            _dst_address,
            _payment_id,
            empty,
            ring_size,
            _priority
    );
    env->ReleaseStringUTFChars(dst_address, _dst_address);
    env->ReleaseStringUTFChars(payment_id, _payment_id);
    return reinterpret_cast<jlong>(pendingTransaction);
}

#ifdef __cplusplus
}
#endif