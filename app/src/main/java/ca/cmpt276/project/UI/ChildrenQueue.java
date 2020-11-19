package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;

public class ChildrenQueue extends AppCompatActivity {


    private ChildManager manager ;
    private List<Child> childQueue = new ArrayList<>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_queue);
        manager = ChildManager.getInstance();
        popQueue(childQueue);
        populateListView();
        clickList();

    }

    
    //populate the queue of children
    private void populateListView() {
        ArrayAdapter<Child> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.children_queue);
        list.setAdapter(adapter);
    }

    private class myListAdapter extends ArrayAdapter<Child> {
        public myListAdapter(){
            super(ChildrenQueue.this,R.layout.queue_items,childQueue );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.queue_items,parent,false);
            }
            //find the coinFlipStats
            Child CurrentChild = childQueue.get(position);



            TextView makeText = (TextView) itemView.findViewById(R.id.child_name);
            makeText.setText(CurrentChild.getName());

            TextView makeText1 = (TextView) itemView.findViewById(R.id.name_queue);
            makeText1.setText(String.valueOf(CurrentChild.getTimesToPick()));



            return itemView;


        }
    }

    //use linked-list to store each child such that the order follows the size of each child's timesToPick.
    public void popQueue(List<Child> childQueue){
         ChildManager tempManager = manager;
         tempManager.setList(manager);
         List<Child> tempList = tempManager.getList();
         int listSize = tempManager.size();
         for(int i = 0;i<listSize;i++){
             Child tempChild = tempManager.childOffer();
             childQueue.add(tempChild);
             tempManager.deleteByObject(tempChild);
         }
    }

    //click list will lead to flip coin with a new child picking head or tail.
    private void clickList() {
        ListView listView = findViewById(R.id.children_queue);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChildrenQueue.this,childQueue.get(position).getName(),Toast.LENGTH_LONG).show();
                //should be replaced with an intent which starts Flip Coin with an override choice instead of default child.
            }
        });
    }



    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildrenQueue.class);
    }

}