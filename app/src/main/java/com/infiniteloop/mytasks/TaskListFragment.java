package com.infiniteloop.mytasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by theotherside on 07/03/15.
 */
public class TaskListFragment extends Fragment {

    private static final String TAG= TaskListFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_fragment,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.list_view_tasks);

        String[] taskArray = {"Chicken in Oven","Talk on Skype with boo","Math assignment"};
        ArrayList<String> taskArrayList = new ArrayList<String>(Arrays.asList(taskArray));
        TaskAdapter adapter = new TaskAdapter(taskArrayList);

        listView.setAdapter(adapter);
        return rootView;
    }


    private class TaskAdapter extends ArrayAdapter<String>{

        public TaskAdapter(ArrayList<String> tasks) {
            super(getActivity(),android.R.layout.simple_list_item_1,tasks);
        }

    }
}