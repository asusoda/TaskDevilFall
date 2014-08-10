package com.asusoda.taskdevil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tyler on 8/9/2014.
 */

enum RecurrenceTypes {
            ONLY_ONCE,
            PERIODIC,
            DAYS_OF_WEEK
        }

public class Task {
    private int mId;
    private String mTitle;
    private String mDescription;
    private RecurrenceTypes mRecurrenceType;

    //not used if mRecurrenceType is ONCE_ONLY
    //represents a UNIX time period if mRecurrenceType is PERIODIC
    //represents a sunday, monday, ..., saturday bitfield if mRecurrenceType is DAYS_OF_WEEK
    private long mRecurrenceValue;

    private int mReminderAdvanceTime;

    //UNIX time value for the next occurrence of this event
    private Long mOccursAt;

    public Task(int id, String title, String description, RecurrenceTypes recurrenceType, long recurrenceValue, int reminderAdvanceTime, long occursAt){
        mId = id;
        mTitle = title;
        mDescription = description;
        mRecurrenceType = recurrenceType;
        mRecurrenceValue = recurrenceValue;
        mReminderAdvanceTime = reminderAdvanceTime;
        mOccursAt = occursAt;
    }

    //sun, mon, ..., fri, sat
    private boolean[] unpackWeek(int fieldInt){
        boolean[] days = new boolean[7];
        for(int i = 6; i > 0; i--){
            if(fieldInt - Math.pow(2, i) >= 0){
                days[6 - i] = true;
                fieldInt -= Math.pow(2,i);
            }
        }
        return days;
    }

    public void updateNextOccurrenceTime(){
        switch(mRecurrenceType){
            case ONLY_ONCE:
                mOccursAt = null;
                break;
            case PERIODIC:
                mOccursAt += mRecurrenceValue;
                break;
            case DAYS_OF_WEEK:
                GregorianCalendar lastOccurrence = new GregorianCalendar();
                lastOccurrence.setTime(new Date(mOccursAt));
                int lastOccurrenceDay = lastOccurrence.get(Calendar.DAY_OF_WEEK);

                //todo: finish this


                break;
            default:
                break;
        }

    }


    public int getId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public RecurrenceTypes getmRecurrenceType() {
        return mRecurrenceType;
    }

    public long getmRecurrenceValue() {
        return mRecurrenceValue;
    }

    public int getmReminderAdvanceTime() {
        return mReminderAdvanceTime;
    }

    public long getmOccursAt() {
        return mOccursAt;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmRecurrenceType(RecurrenceTypes mRecurrenceType) {
        this.mRecurrenceType = mRecurrenceType;
    }

    public void setmRecurrenceValue(int mRecurrenceValue) {
        this.mRecurrenceValue = mRecurrenceValue;
    }

    public void setmReminderAdvanceTime(int mReminderAdvanceTime) {
        this.mReminderAdvanceTime = mReminderAdvanceTime;
    }
}
