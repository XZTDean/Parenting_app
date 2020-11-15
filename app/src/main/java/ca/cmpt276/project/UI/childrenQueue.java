package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;
import ca.cmpt276.project.model.CoinFlipStats;

public class childrenQueue extends AppCompatActivity {


    private ChildManager manager ;
    private List<Child> childQueue = new ArrayList<>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_queue);
        manager = ChildManager.getInstance();
        popQueue(childQueue);
        populateListView();







    }
    private void populateListView() {
        ArrayAdapter<Child> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.children_queue);
        list.setAdapter(adapter);
    }

    private class myListAdapter extends ArrayAdapter<Child> {
        public myListAdapter(){
            super(childrenQueue.this,R.layout.queue_items,childQueue );
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



            return itemView;


        }
    }

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






    public static Intent makeIntent(Context context) {
        return new Intent(context, childrenQueue.class);
    }

}