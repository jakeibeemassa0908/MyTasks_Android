package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.Note;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskLab;

/**
 * Created by theotherside on 17/04/15.
 */
public class NoteFragment extends Fragment{

    private static final String TAG = NoteFragment.class.getSimpleName();

    private Task mTask;
    private EditText mNoteTitle;
    private EditText mNoteContent;
    private TaskLab mTaskLab = TaskLab.get(getActivity());


    public static NoteFragment newInstance(Task task){
        Bundle args = new Bundle();
        args.putParcelable(DetailTaskFragment.EXTRA_TASK,task);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mTask = args.getParcelable(DetailTaskFragment.EXTRA_TASK);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_note,container,false);

        mNoteTitle = (EditText)rootView.findViewById(R.id.note_title);
        mNoteContent = (EditText)rootView.findViewById(R.id.note_content);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                if(saveNote()){
                    //TODO Confirmation dialog
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(),"Empty note", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Verify whether note is not empy, then save to database
     * @return
     */
    private boolean saveNote(){
        String noteTitle = mNoteTitle.getText().toString();
        String noteContent = mNoteContent.getText().toString();

        if(!noteTitle.matches("")){
            Note note = new Note(noteTitle,noteContent,mTask.getId());
            if(mTaskLab.createNote(note)!=-1)
                return true;
            return false;
        }
        return false;
    }


}
