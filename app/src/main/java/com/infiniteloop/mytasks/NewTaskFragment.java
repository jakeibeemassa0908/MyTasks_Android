package com.infiniteloop.mytasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by theotherside on 07/03/15.
 */
public class NewTaskFragment extends Fragment {
    public static final String TAG = NewTaskFragment.class.getSimpleName();

    private static final int REQUEST_DURATION=1;

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private Spinner mVisibilitySpinner;
    private ImageButton mSetTimeButton;
    private EditText mTitleEditText;
    private int mDurationHours;
    private int mDurationMinutes;
    private EditText mDurationText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task,container,false);

        mDurationText = (EditText)rootView.findViewById(R.id.task_duration_edit_text);

        mTitleEditText = (EditText) rootView.findViewById(R.id.task_title_textview);

        mPrioritySpinner = (Spinner)rootView.findViewById(R.id.task_priority_spinner);
        mPrioritySpinner.setAdapter(getSpinnerAdapter(R.array.task_priority_array));

        mVisibilitySpinner= (Spinner) rootView.findViewById(R.id.task_visibility_spinner);
        mVisibilitySpinner.setAdapter(getSpinnerAdapter(R.array.task_visibility_array));

        mCategorySpinner=(Spinner)rootView.findViewById(R.id.task_category_spinner);
        mCategorySpinner.setAdapter(getSpinnerAdapter(R.array.task_category_array));

        mSetTimeButton = (ImageButton) rootView.findViewById(R.id.task_duration_button);
        mSetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                DurationDialog dialog = DurationDialog
                        .newInstance(mDurationHours, mDurationMinutes);
                dialog.setTargetFragment(NewTaskFragment.this,REQUEST_DURATION);
                dialog.show(fm,"duration");

            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)return;
        switch (requestCode){
            case REQUEST_DURATION:
                mDurationHours=data.getIntExtra(DurationDialog.EXTRA_HOURS,0);
                mDurationMinutes=data.getIntExtra(DurationDialog.EXTRA_MINUTES,0);
                updateTimeField();
                break;
            default:
                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task_menu:
                String title=mTitleEditText.getText().toString();
                String category=mCategorySpinner.getSelectedItem().toString();
                String priority=mPrioritySpinner.getSelectedItem().toString();
                String visibility=mVisibilitySpinner.getSelectedItem().toString();
                if(!title.matches("")){
                    boolean isCreated=TaskLab.get().createTask(getActivity(),title,priority,category,visibility);
                    if(isCreated){
                        Intent resultIntent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK,resultIntent);
                        getActivity().finish();
                    }else{
                        Log.d(TAG,"Task Not Created");
                    }
                }else{
                    Toast.makeText(getActivity(),"Empty Title",Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateTimeField() {
        mDurationText.setTextColor(getResources().getColor(R.color.red));
        mDurationText.setText(mDurationHours+" : "+ mDurationMinutes);

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
