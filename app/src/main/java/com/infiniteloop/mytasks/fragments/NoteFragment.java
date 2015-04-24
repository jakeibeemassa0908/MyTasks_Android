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
    private Note mNote;
    private EditText mNoteTitle;
    private EditText mNoteContent;
    private TaskLab mTaskLab = TaskLab.get(getActivity());

    public static final String EXTRA_NOTE = "com.taskrapp.note";


    public static NoteFragment newInstance(Object obj){
        Task task;
        Note note;
        Bundle args = new Bundle();

        //If object received is task, a new note is being created
        if(obj instanceof Task) {
            task = (Task)obj;
            args.putParcelable(DetailTaskFragment.EXTRA_TASK,task);

            //If object is a note, a note is being updated
        }else if(obj instanceof Note){
            note =(Note)obj;
            args.putParcelable(NoteFragment.EXTRA_NOTE,note);
        }

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mTask = args.getParcelable(DetailTaskFragment.EXTRA_TASK);
        mNote = args.getParcelable(EXTRA_NOTE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_note,container,false);

        mNoteTitle = (EditText)rootView.findViewById(R.id.note_title);
        mNoteContent = (EditText)rootView.findViewById(R.id.note_content);

        //Populate the note if note is being updated
        if(mNote!=null){
            mNoteTitle.setText(mNote.getTitle());
            mNoteContent.setText(mNote.getNoteContent());
        }

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
     * Verify whether note is not empty, then save to database
     * @return
     */
    private boolean saveNote(){
        String noteTitle = mNoteTitle.getText().toString();
        String noteContent = mNoteContent.getText().toString();
        Note note;

        if(!noteTitle.matches("")){
            //New note being created
            if(mTask!=null) {
                note = new Note(noteTitle, noteContent, mTask.getId());
                if(mTaskLab.createNote(note)!=-1)
                    return true;
            }else if(mNote!=null){
                mNote = new Note(noteTitle,noteContent,mNote.getTaskId());
                //update mNote;
                return true;
            }
        }
        return false;
    }
}