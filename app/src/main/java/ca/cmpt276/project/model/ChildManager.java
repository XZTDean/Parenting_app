package ca.cmpt276.project.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Singleton Class that stores and manipulates an Array List of Class Child.
 * Called on by any class to update/edit child list.
 * Eg: UI.ChildManagerActivity uses to display/edit child list.
 */
public class ChildManager implements Iterable<Child> {

    private List<Child> children;
    private List<Child> childrenQueue;
    private Child childPlaying;
    private Child recentChildPlayed;
    public final String CHILD_KEY = "ChildList";
    /*
     Singleton Support
     */
    private static ChildManager instance;
    private ChildManager(){
        children = new ArrayList<Child>();
        childrenQueue = new ArrayList<Child>();
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

    public boolean add(Child child){
        for (Child c : children) {
            if (c.equals(child)) {
                return false;
            }
        }
        children.add(child);
        return true;
    }

    public boolean editChildName(int index, String name) {
        if (children.get(index).getName().equals(name)) {
            return true;
        }
        for (Child child : children) {
            if (child.getName().equals(name)) {
                return false;
            }
        }
        children.get(index).setName(name);
        return true;
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

    public List<Child> getChildrenQueue(){
        return childrenQueue;
    }

    public Child getRecentChildPlayed() {
        return recentChildPlayed;
    }

    public void setRecentChildPlayed(Child recentChildPlayed) {
        this.recentChildPlayed = recentChildPlayed;
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

    public void resetRecentlyPlayedChild(){
        for (Child child : children){
            child.setRecentlyPlayed(0);
        }
    }

    public void populateChildrenQueue(){
        List<Child> temp = new ArrayList<Child>(children);
        Collections.sort(temp);

        int size = temp.size();
        for(int i = 0; i < size; i++){
            Child tempChild = temp.get(0);

            for(int j =0; j < temp.size(); j++){
                if(tempChild.getTimesToPick() > temp.get(j).getTimesToPick()){
                    tempChild = temp.get(j);
                }
            }
            childrenQueue.add(tempChild);
            temp.remove(tempChild);
        }
    }

    public void deleteChildrenQueue(){
        childrenQueue.removeAll(childrenQueue);
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

    public  Child getChildPlaying(){
        return childPlaying;
    }

    public void setChildPlaying(Child child){
        childPlaying = child;
    }

    public void updateChildPlayingTimesToPick(){
        childPlaying.updateTimesToPick();
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
        childPlaying = selectedChild;
        return selectedChild;
    }


    @Override
    public Iterator<Child> iterator() {
        return children.iterator();
    }
}
