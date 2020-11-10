package ca.cmpt276.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.CoinFlip;
import ca.cmpt276.project.model.CoinFlipStats;

public class flipHistory extends AppCompatActivity {

    private CoinFlip coinFlipManager = CoinFlip.getInstance();
    List<CoinFlipStats> myList = coinFlipManager.getList();
    private int listSize;
    private boolean toggleChildOnlyHistory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);

        listSize = getIntent().getIntExtra("listSize", 0);
        populateListView();
        saveToDisk();
        setToolbar();
        if(listSize != 0){
            setupToggleButton();
        }
        else{
            hideToggleButton();
        }
    }

    private void hideToggleButton() {
        Button toggleHistoryView = (Button) findViewById(R.id.buttonToggleHistory);
        toggleHistoryView.setVisibility(View.GONE);
    }

    private void setupToggleButton() {
        Button toggleHistoryView = (Button) findViewById(R.id.buttonToggleHistory);
        String childPlayingName = getIntent().getStringExtra("childPlaying");

        toggleHistoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toggleChildOnlyHistory){
                    toggleChildOnlyHistory = true;
                    populateListView();
                    toggleHistoryView.setText(childPlayingName);
                }
                else{
                    toggleChildOnlyHistory = false;
                    populateListView();
                    toggleHistoryView.setText(R.string.all);
                }
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarFlip);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }


    //populate the information of each flip.
    private void populateListView() {
        ArrayAdapter<CoinFlipStats> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.FlipListView);
        list.setAdapter(adapter);
    }

    private class myListAdapter extends ArrayAdapter<CoinFlipStats> {
        public myListAdapter(){
            super(flipHistory.this,R.layout.flip_items,myList );
        }

        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            /*View itemView = convertView;
            //String testOutPut = myList.get(position).getChildName() + " chose " + interpretInt(myList.get(position).getChoice());
            //if(testOutPut.isEmpty())

            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.flip_items,parent,false);
            }
            CoinFlipStats CurrentStats;
            //find the coinFlipStats
            //if(toggleChildOnlyHistory && (myList.get(position).getChildName().compareTo(getIntent().getStringExtra("childPlaying"))) == 0 ){
            CurrentStats = myList.get(position); // get the information of flip No.position

            if(toggleChildOnlyHistory) {
                if ((myList.get(position).getChildName().compareTo(getIntent().getStringExtra("childPlaying"))) == 0) {
                    ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
                    imageView.setImageResource(CurrentStats.getIconID());

                    TextView makeText = (TextView) itemView.findViewById(R.id.textView1);
                    makeText.setText(CurrentStats.getFlipTime());

                    TextView makeText1 = (TextView) itemView.findViewById(R.id.textView2);
                    String outputHistory = CurrentStats.getChildName() + " chose " + interpretInt(CurrentStats.getChoice());
                    makeText1.setText(outputHistory);
                }
            }
            else{
                Log.d("HIHIHIHI", "Entered unfiltered part: "+CurrentStats.getChildName());
                ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
                imageView.setImageResource(CurrentStats.getIconID());

                TextView makeText = (TextView) itemView.findViewById(R.id.textView1);
                makeText.setText(CurrentStats.getFlipTime());

                TextView makeText1 = (TextView) itemView.findViewById(R.id.textView2);
                String outputHistory = CurrentStats.getChildName() + " chose " + interpretInt(CurrentStats.getChoice());
                makeText1.setText(outputHistory);
            }

            return itemView;
        }*/



            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.flip_items,parent,false);
            }
            //find the coinFlipStats
            CoinFlipStats CurrentStats = myList.get(position);

            ImageView imageView =(ImageView)itemView.findViewById(R.id.imageView);
            imageView.setImageResource(CurrentStats.getIconID());

            TextView makeText = (TextView) itemView.findViewById(R.id.textView1);
            makeText.setText(CurrentStats.getFlipTime());

            TextView makeText1 = (TextView) itemView.findViewById(R.id.textView2);
            String outputHistory = CurrentStats.getChildName()+" chose "+ interpretInt(CurrentStats.getChoice());
            makeText1.setText(outputHistory);

            return itemView;


        }
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, flipHistory.class);
    }
    //use SharedPreferences to store history and reload the next time user open the app.
    private void saveToDisk() {
        SharedPreferences prefs = this.getSharedPreferences("AppPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(coinFlipManager.Flip_KEY, coinFlipManager.toJson());
        editor.apply();
    }


    //interpret choice and result as Strings "Head" or "tails".
    public String interpretInt(int choiceOrresult){
        if(choiceOrresult ==1){
            return "head";
        }
        else{
            return "tail";
        }
    }

}