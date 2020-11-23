package com.aeon.app;

import android.util.Log;

import com.aeon.app.models.Node;
import com.aeon.app.models.TransactionInfo;
import com.aeon.app.models.TransactionPending;
import com.aeon.app.models.Wallet;
import com.aeon.app.ui.node.NodeContent;
import com.aeon.app.ui.recent.RecentContent;
import com.aeon.app.ui.wallet.WalletContent;

import java.util.ArrayList;

public class BackgroundThread extends Thread{
    private static final String TAG = "BackgroundThread";
    private static Node node;
    private static Wallet wallet = null;
    private static int sleepTimer;
    private static ArrayList<TransactionPending> pendingOutTransactions = new ArrayList<TransactionPending>();
    private static boolean isNodeChanged;
    public static boolean isRunning = false;
    public static boolean isManaging = false;
    public static boolean isShownNewWalletFragment = false;
    public static String path = null;
    public static String seed = null;

    public BackgroundThread() {

    }

    public void run() {
        isRunning = true;
        Log.v(TAG, "isRunning");
        while(!Thread.interrupted()){
            Log.v(TAG, "!Thread.interrupted()");
            if(isManaging && ! wallet.isClosed) {
                Log.v(TAG, "isManaging && !wallet.isClosed");
                if (wallet.isExists == false) {
                    Log.v(TAG, "!wallet.isExists");
                    wallet.create();
                } else if (wallet.isInit == false) {
                    Log.v(TAG, "!wallet.isInit");
                    this.seed = wallet.seed;
                    connectToNode();
                    init();
                } else {
                    Log.v(TAG, "wallet.isExists && wallet.isInit");
                    sleepTimer=10000;
                    updateTransactions();
                    refreshWalletInfo();
                    refreshNode();
                    clearTransactionQueue();
                }
            }
            try {
                Log.v(TAG, "Thread.sleep");
                Thread.sleep(sleepTimer);
            } catch (InterruptedException e) {
                this.interrupt();
                e.printStackTrace();
            }
        }
        Log.v(TAG, "Thread.interrupted()");
        wallet.close();
        isManaging = false;
        isShownNewWalletFragment=false;
        isRunning = false;
        Log.v(TAG, "!isManaging");
        WalletContent.clearItems();
        sleepTimer = 200;
        Log.v(TAG, "!isRunning");
    }

    public static void queueWallet(String path){
        Log.v(TAG, "queueWallet");
        BackgroundThread.path = path;
        BackgroundThread.seed = null;
        wallet = new Wallet(path,MainActivity.getPassword(),"English");
        isManaging = true;
        sleepTimer = 200;
    }
    public static void queueWallet(String path, String seed, int restoreHeight){
        Log.v(TAG, "queueWallet");
        BackgroundThread.path = path;
        BackgroundThread.seed = null;
        wallet = new Wallet(path,MainActivity.getPassword(),"English", seed, restoreHeight);
        isManaging = true;
        sleepTimer = 200;
    }

    public static void queueWallet(String path, String account, String view, String spend, int restoreHeight) {
        Log.v(TAG, "queueWallet");
        BackgroundThread.path = path;
        BackgroundThread.seed = null;
        wallet = new Wallet(path,MainActivity.getPassword(),"English", account, view, spend, restoreHeight);
        isManaging = true;
        sleepTimer = 200;
    }

    public static void queueTransaction(String dst_address, long amount){
        Log.v(TAG, "queueTransaction");
        pendingOutTransactions.add(new TransactionPending(dst_address,amount));
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

    private void clearTransactionQueue(){
        Log.v(TAG, "clearTransactionQueue");
        ArrayList<TransactionPending> clone = new ArrayList<>();
        clone.addAll(pendingOutTransactions);
        for(TransactionPending tx : clone){
            if(!tx.isCommitted && !tx.isAttempted){
                tx.setHandle(wallet.createTransaction(tx));
                tx.commit();
            } else {
                tx.refresh();
                switch(tx.status){
                    case Ok:
                    case Error:
                    case Critical:
                        wallet.disposeTransaction(tx);
                        pendingOutTransactions.remove(tx);
                        break;
                    default:

                }
            }
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
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_status"), String.valueOf(wallet.status)));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_path"), wallet.path));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_transaction_count"), MainActivity.getString("text_unknown")));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_public_spend_key"), wallet.publicSpendKey));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_public_view_key"), wallet.publicViewKey));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_seed"), wallet.seed));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_secret_spend_key"), wallet.secretSpendKey));
        WalletContent.addItem(new WalletContent.Item(MainActivity.getString("row_wallet_secret_view_key"), wallet.secretViewKey));
        NodeContent.updateItem(MainActivity.getString("row_node_version"),String.valueOf(wallet.node.version));
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
    }
    public void refreshNode(){
        Log.v(TAG, "refreshNode");
        NodeContent.updateItem(MainActivity.getString("row_node_height"),String.valueOf(wallet.node.height));
        NodeContent.updateItem(MainActivity.getString("row_node_target"),String.valueOf(wallet.node.target));
        NodeContent.updateItem(MainActivity.getString("row_node_status"),String.valueOf(wallet.connectionStatus));
        NodeContent.updateItem(MainActivity.getString("row_node_version"),String.valueOf(wallet.node.version));
        if(isNodeChanged){
            wallet.setNode(BackgroundThread.node);
            NodeContent.updateItem(MainActivity.getString("row_node_address"), BackgroundThread.node.hostAddress+":"+ BackgroundThread.node.hostPort);
        }
    }
}