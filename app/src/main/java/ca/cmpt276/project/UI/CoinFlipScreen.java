package ca.cmpt276.project.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.cmpt276.project.R;

import static java.lang.Thread.sleep;

public class CoinFlipScreen extends AppCompatActivity {


    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, CoinFlipScreen.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(view);
            }
        });
        fab.setVisibility(View.GONE);

        //fab.performClick();

        int noOfSecond = 1;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                fab.performClick();
            }
        }, noOfSecond * 500);


        Button flipCoin = (Button) findViewById(R.id.buttonFlipCoin);
        flipCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinTossAnimation();
            }
        });

    }

    private void coinTossAnimation() {
        View heads = findViewById(R.id.imageViewCoin);
        View tails = findViewById(R.id.imageViewCoin);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_heads);

        heads.startAnimation(animation);
    }

    private void showPopUp(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_pop_up, null);

        // create the popup window
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean allowOutsideTouchExit = true; // lets taps outside the popup not dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, allowOutsideTouchExit);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


    }

}