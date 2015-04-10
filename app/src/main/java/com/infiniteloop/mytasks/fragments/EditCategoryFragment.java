package com.infiniteloop.mytasks.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.activities.TaskListActivity;
import com.infiniteloop.mytasks.data.Category;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.data.TaskLab;

/**
 * Created by theotherside on 10/04/15.
 */
public class EditCategoryFragment extends Fragment {

    public static final String TAG = EditCategoryFragment.class.getSimpleName();
    public TaskLab mTaskLab = TaskLab.get(getActivity());
    public long mCatId=0;

    public static EditCategoryFragment newInstance(long mPosition){
        Bundle args = new Bundle();
        args.putLong("EXTRA_CAT",mPosition-100);

        EditCategoryFragment fragmentEditCat = new EditCategoryFragment();
        fragmentEditCat.setArguments(args);
        return fragmentEditCat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCatId = getArguments().getLong("EXTRA_CAT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_category,container,false);


        TextView catName = (TextView)rootView.findViewById(R.id.cat_name);
        Category cat=mTaskLab.queryCategory(mCatId);
        catName.setText(cat.getCategoryName());

        TextView deleteCat = (TextView)rootView.findViewById(R.id.delete_cat);
        deleteCat.setText("Delete "+ cat.getCategoryName());

        View deleteView = rootView.findViewById(R.id.delete_cat_layout);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(getString(R.string.delete_category_dialog_title));
                dialog.setMessage(getString(R.string.delete_category_dialog_question));
                dialog.setPositiveButton(getString(R.string.delete),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTaskLab.deleteCategory(mCatId);
                        ((TaskListActivity)getActivity()).refreshDrawerList(0); //Bad implementation I know, fragment shouldn't explicitly refer to an activity
                    }
                });
                dialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        return rootView;
    }
}
