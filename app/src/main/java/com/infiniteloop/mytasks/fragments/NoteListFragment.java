package com.infiniteloop.mytasks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infiniteloop.mytasks.R;

/**
 * Created by theotherside on 06/06/15.
 */
public class NoteListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_list_fragment,container,false);
        return rootView;
    }
}
