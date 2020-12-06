package ca.cmpt276.project.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ca.cmpt276.project.R;

import static java.lang.Thread.sleep;

/**
 * Screen to display result of toss
 * Has options to view history page or exit to main menu
 */
public class PopEndScreen extends Activity {
    private String childPlaying;
    private int listSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);

        Intent intent = getIntent();
        int result = intent.getIntExtra("Result", 2);
        int winOrLose = intent.getIntExtra("WinOrLose", 1);
        childPlaying = intent.getStringExtra("childPlaying");
        listSize = intent.getIntExtra("listSize", 0);

        setupTextAndImage(result);
        setupBackgroundWinOrLose(winOrLose);
        setupExitButton();
        setupHistoryButton();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8) , (int)(height * 0.8));
    }

    private void setupHistoryButton() {
        Button history = (Button) findViewById(R.id.buttonHistory);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlipHistory.makeIntent(PopEndScreen.this, childPlaying);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupExitButton() {
        Button exit = (Button) findViewById(R.id.buttonExit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setupBackgroundWinOrLose(int winOrLose) {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.endScreenLayout);

        if(winOrLose == 1){
            currentLayout.setBackgroundResource(R.drawable.you_win_background);
        }
        else{
            currentLayout.setBackgroundResource(R.drawable.lose_screen_background);
        }
    }

    private void setupTextAndImage(int result) {
        TextView textResult = (TextView) findViewById(R.id.textViewResult);
        ImageView imageResult = (ImageView) findViewById(R.id.imageViewResult);

        if(result == 1){
            textResult.setText(R.string.heads);
            imageResult.setImageResource(R.drawable.heads__1);
        }
        else{
            textResult.setText(R.string.tails);
            imageResult.setImageResource(R.drawable.tails__1);
        }
    }
}
