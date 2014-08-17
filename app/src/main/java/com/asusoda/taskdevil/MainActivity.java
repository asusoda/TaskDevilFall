package com.asusoda.taskdevil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.asusoda.taskdevil.adapters.TaskListAdapter;
import com.asusoda.taskdevil.data_access_layer.DataAccess;
import com.asusoda.taskdevil.data_access_layer.DataAccess.TaskRetrieveOptions;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList< Task > testingTasks;

    private TaskListAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView taskList = (ListView)findViewById(R.id.TaskList);

        testingTasks = new ArrayList< Task >();

        DataAccess.addTask(this, new Task("I am a task", "Some say they click my checkbox"));
        DataAccess.addTask(this, new Task("I am another task", "Some say I killed Steve"));
        DataAccess.addTask(this, new Task("I am trapped", "Do you think the checkbox will save me?"));

        TaskRetrieveOptions options = new TaskRetrieveOptions();
        options.all = new Boolean(true);

        testingTasks = DataAccess.getTasks(this, options);

        taskAdapter = new TaskListAdapter(this, testingTasks);
        taskList.setAdapter(taskAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){
            case R.id.action_add:
                break;
            case R.id.action_editor:
                Intent editorIntent = new Intent(this, Editor.class);
                startActivity(editorIntent);
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_about:
                AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);

                //title string needs in-code composition
                String title = String.format(getString(R.string.action_about_title), getString(R.string.semantic_version));
                aboutBuilder.setTitle(title);

                aboutBuilder.setMessage(R.string.action_about_message);

                aboutBuilder.setPositiveButton(R.string.action_about_positiveButton, new DialogInterface.OnClickListener() {
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
