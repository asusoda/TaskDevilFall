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




        return rowView;
    }


}