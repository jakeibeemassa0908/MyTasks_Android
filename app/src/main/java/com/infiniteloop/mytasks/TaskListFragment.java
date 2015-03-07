package com.infiniteloop.mytasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by theotherside on 07/03/15.
 */
public class TaskListFragment extends Fragment {

    private static final String TAG= TaskListFragment.class.getSimpleName();
    private ArrayList<Task> mTasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasks=TaskLab.get().getTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_fragment,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.list_view_tasks);

        TaskAdapter adapter = new TaskAdapter(mTasks);

        listView.setAdapter(adapter);
        return rootView;
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
            textView.setText(t.getDescription());
            return convertView;
        }
    }
}