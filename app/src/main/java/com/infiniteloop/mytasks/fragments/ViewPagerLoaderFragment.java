package com.infiniteloop.mytasks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infiniteloop.mytasks.R;

/**
 * Created by theotherside on 06/06/15.
 */
public class ViewPagerLoaderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.viewpager,container,false);
        return rootview;
    }
}
