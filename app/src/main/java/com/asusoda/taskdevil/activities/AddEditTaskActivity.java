package com.asusoda.taskdevil.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.models.Task;
import com.asusoda.taskdevil.data_access_layer.DataAccess;

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
        Task taskToAdd = new Task(title, description);
        DataAccess.addTask(this, new Task(title, description));

        Toast.makeText(getApplicationContext(), R.string.addEdit_confirm_add_toast_text, Toast.LENGTH_SHORT).show();

        Intent i = new Intent();
        this.setResult(RESULT_OK, i);
        this.finish();
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
}
