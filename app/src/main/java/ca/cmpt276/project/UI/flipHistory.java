package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.CoinFlip;
import ca.cmpt276.project.model.CoinFlipStats;

public class flipHistory extends AppCompatActivity {

    private CoinFlip coinFlipManager = CoinFlip.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);

        //Add a case to Test if populateListView works. Will delete later.
        CoinFlipStats flip1 = new CoinFlipStats("timedate","null",0,0);
        flip1.flipCoin("Tom",2);
        CoinFlip.getInstance().addStats(flip1);



        populateListView();
    }


    private void populateListView() {
        String[] history = coinFlipManager.StoreInList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.flip_items,
                history);
        ListView list =findViewById(R.id.FlipListView);
        list.setAdapter(adapter);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, flipHistory.class);
    }





}