package com.asusoda.taskdevil.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.data_access_layer.DataAccess;
import com.asusoda.taskdevil.models.Task;

import java.util.ArrayList;

/**
 * Created by Tyler on 8/16/2014.
 */

public class TaskListAdapter extends ArrayAdapter<Task>{

    private final Context mContext;
    private final ArrayList<Task> mValues;
    public TaskListAdapter(Context context, ArrayList<Task> objects) {
        super(context, R.layout.task_item, objects);
        mContext = context;
        mValues = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // LayoutInflater is necessary to populate the task view from the resource files.
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // rowView is the View object which uses our task_item resource file as its layout for displaying information to the users
        View rowView = inflater.inflate(R.layout.task_item, parent, false);
        rowView.setLongClickable(true);
        // These views will be data fields for the row in the list
        final TextView firstLine = (TextView) rowView.findViewById(R.id.firstline);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondline);

        // Populating our data fields with... DATA.
        Task rowData = mValues.get(position);

        firstLine.setText(rowData.getTitle());
        secondLine.setText(rowData.getDescription());



        //Start GestureDetector setup for detecting swipe.

        //pass the currentTask (in case the task is swiped and needs to be deleted), and the view
        //to the gesture detector
        final GestureDetector gestureDetector = new GestureDetector(firstLine.getContext(), new GestureListener(rowData, rowView));
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        };

        rowView.setOnTouchListener(touchListener);
        //end GestureDetector setup

        return rowView;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnGestureListener{
        private View rowViewReference;
        private Task pressedTaskReference;

        public GestureListener(Task pressedTask, View rowView){
            this.rowViewReference = rowView;
            this.pressedTaskReference = pressedTask;
        }

        @Override
        public boolean onDown(MotionEvent e){
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            //get the references to the title and description fields
            TextView titleFieldReference = ((TextView)this.rowViewReference.findViewById(R.id.firstline));
            TextView descriptionFieldReference = ((TextView)this.rowViewReference.findViewById(R.id.secondline));

            //bit operation to strike-through the appropriate text field
            titleFieldReference.setPaintFlags(titleFieldReference.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            //set the description field to an empty string if the task is being deleted.
            descriptionFieldReference.setText("");

            //delete the task that was swiped
            DataAccess.deleteTask(getContext(), pressedTaskReference);

            return true;
        }

    }
}