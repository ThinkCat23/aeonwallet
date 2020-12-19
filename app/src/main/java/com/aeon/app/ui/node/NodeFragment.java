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
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aeon.app.MainActivity;
import com.aeon.app.R;

public class NodeFragment extends Fragment {
    public static NodeAdapter nodeAdapter;


    public NodeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node, container, false);
        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.rv_node_info_list);
        rv.setLayoutManager(new GridLayoutManager(context, 1));
        nodeAdapter = new NodeAdapter(NodeContent.ITEMS);
        rv.setAdapter(nodeAdapter);
        EditText editText = view.findViewById(R.id.text_node_address);
        String digits = "0123456789.:";
        editText.setKeyListener(DigitsKeyListener.getInstance(digits));
        editText.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideUI();
    }
}