package ca.cmpt276.project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class ChildManager implements Iterable<Child> {

    private ArrayList<Child> children;

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

    public int sizeOfList(){
        int count = 0;
        for(Child c: Children){
            count++;
        }
        return count;
    }


    public void addChild(Child child){
        Children.add(child);
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

    public void loadData(ArrayList<Child> Children){
        this.children = Children;
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
