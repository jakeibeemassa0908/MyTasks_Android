package com.infiniteloop.mytasks.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.CheckList;
import com.infiniteloop.mytasks.data.CheckListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theotherside on 17/04/15.
 */
public class CheckListFragment extends Fragment {

    private static final String TAG = CheckListFragment.class.getSimpleName();
    private ArrayList<CheckListItem> mChecklist;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_checklist,container,false);
        mChecklist = new ArrayList<CheckListItem>();

        //get listView from rootlayout and set the adapter
        ListView checklistItems = (ListView)rootView.findViewById(R.id.checklist_item_list);
        final CheckListAdapter adapter = new CheckListAdapter(getActivity(),R.layout.checklist_item_view,mChecklist);
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
                    mChecklist.add(0,checkListItem);
                    newChecklistItem.setText("");
                    adapter.notifyDataSetChanged();

                }
            }
        });

        return rootView;
    }

    private class CheckListAdapter extends ArrayAdapter<CheckListItem> {


        public CheckListAdapter(Context context, int resource, List<CheckListItem> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If we were not given a view, inflate one
            if(convertView==null){
                convertView=getActivity().getLayoutInflater()
                        .inflate(R.layout.checklist_item_view, parent, false);
            }

            //Configure the  view for the Checklist
            CheckListItem item = getItem(position);

            CheckBox checklistItem = (CheckBox)convertView.findViewById(R.id.checklist_item);
            checklistItem.setText(item.getItem());
            checklistItem.setChecked(item.isCompleted());

            return convertView;

        }
    }
}
