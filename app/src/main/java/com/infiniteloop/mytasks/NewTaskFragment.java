package com.infiniteloop.mytasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by theotherside on 07/03/15.
 */
public class NewTaskFragment extends Fragment {
    public static final String TAG = NewTaskFragment.class.getSimpleName();

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private Spinner mVisibilitySpinner;
    private Button mSetTimeButton;
    private EditText mTitleEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task,container,false);

        mTitleEditText = (EditText) rootView.findViewById(R.id.task_title_textview);

        mPrioritySpinner = (Spinner)rootView.findViewById(R.id.task_priority_spinner);
        mPrioritySpinner.setAdapter(getSpinnerAdapter(R.array.task_priority_array));

        mVisibilitySpinner= (Spinner) rootView.findViewById(R.id.task_visibility_spinner);
        mVisibilitySpinner.setAdapter(getSpinnerAdapter(R.array.task_visibility_array));

        mCategorySpinner=(Spinner)rootView.findViewById(R.id.task_category_spinner);
        mCategorySpinner.setAdapter(getSpinnerAdapter(R.array.task_category_array));

        mSetTimeButton = (Button) rootView.findViewById(R.id.task_duration_button);
        mSetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task_menu:
                String title=mTitleEditText.getText().toString();
                String category=mCategorySpinner.getSelectedItem().toString();
                String priority=mPrioritySpinner.getSelectedItem().toString();
                String visibility=mVisibilitySpinner.getSelectedItem().toString();

                Log.d(TAG,title+" "+category+" "+priority+" "+visibility);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private ArrayAdapter getSpinnerAdapter(int arrayId){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                arrayId,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
