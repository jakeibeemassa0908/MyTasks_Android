package com.infiniteloop.mytasks;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by theotherside on 07/03/15.
 */
public class TaskListFragment extends Fragment {

    private static final String TAG= TaskListFragment.class.getSimpleName();
    private static final int CREATE_NEW_TASK=1;
    private ArrayList<Task> mTasks;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mTasks=TaskLab.get().getTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_fragment,container,false);
        mListView = (ListView)rootView.findViewById(R.id.list_view_tasks);

        TaskAdapter adapter = new TaskAdapter(mTasks);

        mListView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTaskList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_add_task:
                createNewTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CREATE_NEW_TASK:
                Toast.makeText(getActivity(),'"'+mTasks.get(+mTasks.size()-1).getTitle()+'"'+" Added",Toast.LENGTH_LONG).show();
                updateTaskList();
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
                        .inflate(android.R.layout.simple_list_item_1,null);
            }
            Task t = getItem(position);
            TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
            textView.setText(t.getTitle());
            return convertView;
        }
    }
}