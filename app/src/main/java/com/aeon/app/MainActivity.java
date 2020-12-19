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
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aeon.app.models.Node;
import com.aeon.app.ui.contact.ContactContent;
import com.aeon.app.ui.wallet.WalletContent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Set;

public class MainActivity extends ButtonActions {
    public static ConstraintLayout layout_bottom_nav;
    public static Group group_main_on;
    public static Group group_main_off;
    public static MenuItem button_wallet;
    public static MenuItem button_node;
    public static Button button_transfer;
    public static Button button_contacts;
    public static Button button_recents;
    public static Resources res;
    public static String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_transfer, R.id.navigation_contact, R.id.navigation_recent)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        group_main_on = findViewById(R.id.group_main_on);
        group_main_off = findViewById(R.id.group_main_off);
        layout_bottom_nav = MainActivity.this.findViewById(R.id.layout_bottom_nav);
        button_transfer = ((Button) findViewById(R.id.button_transfer));
        button_contacts =((Button) findViewById(R.id.button_contacts));
        button_recents =((Button) findViewById(R.id.button_recents));
        res = getResources();
        packageName = getPackageName();

        loadContacts();
        loadSavedWallet();
        loadSavedNode();

        if(BackgroundThread.isManaging){
            group_main_on.setVisibility(View.VISIBLE);
            group_main_off.setVisibility(View.GONE);
        } else {
            group_main_on.setVisibility(View.GONE);
            group_main_off.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        button_wallet = menu.findItem(R.id.navigation_wallet);
        button_node = menu.findItem(R.id.navigation_node);
        return true;
    }

    public static void hideUI(){
        group_main_on.setVisibility(View.GONE);
        group_main_off.setVisibility(View.GONE);
        layout_bottom_nav.setVisibility(View.GONE);
        button_wallet.setVisible(false);
        button_node.setVisible(false);
    }
    public static void showUI(){
        if(BackgroundThread.isManaging){
            group_main_off.setVisibility(View.GONE);
            group_main_on.setVisibility(View.VISIBLE);
        } else {
            group_main_on.setVisibility(View.GONE);
            group_main_off.setVisibility(View.VISIBLE);
        }
        if(button_wallet!=null) {
            button_wallet.setVisible(true);
            button_node.setVisible(true);
            layout_bottom_nav.setVisibility(View.VISIBLE);
        }
    }
    private void loadContacts(){
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        final Set<String> keys = sharedPref.getAll().keySet();
        for(String key : keys){
            if(!key.equals("path")&&!key.equals("password")&&!key.equals("ip")&&!key.equals("port") ){
                if(!ContactContent.ITEMS.contains(key)) {
                    ContactContent.addItem(
                            new ContactContent.Contact(sharedPref.getString(key, ""), key)
                    );
                }
            }
        }
    }
    private void loadSavedWallet(){
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        String path = sharedPref.getString("path" , null);
        this.password = sharedPref.getString("password" , null);
        if(!BackgroundThread.isManaging && path != null){
            Toast toast = Toast.makeText(getApplicationContext(), getString("toast_wallet_found"), Toast.LENGTH_SHORT);
            toast.show();
            thread = new BackgroundThread();
            thread.start();
            WalletContent.clearItems();
            WalletContent.addItem(
                    new WalletContent.Item("",
                            getResources().getString(R.string.text_loading_wallet))
            );
            BackgroundThread.queueWallet(path);
        }
    }
    private void loadSavedNode(){
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        String ip = sharedPref.getString("ip" , null);
        String port = sharedPref.getString("port" , null);
        if(ip != null){
            BackgroundThread.setNode(new Node(ip,port));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(
                        MainActivity.this,R.id.nav_host_fragment)
                        .navigate(R.id.navigation_transfer);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (BackgroundThread.isManaging && BackgroundThread.isRunning){
            editor.putString("path", BackgroundThread.path);
        } else {
            editor.putString("path", null);
        }
        if(BackgroundThread.isConnected){
            editor.putString("ip", BackgroundThread.node.hostAddress);
            editor.putString("port", BackgroundThread.node.hostPort);
        }
        editor.commit();
    }


    public static String getString(String idName) {
        int resId = res.getIdentifier(idName, "string", packageName);
        return res.getString(resId);
    }
}