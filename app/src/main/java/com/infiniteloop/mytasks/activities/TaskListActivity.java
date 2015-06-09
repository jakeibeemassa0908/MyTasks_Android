package com.infiniteloop.mytasks.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.infiniteloop.mytasks.fragments.AboutFragment;
import com.infiniteloop.mytasks.DrawerItem;
import com.infiniteloop.mytasks.fragments.CheckListFragment;
import com.infiniteloop.mytasks.fragments.Check_ListFragment;
import com.infiniteloop.mytasks.fragments.FeedbackFragment;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskLab;
import com.infiniteloop.mytasks.fragments.NoteListFragment;
import com.infiniteloop.mytasks.fragments.TaskListFragment;
import com.infiniteloop.mytasks.data.Category;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.tab.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskListActivity extends ActionBarActivity {

    private static final int POSITION_ABOUT=9;
    private static final int POSITION_FEEDBACK=8;
    private static final int POSITION_SETTINGS=7;
    private static final int POSITION_CREATE_CATEGORY=11;
    private static final int CAT_INDICATOR=100;


    private static final String TAG= TaskListActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private ArrayList<DrawerItem> mDrawerItems;
    private Toolbar mToolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    FrameLayout mFrameLayout;


    private int mPosition;

    private Map<String,Integer> mDrawerMapping = new HashMap<String, Integer>();


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

        //get frameLayout
        mFrameLayout = (FrameLayout)findViewById(R.id.container);

        mToolbar = (Toolbar)findViewById(R.id.app_bar);
        //mToolbar.setElevation(5);
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mPager=(ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);

        //set list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListenner());

        getSupportActionBar().setElevation(0);

        selectItem(0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TaskListFragment.DELETE_CAT_REQUEST:
                if(resultCode== Activity.RESULT_CANCELED)
                refreshDrawerList(0);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(mDrawerLayout!=null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mDrawerLayout!=null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if(mDrawerLayout!=null)
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListenner implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //if view is a title, dont set listenner
            if(view.findViewById(R.id.drawer_title)!=null){

            }else{
                TextView text = (TextView)view.findViewById(R.id.drawer_item);
                String title = text.getText().toString();
                selectItem(mDrawerMapping.get(title));
            }
        }
    }

    /**
     * Function called when item in the Navigation Drawer are called
     * @param position
     */
        private void selectItem(int position){
        Fragment fragment = new TaskListFragment();
        switch (position){
            case POSITION_ABOUT:
                startActivity(new Intent(this,AboutActivity.class));
                return;
            case POSITION_FEEDBACK:
                startActivity(new Intent(this,FeedbackActivity.class));
                return;
            case POSITION_SETTINGS:
                startActivity(new Intent(this,SettingsActivity.class));
                return;
            case 0:

                mToolbar.setVisibility(View.VISIBLE);
                mPager.setVisibility(View.VISIBLE);
                mFrameLayout.setVisibility(View.GONE);

                setTitle(getString(R.string.app_name));


                mDrawerList.setItemChecked(position, true);
                if (mDrawerLayout!=null)
                    mDrawerLayout.closeDrawer(mDrawerList);
                mPosition=position;
                return;
            default:
                mToolbar.setVisibility(View.GONE);
                mPager.setVisibility(View.GONE);
                mFrameLayout.setVisibility(View.VISIBLE);

                Bundle args = new Bundle();
                args.putInt(TaskListFragment.DRAWER_ITEM_CHOICE,position);
                fragment.setArguments(args);
        }

        //Insert fragment by replacing any existing fragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container,fragment)
                .commit();

        //highlight the selected title
        mDrawerList.setItemChecked(position,true);
        //set the actionbat title to the drawer item title
        setTitle(getKeyByValue(mDrawerMapping,position));
        if (mDrawerLayout!=null)
            mDrawerLayout.closeDrawer(mDrawerList);

        //Create new category from drawer -- No new Fragment needed
        if(position==POSITION_CREATE_CATEGORY){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.add_category));
            final EditText newCategory = new EditText(this);
            dialog.setView(newCategory);
            dialog.setPositiveButton(getString(R.string.add),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String mAddCategory=newCategory.getText().toString();
                    if(!mAddCategory.matches("")){
                        mAddCategory=mAddCategory.trim();
                        long id= TaskLab.get(TaskListActivity.this).insertCategory(mAddCategory);
                        refreshDrawerList((int)(id +CAT_INDICATOR));
                    }

                }
            });
            dialog.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }

        mPosition=position;
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    private void setUpDrawerToggle(){
        if (mDrawerLayout!=null) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                  /* host Activity */
                    mDrawerLayout,         /* DrawerLayout object */
                    R.string.drawer_open,  /* "open drawer" description */
                    R.string.drawer_close  /* "close drawer" description */
            ) {

                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);

                }

                /**
                 * Called when a drawer has settled in a completely open state.
                 */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                }
            };

            // Set the drawer toggle as the DrawerListener
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    /**
     * Populate Navigation Drawer items
     */
    private void setDrawerItems() {
        ArrayList<Category> categoriesList= TaskLab.get(this).getCategories();
        DrawerItem allTask,allReminder,vhPriority,hPriority,
                nPriority,lPriority,completed,newCategory,settings,
                feedback,about,help,more,categories,priorities;

        allTask=new DrawerItem(getString(R.string.all_task),R.drawable.ic_action_action_home);
        mDrawerItems.add(allTask);
        mDrawerMapping.put(allTask.getTitle(),0);

        allReminder=new DrawerItem(getString(R.string.timed_task),R.drawable.ic_action_device_add_alarm);
        mDrawerItems.add(allReminder);
        mDrawerMapping.put(allReminder.getTitle(),1);

        completed=new DrawerItem(getString(R.string.completed_task),R.drawable.ic_action_maps_beenhere);
        mDrawerItems.add(completed);
        mDrawerMapping.put(completed.getTitle(),2);

        priorities= new DrawerItem(getString(R.string.priorities),0);
        priorities.setType(DrawerItem.TYPE_TITLE);
        mDrawerItems.add(priorities);

        vhPriority=new DrawerItem(getString(R.string.very_high_priority),R.drawable.ic_action_image_looks_one);
        vhPriority.setPriority(Task.VERY_HIGH_PRIORITY);
        mDrawerItems.add(vhPriority);
        mDrawerMapping.put(vhPriority.getTitle(),3);

        hPriority=new DrawerItem(getString(R.string.high_priority),R.drawable.ic_action_image_looks_two);
        hPriority.setPriority(Task.HIGH_PRIORITY);
        mDrawerItems.add(hPriority);
        mDrawerMapping.put(hPriority.getTitle(),4);

        nPriority=new DrawerItem(getString(R.string.normal_priority),R.drawable.ic_action_image_looks_3);
        nPriority.setPriority(Task.NORMAL_PRIORITY);
        mDrawerItems.add(nPriority);
        mDrawerMapping.put(nPriority.getTitle(),5);

        lPriority=new DrawerItem(getString(R.string.low_priority),R.drawable.ic_action_image_looks_4);
        lPriority.setPriority(Task.LOW_PRIORITY);
        mDrawerItems.add(lPriority);
        mDrawerMapping.put(lPriority.getTitle(),6);

        categories= new DrawerItem(getString(R.string.categories),0);
        categories.setType(DrawerItem.TYPE_TITLE);
        mDrawerItems.add(categories);

        //Categories drawer Items
        for(int j=0;j<categoriesList.size();j++){
            Category cat =categoriesList.get(j);
            String title = cat.getCategoryName();
            mDrawerItems.add(new DrawerItem(title,R.drawable.ic_action_action_description));

            //set the category mapping to be the id of the category + 100
            int assignedNumber=(int)cat.getId()+CAT_INDICATOR;
            mDrawerMapping.put(title,assignedNumber);
        }

        newCategory=new DrawerItem(getString(R.string.add_category),R.drawable.ic_action_content_add);
        mDrawerItems.add(newCategory);
        mDrawerMapping.put(newCategory.getTitle(),11);


        more= new DrawerItem(getString(R.string.more),0);
        more.setType(DrawerItem.TYPE_TITLE);
        mDrawerItems.add(more);


        feedback=new DrawerItem(getString(R.string.feedback),R.drawable.ic_action_action_stars);
        mDrawerItems.add(feedback);
        mDrawerMapping.put(feedback.getTitle(),POSITION_FEEDBACK);

        about=new DrawerItem(getString(R.string.about),R.drawable.ic_action_editor_insert_emoticon);
        mDrawerItems.add(about);
        mDrawerMapping.put(about.getTitle(),POSITION_ABOUT);

        settings=new DrawerItem(getString(R.string.settings),R.drawable.ic_action_action_settings);
        mDrawerItems.add(settings);
        mDrawerMapping.put(settings.getTitle(),POSITION_SETTINGS);

    }

    /**
     * Refresh the navigation drawer so that new item can appear
     * @param id the id of the section of the drawer that should display after the refresh
     */
    public void refreshDrawerList(int id){
        mDrawerItems=new ArrayList<DrawerItem>();
        setDrawerItems();
        mDrawerList.setAdapter(null);
        mDrawerList.setAdapter(new DrawerAdapter(mDrawerItems));
        //show home task list
        selectItem(id);
    }

    private class DrawerAdapter extends ArrayAdapter<DrawerItem>{

        public DrawerAdapter(ArrayList<DrawerItem> items){
            super(TaskListActivity.this,0,items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DrawerItem item = getItem(position);
            if(convertView==null){
                //check whether it is a title of a item
                if(item.getType()==DrawerItem.TYPE_TITLE){
                    convertView=getLayoutInflater().inflate(R.layout.drawer_title,null);
                }else{
                    convertView= getLayoutInflater().inflate(R.layout.drawer_list_item,null);
                }
            }else{
                //check again whether it is a title or an item when the review is returned
                if(item.getType()==DrawerItem.TYPE_TITLE){
                    convertView=getLayoutInflater().inflate(R.layout.drawer_title,null);
                }else{
                    convertView= getLayoutInflater().inflate(R.layout.drawer_list_item,null);
                }
            }


            if(item.getType()==DrawerItem.TYPE_TITLE){
                TextView text = (TextView)convertView.findViewById(R.id.drawer_title);
                if(text!=null) {
                    text.setText(item.getTitle());
                }
            }else{
                //Set text of drawer item
                TextView text = (TextView)convertView.findViewById(R.id.drawer_item);
                if(text!=null){
                    text.setText(item.getTitle());
                }

                //Set drawer item image
                ImageView imageView =(ImageView)convertView.findViewById(R.id.drawer_item_image);
                if(imageView!=null){
                    imageView.setImageResource(item.getImage());
                }
            }

            //check if the clicked text equal the select one
            if(item.getType()!=DrawerItem.TYPE_TITLE){
                String title=((TextView)convertView.findViewById(R.id.drawer_item)).getText().toString();
                if(mDrawerMapping.get(title) == mPosition){
                    convertView.setBackgroundColor(getResources().getColor(R.color.light_grey));
                }else{
                    convertView.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }

            return convertView;
        }
    }


    //Get the key from the value
    public static String getKeyByValue(Map<String, Integer> map, int value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (value==entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    class MyPagerAdapter extends FragmentPagerAdapter{

        String [] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position){
                case 0:
                    fragment = new TaskListFragment();
                    Bundle args = new Bundle();
                    args.putInt(TaskListFragment.DRAWER_ITEM_CHOICE, 0);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new NoteListFragment();
                    break;
                case 2:
                    fragment = new Check_ListFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}