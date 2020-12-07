package ca.cmpt276.project.model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * TaskManager store a list of tasks. You can add, get
 * and remove tasks here. It can convert its content to
 * JSON and get content from JSON.
 */
public class TaskManager implements Iterable<Task> {
    private static TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    private List<Task> taskList;
    public final String TASK_KEY = "TaskList";

    private TaskManager() {
        taskList = new ArrayList<>();
    }

    public boolean add(Task task) {
        if (!hasTask(task)) {
            taskList.add(task);
            return true;
        }
        return false;
    }

    public Task get(int i) {
        return taskList.get(i);
    }

    public boolean edit(int i, String name, String desc) {
        final Task tmp = new Task(name, desc);
        final Task target = taskList.get(i);

        if (target.equals(tmp)) {
            return true;
        }
        for (Task task : taskList) {
            if (task.equals(tmp)) {
                return false;
            }
        }
        target.setName(name);
        target.setDescription(desc);
        return true;
    }

    public void remove(int i) {
        taskList.remove(i);
    }

    public int size() {
        return taskList.size();
    }

    public boolean hasTask(Task rhs) {
        for (Task task: taskList) {
            if (task.equals(rhs)) {
                return true;
            }
        }
        return false;
    }

    public List<Task> getList() {
        return taskList;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(taskList);
    }

    public void loadFromJson(String json) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<Task>>(){}.getType();
        taskList = gson.fromJson(json, collectionType);
    }

    @NonNull
    @Override
    public Iterator<Task> iterator() {
        return taskList.iterator();
    }
}
