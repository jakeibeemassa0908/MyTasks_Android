package com.infiniteloop.mytasks;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by theotherside on 07/03/15.
 */
public class TaskListFragment extends Fragment {

    private static final String TAG= TaskListFragment.class.getSimpleName();
    private static final int CREATE_NEW_TASK=1;
    private ArrayList<Task> mTasks;
    ListView mListView;
    private ImageView mAddTaskImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasks=TaskLab.get().getTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_fragment,container,false);
        mListView = (ListView)rootView.findViewById(R.id.list_view_tasks);

        TaskAdapter adapter = new TaskAdapter(mTasks);

        mAddTaskImageView = (ImageView)rootView.findViewById(R.id.add_task_imageView);
        mAddTaskImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });

        mListView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTaskList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CREATE_NEW_TASK:
                if(resultCode== Activity.RESULT_OK){
                    Toast.makeText(getActivity(),
                    '"'+mTasks.get(+mTasks.size()-1).getTitle()+'"'+" Added",
                    Toast.LENGTH_LONG).show();
                    updateTaskList();
                }
                break;
            default:
                break;
        }
    }

    private void updateTaskList() {
        ((TaskAdapter)mListView.getAdapter()).notifyDataSetChanged();
    }

    private void createNewTask() {
        Intent i = new Intent(getActivity(),NewTaskActivity.class);
        startActivityForResult(i, CREATE_NEW_TASK);
    }

    private class TaskAdapter extends ArrayAdapter<Task>{

        public TaskAdapter(ArrayList<Task> tasks) {
            super(getActivity(),0,tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.task_list_item,null);
            }
            Task t = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.task_item_title_textview);
            titleTextView.setText(t.getTitle());
            TextView priorityTextview = (TextView)convertView.findViewById(R.id.task_item_category_textview);

            switch (t.getPriority()){
                case Task.VERY_HIGH_PRIORITY:
                    titleTextView.setTextColor(getResources().getColor(R.color.red));
                    break;
                case Task.HIGH_PRIORITY:
                    titleTextView.setTextColor(getResources().getColor(R.color.orange));
                    break;
                case Task.NORMAL_PRIORITY:
                    titleTextView.setTextColor(getResources().getColor(R.color.sunshine_blue));
                    break;
                case Task.LOW_PRIORITY:
                    titleTextView.setTextColor(getResources().getColor(R.color.green));

            }

            return convertView;
        }
    }
}