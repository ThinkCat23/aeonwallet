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

import com.aeon.app.BackgroundThread;

import java.math.BigDecimal;

public class TransactionInfo {
    static {
        System.loadLibrary("transaction_info_jni");
    }
    private static final String TAG = "Info";
    private final long handle;
    public Direction direction = Direction.Unknown;
    public boolean isPending = false;
    public boolean isFailed = false;
    public BigDecimal amount = new BigDecimal(0);
    public BigDecimal fee = new BigDecimal(0);
    public long height = 0 ;
    public long confirmations= 0;
    public long unlockTime = 0;
    public long timestamp = 0;
    public String hash = "";
    public String paymentId= "";

    public enum Direction {
        In,
        Out,
        Unknown
    }
    public TransactionInfo(long handle){
        Log.v(TAG, "Info");
        this.handle = handle;
    }

    public void refresh(){
        Log.v(TAG, "refresh >> "+handle);
        if(this.confirmations<10) {
            getDirection();
            getHash();
            getAmount();
            getHeight();
            isPending();
            isFailed();
            getFee();
            getConfirmations();
            getUnlockTime();
            getPaymentId();
            getTimestamp();
        }
    }
    public Direction getDirection(){
        Log.v(TAG, "getDirection");
        this.direction= Direction.values()[getDirectionJNI()];
        return direction;
    }
    public boolean isPending(){
        Log.v(TAG, "isPending");
        this.isPending=isPendingJNI();
        return isPending;

    }
    public boolean isFailed(){
        Log.v(TAG, "isFailed");
        this.isFailed=isFailedJNI();
        return isFailed;

    }
    public BigDecimal getAmount(){
        Log.v(TAG, "getAmount");
        this.amount= (new BigDecimal(getAmountJNI())).movePointLeft(12);
        return amount;

    }
    public BigDecimal getFee(){
        Log.v(TAG, "getFee");
        this.fee= (new BigDecimal(getFeeJNI())).movePointLeft(12);
        return fee;
    }
    public long getHeight(){
        Log.v(TAG, "getHeight");
        this.height=getHeightJNI();
        if(this.height==0){
            this.height = BackgroundThread.height+1;
        }
        return height;
    }
    public long getConfirmations(){
        Log.v(TAG, "getConfirmations");
        this.confirmations=getConfirmationsJNI();
        return confirmations;

    }
    public long getUnlockTime(){
        Log.v(TAG, "getUnlockTime");
        this.unlockTime=getUnlockTimeJNI();
        return unlockTime;

    }
    public String getHash() {
        Log.v(TAG, "getHash");
        this.hash=getHashJNI();
        return hash;

    }
    public String getPaymentId() {
        Log.v(TAG, "getPaymentId");
        this.paymentId=getPaymentIdJNI();
        return paymentId;

    }
    public long getTimestamp() {
        Log.v(TAG, "getPaymentId");
        this.timestamp=getTimestampJNI();
        return timestamp;
    }
    private native int getDirectionJNI();
    private native boolean isPendingJNI();
    private native boolean isFailedJNI();
    private native long getAmountJNI();
    private native long getFeeJNI();
    private native long getHeightJNI();
    private native long getConfirmationsJNI();
    private native long getUnlockTimeJNI();
    private native String getHashJNI() ;
    private native String getPaymentIdJNI() ;
    private native int getTimestampJNI();
}
