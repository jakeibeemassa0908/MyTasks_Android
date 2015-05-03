package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.activities.CheckListActivity;
import com.infiniteloop.mytasks.activities.NoteActivity;
import com.infiniteloop.mytasks.activities.PhotoPagerActivity;
import com.infiniteloop.mytasks.data.CheckList;
import com.infiniteloop.mytasks.data.Note;
import com.infiniteloop.mytasks.data.Photo;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
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
        switch (resultCode){
            case Activity.RESULT_OK:
                mList.clear();
                getLoaderManager().restartLoader(0,null,this);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args= getArguments();
        mTaskId= args.getLong(DetailTaskFragment.EXTRA_TASK);
        String type = args.getString(EXTRA_TYPE);

        //If the task id and the type of the list data is defined
        if(mTaskId!=-1 && type!=null){
            mType=type;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.task_list_context,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mType.equals(Note.class.getName())){
            return new CursorLoader.NoteCursorLoader(getActivity(),mTaskId);
        }else if(mType.equals(CheckList.class.getName())){
            return new CursorLoader.ChecklistCursorLoader(getActivity(),mTaskId);
        }else if (mType.equals(Photo.class.getName())){
            return new CursorLoader.PhotoCursorLoader(getActivity(),mTaskId);
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
        else if (mType.equals(Photo.class.getName())){
            TaskDataBaseHelper.PhotoCursor cursor = (TaskDataBaseHelper.PhotoCursor)data;
            getActivity().setTitle(getString(R.string.all_pictures));
            if(cursor!=null){
                cursor.moveToFirst();
                for(int i =0;i<cursor.getCount();i++){
                    Photo photo = cursor.getPhoto();
                    if(!Helpers.imageExists(photo.getFilename())){
                        continue;
                    }
                    mList.add(photo);
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
            if(convertView==null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                view = inflater.inflate(R.layout.custom_grid_texts, null);
            }
                TextView titleText = (TextView)view.findViewById(R.id.grid_item_list_text);
                TextView dateText = (TextView)view.findViewById(R.id.grid_item_list_date);
                ImageView image = (ImageView)view.findViewById(R.id.grid_item_cover);


                if(mList.get(0) instanceof Note){
                    titleText.setText(((Note)mList.get(position)).getTitle());
                    long time =((Note)mList.get(position)).getCreationDate().getTime();
                    dateText.setText(Helpers.dateToString(time));
                }
                else if(mList.get(0) instanceof CheckList){
                    titleText.setText(((CheckList)mList.get(position)).getName());
                    long time =((CheckList)mList.get(position)).getEditedDate().getTime();
                    dateText.setText(Helpers.dateToString(time));
                } else if(mList.get(0) instanceof Photo){

                    titleText.setVisibility(View.GONE);
                    dateText.setVisibility(View.GONE);
                    image.setBackgroundColor(Color.TRANSPARENT);

                    String path = ((Photo)mList.get(position)).getFilename();
                    image.setImageBitmap(Helpers.getTailoredBitmap(path,image));
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

            else if(obj instanceof Photo){
                Photo photo = (Photo)obj;
                Intent intent = new Intent(getActivity(), PhotoPagerActivity.class);
                intent.putExtra(DetailTaskFragment.EXTRA_TASK,mTaskId);
                intent.putExtra(PhotoFragment.EXTRA_PICTURE,photo.getId());
                startActivity(intent);
            }

        }
    }

}
