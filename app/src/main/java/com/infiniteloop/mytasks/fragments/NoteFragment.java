package com.infiniteloop.mytasks.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;

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

        //If note is not new, set the share button intent
        if(mNote!=null){
            mShareIntent = new Intent(Intent.ACTION_SEND);
            mShareIntent.setType("text/plain");
            mShareIntent.putExtra(Intent.EXTRA_TEXT,mNote.getNoteContent());
            mShareIntent.putExtra(Intent.EXTRA_TITLE,mNote.getTitle());
            mShareIntent.putExtra(Intent.EXTRA_SUBJECT,mNote.getTitle());
        }

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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // // Locate MenuItem with delete action
        MenuItem item_delete = menu.findItem(R.id.delete_default_view);

        // Locate MenuItem with ShareActionProvider
        MenuItem item_share = menu.findItem(R.id.menu_item_share);

        //If note is a new one, don't show the delete and the share menu button
        if(mTask != null){
            item_delete.setVisible(false);
            item_share.setVisible(false);
        }

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item_share);
        setShareIntent(mShareIntent);
    }

    // Call to update the share intent
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                final String noteTitle = mNoteTitle.getText().toString().trim();
                final String noteContent = mNoteContent.getText().toString().trim();

                //Note title is empty
                if(!noteTitle.matches("")){
                    //New Note being saved
                    if(mTask!=null){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle(getString(R.string.save_note));
                        dialog.setMessage(getString(R.string.save_note_dialog_q));
                        dialog.setPositiveButton(R.string.save_note,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note note = new Note(noteTitle,noteContent,mTask.getId());
                                long result =TaskLab.get(getActivity()).createNote(note);
                                if(result!=-1)
                                    getActivity().setResult(Activity.RESULT_OK);
                                    getActivity().finish();

                            }
                        });
                        dialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();

                        //Note being updated
                    }else if(mNote!=null && dataHasChanged(noteTitle,noteContent)){
                        final Note newNote = new Note(noteTitle,noteContent,mNote.getTaskId());
                        newNote.setId(mNote.getId());

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle(getString(R.string.save_note));
                        dialog.setMessage(getString(R.string.save_note_dialog_q));
                        dialog.setPositiveButton(R.string.save_note,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long result =TaskLab.get(getActivity()).updateNote(newNote);
                                if(result!=-1){
                                    getActivity().setResult(Activity.RESULT_OK);
                                    getActivity().finish();
                                }
                            }
                        });
                        dialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();

                        //Nothing has changed
                    }else{
                        getActivity().setResult(Activity.RESULT_CANCELED);
                        getActivity().finish();
                    }
                }else{
                    Toast.makeText(getActivity(),getString(R.string.empty_note_title),Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.delete_default_view:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage(getString(R.string.delete_question));
                dialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskLab.get(getActivity()).deleteNote(mNote.getId());
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean dataHasChanged(String title, String content){
        return !(mNote.getTitle().equals(title) && mNote.getNoteContent().equals(content));
    }
}