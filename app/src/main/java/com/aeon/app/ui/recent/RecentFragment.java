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
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aeon.app.MainActivity;
import com.aeon.app.R;

/**
 * A fragment representing a list of Items.
 */
public class RecentFragment extends Fragment {
    public static RecentAdapter recentAdapter;

    public RecentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_recent_item_list);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recentAdapter = new RecentAdapter(RecentContent.ITEMS);
        recyclerView.setAdapter(recentAdapter);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.showUI();
    }
}