package com.infiniteloop.mytasks;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by theotherside on 07/03/15.
 */
public class TaskListFragment extends ListFragment {

    private static final String TAG= TaskListFragment.class.getSimpleName();
    private static final int CREATE_NEW_TASK=1;
    private ArrayList<Task> mTasks;
    ListView mListView;
    private ImageView mAddTaskImageView;
    private TaskAdapter mTaskAdapter;
    private  View rootView;
    private View mBottomLayoutItem;
    private View expandedToolbar;
    private ImageButton mStart,mEdit,mComplete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasks=TaskLab.get().getTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.task_list_fragment,container,false);
        mListView = (ListView)rootView.findViewById(android.R.id.list);

        mTaskAdapter = new TaskAdapter(mTasks);

        mBottomLayoutItem = rootView.findViewById(R.id.expandable_list_details);

        mAddTaskImageView = (ImageView)rootView.findViewById(R.id.add_task_imageView);
        mAddTaskImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });

        mListView.setAdapter(mTaskAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task t = mTaskAdapter.getItem(position);
        View toolbar = v.findViewById(R.id.expandable_list_details);

        View toolBarToClose=expandedToolbar;

        if(toolBarToClose!=null && toolBarToClose!=toolbar){
            ExpandAnimation closeAnimation = new ExpandAnimation(toolBarToClose,200);
            toolBarToClose.startAnimation(closeAnimation);
        }
            ExpandAnimation expandAnimation = new ExpandAnimation(toolbar,200);
            toolbar.startAnimation(expandAnimation);

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
                    Toast.LENGTH_SHORT).show();
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.task_list_item,parent,false);
            }
            Task t = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.task_item_title_textview);
            titleTextView.setText(t.getTitle());
            TextView categoryTextView = (TextView)convertView.findViewById(R.id.task_item_category_textview);
            categoryTextView.setText(t.getCategory());
            View toolbar=convertView.findViewById(R.id.expandable_list_details);

            mEdit = (ImageButton)toolbar.findViewById(R.id.edit_task_imageButton);
            mComplete = (ImageButton)toolbar.findViewById(R.id.mark_complete_button);
            mStart=(ImageButton)toolbar.findViewById(R.id.start_timer_imageButton);



            switch (t.getPriority()){
                case Task.VERY_HIGH_PRIORITY:
                    convertView.setBackgroundColor(getResources().getColor(R.color.red));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.dark_red));
                    break;

                case Task.HIGH_PRIORITY:
                    convertView.setBackgroundColor(getResources().getColor(R.color.orange));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                    break;

                case Task.NORMAL_PRIORITY:
                    convertView.setBackgroundColor(getResources().getColor(R.color.sunshine_blue));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.sunshine_dark_blue));
                    break;

                case Task.LOW_PRIORITY:
                    convertView.setBackgroundColor(getResources().getColor(R.color.green));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.dark_green));


            }

            toolbar = convertView.findViewById(R.id.expandable_list_details);
            ((LinearLayout.LayoutParams)toolbar.getLayoutParams()).bottomMargin=-50;
            toolbar.setVisibility(View.GONE);

            return convertView;
        }
    }

    public class ExpandAnimation extends Animation {
        private View mAnimatedView;
        private LinearLayout.LayoutParams mViewLayoutParams;
        private int mMarginStart, mMarginEnd;
        private boolean mIsVisibleAfter = false;
        private boolean mWasEndedAlready = false;

        /**
         * Initialize the animation
         * @param view The layout we want to animate
         * @param duration The duration of the animation, in ms
         */
        public ExpandAnimation(View view, int duration) {

            setDuration(duration);
            mAnimatedView = view;
            mViewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();

            // decide to show or hide the view
            mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);




            mMarginStart = mViewLayoutParams.bottomMargin;
            mMarginEnd = (mMarginStart == 0 ? (0- view.getHeight()) : 0);

            view.setVisibility(View.VISIBLE);
            expandedToolbar=view;

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            if (interpolatedTime < 1.0f) {

                // Calculating the new bottom margin, and setting it
                mViewLayoutParams.bottomMargin = mMarginStart
                        + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

                // Invalidating the layout, making us seeing the changes we made
                mAnimatedView.requestLayout();


                // Making sure we didn't run the ending before (it happens!)
            } else if (!mWasEndedAlready) {
                mViewLayoutParams.bottomMargin = mMarginEnd;
                mAnimatedView.requestLayout();

                if (mIsVisibleAfter) {
                    mAnimatedView.setVisibility(View.GONE);
                    expandedToolbar=null;
                }
                mWasEndedAlready = true;
            }
        }
    }
}