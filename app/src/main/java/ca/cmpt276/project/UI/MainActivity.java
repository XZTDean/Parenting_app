package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.ChildManager;
import ca.cmpt276.project.model.CoinFlip;
import ca.cmpt276.project.model.TaskManager;

/**
 * App to flip coins to settle disputes among children
 * Has Screen to configure Children, Flip the coin and a Timeout Timer
 * Saves Data using json and Shared Preferences
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent intent = ChildManagerActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button taskManage = findViewById(R.id.task_manager);
        taskManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskListActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        ChildManager childManager = ChildManager.getInstance();
        CoinFlip coinFlipManager = CoinFlip.getInstance();
        TaskManager taskManager = TaskManager.getInstance();

        SharedPreferences prefs = getSharedPreferences("AppPreference", MODE_PRIVATE);
        String childData = prefs.getString(childManager.CHILD_KEY, "[]");
        String flipData = prefs.getString(coinFlipManager.Flip_KEY,"[]");
        String taskData = prefs.getString(taskManager.TASK_KEY, "[]");
        childManager.loadFromJson(childData);
        coinFlipManager.loadFromJson(flipData);
        taskManager.loadFromJson(taskData);
    }
}