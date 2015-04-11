package com.infiniteloop.mytasks.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infiniteloop.mytasks.R;

import java.util.Calendar;

/**
 * Created by theotherside on 27/03/15.
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.about_fragment,container,false);


        TextView versionText = (TextView)rootView.findViewById(R.id.version);
        String name="";
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            name = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionText.setText(String.format(getResources().getString(R.string.version),name));


        int year = Calendar.getInstance().get(Calendar.YEAR);
        TextView copyrightYear = (TextView)rootView.findViewById(R.id.copyright);
        copyrightYear.setText(String.format(getResources().getString(R.string.copyright_text),year));
        return rootView;
    }
}
