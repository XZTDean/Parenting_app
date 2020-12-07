package ca.cmpt276.project.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Breath;

/*
 * BreathActivity allows users to calm down by taking beep breaths.
 * This class makes use of the Breath model class, and the Circle and
 * the CircleAngleAnimation UI classes.
 */
public class BreathActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String BREATH_IN_MESSAGE;
    private String BREATH_OUT_MESSAGE;
    private String FINISH_MESSAGE;
    private String RELEASE_MESSAGE;
    private String IN;
    private String OUT;
    private String BEGIN;
    private String GOOD_JOB;
    private String DEFAULT_MESSAGE;

    private TextView helpMessage;

    private Boolean isBreathComplete = false;

    private float radius = 220;

    private int breathsSelected = 3;



    private MediaPlayer mp;

    private Thread thread;
    private Thread forceChangeThread;
    private Thread asyncThread;

    private Circle circle;
    CircleAngleAnimation animation;

    private long breathRemainingTime = 3000;
    private long animationTime = 10000;

    private Boolean readyToExhale = false;

    private final Runnable changeBreathRunnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isBreathComplete = true;
                    if(!breath.isInhaling()){
                        helpMessage.setText(BREATH_IN_MESSAGE);
                        begin.setText(IN);
                        readyToExhale = false;

                        breath.updateBreathLeft();

                        if(breath.getBreathNum() < 1){
                            onFinish();
                        }

                        begin.setEnabled(true);

                    } else {
                        helpMessage.setText(BREATH_OUT_MESSAGE);
                        begin.setText(OUT);
                        readyToExhale = true;
                    }
                }

            });
        }
    };

    private final Runnable asyncRunnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isBreathComplete){

                        breathComplete();
                        if(readyToExhale){
                            startBreath();
                            startForceChange();
                        }
                    } else {
                        reset();
                    }
                }
            });
        }
    };

    private final Runnable forceChangeBreathRunnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(breath.isInhaling()) {
                        helpMessage.setText(RELEASE_MESSAGE);
                    }
                    breathComplete();
                    if(readyToExhale) {
                        startBreath();
                        startForceChange();
                    }
                }
            });
        }
    };


    private Breath breath = new Breath(3, "");
    private Spinner breathsSpinner;

    private Button begin;

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreathActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);
        displayLastBreath();
        setToolbar();


        initializeStringConstants();
        initializeButton();
        initializeCircle();
        initializeSpinner();

        helpMessage = (TextView) findViewById(R.id.helpMessage);
        helpMessage.setText(DEFAULT_MESSAGE);

        mp = MediaPlayer.create(this, R.raw.relaxing_music);
    }

    private void initializeSpinner() {
        breathsSpinner = (Spinner) findViewById(R.id.breathsSpinner);
        setSpinner();
        breathsSpinner.setOnItemSelectedListener(this);
    }

    private void initializeCircle() {
        circle = (Circle) findViewById(R.id.circle);
        circle.setRadius(0);
        animation = new CircleAngleAnimation(circle, radius);
    }

    private void initializeButton() {
        begin = (Button) findViewById(R.id.begin);
        begin.setText(BEGIN);
        begin.setOnClickListener(v -> beginSelected());
    }

    private void initializeStringConstants() {
        BREATH_IN_MESSAGE = getString(R.string.breath_in_message);
        BREATH_OUT_MESSAGE = getString(R.string.breath_out_message);
        FINISH_MESSAGE = getString(R.string.finish_message);
        RELEASE_MESSAGE = getString(R.string.release_message);
        IN = getString(R.string.in);
        OUT = getString(R.string.out);
        BEGIN = getString(R.string.begin);
        GOOD_JOB = getString(R.string.good_job);
        DEFAULT_MESSAGE = getString(R.string.default_message);




    }

    private void displayLastBreath() {
        int lastBreath = getBreathFromSharedPref();

        TextView textview = (TextView) findViewById(R.id.prevBreath);
        String breathNum= "last breath: "+String.valueOf(lastBreath);
        textview.setText(breathNum);
    }

    private int getBreathFromSharedPref() {
        SharedPreferences prefs = getSharedPreferences("breathNum",MODE_PRIVATE);
        return prefs.getInt("breathN",0);
    }

    private void storeBreathNumFromSharedPrefs(){
        SharedPreferences prefs = getSharedPreferences("breathNum",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("breathN",breathsSelected);
        editor.commit();


    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void stopMusic(){
        mp.stop();
        mp = MediaPlayer.create(this, R.raw.relaxing_music);

    }

    private void breathComplete() {
        stopMusic();
        pauseAnimation();
        circle.saveRadius();

        if(!breath.isInhaling()) {
            begin.setEnabled(true);
        } else {
            begin.setEnabled(false);
        }

        breath.changeBreath();
        breathRemainingTime = 3000;
        isBreathComplete = false;

    }

    private void finishToast() {
        Context context = getApplicationContext();
        CharSequence text = "Namaste -- Your breaths are complete :)";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void onFinish() {
        thread.interrupt();
        if(asyncThread != null) {
            asyncThread.interrupt();
        }
        forceChangeThread.interrupt();

        finishToast();

        begin.setText(GOOD_JOB);
        helpMessage.setText(FINISH_MESSAGE);
        breathsSpinner.setEnabled(true);
        breath.setBreathNum(breathsSelected);
    }

    private void startBreath() {

        thread = new Thread(changeBreathRunnable){

            @Override
            public void run() {

                mp.start();
                if(breath.isInhaling()){
                    radius = 220;
                } else {
                    radius = 0;
                }

                animation = new CircleAngleAnimation(circle, radius);
                animation.setDuration(animationTime);
                circle.swapColor(breath.isInhaling());
                circle.startAnimation(animation);
                try {
                    Thread.sleep(breathRemainingTime);
                } catch (InterruptedException e) {
                    return;
                }
                super.run();
                endTimer();
            }
        };
        thread.start();
    }

    private void startAsyncHandler() {

        asyncThread = new Thread(asyncRunnable){

            @Override
            public void run() {

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    return;
                }
                super.run();
            }
        };
        asyncThread.start();
    }

    private void startForceChange() {

        forceChangeThread = new Thread(forceChangeBreathRunnable){

            @Override
            public void run() {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    return;
                }
                super.run();
                endTimer();
            }
        };
        forceChangeThread.start();
    }

    public void reset() {
        thread.interrupt();
        asyncThread.interrupt();

        stopMusic();

        radius = circle.getSavedRadius();

        animation = new CircleAngleAnimation(circle, radius);
        animation.setDuration(150);
        circle.swapColor(breath.isInhaling());
        circle.startAnimation(animation);

        breathRemainingTime = 3000;
    }

    private void pauseAnimation(){
        animation = new CircleAngleAnimation(circle, circle.getRadius());
        animation.setDuration(1);
        circle.startAnimation(animation);
    }

    public void endTimer() {
        breathRemainingTime = 0;
    }

    private void buttonClickHandler() {

        if(helpMessage.getText() == RELEASE_MESSAGE){
            helpMessage.setText(BREATH_OUT_MESSAGE);
        }

        if(!breath.isInhaling()){
            thread.interrupt();
            if(asyncThread != null) {
                asyncThread.interrupt();
            }
            forceChangeThread.interrupt();
            breathComplete();
        }

        //Starting breaths again.
        if(begin.getText() == GOOD_JOB){
            begin.setText(IN);
            breathsSpinner.setEnabled(false);
            helpMessage.setText(BREATH_IN_MESSAGE);
        }

        startBreath();
        startForceChange();
    }

    private void buttonReleaseHandler() {
        if(forceChangeThread.isAlive()) {
            forceChangeThread.interrupt();
        }
        if(!breath.isInhaling()){
            begin.setEnabled(false);
        }

        pauseAnimation();
        startAsyncHandler();
    }

    private void beginSelected() {

        breathsSpinner.setEnabled(false);
        helpMessage.setText(BREATH_IN_MESSAGE);
        begin.setText(IN);

        begin.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(thread == null || !thread.isAlive()) {
                        buttonClickHandler();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                   buttonReleaseHandler();
                }

                return true;
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (pos) {
            case 1:
                breathsSelected = 1;
                break;
            case 2:
                breathsSelected = 2;
                break;
            case 3:
                breathsSelected = 3;
                break;
            case 4:
                breathsSelected = 4;
                break;
            case 5:
                breathsSelected = 5;
                break;
            case 6:
                breathsSelected = 6;
                break;
            case 7:
                breathsSelected = 7;
                break;
            case 8:
                breathsSelected = 8;
                break;
            case 9:
                breathsSelected = 9;
                break;
            case 10:
                breathsSelected = 10;
                break;
        }
        breath.setBreathNum(breathsSelected);
        storeBreathNumFromSharedPrefs();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.breathsSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breathsSpinner.setAdapter(adapter);
    }

}
