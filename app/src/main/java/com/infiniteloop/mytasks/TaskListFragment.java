package com.infiniteloop.mytasks;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infiniteloop.mytasks.data.SQLiteCursorLoader;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by theotherside on 07/03/15.
 */
public class TaskListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG= TaskListFragment.class.getSimpleName();
    private static final int CREATE_NEW_TASK=1;
    private static final int EDIT_TASK=2;
    private ArrayList<Task> mTasks;
    private ImageView mAddTaskImageView;
    private  View rootView;
    private View expandedToolbar;
    private ImageButton mDelete,mStart,mEdit,mComplete;

    private int mPosition;

    public static final String DRAWER_ITEM_CHOICE = "DrawerItemChoice";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(DRAWER_ITEM_CHOICE);
        mTasks=TaskLab.get(getActivity()).getTasks();
        //Initialize the loader to load the list of runs
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.task_list_fragment,container,false);


        mAddTaskImageView = (ImageView)rootView.findViewById(R.id.add_task_imageView);
        mAddTaskImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });

         return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
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
        //updateTaskList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CREATE_NEW_TASK:
                if(resultCode== Activity.RESULT_OK){
                    Toast.makeText(getActivity(),
                    '"'+mTasks.get(+mTasks.size()-1).getTitle()+'"'+ getString(R.string.added),
                    Toast.LENGTH_SHORT).show();
                    //restart loader to get any new task available
                    getLoaderManager().restartLoader(0,null,this);
                }
                break;
            case EDIT_TASK:
                if(resultCode==Activity.RESULT_OK){
                    //restart loader to get any edited task data
                    getLoaderManager().restartLoader(0,null,this);
                }
            default:
                break;
        }
    }

    private void createNewTask() {
        Intent i = new Intent(getActivity(),NewTaskActivity.class);
        startActivityForResult(i, CREATE_NEW_TASK);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //You only ever load the runs, so assume this is the case
        return new TaskListCursorLoader(getActivity(),mPosition);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //create an adapter to point at this cursor
        TaskCursorAdapter adapter =
                new TaskCursorAdapter(getActivity(), (TaskDataBaseHelper.TaskCursor)cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //stop using the cursor (via the adapter)
        setListAdapter(null);

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

    private  class TaskCursorAdapter extends CursorAdapter{
        private TaskDataBaseHelper.TaskCursor mTaskCursor;
        public TaskCursorAdapter(Context context,TaskDataBaseHelper.TaskCursor cursor){
            super(context,cursor,0);
            mTaskCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            //use a layout inflater to get a row view
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.task_list_item,parent,false);
        }

        @Override
        public void bindView(View convertView, Context context, Cursor cursor) {
            final Task task = mTaskCursor.getTask();
            TextView titleTextView = (TextView)convertView.findViewById(R.id.task_item_title_textview);
            titleTextView.setText(task.getTitle());
            TextView categoryTextView = (TextView)convertView.findViewById(R.id.task_item_category_textview);
            categoryTextView.setText(TaskLab.get(getActivity()).queryCatName(task.getCategory()));
            TextView dateTextView = (TextView)convertView.findViewById(R.id.task_item_date_textview);
            dateTextView.setText(task.toString());
            View toolbar=convertView.findViewById(R.id.expandable_list_details);

            Log.d(TAG,task.isCompleted()?"1":"0");

            mEdit = (ImageButton)toolbar.findViewById(R.id.edit_task_imageButton);
            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),EditTaskActivity.class);
                    intent.putExtra(EditTaskFragment.EXTRA_TASK,task);
                    startActivityForResult(intent,EDIT_TASK);
                }
            });
            mComplete = (ImageButton)toolbar.findViewById(R.id.mark_complete_button);
            mComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle(getString(R.string.complete_dialog_title));
                    dialog.setMessage(getString(R.string.complete_dialog_question));
                    dialog.setPositiveButton(getString(R.string.complete_dialog_yes),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                TaskLab.get(getActivity()).setComplete(task);
                                getLoaderManager().restartLoader(0,null,TaskListFragment.this);
                                Toast.makeText(getActivity(),
                                        '"'+task.getTitle()+'"'+ getString(R.string.complete_dialog_toast),
                                        Toast.LENGTH_SHORT)
                                        .show();
                        }
                    });
                    dialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();

                }
            });
            mStart=(ImageButton)toolbar.findViewById(R.id.start_timer_imageButton);
            mStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimeAndDatePickerFragment.DatePickerFragment pickers = new TimeAndDatePickerFragment.DatePickerFragment(getFragmentManager());
                    pickers.show(getFragmentManager(),"pickers");
                }
            });
            mDelete=(ImageButton)toolbar.findViewById(R.id.delete_task_imageButton);
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
                    deleteDialog.setTitle(getString(R.string.delete_task));
                    deleteDialog.setMessage(getString(R.string.delete_task_question));
                    deleteDialog.setPositiveButton(getString(R.string.delete),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean deleted=TaskLab.get(getActivity()).removeTask(task);
                            if(deleted){
                                getLoaderManager().restartLoader(0,null,TaskListFragment.this);
                                Toast.makeText(getActivity(),'"'+task.getTitle()+'"'+ getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    deleteDialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    deleteDialog.show();
                }
            });



            switch (task.getPriority()){
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
        }
    }

    public static class TaskListCursorLoader extends SQLiteCursorLoader{
        int mPosition=0;

        public TaskListCursorLoader(Context context,int position){
            super (context);
            mPosition=position;
        }

        @Override
        protected Cursor loadCursor() {
            //Query the list of runs
            return TaskLab.get(getContext()).queryTasks(mPosition);
        }
    }
}