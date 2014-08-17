package com.asusoda.taskdevil.data_access_layer;

import android.content.Context;
import android.database.Cursor;

import com.asusoda.taskdevil.DatabaseManager;
import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.Task;
import com.asusoda.taskdevil.Task.RecurrenceTypes;

import java.util.ArrayList;

/**
 * Created by root on 8/11/14.
 */
public class DataAccess {

    public static class TaskRetrieveOptions {
        public Boolean all;
        public Integer startTime;
        public Integer endTime;
        public Integer occursAt;
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
                rowOccursAt = rows.getColumnIndex(context.getString(R.string.task_occurs_at_field));

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
                        rows.getInt(rowOccursAt)
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
        if (options.all != null && options.all.booleanValue() == true) {
            DatabaseManager dbManager = new DatabaseManager(context);
            Cursor rows = dbManager.retrieveAllTasks();
            out = cursorToTaskArrayList(context, rows);
        }

        return out;
    }

    public static boolean updateTask(Context context, Task task) {
        return false;
    }
}
