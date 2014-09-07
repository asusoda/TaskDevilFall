package com.asusoda.taskdevil.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.models.Task;
import com.asusoda.taskdevil.adapters.TaskListAdapter;
import com.asusoda.taskdevil.data_access_layer.DataAccess;
import com.asusoda.taskdevil.data_access_layer.DataAccess.TaskRetrieveOptions;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<Task> testingTasks;

    private TaskListAdapter taskAdapter;

    private ListView taskList;

    public void inflateTaskListAll() {
        TaskRetrieveOptions options = new TaskRetrieveOptions();
        options.all = new Boolean(true);

        testingTasks = DataAccess.getTasks(this, options);

        taskAdapter = new TaskListAdapter(this, testingTasks);
        taskList.setAdapter(taskAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskList = (ListView)findViewById(R.id.TaskList);

        testingTasks = new ArrayList< Task >();

        inflateTaskListAll();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        ActivityCodes activityCode = ActivityCodes.values()[requestCode];

        switch (activityCode) {
            case ADD_TASK:
                //on successful add, refresh the data source
                if (resultCode == RESULT_OK) {
                    int a = 1;

                    inflateTaskListAll();

                    taskList.postInvalidate();

                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){
            case R.id.action_add:
                Intent addIntent = new Intent(this, AddEditTaskActivity.class);
                addIntent.putExtra(Intent.ACTION_INSERT, true);
                startActivityForResult(addIntent, ActivityCodes.ADD_TASK.val);
                break;
            case R.id.action_calendar:
                /* Temporarily disable because it's not done yet
                Intent calendarIntent = new Intent(this, CalendarActivity.class);
                startActivity(calendarIntent); */
                Toast.makeText(getApplicationContext(), "NYI", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_mass_delete:
                /* Temporarily disable because it's not done yet
                Intent massDeleteIntent = new Intent(this, MassDeleteActivity.class);
                startActivity(massDeleteIntent); */
                Toast.makeText(getApplicationContext(), "NYI", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_about:
               AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);

                //title string needs in-code composition
                String title = String.format(getString(R.string.main_about_title), getString(R.string.semantic_version));
                aboutBuilder.setTitle(title);

                aboutBuilder.setMessage(R.string.main_about_message);

                aboutBuilder.setPositiveButton(R.string.main_about_positiveButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id){

                        }
                    });

                AlertDialog aboutDialog = aboutBuilder.create();
                aboutDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
