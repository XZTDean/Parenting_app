package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;
import ca.cmpt276.project.model.CoinFlip;


public class MainActivity extends AppCompatActivity {

    ChildManager childManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        childManager = ChildManager.getInstance();
        loadData();

        Button timeoutTimer = findViewById(R.id.timeoutTimer);
        timeoutTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TimeoutTimerUI.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        /*
        The following is an outline of how the buttons in activity_main could
        be used. Feel free to edit and remove.
        */

        Button configureChildren = findViewById(R.id.configureChildren);
        configureChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildManagerActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        Button coinFlip = findViewById(R.id.coinFlip);
        coinFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =FlipCoinScreen.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });/*

        Button timeoutTimer = findViewById(R.id.timeoutTimer);
        timeoutTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildManager.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

         */

    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("AppPreference", MODE_PRIVATE);
        String json = prefs.getString(childManager.CHILD_KEY, "[]");
        childManager.loadFromJson(json);
    }
}