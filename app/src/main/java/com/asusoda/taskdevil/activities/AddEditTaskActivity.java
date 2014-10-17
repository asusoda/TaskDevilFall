package com.asusoda.taskdevil.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.models.Task;
import com.asusoda.taskdevil.data_access_layer.DataAccess;

import java.util.Calendar;

public class AddEditTaskActivity extends Activity {

    // Temporary workaround. Bad convention, but it works.
    protected static Calendar occurs;
    protected static int recurrence;
    protected static Calendar reminderAt;
    protected static boolean hasSetReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        occurs = Calendar.getInstance();
        recurrence = -1;
        reminderAt = Calendar.getInstance();
        hasSetReminder = false;
        getActionBar().setTitle(R.string.addEdit_add_action_bar_title);
    }

    public void addNewTask(View view){
        String title = ((EditText) findViewById(R.id.add_title_field)).getText().toString();
        String description = ((EditText) findViewById(R.id.add_description_field)).getText().toString();
        String time = ((EditText) findViewById(R.id.add_time_field)).getText().toString();
        String date = ((EditText) findViewById(R.id.add_date_field)).getText().toString();
        String recurs = ((EditText) findViewById(R.id.add_recurrence_field)).getText().toString();
        String reminderTime = ((EditText) findViewById(R.id.add_reminder_time)).getText().toString();
        int flags = 0;
        flags |= (title.equals("") ? 0 : 1);
        flags |= (description.equals("") ? 0 : 2);
        flags |= (time.equals("") ? 0 : 4);
        flags |= (date.equals("") ? 0 : 8);
        flags |= (recurs.equals("") ? 0 : 16);
        flags |= (reminderTime.equals("") ? 0 : 32);
        if (flags != 63) {
            Toast.makeText(getApplicationContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
        else {
            // Need to set the id manually? Fuck. Shouldn't it auto-increment and auto-assign?
            Task taskToAdd;
            switch(recurrence) {
                case 0:
                    taskToAdd = new Task(0, title, description, Task.RecurrenceTypes.ONLY_ONCE, 0L, 0, occurs.getTimeInMillis() / 1000L, reminderAt.getTimeInMillis() / 1000L);
                    break;
                case 1:
                    taskToAdd = new Task(0, title, description, Task.RecurrenceTypes.PERIODIC, 86400L, 0, occurs.getTimeInMillis() / 1000L, reminderAt.getTimeInMillis() / 1000L);
                    break;
                case 2:
                    taskToAdd = new Task(0, title, description, Task.RecurrenceTypes.PERIODIC, 604800L, 0, occurs.getTimeInMillis() / 1000L, reminderAt.getTimeInMillis() / 1000L);
                    break;
                case 3:
                    taskToAdd = new Task(0, title, description, Task.RecurrenceTypes.PERIODIC, 2592000L, 0, occurs.getTimeInMillis() / 1000L, reminderAt.getTimeInMillis() / 1000L);
                    break;
                case 4:
                    taskToAdd = new Task(0, title, description, Task.RecurrenceTypes.PERIODIC, 31557600L, 0, occurs.getTimeInMillis() / 1000L, reminderAt.getTimeInMillis() / 1000L);
                    break;
                default:
                    taskToAdd = new Task(0, title, description, Task.RecurrenceTypes.ONLY_ONCE, 0L, 0, occurs.getTimeInMillis() / 1000L, reminderAt.getTimeInMillis() / 1000L);
                    break;
            }
            DataAccess.addTask(this, taskToAdd);
            Toast.makeText(getApplicationContext(), R.string.addEdit_confirm_add_toast_text, Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            this.setResult(RESULT_OK, i);
            this.finish();
        }
    }

    public void selectOccurrenceTime(View view) {
        DialogFragment fragment = new OccursTimePickerFragment();
        fragment.show(getFragmentManager(), "selectOccurrenceTime");
    }

    public void selectDate(View view) {
        DialogFragment fragment = new OccursDatePickerFragment();
        fragment.show(getFragmentManager(), "selectDate");
    }

    public void selectRecurrence(View view) {
        DialogFragment fragment = new RecurrenceFragment();
        fragment.show(getFragmentManager(), "selectRecurrence");
    }

    public void selectReminderTime(View view) {
        DialogFragment fragment = new ReminderTimePickerFragment();
        fragment.show(getFragmentManager(), "selectReminderTime");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class OccursTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = occurs;
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            occurs.set(Calendar.HOUR_OF_DAY, hourOfDay);
            occurs.set(Calendar.MINUTE, minute);
            reminderAt.setTimeInMillis(occurs.getTimeInMillis() - 300000L);
            if (hasSetReminder) {

                EditText editText = (EditText) getActivity().findViewById(R.id.add_reminder_time);
                editText.setText("Remind Me At: " + DateFormat.format("hh:mm a", reminderAt));
            }
            EditText editText = (EditText) getActivity().findViewById(R.id.add_time_field);
            editText.setText(DateFormat.format("hh:mm a", occurs));
        }

    }

    public static class OccursDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year = occurs.get(Calendar.YEAR);
            int month = occurs.get(Calendar.MONTH);
            int day = occurs.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            occurs.set(Calendar.YEAR, year);
            occurs.set(Calendar.MONTH, month);
            occurs.set(Calendar.DAY_OF_MONTH, day);
            reminderAt.setTimeInMillis(occurs.getTimeInMillis() - 300000L);
            CharSequence dateString;
            if(occurs.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR)) {
                dateString = DateFormat.format("MM/dd/yyyy", occurs);
            }
            else {
                dateString = DateFormat.format("MM/dd", occurs);
            }
            EditText editText = (EditText) getActivity().findViewById(R.id.add_date_field);
            editText.setText(dateString);
        }
    }

    public static class RecurrenceFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String[] items = new String[] {"Once Only", "Daily", "Weekly", "Monthly", "Yearly"};
            builder.setTitle("Recurrence")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = (EditText) getActivity().findViewById(R.id.add_recurrence_field);
                            recurrence = which;
                            switch(which) {
                                case 0:
                                    editText.setText("Once Only");
                                    break;
                                case 1:
                                    editText.setText("Daily");
                                    break;
                                case 2:
                                    editText.setText("Weekly");
                                    break;
                                case 3:
                                    editText.setText("Monthly");
                                    break;
                                case 4:
                                    editText.setText("Yearly");
                                    break;
                            }
                        }
                    });
            return builder.create();
        }

    }

    public static class ReminderTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int hour = reminderAt.get(Calendar.HOUR_OF_DAY);
            int minute = reminderAt.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hasSetReminder = true;
            reminderAt.set(Calendar.HOUR_OF_DAY, hourOfDay);
            reminderAt.set(Calendar.MINUTE, minute);
            EditText editText = (EditText) getActivity().findViewById(R.id.add_reminder_time);
            editText.setText("Remind Me At: " + DateFormat.format("hh:mm a", reminderAt));
        }

    }
}
