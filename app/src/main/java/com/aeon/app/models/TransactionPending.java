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

public class TransactionPending {
    static {
        System.loadLibrary("transaction_pending_jni");
    }
    private static final String TAG = "Pending";
    public boolean isDisposedByUser;
    private long handle;
    public boolean isCommitted;
    public boolean isConfirmedByUser;
    public boolean isCreated;
    public boolean isAttempted;
    public String recipient;
    public String paymentID = "";
    public long amount;
    public long fee;
    public long dust;
    public Status status;
    public enum Status {
        Ok,
        Error,
        Critical
    }

    public enum Priority {
        Priority_Default,
        Priority_Low,
        Priority_Medium,
        Priority_High,
        Priority_Last
    }
    public TransactionPending(String recipient, long amount){
        Log.v(TAG, "Pending");
        isCommitted = false;
        isConfirmedByUser = false;
        isDisposedByUser = false;
        isAttempted = false;
        isCreated = false;
        this.recipient = recipient;
        this.amount = amount;
        this.fee = fee;
    }
    public TransactionPending(String recipient, long amount, String paymentID){
        Log.v(TAG, "Pending");
        isCommitted = false;
        isConfirmedByUser = false;
        isDisposedByUser = false;
        isAttempted = false;
        isCreated = false;
        this.recipient = recipient;
        this.paymentID = paymentID;
        this.amount = amount;
        this.fee = fee;
    }
    public void setHandle(long handle){
        Log.v(TAG, "setHandle");
        this.handle = handle;
        refresh();
    }
    public long getHandle(){
        return handle;
    }
    public void refresh(){
        Log.v(TAG, "refresh");
        this.status = Status.values()[getStatusJNI()];
        this.fee = getFeeJNI();
        this.amount = getAmountJNI();
        this.dust = getDustJNI();
    }
    public void commit(){
        Log.v(TAG, "commit");
        isCommitted = commitJNI();
        isAttempted = true;
    }
    private native boolean commitJNI();
    private native int getStatusJNI();
    public native long getFeeJNI();
    public native long getAmountJNI();
    public native long getDustJNI();
}
