package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.activities.CheckListActivity;
import com.infiniteloop.mytasks.activities.NoteActivity;
import com.infiniteloop.mytasks.data.CheckList;
import com.infiniteloop.mytasks.data.CheckListItem;
import com.infiniteloop.mytasks.data.Note;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.data.TaskLab;
import com.infiniteloop.mytasks.loaders.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import java.util.ArrayList;

/**
 * Created by theotherside on 26/04/15.
 */
public class DefaultGridFragment extends Fragment  implements LoaderCallbacks<Cursor>{
    private static final String TAG = DefaultGridFragment.class.getSimpleName();

    private ArrayList<Object> mList = new ArrayList<Object>();

    private long mTaskId;
    private GridView mGridView;
    private String mType = "";

    public static final String EXTRA_TYPE="typeOfReceivedList";


    public static DefaultGridFragment newInstance(long taskId,String type){
        Bundle args = new Bundle();
        args.putLong(DetailTaskFragment.EXTRA_TASK, taskId);
        args.putString(EXTRA_TYPE,type);
        DefaultGridFragment fragment = new DefaultGridFragment();
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case DetailTaskFragment.REQUEST_CHECKLIST:
                if(resultCode== Activity.RESULT_OK){
                    //refreshAdapter();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args= getArguments();
        mTaskId= args.getLong(DetailTaskFragment.EXTRA_TASK);
        String type = args.getString(EXTRA_TYPE);

        //If the task id and the type of the list data is defined
        if(mTaskId!=-1 && type!=null){
            //if we are given a list of notes
            if (type.equals(Note.class.getName())){
                mType = Note.class.getName();

                //If we are given a list of cheklist
            }else if(type.equals(CheckList.class.getName())){
                mType = CheckList.class.getName();
            }
        }

        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_and_list_view,container,false);

        mGridView = (GridView)rootView.findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(new ClickListener(mList));

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mType.equals(Note.class.getName())){
            return new CursorLoader.NoteCursorLoader(getActivity(),mTaskId);
        }else if(mType.equals(CheckList.class.getName())){
            return new CursorLoader.ChecklistCursorLoader(getActivity(),mTaskId);
        }
        return null;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mType.equals(Note.class.getName())) {
            TaskDataBaseHelper.NoteCursor cursor = (TaskDataBaseHelper.NoteCursor)data;
            getActivity().setTitle(getString(R.string.all_note));
            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    mList.add(cursor.getNote());
                    cursor.moveToNext();
                }
            }
        }
        else if(mType.equals(CheckList.class.getName())){
            TaskDataBaseHelper.ChecklistCursor cursor = (TaskDataBaseHelper.ChecklistCursor)data;
            getActivity().setTitle(getString(R.string.all_checklist));
            if (cursor!=null){
                cursor.moveToFirst();
                for (int i=0;i<cursor.getCount();i++){
                    mList.add(cursor.getChecklist());
                    cursor.moveToNext();
                }
            }
        }

        mGridView.setAdapter(new GridViewAdapter(mList));
    }



    private class GridViewAdapter extends BaseAdapter{
        ArrayList<Object> mList;

        public GridViewAdapter(ArrayList<Object> list){
            mList=list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(convertView==null){
                LayoutInflater inflater= getActivity().getLayoutInflater();
                view = inflater.inflate(R.layout.custom_grid_texts,null);

                TextView titleText = (TextView)view.findViewById(R.id.grid_item_list_text);
                TextView dateText = (TextView)view.findViewById(R.id.grid_item_list_date);

                if(mList.get(0) instanceof Note){
                    titleText.setText(((Note)mList.get(position)).getTitle());
                    long time =((Note)mList.get(position)).getCreationDate().getTime();
                    dateText.setText(Helpers.dateToString(time));
                }
                else if(mList.get(0) instanceof CheckList){
                    titleText.setText(((CheckList)mList.get(position)).getName());
                    long time =((CheckList)mList.get(position)).getEditedDate().getTime();
                    dateText.setText(Helpers.dateToString(time));
                }

            }
            return view;
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {
        Intent mIntent;
        ArrayList<Object> mList;
        public ClickListener(ArrayList<Object> list){
            mList = list;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object obj = mList.get(position);
            //if we have received a list of note
            if (obj instanceof Note){
                Note note = (Note)obj;
                mIntent = new Intent(getActivity(),NoteActivity.class);
                mIntent.putExtra(NoteFragment.EXTRA_NOTE,note);
                startActivityForResult(mIntent, DetailTaskFragment.REQUEST_NOTE);

            //if we have received a list of checklist
            }else if (obj instanceof CheckList){

                CheckList checkList =(CheckList)obj;
                mIntent = new Intent(getActivity(), CheckListActivity.class);
                mIntent.putExtra(CheckListFragment.EXTRA_CHECKLIST,checkList);
                startActivityForResult(mIntent, DetailTaskFragment.REQUEST_CHECKLIST);
            }

        }
    }

}
