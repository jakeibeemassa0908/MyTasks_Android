package com.infiniteloop.mytasks.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.activities.NoteActivity;
import com.infiniteloop.mytasks.data.Note;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.loaders.CursorLoader;

/**
 * Created by theotherside on 06/06/15.
 */

public class NoteListFragment extends VisibleListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final int INDEPENDENT_NOTES = -1;
    public static final int LOADER_ID =1;
    public static final int REQUEST_NOTE=3;

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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Note note =((TaskDataBaseHelper.NoteCursor)getListAdapter().getItem(position)).getNote();
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(NoteFragment.EXTRA_NOTE,note);
        startActivityForResult(intent,REQUEST_NOTE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLoaderManager().restartLoader(LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader.NoteCursorLoader(getActivity(),INDEPENDENT_NOTES);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ID){
            TaskDataBaseHelper.NoteCursor cursor = (TaskDataBaseHelper.NoteCursor)data;
            if(cursor!=null){
                NoteCursorAdapter adapter = new NoteCursorAdapter(getActivity(),cursor);
                setListAdapter(adapter);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }



    private class NoteCursorAdapter extends CursorAdapter {
        TaskDataBaseHelper.NoteCursor mNoteCursor;
        private TextView titleTextView;

        public NoteCursorAdapter(Context context, Cursor c) {
            super(context, c,0);
            mNoteCursor = (TaskDataBaseHelper.NoteCursor)c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.note_list_item,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final Note note = mNoteCursor.getNote();
            titleTextView = (TextView)view.findViewById(R.id.note_item_title_textview);
            titleTextView.setText(note.getTitle());

        }
    }
}