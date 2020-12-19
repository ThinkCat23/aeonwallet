package com.aeon.app.ui.node;

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
import com.aeon.app.ui.wallet.WalletAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeContent {

    public static final List<Item> ITEMS = new ArrayList<NodeContent.Item>();
    public static final Map<String, Integer> ITEM_MAP = new HashMap<String, Integer>();

    static {
        addItem(new Item("","Disconnected."));
    }

    public static void addItem(NodeContent.Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, ITEMS.size()-1);
        notifyDataSetChanged(NodeFragment.nodeAdapter);
    }
    public static void clearItems() {
        ITEMS.clear();
        ITEM_MAP.clear();
        notifyDataSetChanged(NodeFragment.nodeAdapter);
    }
    public static void reset() {
        for(String key : ITEM_MAP.keySet()) {
            ITEMS.set(ITEM_MAP.get(key),new Item(key,"..."));
        }
        notifyDataSetChanged(NodeFragment.nodeAdapter);
    }

    public static void updateItem(String key, String value) {
        ITEMS.set(ITEM_MAP.get(key),new Item(key,value));
        notifyDataSetChanged(NodeFragment.nodeAdapter);
    }

    private static void notifyDataSetChanged(RecyclerView.Adapter<NodeAdapter.ViewHolder> adapter){
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