package ca.cmpt276.project.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChildManager implements Iterable<Child> {

    private List<Child> children;
    public final String CHILD_KEY = "ChildList";
    /*
     Singleton Support
     */
    private static ChildManager instance;
    private ChildManager(){
        children = new ArrayList<Child>();
    }
    public static ChildManager getInstance(){
        if (instance == null){
            instance = new ChildManager();
        }
        return instance;
    }

    public int size() {
        return children.size();
    }

    public void add(Child child){
        children.add(child);
    }

    public void editChildName(int index, String name){ //Maybe just editChild
        children.get(index).setName(name);
    }

    public void delete(int index){
        children.remove(index);
    }

    public Child get(int index){
        return children.get(index);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(children);
    }

    public void loadFromJson(String json) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<Child>>(){}.getType();
        children = gson.fromJson(json, collectionType);
    }

    public String childOffer(){
        Child selectedChild = children.get(0);
        for(Child c: children){
            if(c.getTimesToPick() < selectedChild.getTimesToPick()){
                selectedChild = c;
            }
        }
        selectedChild.updateTimesToPick();
        return selectedChild.getName();
    }

    @Override
    public Iterator<Child> iterator() {
        return children.iterator();
    }
}
