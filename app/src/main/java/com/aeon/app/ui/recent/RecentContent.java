package com.aeon.app.ui.recent;

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

import com.aeon.app.models.TransactionInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentContent {

    public static final List<TransactionInfo> ITEMS = new ArrayList<TransactionInfo>();
    public static final Map<String, TransactionInfo> ITEM_MAP = new HashMap<String, TransactionInfo>();

    public static void addItem(TransactionInfo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.hash, item);
        notifyDataSetChanged(RecentFragment.recentAdapter);
    }

    public static void updateItems(ArrayList<TransactionInfo> transactions){
        ITEMS.clear();
        ITEM_MAP.clear();
        transactions.sort( new Comparator<TransactionInfo>() {
            @Override
            public int compare(TransactionInfo lhs, TransactionInfo rhs) {
                return lhs.height > rhs.height ? -1 : (lhs.height < rhs.height) ? 1 : 0;
            }
        });
        for(TransactionInfo tx : transactions){
            addItem(tx);
        }
        notifyDataSetChanged(RecentFragment.recentAdapter);
    }

    private static void notifyDataSetChanged(RecyclerView.Adapter<RecentAdapter.ViewHolder> adapter){
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
}