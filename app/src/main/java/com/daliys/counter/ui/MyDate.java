package com.daliys.counter.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {

    public static int GetDay(Date date){
        try{
            return Integer.parseInt(new SimpleDateFormat("dd").format(date.getTime()));
        }catch (Exception e){}
        return -1;
    }

    public static int GetMonth(Date date){
        try{
            return Integer.parseInt(new SimpleDateFormat("MM").format(date));
        }catch (Exception e){}
        return -1;
    }

    public static int GetYear(Date date){
        try{
            return Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        }catch (Exception e){}
        return -1;
    }

}
