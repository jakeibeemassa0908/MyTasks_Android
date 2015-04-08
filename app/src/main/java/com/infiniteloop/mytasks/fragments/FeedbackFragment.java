package com.infiniteloop.mytasks.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infiniteloop.mytasks.R;

/**
 * Created by theotherside on 30/03/15.
 */
public class FeedbackFragment extends Fragment {

    public static final String TAG= FeedbackFragment.class.getSimpleName();
    private static final String SUBJECT = "Task Android App Feedback";
    private static final String EMAIL = "jac.massa0908@gmail.com";

    private static final String URL_STORE="https://play.google.com/store/apps/details?id=com.infiniteloop.mytasks";

    private EditText mFeedback;
    private Button mGooglePlay,mSubmitFeedback;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.feedback_fragment,container,false);

        mFeedback =(EditText)root.findViewById(R.id.feedback_text);
        mSubmitFeedback = (Button)root.findViewById(R.id.submit_feedbackButton);
        mSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = mFeedback.getText().toString();
                if(!feedback.matches("")){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");

                    intent.putExtra(Intent.EXTRA_SUBJECT,SUBJECT);
                    intent.putExtra(Intent.EXTRA_TEXT,feedback);
                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{EMAIL});
                    try{
                        startActivity(intent);
                    }catch (android.content.ActivityNotFoundException ex ){
                        Toast.makeText(getActivity(),getString(R.string.no_email_client),Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        mGooglePlay = (Button)root.findViewById(R.id.rateusButton);
        mGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_VIEW);
                //Try Google play
                intent.setData(Uri.parse("market://details?id=com.infiniteloop.mytasks"));
                if (!MyStartActivity(intent)) {
                    //Market (Google play) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse(URL_STORE));
                }
            }
        });

        return root;
    }

    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }
}
