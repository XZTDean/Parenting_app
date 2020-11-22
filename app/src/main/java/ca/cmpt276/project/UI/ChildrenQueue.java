package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        Toolbar toolbar = findViewById(R.id.toolbarQ);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        manager = ChildManager.getInstance();
        popQueue();
        populateListView();
        clickList();
        clickNoChildOption();
    }

    private void clickNoChildOption() {
        Button noChild = (Button) findViewById(R.id.buttonNoChildSelected);
        noChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlipCoinScreen.makeIntent(ChildrenQueue.this);
                intent.putExtra(getString(R.string.no_child_playing), 1);
                startActivity(intent);
                finish();
            }
        });
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


    public void popQueue(){
         manager.deleteChildrenQueue();
         manager.populateChildrenQueue();
         childQueue = manager.getChildrenQueue();
    }

    //click list will lead to flip coin with a new child picking head or tail.
    private void clickList() {
        ListView listView = findViewById(R.id.children_queue);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChildrenQueue.this,childQueue.get(position).getName(),Toast.LENGTH_LONG).show();
                manager.setChildPlaying(childQueue.get(position));

                Intent intent = FlipCoinScreen.makeIntent(ChildrenQueue.this);
                intent.putExtra(getString(R.string.override_default), 1);
                startActivity(intent);
                finish();
            }
        });
    }



    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildrenQueue.class);
    }

}