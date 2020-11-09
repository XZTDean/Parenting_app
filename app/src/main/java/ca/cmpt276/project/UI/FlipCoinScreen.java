package ca.cmpt276.project.UI;

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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;
import ca.cmpt276.project.model.CoinFlip;
import ca.cmpt276.project.model.CoinFlipStats;

public class FlipCoinScreen extends AppCompatActivity {

    private ChildManager childList;
    private Child childPlaying;
    private CoinFlip coinFlip;
    private int choice;
    private boolean choiceScreenShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        coinFlip = CoinFlip.getInstance();
        childList = ChildManager.getInstance();
        childPlaying = new Child("Tom");
        childList.addChild(childPlaying);

        if(childList.sizeOfList() != 0){
            choiceScreenShown = true;
            setupChoiceScreen();
        }

        setupFlipButton();

        Button btn = findViewById(R.id.historyButton);
        btn.setOnClickListener(v -> {
            Intent intent = flipHistory.makeIntent(FlipCoinScreen.this);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupChoiceScreen() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showPopUp(view));

        int noOfSecond = 1;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                fab.performClick();
            }
        }, noOfSecond * 200);
    }

    private void setupFlipButton() {
        Button flipCoin = (Button) findViewById(R.id.buttonFlipCoin);
        flipCoin.setOnClickListener(v -> {
            resetResultText();
            resetCoinFaces();
            coinTossAnimation();
        });
    }

    private void resetCoinFaces() {
        ImageView heads = findViewById(R.id.imageViewCoinHeads);
        ImageView tails = findViewById(R.id.imageViewCoinTails);

        heads.setImageResource(R.drawable.heads__1);
        tails.setImageResource(R.drawable.tails__1);
    }

    private void resetResultText() {
        TextView textTails = (TextView) findViewById(R.id.textViewTails);
        textTails.setVisibility(View.INVISIBLE);
        TextView textHeads = (TextView) findViewById(R.id.textViewHeads);
        textHeads.setVisibility(View.INVISIBLE);
    }

    private void coinTossAnimation() {
        CoinFlipStats resultStats;
        int finalResult;

        ImageView heads = findViewById(R.id.imageViewCoinHeads);
        ImageView tails = findViewById(R.id.imageViewCoinTails);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_heads);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_tails);

        heads.startAnimation(animation);
        tails.startAnimation(animation1);


        if(choiceScreenShown){
            resultStats = coinFlip.flipCoin(childPlaying);
            finalResult = resultStats.getResult();
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    displayEndScreen(resultStats);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        else{
            finalResult = Math.random() < 0.5 ? 1 : 2;
        }

        new Handler().postDelayed((Runnable) () -> {
            if(finalResult == 1){
                heads.setImageResource(R.drawable.heads__1);
                tails.setImageResource(R.drawable.heads__1);
                TextView textTails = (TextView) findViewById(R.id.textViewTails);
                textTails.setVisibility(View.INVISIBLE);
                TextView textHeads = (TextView) findViewById(R.id.textViewHeads);
                textHeads.setVisibility(View.VISIBLE);
            }
            else if(finalResult == 2){
                heads.setImageResource(R.drawable.tails__1);
                tails.setImageResource(R.drawable.tails__1);
                TextView textHeads = (TextView) findViewById(R.id.textViewHeads);
                textHeads.setVisibility(View.INVISIBLE);
                TextView textTails = (TextView) findViewById(R.id.textViewTails);
                textTails.setVisibility(View.VISIBLE);
            }
        }, 3500);

    }

    private void displayEndScreen(CoinFlipStats resultStats) {
        startActivity(new Intent(FlipCoinScreen.this, PopEndScreen.class)
                .putExtra("Result",resultStats.getResult())
                .putExtra("WinOrLose",resultStats.winOrLose()));
    }


    private void showPopUp(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_pop_up, null);

        // create the popup window
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // setup Heads and Tails buttons
        Button heads = (Button)popupView.findViewById(R.id.buttonHeads);
        heads.setOnClickListener(v -> {
            childPlaying.setChoiceOfHeadsOrTails(1);
            popupWindow.dismiss();
        });

        Button tails = (Button)popupView.findViewById(R.id.buttonTails);
        tails.setOnClickListener(v -> {
            childPlaying.setChoiceOfHeadsOrTails(2);
            popupWindow.dismiss();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, FlipCoinScreen.class);
    }

}