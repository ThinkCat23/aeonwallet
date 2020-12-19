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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aeon.app.BackgroundThread;
import com.aeon.app.R;
import com.aeon.app.ui.contact.ContactAdapter;
import com.aeon.app.ui.contact.ContactContent;

public class SendFragment extends Fragment {
    public static ContactAdapter contactAdapter=null;
    private EditText recipient;
    private CheckBox check_payment_id;
    private EditText text_payment_id;

    @Override
    public void onResume() {
        text_payment_id.setVisibility(View.GONE);
        BackgroundThread.disposeTransaction();
        recipient.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getActivity().getCurrentFocus(), 0);
        super.onResume();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        Context context = view.getContext();
        RecyclerView rv = view.findViewById(R.id.rv_send_contact_list);
        rv.setLayoutManager(new GridLayoutManager(context, 1));
        if(contactAdapter ==null) {
            contactAdapter = new ContactAdapter(ContactContent.ITEMS);
        }
        rv.setAdapter(contactAdapter);
        recipient = (EditText)view.findViewById(R.id.transfer_recipient_info);
        check_payment_id = (CheckBox)view.findViewById(R.id.check_payment_id);
        text_payment_id = (EditText)view.findViewById(R.id.text_payment_id);
        check_payment_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText text_payment_id = (EditText)view.findViewById(R.id.text_payment_id);
                if(isChecked){
                    text_payment_id.setVisibility(View.VISIBLE);
                } else {
                    text_payment_id.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }
}

