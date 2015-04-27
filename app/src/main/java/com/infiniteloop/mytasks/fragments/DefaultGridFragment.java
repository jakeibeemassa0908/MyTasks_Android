package com.infiniteloop.mytasks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.CheckList;

import java.util.ArrayList;

/**
 * Created by theotherside on 26/04/15.
 */
public class DefaultGridFragment extends Fragment {
    private static final String TAG = DefaultGridFragment.class.getSimpleName();
    private ArrayList<Object> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_and_list_view,container,false);

        mList = new ArrayList<Object>();
        mList.add(new CheckList());

        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(mList));

        return rootView;
    }


    private class GridViewAdapter extends BaseAdapter{
        ArrayList<Object> mList;

        public GridViewAdapter(ArrayList<Object> list){
            mList=list;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(convertView==null){
                LayoutInflater inflater= getActivity().getLayoutInflater();
                view = inflater.inflate(R.layout.custom_grid_texts,null);
            }
            return view;
        }
    }

}
