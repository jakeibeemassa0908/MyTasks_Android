package com.infiniteloop.mytasks.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.CheckList;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.loaders.CursorLoader;

/**
 * Created by theotherside on 06/06/15.
 */
public class Check_ListFragment extends VisibleListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int INDEPENDENT_CHEKLIST = -1;
    public static final int LOADER_ID =2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_list_fragment,container,false);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader.ChecklistCursorLoader(getActivity(),INDEPENDENT_CHEKLIST);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ID){
            TaskDataBaseHelper.ChecklistCursor cursor = (TaskDataBaseHelper.ChecklistCursor)data;
            if(cursor!=null){
                CheckListAdapter adapter = new CheckListAdapter(getActivity(),cursor);
                setListAdapter(adapter);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class CheckListAdapter extends CursorAdapter {
        TaskDataBaseHelper.ChecklistCursor mChecklistCursor;
        private TextView titleTextView;

        public CheckListAdapter(Context context, Cursor c) {
            super(context, c,0);
            mChecklistCursor = (TaskDataBaseHelper.ChecklistCursor)c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.checklist_fragment,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final CheckList note = mChecklistCursor.getChecklist();
            titleTextView = (TextView)view.findViewById(R.id.note_item_title_textview);
            titleTextView.setText(note.getName());
        }
    }
}
