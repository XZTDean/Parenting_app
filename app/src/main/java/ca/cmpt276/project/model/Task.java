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
        initializeMap();
    }

    private void initializeMap() {
        ChildManager manager = ChildManager.getInstance();
        for (Child child: manager) {
            taskTaken.put(child.getName(), 0);
        }
    }

    public String getNext() {
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
        return potential.get(random);
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
