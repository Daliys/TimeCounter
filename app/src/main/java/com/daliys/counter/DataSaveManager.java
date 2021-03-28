package com.daliys.counter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


// я начал перенос методов ( еще не закончил)
public class DataSaveManager {

    public void SaveValueByKey(String key, float value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putFloat(key, value).apply();
    }

    public float GetValueByKey(String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return mPrefs.getFloat(key, 0f);
    }



}
