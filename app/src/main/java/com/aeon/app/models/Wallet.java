package com.aeon.app.models;

/*
Copyright 2020 ivoryguru

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Wallet {
    static {
        System.loadLibrary("wallet_jni");
    }
    private static final String TAG = "Wallet";
    private long handle;
    public BigDecimal balance = new BigDecimal(0);
    public BigDecimal unlockedBalance = new BigDecimal(0);
    public int index = 0;
    public long restoreHeight = 0;
    public String name;
    public String path;
    public String password;
    public String language;
    public String seed;
    public String address;
    public String account;
    public String publicSpendKey;
    public String publicViewKey;
    public String secretSpendKey;
    public String secretViewKey;
    public long height;
    public long heightEstimate;
    public ConnectionStatus connectionStatus;
    public Status status;
    public Node node;
    public TransactionHistory transactionHistory;
    public ArrayList<TransactionInfo> transactions = new ArrayList<>();
    public boolean isExists;
    public boolean isSynchronized;
    public boolean isInit;
    public boolean isClosed;


    enum Status {
        Ok,
        Error,
        Critical
    }
    public enum ConnectionStatus {
        Disconnected,
        Connected,
        WrongVersion
    }
    public Wallet(String path, String password,String language){
        isExists = false;
        isSynchronized = false;
        isInit = false;
        isClosed = false;
        Log.v(TAG, "Wallet");
        this.path = path;
        String[] pathArray = path.split("/");
        this.name = pathArray[pathArray.length-1];
        this.password = password;
        this.language = language;
    }
    public Wallet(String path, String password, String language, String seed, long restoreHeight){
        Log.v(TAG, "Wallet");
        isExists = false;
        isSynchronized = false;
        isInit = false;
        isClosed = false;
        this.seed = seed;
        this.restoreHeight = restoreHeight;
        this.path = path;
        String[] pathArray = path.split("/");
        this.name = pathArray[pathArray.length-1];
        this.password = password;
        this.language = language;
    }
    public Wallet(String path, String password, String language, String account,
                  String view, String spend, long restoreHeight) {
        Log.v(TAG, "Wallet");
        isExists = false;
        isSynchronized = false;
        isInit = false;
        isClosed = false;
        this.account = account;
        this.secretViewKey = view;
        this.secretSpendKey = spend;
        this.restoreHeight = restoreHeight;
        this.path = path;
        String[] pathArray = path.split("/");
        this.name = pathArray[pathArray.length-1];
        this.password = password;
        this.language = language;
    }
    public void close() {
        Log.v(TAG, "close");
        isClosed = true;
    }
    public void create(){
        Log.v(TAG, "create");
        if(isExistsJNI(path)){
            handle = openWalletJNI(path, password);
        } else if( this.seed != null ){
            handle = createFromSeedJNI(path, password, seed, restoreHeight);
        } else if(this.secretViewKey != null){
            handle = createFromKeysJNI(path, password, account, secretViewKey,secretSpendKey, restoreHeight);
        } else {
            handle = createJNI(path, password, language);
        }
        address = getAddressJNI(0,index);
        account = getAddressJNI(0,index);
        seed = getSeedJNI();
        isExists = isExistsJNI(path);
        status = Status.values()[getStatusJNI()];
        transactionHistory = new TransactionHistory(getTransactionHistoryJNI());
        publicSpendKey = getPublicSpendKeyJNI();
        publicViewKey = getPublicViewKeyJNI();
        secretSpendKey = getSecretSpendKeyJNI();
        secretViewKey = getSecretViewKeyJNI();
        storeJNI("");
    }
    public long createTransaction(TransactionPending tx) {
        Log.v(TAG, "createTransaction");
        BigDecimal atomicBalance = new BigDecimal(getBalanceJNI(0));
        if(Math.abs(tx.amount-atomicBalance.longValue())<1){
            return createSweepAllJNI(tx.recipient,tx.paymentID,3,0);
        } else {
            return createTransactionJNI(tx.recipient, tx.paymentID, tx.amount, 3, 0);
        }
    }
    public void disposeTransaction(TransactionPending pendingTransaction){
        Log.v(TAG, "disposeTransaction");
        disposeTransactionJNI(pendingTransaction);
    };
    public void init(){
        Log.v(TAG, "init");
        isInit = initJNI(node.hostAddress+":"+node.hostPort,
                0,"","");
    }
    public void refresh(){
        Log.v(TAG, "refresh >> "+handle);
        isExists = isExistsJNI(path);
        isSynchronized = isSynchronizedJNI();
        address = getAddressJNI(0,index);
        balance = new BigDecimal(getBalanceJNI(0)).movePointLeft(12);
        unlockedBalance = new BigDecimal(getUnlockedBalanceJNI(0)).movePointLeft(12);
        if(isSynchronized) {
            transactionHistory.getCount();
            if (transactionHistory.count > transactions.size()) {
                storeJNI("");
            }
        }
        connectionStatus = ConnectionStatus.values()[getConnectionStatusJNI()];
        if(connectionStatus == ConnectionStatus.Connected) {
            startRefreshJNI();
            if(isSynchronized) {
                transactionHistory.refresh();
                transactions.clear();
                while (transactionHistory.count > transactions.size()) {
                    transactions.add(transactionHistory.getTransaction(transactions.size()));
                }
            }
            node.height = getDaemonBlockChainHeightJNI();
            node.target = getDaemonBlockChainTargetHeightJNI();
            height = getBlockChainHeightJNI();
            heightEstimate = getBlockChainHeightEstimateJNI();
            node.version = getDaemonVersionJNI();
            restoreHeight = getRefreshFromBlockHeightJNI();
        }
    }
    public void setNode(Node node) {
        Log.v(TAG, "setNode");
        this.node = node;
        setDaemonAddressJNI(node.hostAddress+":"+node.hostPort);
        this.node.version = getDaemonVersionJNI();
    }
    public void setAddressIndex(int index) {
        Log.v(TAG, "setAddressIndex");
        this.index = index;
    }
    private native long createTransactionJNI(String dst_address, String payment_id, long amount, int ring_size, int priority);
    private native long createJNI(String path, String password, String language);
    private native long createFromSeedJNI(String path, String password, String seed, long restoreHeight);
    private native long createFromKeysJNI(String path, String password, String address,String view, String spend, long restoreHeight);
    private native long createSweepAllJNI(String dst_address, String payment_id, int ring_size, int priority);
    private native void disposeTransactionJNI(TransactionPending pendingTransaction);
    private native boolean initJNI(String daemon_address, long upper_transaction_size_limit, String daemon_username, String daemon_password);
    private native boolean isExistsJNI(String path);
    private native boolean isSynchronizedJNI();
    private native String getAddressJNI(int accountIndex, int addressIndex);
    private native long getBalanceJNI(int accountIndex);
    private native long getBlockChainHeightJNI();
    private native long getBlockChainHeightEstimateJNI();
    private native int getConnectionStatusJNI();
    private native long getDaemonBlockChainHeightJNI();
    private native long getDaemonBlockChainTargetHeightJNI();
    private native int getDaemonVersionJNI();
    private native String getPublicSpendKeyJNI();
    private native String getPublicViewKeyJNI();
    private native long getRefreshFromBlockHeightJNI();
    private native String getSecretSpendKeyJNI();
    private native String getSecretViewKeyJNI();
    private native String getSeedJNI();
    private native int getStatusJNI();
    private native long getTransactionHistoryJNI();
    private native long getUnlockedBalanceJNI(int accountIndex);
    private native long openWalletJNI(String path,String password);
    private native void setDaemonAddressJNI( String daemon_address);
    private native void startRefreshJNI();
    private native void storeJNI(String path);
}