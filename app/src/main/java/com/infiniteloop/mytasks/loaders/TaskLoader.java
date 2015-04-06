package com.infiniteloop.mytasks.loaders;

import android.content.Context;

import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskLab;

/**
 * Created by theotherside on 14/03/15.
 */
public class TaskLoader extends DataLoader<Task> {
    private long mTaskId;

    public TaskLoader(Context context,long taskId){
        super(context);
        mTaskId=taskId;
    }

    @Override
    public Task loadInBackground() {
        return TaskLab.get(getContext()).queryTask(mTaskId);
    }
}
