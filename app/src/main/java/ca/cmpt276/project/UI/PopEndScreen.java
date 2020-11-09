package ca.cmpt276.project.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ca.cmpt276.project.R;

import static java.lang.Thread.sleep;

public class PopEndScreen extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);

        Intent intent = getIntent();
        int result = intent.getIntExtra("Result", 2);
        int winOrLose = intent.getIntExtra("WinOrLose", 1);

        setupTextAndImage(result);
        setupBackgroundWinOrLose(winOrLose);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8) , (int)(height * 0.8));
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
            textResult.setText("HEADS");
            imageResult.setImageResource(R.drawable.heads__1);
        }
        else{
            textResult.setText("TAILS");
            imageResult.setImageResource(R.drawable.tails__1);
        }
    }
}
