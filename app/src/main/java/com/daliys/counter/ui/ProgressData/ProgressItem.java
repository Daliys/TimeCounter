package com.daliys.counter.ui.ProgressData;

import com.daliys.counter.ui.MyDate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgressItem {

    private float value;
    private Date date;
    private ProgressData.TypeOfUsage typeOfUsage;

    public ProgressItem(ProgressData.TypeOfUsage typeOfUsage, float value, Long time){
        this.value = value;
        this.date = new Date(time);
        this.typeOfUsage = typeOfUsage;
    }

    public void AddValue(float value){
        this.value += value;
    }

    public float GetValue(){
        return value;
    }


    public int GetFirstDate(){
        if(typeOfUsage == ProgressData.TypeOfUsage.DayAndMonth){
            return MyDate.GetDay(date);
        }else if(typeOfUsage == ProgressData.TypeOfUsage.MonthAndYear){
            return MyDate.GetMonth(date);
        }
        return 0;
    }

    public int GetSecondDate(){
        if(typeOfUsage == ProgressData.TypeOfUsage.DayAndMonth){
            return MyDate.GetMonth(date);
        }else if(typeOfUsage == ProgressData.TypeOfUsage.MonthAndYear){
            return MyDate.GetYear(date);
        }
        return 0;
    }

    public long GetDateLong(){
        return date.getTime();
    }
}
