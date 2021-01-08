package com.daliys.counter.ui.HistoryData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryItem {
    private String dateText;
    private float value;
    private Date date;

    public HistoryItem(float value, long time){
        date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(" hh:mm / dd.MM");
        dateText = sdf.format(date);
        this.value = value;
    }

    private String GetTimeToString(float time){
        String str = "+ " + (((int)(time/60)) > 0? (((int)(time/60)) + " h ") : (""));
        str+= ((int)(time - ((int)(time/60)) * 60)) + " m";
        return str;
    }

    public String getFormattedDateText() {
        return dateText;
    }
    public String getDateString(){return Long.toString(date.getTime());}
    public String getFormattedValueText() {
        return GetTimeToString(value);
    }

    public Date getDate(){return date;}
    public float getValue() {
        return value;
    }
}
