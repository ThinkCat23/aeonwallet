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

public class TransactionHistory {
    static {
        System.loadLibrary("transaction_history_jni");
    }
    private static final String TAG = "History";
    private long handle;
    public int count;
    public TransactionHistory(long handle){
        Log.v(TAG, "History");
        this.handle = handle;
    }
    public int getCount(){
        Log.v(TAG, "getCount");
        this.count = getCountJNI();
        return count;
    }
    public TransactionInfo getTransaction(int index){
        Log.v(TAG, "getTransaction");
        return new TransactionInfo(getTransactionJNI(index));
    }
    public void refresh(){
        Log.v(TAG, "refresh >> "+handle);
        refreshJNI();
    }
    private native int getCountJNI();
    private native long getTransactionJNI(int index);
    private native void refreshJNI();
}
