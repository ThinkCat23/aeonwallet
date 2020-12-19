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
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aeon.app.BackgroundThread;
import com.aeon.app.MainActivity;
import com.aeon.app.R;

public class WalletFragment extends Fragment {
    public static WalletAdapter walletAdapter;

    public WalletFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        if(BackgroundThread.isManaging){
            openWalletView(view);
        } else {
            closeWalletView(view);
        }

        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.rv_wallet_info_list);
        rv.setLayoutManager(new GridLayoutManager(context, 1));
        walletAdapter = new WalletAdapter(WalletContent.ITEMS);
        rv.setAdapter(walletAdapter);
        return view;
    }

    public static void openWalletView(View view){
        Group g = view.findViewById(R.id.group_wallet_off);
        g.setVisibility(View.GONE);
        g = view.findViewById(R.id.group_wallet_on);
        g.setVisibility(View.VISIBLE);
    }

    public static void closeWalletView(View view){
        Group g = view.findViewById(R.id.group_wallet_off);
        g.setVisibility(View.VISIBLE);
        g = view.findViewById(R.id.group_wallet_on);
        g.setVisibility(View.GONE);
    }

    public static void hideUI(View view){
        Group g = view.findViewById(R.id.group_wallet_off);
        g.setVisibility(View.GONE);
        g = view.findViewById(R.id.group_wallet_on);
        g.setVisibility(View.GONE);
    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideUI();
    }
}