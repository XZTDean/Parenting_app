package ca.cmpt276.project.UI.FlipCoin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.COIN_FLIP.CoinFlip;
import ca.cmpt276.project.model.COIN_FLIP.CoinFlipStats;

public class FlipHistory extends AppCompatActivity {

    private static final String CHILD_PLAYING = "childPlaying";
    private final CoinFlip coinFlipManager = CoinFlip.getInstance();
    private List<CoinFlipStats> myList = coinFlipManager.getList();
    private boolean toggleChildOnlyHistory = false;
    private String childPlayingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);

        populateListView();
        saveToDisk();
        setToolbar();
        childPlayingName = getIntent().getStringExtra(CHILD_PLAYING);

        if(childPlayingName != null){
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

        toggleHistoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toggleChildOnlyHistory){
                    toggleChildOnlyHistory = true;
                    myList = coinFlipManager.getSpecificChild(childPlayingName);
                    populateListView();
                    toggleHistoryView.setText(childPlayingName);
                }
                else{
                    toggleChildOnlyHistory = false;
                    myList = coinFlipManager.getList();
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
            super(FlipHistory.this,R.layout.flip_items,myList );
        }

        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {

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
            String outputHistory = getString(R.string.flip_history_display,
                    CurrentStats.getChildName(), interpretInt(CurrentStats.getChoice()));
            makeText1.setText(outputHistory);



            try {
                Bitmap photo = CurrentStats.getChildPortrait();
                ImageView childIcon = itemView.findViewById(R.id.child_icon_history);
                if(childIcon != null && photo != null) {
                    childIcon.setImageBitmap(photo);
                }
            } catch(Exception ignored) {

            }

            return itemView;

        }
    }



    public static Intent makeIntent(Context context, String childName) {
        Intent intent = new Intent(context, FlipHistory.class);
        intent.putExtra(CHILD_PLAYING, childName);
        return intent;
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
            return getString(R.string.head);
        }
        else{
            return getString(R.string.tail);
        }
    }

}
