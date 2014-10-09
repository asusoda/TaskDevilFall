package com.asusoda.taskdevil.data_access_layer;

import android.content.Context;
import android.database.Cursor;
import android.util.Pair;

import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.models.Task;
import com.asusoda.taskdevil.models.Task.RecurrenceTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by root on 8/11/14.
 */
public class DataAccess {

    public static class TaskRetrieveOptions {
        public Boolean all;
        public Boolean nextHour;
        public Boolean thisHour;
        public Integer startTime;
        public Integer endTime;
        public Integer id;
    }

    public static boolean addTask(Context context, Task task) {
        DatabaseManager dbManager = new DatabaseManager(context);
        dbManager.insertTask(task);
        return false;
    }

    public static boolean deleteTask(Context context, Task task) {
        return false;
    }

    public static Task getTask(Context context, TaskRetrieveOptions options) {

        return null;
    }
/*
    public static class TimeComparator implements Comparator<Pair<Long, Integer>> {
        @Override
        public int compare(Pair<Long, Integer> a, Pair<Long, Integer> b) {
            if (a.first < b.first) return -1;
            else if (a.first > b.first) return 1;
            else return 0;
        }
    }

    private static PriorityQueue< Pair< Long, Integer > > getNotificationTasks(Context context, Cursor rows) {
        TimeComparator comparator = new TimeComparator();
        PriorityQueue< Pair< Long, Integer > > result = new PriorityQueue< Pair< Long, Integer > >(100, comparator);

        if (rows.getCount() > 0) {
            int rowID = rows.getColumnIndex(context.getString(R.string.task_id_field)),
                rowNotificationAt = rows.getColumnIndex(context.getString(R.string.task_notification_at_field));

            rows.moveToFirst();
            while(!rows.isAfterLast()) {
                Pair<Long, Integer> temp = new Pair<Long, Integer>(rows.getLong(rowNotificationAt), rows.getInt(rowID));
                result.add(temp);
            }
        }

        return result;
    }
*/
    private static ArrayList<Task> cursorToTaskArrayList(Context context, Cursor rows) {
        ArrayList<Task> out = new ArrayList<Task>();
        if (rows.getCount() > 0) {
            // these integer values represent the column indices of the retrieve - we may not retrieve these in the order that we expect, so we get the indices dynamically
            int rowID = rows.getColumnIndex(context.getString(R.string.task_id_field)),
                rowTitle = rows.getColumnIndex(context.getString(R.string.task_title_field)),
                rowDescription = rows.getColumnIndex(context.getString(R.string.task_description_field)),
                rowRecurrenceType = rows.getColumnIndex(context.getString(R.string.task_recurrence_type_field)),
                rowRecurrenceValue = rows.getColumnIndex(context.getString(R.string.task_recurrence_value_field)),
                rowReminderAdvanceTime = rows.getColumnIndex(context.getString(R.string.task_reminder_advance_time_field)),
                rowOccursAt = rows.getColumnIndex(context.getString(R.string.task_occurs_at_field)),
                rowNotificationAt = rows.getColumnIndex(context.getString(R.string.task_notification_at_field));


            // put the cursor at the first row
            rows.moveToFirst();
            while (!rows.isAfterLast()) { // create a task at every iteration
                Task task = new Task(
                        rows.getInt(rowID),
                        rows.getString(rowTitle),
                        rows.getString(rowDescription),
                        RecurrenceTypes.values()[rows.getInt(rowRecurrenceType)],
                        rows.getInt(rowRecurrenceValue),
                        rows.getInt(rowReminderAdvanceTime),
                        rows.getLong(rowOccursAt),
                        rows.getLong(rowNotificationAt)
                );
                // add the task to the list
                out.add(task);
                // move to the next task
                rows.moveToNext();
            }
        }

        // return our list of tasks
        return out;
    }

    public static ArrayList<Task> getTasks(Context context, TaskRetrieveOptions options) {
        ArrayList<Task> out = new ArrayList<Task>();
        DatabaseManager dbManager = new DatabaseManager(context);
        if (options.all != null && options.all.booleanValue() == true) {
            Cursor rows = dbManager.retrieveAllTasks();
            out = cursorToTaskArrayList(context, rows);
        } else if (options.nextHour != null && options.nextHour.booleanValue() == true) {
            // so basically this is the hour after the current hour, so if you're at 12:50, your "this" hour is 12 and your next hour is 13:00.
            long currentTimeSeconds = System.currentTimeMillis() / 1000L,
                 anHour = 3600,
                 nextHour;

            nextHour = currentTimeSeconds+anHour;

            Cursor rows = dbManager.retrieveTasksByTimeWindow(currentTimeSeconds, nextHour);
            out = cursorToTaskArrayList(context, rows);

        }

        return out;
    }

    public static boolean updateTask(Context context, Task task) {
        return false;
    }
}
