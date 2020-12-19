package com.aeon.app.ui.contact;

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

import androidx.recyclerview.widget.RecyclerView;

import com.aeon.app.ui.transfer.SendFragment;
import com.aeon.app.ui.wallet.WalletAdapter;
import com.aeon.app.ui.wallet.WalletContent;
import com.aeon.app.ui.wallet.WalletFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactContent {

    public static final List<Contact> ITEMS = new ArrayList<Contact>();
    public static final Map<String, Integer> ITEM_MAP = new HashMap<String, Integer>();

    public static void addItem(Contact item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.address, ITEMS.size()-1);
        notifyDataSetChanged(ContactFragment.contactAdapter);
        notifyDataSetChanged(SendFragment.contactAdapter);
    }
    public static void clearItems() {
        ITEMS.clear();
        ITEM_MAP.clear();
        notifyDataSetChanged(ContactFragment.contactAdapter);
        notifyDataSetChanged(SendFragment.contactAdapter);
    }

    public static void deleteItem(String address) {
        ITEMS.remove(ITEM_MAP.get(address));
        ITEM_MAP.remove(address);
        notifyDataSetChanged(ContactFragment.contactAdapter);
        notifyDataSetChanged(SendFragment.contactAdapter);
    }
    public static void deleteItem(int position) {
        String address = ITEMS.get(position).address;
        ITEMS.remove(position);
        ITEM_MAP.remove(address);
        notifyDataSetChanged(ContactFragment.contactAdapter);
        notifyDataSetChanged(SendFragment.contactAdapter);
    }

    private static void notifyDataSetChanged(RecyclerView.Adapter<ContactAdapter.ViewHolder> adapter){
        if(adapter!=null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
    public static class Contact {
        public final String name;
        public final String address;

        public Contact(String name, String address) {
            this.name = name;
            this.address = address;
        }

        @Override
        public String toString() {
            return address;
        }
    }
}