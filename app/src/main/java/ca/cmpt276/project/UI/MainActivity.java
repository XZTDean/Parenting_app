package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;


public class MainActivity extends AppCompatActivity {

    ChildManager childManager;
    ArrayList<Child> Children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        childManager = ChildManager.getInstance();
        childManager.loadData(Children);

        Button coinFlip = findViewById(R.id.coinFlip);
        coinFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CoinFlipScreen.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        /*
        The following is an outline of how the buttons in activity_main could
        be used. Feel free to edit and remove.

        Button configureChildren = findViewById(R.id.configureChildren);
        configureChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildManager.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });


        Button timeoutTimer = findViewById(R.id.timeoutTimer);
        timeoutTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildManager.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

         */

        saveData();

    }

    //Adapted from: https://codinginflow.com/tutorials/android/save-arraylist-to-sharedpreferences-with-gson
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Children);
        editor.putString("task list", json);
        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Child>>() {}.getType();
        Children = gson.fromJson(json, type);
        if (Children == null) {
            Children = new ArrayList<>();
        }
    }
}