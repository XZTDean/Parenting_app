package ca.cmpt276.project.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ca.cmpt276.project.R;

public class HelpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);
        setToolbar();
        setText();


    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarHelp);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setText(){
        TextView textView = (TextView) findViewById(R.id.developer_name);
        textView.setText("Developer: \nRepo Manager: \tShubh kumar Mall\n" +
                "Product Owner:\tXingjian Ding\n" +
                "Scrum Master:\tDean Xiong\n" +
                "Team member: \tJoshua Taylor");



    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpScreen.class);
    }
}
