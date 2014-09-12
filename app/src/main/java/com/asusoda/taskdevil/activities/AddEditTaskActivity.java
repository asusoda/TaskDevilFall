package com.asusoda.taskdevil.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        getActionBar().setTitle(R.string.addEdit_add_action_bar_title);
    }

    public void addNewTask(View view){
        String title = ((EditText) findViewById(R.id.add_title_field)).getText().toString();
        String description = ((EditText) findViewById(R.id.add_description_field)).getText().toString();
        if (title.equals("") && description.equals("")) {
            Toast.makeText(getApplicationContext(), "You left both fields blank. (Title/Description)", Toast.LENGTH_SHORT).show();
        }
        else if(title.equals("")){
            Toast.makeText(getApplicationContext(), "You left a field blank. (Title)", Toast.LENGTH_SHORT).show();
        }
        else if(description.equals("")){
            Toast.makeText(getApplicationContext(), "You left a field blank. (Description)", Toast.LENGTH_SHORT).show();
        }
        else {
            Task taskToAdd = new Task(title, description);
            DataAccess.addTask(this, new Task(title, description));
            Toast.makeText(getApplicationContext(), R.string.addEdit_confirm_add_toast_text, Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            this.setResult(RESULT_OK, i);
            this.finish();
        }
    }

    public void selectTime(View view) {
        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getFragmentManager(), "selectTime");
    }

    public void selectDate(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "selectDate");
    }

    // TODO: Implement
    public void selectRecurrence(View view) {
        Toast.makeText(getApplicationContext(), "NYI", Toast.LENGTH_SHORT).show();
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

    // Used for TimePickerDialog
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Toast.makeText(getActivity(), "NYI", Toast.LENGTH_SHORT).show();
        }

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Toast.makeText(getActivity(), "NYI", Toast.LENGTH_SHORT).show();
        }
    }
}
