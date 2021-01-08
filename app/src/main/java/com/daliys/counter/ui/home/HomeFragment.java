package com.daliys.counter.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.daliys.counter.R;
import com.daliys.counter.ui.LogicApp;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    TextView textViewProgress;
    TextView textInputEditText;
    CircularFillableLoaders progressBar;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
       /* homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Initialization(root);
/*
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });*/
        return root;
    }

    //
    // you should add button to restart value

    public void Initialization(View v) {
        textViewProgress = v.findViewById(R.id.ProgressTextView);
        textInputEditText = v.findViewById(R.id.editTextData);
        progressBar = v.findViewById(R.id.progressBar);
        textViewProgress.setText((int)(Math.abs(LogicApp.selfLogicApp.getRemainingProgress())) + "");


        int progress = (int)(LogicApp.selfLogicApp.getCurrentProgress() * 104 / LogicApp.selfLogicApp.getTotalGoal());
        progressBar.setProgress(104 - (progress) - 2);
        if(LogicApp.selfLogicApp.getRemainingProgress() <= 0){

            try {
                progressBar.setColor(ContextCompat.getColor(getContext(), R.color.colorProgressBarComplited));
            }catch (Exception e){}

        }

        Button addValueButton = v.findViewById(R.id.buttonAddValue);
        addValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = textInputEditText.getText().toString();
                String[] splited = str.split("[:.]", 2);
                try {
                    float addTimeMin = 0f;
                    if (splited.length == 1) {
                        addTimeMin = Float.parseFloat(splited[0]);
                    } else if (splited.length == 2) {
                        addTimeMin = Float.parseFloat(splited[0]) * 60 + Float.parseFloat(splited[1]);
                    }

                    if(addTimeMin > 0) {
                        LogicApp.selfLogicApp.SaveToProgress(addTimeMin, getContext());
                        textViewProgress.setText(((int) Math.abs(LogicApp.selfLogicApp.getRemainingProgress()) )+ "");
                    }

                    if(LogicApp.selfLogicApp.getRemainingProgress() <= 0){
                        progressBar.setColor(ContextCompat.getColor(getContext(),R.color.colorProgressBarComplited));
                    }
                    int progress = (int)(LogicApp.selfLogicApp.getCurrentProgress() * 104 / LogicApp.selfLogicApp.getTotalGoal());
                    progressBar.setProgress(104 - (progress) - 2);

                    textInputEditText.setText("");

                } catch (Exception e) {
                }
            }
        });

    }
}