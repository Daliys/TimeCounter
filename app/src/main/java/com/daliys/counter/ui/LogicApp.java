package com.daliys.counter.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.daliys.counter.MainActivity;
import com.daliys.counter.ui.HistoryData.HistoryItem;
import com.daliys.counter.ui.ProgressData.ProgressData;
import com.daliys.counter.ui.ProgressData.ProgressItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class LogicApp extends MainActivity {
    private final String fileNameHistoryDataBase = "progressHistoryDataBase.txt";
    private final String fileNameMonthsDateBase = "progressMonthsDataBase.txt";
    private final String goalKeyCode = "timeGoal";
    private final String currentProgressKeyCode = "currentProgress";

    public static LogicApp selfLogicApp = null;
    private float totalGoal;
    private float currentProgress;

    private ArrayList<HistoryItem> historyItems;
    private ProgressData progressDataForDays;
    private ProgressData progressDataForMonth;


    public LogicApp(Context context) {
        if (selfLogicApp == null) selfLogicApp = this;

        //SaveDate(goalKeyCode, 60000f); // 1000 hours = 60 000 min
        //SaveDate(currentProgressKeyCode, 0);

        progressDataForDays = new ProgressData(ProgressData.TypeOfUsage.DayAndMonth);
        progressDataForMonth = new ProgressData(ProgressData.TypeOfUsage.MonthAndYear);

        //CleanHistoryData(context);
        //CleanMonthsData(context);

        totalGoal = GetDate(goalKeyCode);
        currentProgress = GetDate(currentProgressKeyCode);
        Log.e("Log2 " , totalGoal + " ");

        historyItems = GetHistoryData(context);
        Collections.reverse(historyItems);
/*
      Random random = new Random();
        long current = new Date().getTime();
        current -= (long)(60 * (long)(1000*60*60*24));
        for(int i = 0 ; i < 60; i++){

            int randNum = random.nextInt(100)+1;
            SaveHistoryData(new HistoryItem(randNum, current), context);
            historyItems.add(0,new HistoryItem(randNum, current));
            current += 1000*60*60*24;
            currentProgress += randNum;
            SaveDate(currentProgressKeyCode, currentProgress);
        }
*/
//  Date date = new Date(progressDataForDays.GetLastAddedItem().GetDateLong());
        //  Log.e("My Log", new SimpleDateFormat("MM.dd.yyyy mm").format(date) + " " + progressDataForDays.GetLastAddedItem().GetValue() );

      //  SaveHistoryData(new HistoryItem(2000, new Date().getTime() - 1000*60*60*24*100), context);
       // historyItems.add(0,new HistoryItem(2000, new Date().getTime() - 1000*60*60*24*100));
      //  currentProgress += 2000;
       // SaveDate(currentProgressKeyCode, currentProgress);

        progressDataForMonth.SetProgressItems(GetMonthData(context));

        //CleanMonthsData(context);
    }

    public void SaveToProgress(float value, Context context) {
        currentProgress += value;
        SaveDate(currentProgressKeyCode, currentProgress);

        long time = new Date().getTime();

        SaveHistoryData(new HistoryItem(value, time), context);
        historyItems.add(0, new HistoryItem(value, time));

        progressDataForDays.AddValue(value, time);
        progressDataForMonth.AddValue(value, time);
        ReSaveMonthsData(context);
    }

    protected void SaveDate(String key, float value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putFloat(key, value).apply();
    }

    protected float GetDate(String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return mPrefs.getFloat(key, 0f);
    }


    public void SaveHistoryData(HistoryItem historyItem, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(fileNameHistoryDataBase, MODE_APPEND));
            osw.write(historyItem.getValue() + "/$" + historyItem.getDateString() + "\n");
            osw.close();
        } catch (Exception e) {
            Log.e("WRITTER", "Error Writting Data to File " + e.toString());
        }
    }

    private void ReSaveHistoryData(Context context, ArrayList<HistoryItem> items) {
        CleanHistoryData(context);
        for (int i = 0; i < items.size(); i++) {
            SaveHistoryData(items.get(i), context);
        }
    }

    public void CleanHistoryData(Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(fileNameHistoryDataBase, MODE_PRIVATE));
            osw.close();
        } catch (Exception e) {
            Log.e("WRITTER", "Error Writting Data to File " + e.toString());
        }
    }

    public void CancelLastAddedValue(Context context) {
        float value = historyItems.get(0).getValue();
        Date date = historyItems.get(0).getDate();
        currentProgress -= value;
        SaveDate(currentProgressKeyCode, currentProgress);

        historyItems.remove(0);
        progressDataForDays.SubtractValue(value, date);
        progressDataForMonth.SubtractValue(value, date);

        ReSaveMonthsData(context);
        ReSaveHistoryData(context, historyItems);
    }

    private ArrayList<HistoryItem> GetHistoryData(Context context) {
        ArrayList<HistoryItem> outItems = new ArrayList<>();
        boolean isCrowded = false;
        try {
            InputStream inputStream = context.openFileInput(fileNameHistoryDataBase);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveStr = "";

                while ((receiveStr = bufferedReader.readLine()) != null) {
                    String[] splited = receiveStr.toString().split("/\\$", 2);

                    long currentDate = new Date().getTime();
                    long itemDate = Long.parseLong(splited[1]);
                    Long date = currentDate - itemDate;
                    if (date / 1000 / 60 / 60 / 24 <= 30) {
                        //Log.e("My log", date / 1000 / 60 / 60 + "");
                        outItems.add(new HistoryItem(Float.parseFloat(splited[0]), itemDate));
                        progressDataForDays.AddValue(Float.parseFloat(splited[0]), itemDate);
                    } else {
                        isCrowded = true;
                    }
                }
                bufferedReader.close();
                if (isCrowded) ReSaveHistoryData(context, outItems);
            }
        } catch (Exception e) {
            Log.e("READER", "Error read Data to File " + e.toString());
        }
        return outItems;
    }

    private void CleanMonthsData(Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(fileNameMonthsDateBase, MODE_PRIVATE));
            osw.close();
        } catch (Exception e) {
            Log.e("WRITTER", "Error Writting Data to File " + e.toString());
        }
    }

    private void SaveMonthsData(Context context, ProgressItem progressItem) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(fileNameMonthsDateBase, MODE_APPEND));
            osw.write(progressItem.GetValue() + "/$" + progressItem.GetDateLong() + "\n");
            osw.close();
        } catch (Exception e) {
            Log.e("WRITTER", "Error Writting Data to File " + e.toString());
        }
    }

    private void ReSaveMonthsData(Context context) {
        CleanMonthsData(context);
        for (int i = 0; i < progressDataForMonth.GetProgressItems().size(); i++) {
            SaveMonthsData(context, progressDataForMonth.GetProgressItems().get(i));
        }
    }

    private ArrayList<ProgressItem> GetMonthData(Context context) {
        ArrayList<ProgressItem> outItems = new ArrayList<>();

        try {
            InputStream inputStream = context.openFileInput(fileNameMonthsDateBase);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveStr = "";

                while ((receiveStr = bufferedReader.readLine()) != null) {
                    String[] splited = receiveStr.toString().split("/\\$", 2);
                    outItems.add(new ProgressItem(ProgressData.TypeOfUsage.MonthAndYear, Float.parseFloat(splited[0]), Long.parseLong(splited[1])));
                }
                bufferedReader.close();
            }
        } catch (Exception e) {
            Log.e("READER", "Error read Data to File " + e.toString());
        }
        return outItems;
    }

    public ArrayList<HistoryItem> getHistoryItems() {
        return historyItems;
    }

    public float getCurrentProgress() {
        return currentProgress;
    }

    public ProgressData GetProgressDataForDays() {
        return progressDataForDays;
    }

    public ProgressData GetProgressDataForMonth() {
        return progressDataForMonth;
    }

    public float getTotalGoal() {
        return totalGoal;
    }

    public float getMonthGoal() {
        return totalGoal / 12;
    }

// i should compare months and decided added new value or not

    public float getRemainingProgress() {
        float perMonth = totalGoal / 12;
        int currentMonth = MyDate.GetMonth(new Date());
        int monthLatest = 0;

        if(progressDataForDays.GetLastAddedItem() != null) {
            monthLatest = MyDate.GetMonth(new Date(progressDataForDays.GetLastAddedItem().GetDateLong()));
        }
        Log.e("My LOG", " - Current " + progressDataForMonth.GetProgressItems().size() + "  cur " + currentProgress);
        if (monthLatest == currentMonth) {

            return (totalGoal / 12 * progressDataForMonth.GetProgressItems().size() - currentProgress);

        } else {
            return (totalGoal / 12 * progressDataForMonth.GetProgressItems().size() - currentProgress) + (totalGoal / 12);
        }

        //Log.e("My Log", "C " + currentMonth + "  M " + monthLatest );

        //if()

        //Log.e("My LOG",progressDataForMonth.GetProgressItems().size() + "");
        // 60 000 / 12 = 5000 min\month
        // start 12.2020 - finish 12.2021
        //return totalGoal - currentProgress;
    }
}
