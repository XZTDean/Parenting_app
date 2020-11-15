package ca.cmpt276.project.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Task {
    private String name;
    private String description;
    private final Map<String, Integer> taskTaken;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        taskTaken = new HashMap<>();
        addChildToMap();
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
        addChildToMap();

        ChildManager manager = ChildManager.getInstance();
        List<String> potential = new ArrayList<>();
        Collection<Integer> values = taskTaken.values();

        while (potential.isEmpty()) {
            int min = Collections.min(values);
            for (Map.Entry<String, Integer> entry : taskTaken.entrySet()) {
                if (entry.getValue() == min && manager.isChildNameExist(entry.getKey())) {
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

        int random = new Random().nextInt(potential.size());
        String child = potential.get(random);
        return manager.getChildByName(name);
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
