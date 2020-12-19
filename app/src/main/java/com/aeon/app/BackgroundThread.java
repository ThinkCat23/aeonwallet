package com.aeon.app;

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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.aeon.app.models.Node;
import com.aeon.app.models.TransactionInfo;
import com.aeon.app.models.TransactionPending;
import com.aeon.app.models.Wallet;
import com.aeon.app.ui.node.NodeContent;
import com.aeon.app.ui.recent.RecentContent;
import com.aeon.app.ui.transfer.ReceiveFragment;
import com.aeon.app.ui.transfer.TransferFragment;
import com.aeon.app.ui.wallet.WalletContent;

public class BackgroundThread extends Thread{
    private static final String TAG = "BackgroundThread";
    private int sleepCount = 0;
    private static Wallet wallet = null;
    private static boolean isNodeChanged;
    public static boolean isRunning = false;
    public static boolean isManaging = false;
    public static boolean isConnected = false;
    public static boolean isShownNewWalletFragment = false;
    public static TransactionPending pendingTransaction = null;
    public static Node node;
    public static long height;
    public static String path = null;
    public static String seed = null;
    public BackgroundThread() {

    }

    public void run() {
        isRunning = true;
        Log.v(TAG, "isRunning");
        while(!Thread.interrupted()){
            if(isManaging && ! wallet.isClosed) {
                if (wallet.isExists == false) {
                    Log.v(TAG, "!wallet.isExists");
                    wallet.create();
                } else if (wallet.isInit == false) {
                    Log.v(TAG, "!wallet.isInit");
                    this.seed = wallet.seed;
                    connectToNode();
                    init();
                } else {
                    if(sleepCount==300){
                        sleepCount = 0;
                        updateTransactions();
                    }
                    if(sleepCount%100==0) {
                        refreshWalletInfo();
                        refreshNode();
                        clearTransactionQueue();
                    }
                }
            }
            try {
                Thread.sleep(100);
                sleepCount++;
            } catch (InterruptedException e) {
                this.interrupt();
                e.printStackTrace();
            }
        }
        Log.v(TAG, "Thread.interrupted()");
        wallet.close();
        isManaging = false;
        isRunning = false;
        isConnected=false;
        isShownNewWalletFragment=false;
        Log.v(TAG, "!isManaging");
        WalletContent.clearItems();
        Log.v(TAG, "!isRunning");
    }

    public static void queueWallet(String path){
        Log.v(TAG, "queueWallet");
        BackgroundThread.path = path;
        BackgroundThread.seed = null;
        wallet = new Wallet(path,MainActivity.getPassword(),"English");
        isManaging = true;
    }
    public static void queueWallet(String path, String seed, long restoreHeight){
        Log.v(TAG, "queueWallet");
        BackgroundThread.path = path;
        BackgroundThread.seed = null;
        wallet = new Wallet(path,MainActivity.getPassword(),"English", seed, restoreHeight);
        isManaging = true;
    }

    public static void queueWallet(String path, String account, String view, String spend, long restoreHeight) {
        Log.v(TAG, "queueWallet");
        BackgroundThread.path = path;
        BackgroundThread.seed = null;
        wallet = new Wallet(path,MainActivity.getPassword(),"English", account, view, spend, restoreHeight);
        isManaging = true;
    }

    public static void queueTransaction(String dst_address, long amount){
        Log.v(TAG, "queueTransaction");
        pendingTransaction = new TransactionPending(dst_address,amount);
    }
    public static void queueTransaction(String dst_address, long amount, String paymentID){
        Log.v(TAG, "queueTransaction");
        pendingTransaction = new TransactionPending(dst_address,amount,paymentID);
    }
    public static void confirmTransaction(){
        Log.v(TAG, "confirmTransaction");
        if(pendingTransaction!=null) {
            pendingTransaction.isConfirmedByUser = true;
        }
    }
    public static void disposeTransaction(){
        if(pendingTransaction!=null) {
            Log.v(TAG, "disposeTransaction");
            pendingTransaction.isDisposedByUser = true;
        }
    }

    public static void setAddressIndex(int index){
        Log.v(TAG, "setAddressIndex");
        wallet.setAddressIndex(index);
    }


    public static void setNode(Node node) {
        Log.v(TAG, "setNode");
        BackgroundThread.node = node;
        isNodeChanged = true;
    }
    public static void setNode() {
        Log.v(TAG, "setNode");
        if(BackgroundThread.node==null){
            setNode(Node.pickRandom());
        } else {
            setNode(Node.pickRandom(BackgroundThread.node.hostAddress));
        }
    }

    private void clearTransactionQueue(){
        if(pendingTransaction == null){
            return;
        } else if(!pendingTransaction.isCreated){
            Log.v(TAG, "clearTransactionQueue");
            pendingTransaction.setHandle(wallet.createTransaction(pendingTransaction));
            pendingTransaction.isCreated = true;
        } else if(pendingTransaction.isConfirmedByUser) {
            Log.v(TAG, "clearTransactionQueue");
            pendingTransaction.commit();
            pendingTransaction.refresh();
            switch (pendingTransaction.status) {
                case Ok:
                case Error:
                case Critical:
                    wallet.disposeTransaction(pendingTransaction);
                    pendingTransaction = null;
                    break;
                default:
            }
        } else if (pendingTransaction.isDisposedByUser && !pendingTransaction.isConfirmedByUser) {
            wallet.disposeTransaction(pendingTransaction);
            pendingTransaction = null;
        } else {
            pendingTransaction.refresh();
        }
    }

    public void updateTransactions(){
        Log.v(TAG, "updateTransactions");
        for(TransactionInfo tx : wallet.transactions) {
            tx.refresh();
        }
        RecentContent.updateItems(wallet.transactions);
    }

    private void init(){
        Log.v(TAG, "init");
        wallet.init();
        WalletContent.clearItems();
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_name"), wallet.name));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_account"), wallet.account));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_address"), wallet.address));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_index"), String.valueOf(wallet.index)));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_balance_total"), MainActivity.getString("text_unknown")));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_balance_spendable"), MainActivity.getString("text_unknown")));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_synchronized"), String.valueOf(wallet.isSynchronized)));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_synchronized_height"), String.valueOf(wallet.height)));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_status"), String.valueOf(wallet.status)));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_path"), wallet.path));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_transaction_count"), MainActivity.getString("text_unknown")));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_restore_height"),String.valueOf(wallet.restoreHeight)));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_public_spend_key"), wallet.publicSpendKey));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_public_view_key"), wallet.publicViewKey));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_seed"), wallet.seed));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_secret_spend_key"), wallet.secretSpendKey));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_secret_view_key"), wallet.secretViewKey));
        if(wallet.node!=null && wallet.node.version!=-1) {
            NodeContent.updateItem(MainActivity.getString("row_node_version"), String.valueOf(wallet.node.version));
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                TransferFragment.updateWalletInfo(MainActivity.getString("text_unknown"),"");
                ReceiveFragment.updateAddress(MainActivity.getString("text_unknown"));
            }
        });

    }
    private void connectToNode(){
        Log.v(TAG, "connectToNode");
        if(wallet.node == null){
            if(BackgroundThread.node==null){
                BackgroundThread.node = Node.pickRandom();
            }
            wallet.setNode(BackgroundThread.node);
            isNodeChanged = false;
            NodeContent.clearItems();
            NodeContent.addItem(new NodeContent.Item(MainActivity.getString("row_node_address"),node.hostAddress+":"+node.hostPort));
            NodeContent.addItem(new NodeContent.Item(MainActivity.getString("row_node_height"),MainActivity.getString("text_unknown")));
            NodeContent.addItem(new NodeContent.Item(MainActivity.getString("row_node_target"),MainActivity.getString("text_unknown")));
            NodeContent.addItem(new NodeContent.Item(MainActivity.getString("row_node_status"),MainActivity.getString("text_unknown")));
            NodeContent.addItem(new NodeContent.Item(MainActivity.getString("row_node_version"),MainActivity.getString("text_unknown")));
        }
    }
    public void refreshWalletInfo(){
        Log.v(TAG, "refreshWalletInfo");
        wallet.refresh();
        WalletContent.updateItem(MainActivity.getString("row_wallet_address"), wallet.address);
        WalletContent.updateItem(MainActivity.getString("row_wallet_index"), String.valueOf(wallet.index));
        WalletContent.updateItem(MainActivity.getString("row_wallet_transaction_count"), String.valueOf(wallet.transactionHistory.count));
        WalletContent.updateItem(MainActivity.getString("row_wallet_balance_total"), wallet.balance.toPlainString());
        WalletContent.updateItem(MainActivity.getString("row_wallet_balance_spendable"), wallet.unlockedBalance.toPlainString());
        WalletContent.updateItem(MainActivity.getString("row_wallet_synchronized"), String.valueOf(wallet.isSynchronized));
        WalletContent.updateItem(MainActivity.getString("row_wallet_status"), String.valueOf(wallet.status));
        WalletContent.updateItem(MainActivity.getString("row_wallet_synchronized_height"), String.valueOf(wallet.height));
        WalletContent.updateItem(MainActivity.getString("row_wallet_restore_height"), String.valueOf(wallet.restoreHeight));
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                TransferFragment.updateWalletInfo(wallet.unlockedBalance.toPlainString(),wallet.balance.toPlainString());
                ReceiveFragment.updateAddress(wallet.address);
            }
        });
    }
    public void refreshNode(){
        Log.v(TAG, "refreshNode");
        this.height=wallet.node.height;
        NodeContent.updateItem(MainActivity.getString("row_node_height"),String.valueOf(wallet.node.height));
        NodeContent.updateItem(MainActivity.getString("row_node_target"),String.valueOf(wallet.node.target));
        NodeContent.updateItem(MainActivity.getString("row_node_status"),String.valueOf(wallet.connectionStatus));
        NodeContent.updateItem(MainActivity.getString("row_node_version"),String.valueOf(wallet.node.version));
        if(isNodeChanged){
            isConnected = false;
            wallet.setNode(BackgroundThread.node);
            isNodeChanged=false;
            NodeContent.updateItem(MainActivity.getString("row_node_address"), BackgroundThread.node.hostAddress+":"+ BackgroundThread.node.hostPort);
        } else if (wallet.connectionStatus == Wallet.ConnectionStatus.Connected){
            isConnected = true;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                TransferFragment.updateNodeInfo(String.valueOf(wallet.node.height),String.valueOf(wallet.node.hostAddress));
            }
        });
    }
}