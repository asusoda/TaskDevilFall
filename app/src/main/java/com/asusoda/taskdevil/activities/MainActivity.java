package com.asusoda.taskdevil.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.models.Task;
import com.asusoda.taskdevil.adapters.TaskListAdapter;
import com.asusoda.taskdevil.data_access_layer.DataAccess;
import com.asusoda.taskdevil.data_access_layer.DataAccess.TaskRetrieveOptions;

import java.util.ArrayList;

import de.timroes.android.listview.EnhancedListView;

public class MainActivity extends Activity {

    private ArrayList<Task> testingTasks;

    private TaskListAdapter taskAdapter;

    private EnhancedListView taskList;

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
        taskList = (EnhancedListView)findViewById(R.id.TaskList);



        testingTasks = new ArrayList< Task >();

        inflateTaskListAll();

        setUpGestureDetection();
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


    //Show context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.long_press_context, menu);
    }


    //determines what to do when a menu item in the context menu is selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_delete:
                //where task will be deleted
                //deleteTask(info.id);
                Toast.makeText(getApplicationContext(), "NYI", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.context_edit:
                //where a task will be edited
                //editTask(info.id);
                Toast.makeText(getApplicationContext(), "NYI", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }





    public void setUpGestureDetection(){
        //giving all tasks the ability to open a context menu
        //on a long press



        //credit to https://github.com/timroes/EnhancedListView/ for the EnhancedListView

        //set up the delete and undo functionality
        taskList.setDismissCallback(new EnhancedListView.OnDismissCallback() {

            @Override public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

                // Store the item for later undo
                final Task item = (Task) taskAdapter.getItem(position);

                // Remove the item from the adapter
                taskAdapter.remove(taskAdapter.getItem(position));

                // return an Undoable
                return new EnhancedListView.Undoable() {
                    // Reinsert the item to the adapter
                    @Override public void undo() {
                        taskAdapter.insert(item, position);
                    }

                    // Return a string for your item
                    @Override public String getTitle() {
                        return "Deleted '" + item.getTitle() + "'"; // Plz, use the resource system :)
                    }

                    // Delete item completely from your persistent storage
                    @Override public void discard() {
                        DataAccess.deleteTask(getApplicationContext(), item);
                    }
                };

            }

        });

        //allow for multilevel undoing
        taskList.setUndoStyle(EnhancedListView.UndoStyle.MULTILEVEL_POPUP);


        //add swipe functionality
        taskList.setShouldSwipeCallback(new EnhancedListView.OnShouldSwipeCallback() {
            @Override
            public boolean onShouldSwipe(EnhancedListView enhancedListView, int i) {
                return true;
            }
        });


        //enable the swipe/undo
        taskList.enableSwipeToDismiss();

        //register list view for context menu
        registerForContextMenu(taskList);

    }

}
