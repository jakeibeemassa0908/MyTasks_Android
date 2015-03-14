package com.infiniteloop.mytasks;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskListActivity extends ActionBarActivity {
    private static final String TAG= TaskListActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private ArrayList<DrawerItem> mDrawerItems;
    private String[] mDrawerTitle;
    private TypedArray mDrawerIcons;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList=(ListView)findViewById(R.id.left_drawer);
        mDrawerItems= new ArrayList<DrawerItem>();

        setDrawerItems();

        //set adapter for listView drawer
        mDrawerList.setAdapter(new DrawerAdapter(mDrawerItems));
        setUpDrawerToggle();

        //set list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListenner());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if(fragment == null){
            fragment = new TaskListFragment();
            fm.beginTransaction()
                    .add(R.id.container,fragment)
                    .commit();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListenner implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position){
        Fragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt(TaskListFragment.DRAWER_ITEM_CHOICE,position);
        fragment.setArguments(args);

        //Insert fragment by replacing any existing fragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container,fragment)
                .commit();

        //highlight the selected title
        mDrawerList.setItemChecked(position,true);
        setTitle(mDrawerTitle[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    private void setUpDrawerToggle(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setDrawerItems() {
        mDrawerTitle=getResources().getStringArray(R.array.drawer_menu_array);
        mDrawerIcons=getResources().obtainTypedArray(R.array.drawer_images);
        for(int i=0;i<mDrawerTitle.length;i++){
            mDrawerItems.add(new DrawerItem(mDrawerTitle[i],mDrawerIcons.getResourceId(i,0)));
        }

    }

    private class DrawerAdapter extends ArrayAdapter<DrawerItem>{

        public DrawerAdapter(ArrayList<DrawerItem> items){
            super(TaskListActivity.this,0,items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= getLayoutInflater().inflate(R.layout.drawer_list_item,null);
            }
            DrawerItem item = getItem(position);

            TextView t = (TextView)convertView.findViewById(R.id.drawer_item);
            t.setText(item.getTitle());

            ImageView imageView =(ImageView)convertView.findViewById(R.id.drawer_item_image);
            imageView.setImageResource(item.getImage());

            return convertView;
        }
    }

}