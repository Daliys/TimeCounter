package com.daliys.counter.ui.dashboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.daliys.counter.R;
import com.daliys.counter.ui.LogicApp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.DataRenderer;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        BarChart barChart = root.findViewById(R.id.barChart);

        ArrayList<BarEntry> entries = new ArrayList<>();


        Calendar calendar = Calendar.getInstance();
        int t = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Date date = new Date();
        for(int i = 0; i < 7; i++){
            int value = LogicApp.selfLogicApp.GetProgressDataForDays().GetValueByDay((date.getTime() - i * (1000*60*60*24)));
            entries.add(new BarEntry(6-i, value));
        }

        ArrayList<String> xAxisLabels = new ArrayList();

        long dateM = new Date().getTime();
        for(int i = 0; i < 7; i++) {
            xAxisLabels.add(new SimpleDateFormat("dd.MM").format(dateM));
            dateM -= 1000*60*60*24;
        }
        Collections.reverse(xAxisLabels);

        BarDataSet dataSet = new BarDataSet(entries, "Progress");
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        dataSet.setValueTextSize(20f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value == 0) return "";
                return (((int)value) + "");
            }
        });


        //dataSet.setGradientColor(Color.RED, Color.BLUE);
        dataSet.setBarBorderColor(Color.YELLOW);
        dataSet.setColor(ContextCompat.getColor(getContext(),R.color.elementColor));

        BarData barData = new BarData(dataSet);
        //barData.setValueFormatter(new IndexAxisValueFormatter(entriesFormatted));

        barChart.setFitBars(false);
        barChart.setData(barData);
        //barChart.getDescription().setText("My Progress Bar");

        barChart.setTouchEnabled(true);
        barChart.setScaleEnabled(false);
        barChart.setGridBackgroundColor(Color.RED);
        barChart.setDrawGridBackground(false);
        barChart.setDrawMarkers(false);
        barChart.setDrawBorders(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDescription(null);


        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getXAxis().setTextSize(12f);
        //barChart.getXAxis().setLabelCount(7);
        //barChart.setVisibleXRangeMaximum(7);
        //barChart.setVisibleXRangeMinimum(7);


        barChart.getXAxis().setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextGraph));
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(1000);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels)); //.setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);


       // barChart.setRenderer(dataRenderer);
        return root;
    }


}

