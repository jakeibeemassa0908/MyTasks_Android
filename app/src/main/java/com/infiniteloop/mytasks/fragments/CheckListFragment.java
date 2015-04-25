package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.CheckList;
import com.infiniteloop.mytasks.data.CheckListItem;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskLab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by theotherside on 17/04/15.
 */
public class CheckListFragment extends Fragment {

    private static final String TAG = CheckListFragment.class.getSimpleName();

    private ArrayList<CheckListItem> mChecklistItems;
    private EditText mChecklistTitle;
    private Task mTask;


    public static CheckListFragment newInstance(Task task){
        Bundle args = new Bundle();
        args.putParcelable(DetailTaskFragment.EXTRA_TASK,task);
        CheckListFragment fragment = new CheckListFragment();
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

        View rootView = inflater.inflate(R.layout.new_checklist,container,false);
        mChecklistTitle =(EditText)rootView.findViewById(R.id.checklistTitle);
        mChecklistItems = new ArrayList<CheckListItem>();


        //get listView from rootlayout and set the adapter
        ListView checklistItems = (ListView)rootView.findViewById(R.id.checklist_item_list);
        final CheckListAdapter adapter = new CheckListAdapter(getActivity(),R.layout.checklist_item_view,mChecklistItems);
        checklistItems.setAdapter(adapter);

        final EditText newChecklistItem = (EditText)rootView.findViewById(R.id.newChecklistItem);


        ImageButton addItem = (ImageButton)rootView.findViewById(R.id.addItemButton);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = newChecklistItem.getText().toString();
                if(!item.matches("")){
                    CheckListItem checkListItem = new CheckListItem();
                    checkListItem.setItem(item);
                    checkListItem.setCompleted(false);
                    mChecklistItems.add(checkListItem);
                    newChecklistItem.setText("");
                    adapter.notifyDataSetChanged();

                }
            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                if(saveChecklist()){
                    //save the checklistITem
                    if(dataHasChanged()){
                        //show confirm dialog
                    }
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean saveChecklist(){

        String title =mChecklistTitle.getText().toString();
        if(title.matches(""))return false;

        //Create new Checklist object and set values
        CheckList newChecklist = new CheckList();
        newChecklist.setChecklistItems(mChecklistItems);
        newChecklist.setCreatedDate(new Date());
        newChecklist.setEditedDate(new Date());
        newChecklist.setName(title);
        newChecklist.setTaskId(mTask.getId());


        long result =TaskLab.get(getActivity()).createCheckList(newChecklist);
        if(result ==1)
            return true;
        return false;
    }

    private class CheckListAdapter extends ArrayAdapter<CheckListItem> {
        public CheckListAdapter(Context context, int resource, List<CheckListItem> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //If we were not given a view, inflate one
            if(convertView==null){
                convertView=getActivity().getLayoutInflater()
                        .inflate(R.layout.checklist_item_view, parent, false);
            }

            //Configure the  view for the Checklist
            final CheckListItem item = getItem(position);

            CheckBox checklistItem = (CheckBox)convertView.findViewById(R.id.checklist_item);
            checklistItem.setText(item.getItem());
            checklistItem.setChecked(item.isCompleted());
            checklistItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setCompleted(isChecked);
                }
            });
            return convertView;

        }
    }
}
