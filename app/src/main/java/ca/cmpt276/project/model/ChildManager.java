package ca.cmpt276.project.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Singleton Class that stores and manipulates an Array List of Class Child.
 * Called on by any class to update/edit child list.
 * Eg: UI.ChildManagerActivity uses to display/edit child list.
 */
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

    public void setList(ChildManager manager){
        children = manager.getList();

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

    public void deleteByObject(Child child) {
        children.remove(child);
    }

    public Child get(int index){
        return children.get(index);
    }

    public List<Child >getList(){
        return children;
    }

    public Child getChildByName(String name) {
        if (name == null) {
            return null;
        }
        for (Child child: children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    public boolean isChildNameExist(String name) {
        return getChildByName(name) != null;
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

    public Child childOffer(){
        Child selectedChild = children.get(0);

        int index = 0;
        for(int i =0; i < size(); i++){
            if(selectedChild.getTimesToPick() > get(i).getTimesToPick()){
                selectedChild = get(i);
                index = i;
            }
        }
        children.get(index).updateTimesToPick();
        return selectedChild;
    }

    @Override
    public Iterator<Child> iterator() {
        return children.iterator();
    }
}
