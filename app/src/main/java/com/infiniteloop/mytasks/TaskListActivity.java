package com.infiniteloop.mytasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.infiniteloop.mytasks.data.Category;
import com.infiniteloop.mytasks.data.SQLiteCursorLoader;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskListActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int POSITION_ABOUT=9;
    private static final int POSITION_CREATE_CATEGORY=11;

    private static final String TAG= TaskListActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private ArrayList<DrawerItem> mDrawerItems;

    private int mPosition;
    private ArrayList<Category>mCategories= new ArrayList<Category>();

    private Map<String,Integer> mDrawerMapping = new HashMap<String, Integer>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getSupportLoaderManager().initLoader(0, null, this);

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

        selectItem(0);

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

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CategoryListCursorLoader(this);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> cursorLoader) {

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

    private void selectItem(int position){

        Fragment fragment = new TaskListFragment();;
        switch (position){
            case POSITION_ABOUT:
                fragment=new AboutFragment();
                break;
            default:
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
        mDrawerLayout.closeDrawer(mDrawerList);

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
                        TaskLab.get(TaskListActivity.this).insertCategory(mAddCategory);
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
        TaskDataBaseHelper.CategoryCursor categoryCursor = TaskLab.get(this).getCategories();;
        if(categoryCursor!=null){
            categoryCursor.moveToFirst();
            for(int i=0;i<categoryCursor.getCount();i++){
                mCategories.add(categoryCursor.getCategory());
                categoryCursor.moveToNext();
            }
        }
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
        for(int j=0;j<mCategories.size();j++){
            Category cat =mCategories.get(j);
            String title = cat.getCategoryName();
            mDrawerItems.add(new DrawerItem(title,R.drawable.ic_action_action_description));

            //set the category mapping to be the id of the category + 100
            int assignedNumber=(int)cat.getId()+100;
            mDrawerMapping.put(title,assignedNumber);
        }

        newCategory=new DrawerItem(getString(R.string.add_category),R.drawable.ic_action_content_add);
        mDrawerItems.add(newCategory);
        mDrawerMapping.put(newCategory.getTitle(),11);


        more= new DrawerItem(getString(R.string.more),0);
        more.setType(DrawerItem.TYPE_TITLE);
        mDrawerItems.add(more);

        settings=new DrawerItem(getString(R.string.settings),R.drawable.ic_action_action_settings);
        mDrawerItems.add(settings);
        mDrawerMapping.put(settings.getTitle(),7);

        feedback=new DrawerItem(getString(R.string.feedback),R.drawable.ic_action_action_stars);
        mDrawerItems.add(feedback);
        mDrawerMapping.put(feedback.getTitle(),8);

        about=new DrawerItem(getString(R.string.about),R.drawable.ic_action_editor_insert_emoticon);
        mDrawerItems.add(about);
        mDrawerMapping.put(about.getTitle(),9);

        help=new DrawerItem(getString(R.string.help),R.drawable.ic_action_question);
        mDrawerItems.add(help);
        mDrawerMapping.put(help.getTitle(),10);

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

//                if(mDrawerMapping.get(title) == mPosition){
//                    convertView.setBackgroundColor(getResources().getColor(R.color.grey));
//                }else{
//                    convertView.setBackgroundColor(getResources().getColor(R.color.white));
//                }
            }

            return convertView;
        }
    }

    //TODO create category loader and load categories
    public static class CategoryListCursorLoader extends SQLiteCursorLoader {

        public CategoryListCursorLoader(Context context){
            super (context);
        }

        @Override
        protected Cursor loadCursor() {
            //Query the list of runs
            return TaskLab.get(getContext()).getCategories();
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

}