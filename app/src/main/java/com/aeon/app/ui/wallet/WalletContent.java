package com.aeon.app.ui.wallet;

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

import com.aeon.app.ui.transfer.TransferFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletContent {

    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Map<String, Integer> ITEM_MAP = new HashMap<String, Integer>();

    public static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, ITEMS.size()-1);
        notifyDataSetChanged(WalletFragment.walletAdapter);
    }
    public static void clearItems() {
        ITEMS.clear();
        ITEM_MAP.clear();
        notifyDataSetChanged(WalletFragment.walletAdapter);
    }

    public static void updateItem(String key, String value) {
        if(!ITEMS.get(ITEM_MAP.get(key)).content.equals(value)) {
            ITEMS.set(ITEM_MAP.get(key), new Item(key, value));
            notifyDataSetChanged(WalletFragment.walletAdapter);
        }
    }
    private static void notifyDataSetChanged(RecyclerView.Adapter<WalletAdapter.ViewHolder> adapter){
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
    public static class Item {
        public final String id;
        public final String content;

        public Item(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}