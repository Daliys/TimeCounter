package com.daliys.counter.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daliys.counter.R;
import com.daliys.counter.ui.HistoryData.HistoryAdapter;
import com.daliys.counter.ui.HistoryData.HistoryItem;
import com.daliys.counter.ui.LogicApp;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Button undoLastValueButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        undoLastValueButton = root.findViewById(R.id.button_remove_last_value);
        undoLastValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LogicApp.selfLogicApp.getHistoryItems().size() > 0) {
                    LogicApp.selfLogicApp.CancelLastAddedValue(getContext());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ArrayList<HistoryItem> items = LogicApp.selfLogicApp.getHistoryItems();

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        adapter = new HistoryAdapter(items);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        // if you want to change list in the progress you should use this code:
        //adapter.notifyDataSetChanged();

        return root;
    }


}