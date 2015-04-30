package com.infiniteloop.mytasks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;

/**
 * Created by theotherside on 30/04/15.
 */
public class PhotoFragment extends Fragment {

    private static final String TAG = PhotoFragment.class.getName();
    public static final String EXTRA_PICTURE = "pictureOfFragment";
    public ImageView mPicture;


    public static PhotoFragment newInstance(String path){
        Bundle args = new Bundle();
        args.putString(EXTRA_PICTURE,path);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_view,container,false);

        Bundle bundle = getArguments();
        String path  = bundle.getString(EXTRA_PICTURE);
        ImageView image = (ImageView)rootView.findViewById(R.id.picture);
        image.setImageBitmap(Helpers.getTailoredBitmap(path,image));
        return rootView;
    }
}
