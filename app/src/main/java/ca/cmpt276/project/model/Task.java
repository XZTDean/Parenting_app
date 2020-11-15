package ca.cmpt276.project.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This Task class store the info of task name & description
 * & times each child finish the task, and the current assigned
 * child for this task.
 */
public class Task {
    private String name;
    private String description;
    private final Map<String, Integer> taskTaken;
    private String current; // use string to prevent store too much useless info when save in JSON
    private String previous;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        taskTaken = new HashMap<>();
        addChildToMap();
        current = previous = null;
    }

    private void addChildToMap() {
        ChildManager manager = ChildManager.getInstance();
        for (Child child: manager) {
            String childName = child.getName();
            if (!taskTaken.containsKey(childName)) {
                taskTaken.put(childName, 0);
            }
        }
    }

    public Child getNext() {
        ChildManager manager = ChildManager.getInstance();

        if (current == null || !manager.isChildNameExist(current)) {
            updateAssign();
        }
        return manager.getChildByName(current);
    }

    private void updateAssign() {
        addChildToMap();
        current = null;
        if (taskTaken.isEmpty()) {
            return; // No child cannot update
        }

        ChildManager manager = ChildManager.getInstance();
        List<String> potential = new ArrayList<>();
        Collection<Integer> values = taskTaken.values();

        while (potential.isEmpty() && !values.isEmpty()) {
            int min = Collections.min(values);
            for (Map.Entry<String, Integer> entry : taskTaken.entrySet()) {
                if (entry.getValue() == min && manager.isChildNameExist(entry.getKey())
                    && !entry.getKey().equals(previous)) {
                    potential.add(entry.getKey());
                }
            }

            // Clean the min value if the child of min value is not exist
            if (potential.isEmpty()) {
                while (values.contains(min)) {
                    values.remove(min);
                }
            }
        }

        if (potential.isEmpty()) {
            if (manager.isChildNameExist(previous)) { // no other choice other than previous child
                current = previous;
            }
        } else {
            int random = new Random().nextInt(potential.size());
            this.current = potential.get(random);
        }
    }

    public void finishTask(Child child) {
        String childName = child.getName();
        Integer times = taskTaken.get(childName);
        if (times == null) {
            times = 1;
        } else {
            taskTaken.remove(childName);
            times++;
        }
        taskTaken.put(childName, times);
        if (childName.equals(current)) {
            current = null;
            previous = childName;
        }
    }

    public Child changeNext() {
        current = null;
        updateAssign();
        return ChildManager.getInstance().getChildByName(current);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
