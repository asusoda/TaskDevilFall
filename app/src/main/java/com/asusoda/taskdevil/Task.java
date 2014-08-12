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
    private static boolean[] unpackWeek(long fieldInt){
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
                lastOccurrence.setTime(new Date(mOccursAt * 1000));
                int lastOccurrenceDay = lastOccurrence.get(Calendar.DAY_OF_WEEK);

                //necessary to map between the enum values and our internal representation
                switch(lastOccurrenceDay){
                    case Calendar.SUNDAY:
                        lastOccurrenceDay = 0;
                        break;
                    case Calendar.MONDAY:
                        lastOccurrenceDay = 1;
                        break;
                    case Calendar.TUESDAY:
                        lastOccurrenceDay = 2;
                        break;
                    case Calendar.WEDNESDAY:
                        lastOccurrenceDay = 3;
                        break;
                    case Calendar.THURSDAY:
                        lastOccurrenceDay = 4;
                        break;
                    case Calendar.FRIDAY:
                        lastOccurrenceDay = 5;
                        break;
                    case Calendar.SATURDAY:
                        lastOccurrenceDay = 6;
                        break;
                    default:
                        break;
                }

                boolean finished = false;
                boolean weekBitfield[] = Task.unpackWeek((mRecurrenceValue));
                int dayCounter = lastOccurrenceDay;
                GregorianCalendar nextOccurrence = (GregorianCalendar)lastOccurrence.clone();
                nextOccurrence.add(Calendar.DAY_OF_MONTH, 1);

                while(!finished){
                    ++dayCounter;

                    if(dayCounter == 7){
                        dayCounter = 0;
                    }

                    if(weekBitfield[dayCounter]){
                        finished = true;
                    }
                    else{
                        nextOccurrence.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }
                //must handle case of no days selected - or is that part of data integrity?
                mOccursAt = nextOccurrence.getTimeInMillis() / 1000;
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