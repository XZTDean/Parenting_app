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

/**
 * App to flip coins to settle disputes among children
 * Has Screen to configure Children, Flip the coin and a Timeout Timer
 * Saves Data using json and Shared Preferences
 */
public class MainActivity extends AppCompatActivity {

    ChildManager childManager;
    CoinFlip coinFlipManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        childManager = ChildManager.getInstance();
        coinFlipManager = CoinFlip.getInstance();
        loadData();

        setupButtons();

    }

    private void setupButtons() {
        Button timeoutTimer = findViewById(R.id.timeoutTimer);
        timeoutTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TimeoutTimerUI.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button coinFlip = findViewById(R.id.coinFlip);
        coinFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlipCoinScreen.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button configureChildren = findViewById(R.id.configureChildren);
        configureChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskListActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("AppPreference", MODE_PRIVATE);
        String json = prefs.getString(childManager.CHILD_KEY, "[]");
        String json1 = prefs.getString(coinFlipManager.Flip_KEY,"[]");
        childManager.loadFromJson(json);
        coinFlipManager.loadFromJson(json1);
    }
}