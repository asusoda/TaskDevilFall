package com.asusoda.taskdevil.models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tyler on 8/9/2014.
 */


public class Task {
    // Java declares that if this enum is not within the bounds of this class, other files don't like it.
    // Don't move me.
    public enum RecurrenceTypes {
        ONLY_ONCE,
        PERIODIC,
        DAYS_OF_WEEK
    }

    // Bit masks for the days of the week
    private static int bm_sun = 64; // 0b1000000
    private static int bm_mon = 32; // 0b0100000
    private static int bm_tue = 16; // 0b0010000
    private static int bm_wed = 8;  // 0b0001000
    private static int bm_thu = 4;  // 0b0000100
    private static int bm_fri = 2;  // 0b0000010
    private static int bm_sat = 1;  // 0b0000001

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

    private Long mNotificationAt;

    public Task(int id, String title, String description, RecurrenceTypes recurrenceType, long recurrenceValue, int reminderAdvanceTime, long occursAt, long notificationAt){
        mId = id;
        mTitle = title;
        mDescription = description;
        mRecurrenceType = recurrenceType;
        mRecurrenceValue = recurrenceValue;
        mReminderAdvanceTime = reminderAdvanceTime;
        mOccursAt = occursAt;
        mNotificationAt = notificationAt;
    }

    public Task(String title, String description) {
        mTitle = title;
        mDescription = description;
        mRecurrenceType = RecurrenceTypes.ONLY_ONCE;
        mRecurrenceValue = 0;
        mReminderAdvanceTime = 0;
        mOccursAt = 0L;
        mNotificationAt = 0L;
    }

    // sun, mon, ..., fri, sat
    // Revamped to use the bitmasks defined above to check the status of individual weekdays.
    // The code is currently pretty verbose, though. Can revise later to iterate through a for
    // loop of masks instead.
    private static boolean[] unpackWeek(long fieldInt){
        boolean[] days = new boolean[7];
        days[0] = (fieldInt & bm_sun) == bm_sun;
        days[1] = (fieldInt & bm_mon) == bm_mon;
        days[2] = (fieldInt & bm_tue) == bm_tue;
        days[3] = (fieldInt & bm_wed) == bm_wed;
        days[4] = (fieldInt & bm_thu) == bm_thu;
        days[5] = (fieldInt & bm_fri) == bm_fri;
        days[6] = (fieldInt & bm_sat) == bm_sat;
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

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public RecurrenceTypes getRecurrenceType() {
        return mRecurrenceType;
    }

    public long getRecurrenceValue() {
        return mRecurrenceValue;
    }

    public int getReminderAdvanceTime() {
        return mReminderAdvanceTime;
    }

    public long getOccursAt() {
        return mOccursAt;
    }

    public long getNotificationAt() { return mNotificationAt; }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setRecurrenceType(RecurrenceTypes mRecurrenceType) {
        this.mRecurrenceType = mRecurrenceType;
    }

    public void setRecurrenceValue(int mRecurrenceValue) {
        this.mRecurrenceValue = mRecurrenceValue;
    }

    public void setReminderAdvanceTime(int mReminderAdvanceTime) {
        this.mReminderAdvanceTime = mReminderAdvanceTime;
    }
}
