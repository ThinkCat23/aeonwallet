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

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aeon.app.BackgroundThread;
import com.aeon.app.MainActivity;
import com.aeon.app.R;

public class WalletNewFragment extends Fragment {
    private EditText text_seed_phrase;
    private Button understood;
    public WalletNewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_wallet, container, false);
        text_seed_phrase = v.findViewById(R.id.text_seed_phrase);
        understood= v.findViewById(R.id.button_understood);
        if(!BackgroundThread.isShownNewWalletFragment) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (BackgroundThread.seed == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            text_seed_phrase.setText(BackgroundThread.seed);
                            understood.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }).start();
        }
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideUI();
    }
    @Override
    public void onStop() {
        super.onStop();
        text_seed_phrase.setText("");
        BackgroundThread.isShownNewWalletFragment = true;
        understood.setVisibility(View.GONE);
    }
}