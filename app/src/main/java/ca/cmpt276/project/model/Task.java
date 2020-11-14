package ca.cmpt276.project.model;

import java.util.HashMap;
import java.util.Map;

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
