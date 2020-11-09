package ca.cmpt276.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.CoinFlip;
import ca.cmpt276.project.model.CoinFlipStats;

public class flipHistory extends AppCompatActivity {

    private CoinFlip coinFlipManager = CoinFlip.getInstance();
    List<CoinFlipStats> myList = coinFlipManager.getList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);

        //Add a case to Test if populateListView works. Will delete later.
        CoinFlipStats flip1 = new CoinFlipStats("timedate","null",0,0,0);
        flip1.flipCoin("Tom",2);
        CoinFlip.getInstance().addStats(flip1);



        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<CoinFlipStats> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.FlipListView);
        list.setAdapter(adapter);
    }
    private class myListAdapter extends ArrayAdapter<CoinFlipStats> {
        public myListAdapter(){
            super(flipHistory.this,R.layout.flip_items,myList );
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.flip_items,parent,false);
            }
            //find the coinFlipStats

            CoinFlipStats CurrentStats = myList.get(position);

            ImageView imageView =(ImageView)itemView.findViewById(R.id.imageView);
            imageView.setImageResource(CurrentStats.getIconID());







            return itemView;



            //return super.getView(position, convertView, parent);
        }
    }





    public static Intent makeIntent(Context context) {
        return new Intent(context, flipHistory.class);
    }





}