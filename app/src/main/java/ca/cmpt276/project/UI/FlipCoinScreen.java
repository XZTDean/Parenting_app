package ca.cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.CoinFlip;
import ca.cmpt276.project.model.CoinFlipStats;

public class FlipCoinScreen extends AppCompatActivity {

    private CoinFlip coinFlipManager = CoinFlip.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setup pop up screen to choose Heads or tails


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Add a case to Test if populateListView works. Will delete later.
        CoinFlipStats flip1 = new CoinFlipStats("timedate","null",0,0,0);
        flip1.flipCoin("Tom",2);
        CoinFlipStats flip2 = new CoinFlipStats("timedate","null",0,0,0);
        flip2.flipCoin("Jerry",1);
        coinFlipManager.addStats(flip1);
        coinFlipManager.addStats(flip2);


        Button btn = findViewById(R.id.historyButton);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = flipHistory.makeIntent(FlipCoinScreen.this);
                startActivity(intent);
            }
        });



    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, FlipCoinScreen.class);
    }

}