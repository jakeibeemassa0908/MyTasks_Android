package com.infiniteloop.mytasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskFragment extends Fragment {
    private static final String TAG=EditTaskFragment.class.getSimpleName();

    public static String EXTRA_TASK_ID="com.infiniteloop.task_id";
    private long mTaskId;
    private Task mTask;

    public static EditTaskFragment newInstance(long id){
        Bundle args = new Bundle();
        args.putLong(EXTRA_TASK_ID,id);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mTaskId=getArguments().getLong(EXTRA_TASK_ID);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.edit_task_fragment,container,false);
        return rootView;
    }
}
