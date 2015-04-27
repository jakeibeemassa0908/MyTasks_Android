package com.infiniteloop.mytasks.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.CheckList;
import com.infiniteloop.mytasks.data.Note;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.data.TaskLab;

import java.util.ArrayList;

/**
 * Created by theotherside on 26/04/15.
 */
public class DefaultGridFragment extends Fragment {
    private static final String TAG = DefaultGridFragment.class.getSimpleName();

    private ArrayList<Object> mList = new ArrayList<Object>();
    private long mTaskId;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args= getArguments();
        mTaskId= args.getLong(DetailTaskFragment.EXTRA_TASK);
        String type = args.getString(EXTRA_TYPE);

        if(mTaskId!=-1 && type!=null){
            if (type.equals(Note.class.getName())){
                TaskDataBaseHelper.NoteCursor cursor =  TaskLab.get(getActivity()).queryNotes(mTaskId);
                if (cursor!=null){
                    cursor.moveToFirst();
                    for (int i=0;i<cursor.getCount();i++){
                        mList.add(cursor.getNote());
                        cursor.moveToNext();
                    }
                }
            }else if(type.equals(CheckList.class.getName())){
                TaskDataBaseHelper.ChecklistCursor cursor = TaskLab.get(getActivity()).queryChecklist(mTaskId);
                if (cursor!=null){
                    cursor.moveToFirst();
                    for (int i=0;i<cursor.getCount();i++){
                        mList.add(cursor.getChecklist());
                        cursor.moveToNext();
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_and_list_view,container,false);

        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(mList));

        return rootView;
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

}
