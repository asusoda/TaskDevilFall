package com.asusoda.taskdevil;

/**
 * Created by Tyler on 8/9/2014.
 */

enum RecurrenceTypes {

        }

public class Task {
    private int id;
    private String mTitle;
    private String mDescription;
    private int mRecurrenceType;
    private int mRecurrenceValue;
    private int mReminderTime;
    private long mOccursAt;

    public int getId() {
        return id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmRecurrenceType() {
        return mRecurrenceType;
    }

    public int getmRecurrenceValue() {
        return mRecurrenceValue;
    }

    public int getmReminderTime() {
        return mReminderTime;
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

    public void setmRecurrenceType(int mRecurrenceType) {
        this.mRecurrenceType = mRecurrenceType;
    }

    public void setmRecurrenceValue(int mRecurrenceValue) {
        this.mRecurrenceValue = mRecurrenceValue;
    }

    public void setmReminderTime(int mReminderTime) {
        this.mReminderTime = mReminderTime;
    }

    public void setmOccursAt(long mOccursAt) {
        this.mOccursAt = mOccursAt;
    }
}
