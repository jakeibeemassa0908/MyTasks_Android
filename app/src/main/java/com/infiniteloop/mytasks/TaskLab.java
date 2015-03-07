package com.infiniteloop.mytasks;

import java.util.ArrayList;

/**
 * Created by theotherside on 07/03/15.
 */
public class TaskLab {

    private static TaskLab sTaskLab;
    private ArrayList<Task> mTasks;

    private TaskLab(){
        mTasks= new ArrayList<Task>();
        Task task = new Task("Chicken in Oven",1,"Kitchen","Private");
        mTasks.add(task);
    }

    public static TaskLab get(){
        if(sTaskLab == null){
            sTaskLab= new TaskLab();
        }
        return sTaskLab;
    }

    public ArrayList<Task> getTasks(){
        return mTasks;
    }
}
