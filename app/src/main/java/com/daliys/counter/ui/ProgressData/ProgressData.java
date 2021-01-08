package com.daliys.counter.ui.ProgressData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

public class ProgressData {

    public enum TypeOfUsage{
        DayAndMonth, MonthAndYear
    }

    private TypeOfUsage typeOfUsage;
    private ArrayList<ProgressItem> progressItems;

    public ProgressData(TypeOfUsage typeOfUsage){
        progressItems = new ArrayList<>();
        this.typeOfUsage = typeOfUsage;
    }

    public void AddValue(float value, long time) {
        if (progressItems.size() == 0) {
            progressItems.add(new ProgressItem(typeOfUsage, value, time));
        } else {
            if (CompareProgressItems(progressItems.get(progressItems.size() - 1), new ProgressItem(typeOfUsage, value, time))) {
                progressItems.get(progressItems.size() - 1).AddValue(value);
            } else {
                progressItems.add(new ProgressItem(typeOfUsage, value, time));
            }
        }
    }

    public void SubtractValue(float value, Date date){
        ProgressItem removableItem = new ProgressItem(typeOfUsage,value, date.getTime());
        ProgressItem resultItem = FindProgressItemByDayAndMonth(removableItem);

        if(resultItem != null) resultItem.AddValue(-value);
    }

    private boolean CompareProgressItems(ProgressItem progressItem1, ProgressItem progressItem2) {
        if (progressItem1.GetFirstDate() != progressItem2.GetFirstDate()) {
            return false;
        } else if (progressItem1.GetSecondDate() != progressItem2.GetSecondDate()) {
            return false;
        }
        return true;
    }

    private ProgressItem FindProgressItemByDayAndMonth(ProgressItem finedItem){
        for(int i = 0; i < progressItems.size(); i++){
            if(CompareProgressItems(progressItems.get(i), finedItem)) return progressItems.get(i);
        }
        return null;
    }

    public List<ProgressItem> GetProgressItems(){
        return progressItems;
    }

    public int GetValueByDay(Long date){
        ProgressItem pg = new ProgressItem(typeOfUsage, 0,date);
        ProgressItem findedItem = FindProgressItemByDayAndMonth(pg);
        if(findedItem == null) return 0;
        else return (int)findedItem.GetValue();
    }

    public ProgressItem GetLastAddedItem(){
        if(progressItems.size() == 0) return null;
        return progressItems.get(progressItems.size()-1);
    }

    public void SetProgressItems(ArrayList<ProgressItem> progressItems){ this.progressItems = progressItems;}
}
