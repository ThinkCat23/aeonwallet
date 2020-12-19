package com.aeon.app.ui.transfer;

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
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aeon.app.BackgroundThread;
import com.aeon.app.MainActivity;
import com.aeon.app.R;
import com.aeon.app.ui.blog.BlogAdapter;
import com.aeon.app.ui.blog.BlogContent;
import com.aeon.app.ui.wallet.WalletAdapter;
import com.aeon.app.ui.wallet.WalletContent;

public class TransferFragment extends Fragment {
    public static BlogAdapter blogAdapter;
    public static Group transferGroup;
    public static Group onboardGroup;
    public static TextView text_balance;
    public static TextView text_available;
    public static TextView text_node;
    public static String available= "";
    public static String balance= "";
    public static String height= "";
    public static String ip= "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.rv_blog_list);
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        blogAdapter = new BlogAdapter(BlogContent.ITEMS,context);
        rv.setAdapter(blogAdapter);
        transferGroup = view.findViewById(R.id.group_transfer);
        onboardGroup = view.findViewById(R.id.group_onboard);
        text_available = view.findViewById(R.id.text_available);
        text_balance = view.findViewById(R.id.text_balance);
        text_node = view.findViewById(R.id.text_node);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.showUI();
        if(BackgroundThread.isManaging){
            transferGroup.setVisibility(View.VISIBLE);
            onboardGroup.setVisibility(View.GONE);
        } else {
            transferGroup.setVisibility(View.GONE);
            onboardGroup.setVisibility(View.VISIBLE);
        }
        if(text_available!=null) {
            text_available.setText(available);
            text_balance.setText(balance);
        }
        if(text_node!=null) {
            if(!height.equals("")) {
                text_node.setText(MainActivity.getString("text_connected_to")+" "+ip + " @ " + height);
            } else{
                text_node.setText(MainActivity.getString("text_connecting_to_aeon_network"));
            }
        }
    }

    public static void updateWalletInfo(String available2, String balance2){
        available= available2;
        balance= balance2;
        if(text_available!=null) {
            text_available.setText(available);
            text_balance.setText(balance);
        }
    }
    public static void updateNodeInfo(String height2, String ip2){
        height = height2;
        ip = ip2;
        if(text_node!=null) {
            text_node.setText(MainActivity.getString("text_connected_to")+" "+ip + " @ " + height);
        }
    }
}