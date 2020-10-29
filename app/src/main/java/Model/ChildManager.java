package Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class ChildManager extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private ArrayList<Child> Children;

    /*
     Singleton Support
     */
    private static ChildManager instance;
    private ChildManager(){
        Children = new ArrayList<Child>();
    }
    public static ChildManager getInstance(){
        if (instance == null){
            instance = new ChildManager();
        }
        return instance;
    }

    public void addChild(Child child){
        Children.add(child);
    }

    public void editChildName(int index, String name){ //Maybe just editChild
        Children.get(index).setName(name);
    }

    public void deleteChild(int index){
        Children.remove(index);
    }

    public Child getChild(int index){
        return Children.get(index);
    }

    private Set<String> getsChildrenSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("childrenSharedPreferences", Context.MODE_PRIVATE);
        Set<String> extractedChild = prefs.getStringSet("children", null);
        return extractedChild;
    }

    private void storeChildrenSharedPreferences(int gophersInput){
        SharedPreferences prefs = getSharedPreferences("childrenSharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("children", gophersInput);
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
