package ca.cmpt276.project.model;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class TaskManager implements Iterable<Task> {
    private static TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    private List<Task> taskList;

    private TaskManager() {
        taskList = new ArrayList<>();
    }

    public void add(Task task) {
        if (!hasTask(task)) {
            taskList.add(task);
        }
    }

    public Task get(int i) {
        return taskList.get(i);
    }

    public boolean hasTask(Task rhs) {
        for (Task task: taskList) {
            if (task.equals(rhs)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public Iterator<Task> iterator() {
        return taskList.iterator();
    }
}
