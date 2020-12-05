package ca.cmpt276.project.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import ca.cmpt276.project.model.BreathManager;
import ca.cmpt276.project.model.ChildManager;
import ca.cmpt276.project.model.TimeoutTimer;

public class BreathActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String BREATH_IN_MESSAGE = "Press and hold the big button, then breath in.";
    private String BREATH_OUT_MESSAGE = "Press and hold the big button, then breath out.";
    private String FINISH_MESSAGE = "Tap the big button to start again.";
    private TextView helpMessage;

    private Boolean isBreathComplete = false;
    private Boolean isFirstInhale = true;

    private float radius = 220;
    private long finishTime;
    private TimeoutTimer.Status status;
    public enum Status {
        ready, running, paused, stop
    }

    private int breathsSelected = 3;

    private MediaPlayer mp;

    private Thread thread;
    private Thread forceChangeThread;
    private Thread asyncThread;

    private String IN = "In";
    private String OUT = "Out";

    private Circle circle;
    CircleAngleAnimation animation;

    private long breathRemainingTime = 3000;
    private long animationTime = 10000;

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
                    } else {
                        helpMessage.setText(BREATH_OUT_MESSAGE);
                        begin.setText(OUT);
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

                    breathComplete();
                }

            });
        }
    };


    private Breath breath = new Breath(3, "Tom");
    private Spinner breathsSpinner;

    private Button begin;

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreathActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);

        setToolbar();

        status = TimeoutTimer.Status.ready;

        helpMessage = (TextView) findViewById(R.id.helpMessage);

        mp = MediaPlayer.create(this, R.raw.relaxing_music);

        begin = (Button) findViewById(R.id.begin);
        begin.setText("begin");

        breathsSpinner = (Spinner) findViewById(R.id.breathsSpinner);
        setSpinner();
        breathsSpinner.setOnItemSelectedListener(this);

        begin.setOnClickListener(v -> beginSelected());

        circle = (Circle) findViewById(R.id.circle);
        circle.setRadius(0);
        animation = new CircleAngleAnimation(circle, radius);

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void breathComplete() {
        mp.stop();
        mp = MediaPlayer.create(this, R.raw.relaxing_music);

        pauseAnimation();
        circle.saveRadius();

        if(!breath.isInhaling()) {
            breath.updateBreathLeft();
        }
        breath.changeBreath();

        breathRemainingTime = 3000;

        isBreathComplete = false;

        if(breath.getBreathNum() < 1){
            onFinish();
        }
    }

    private void onFinish() {
        Context context = getApplicationContext();
        CharSequence text = "Namaste -- Your breaths are complete :)";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        begin.setText("Good Job");
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
        finishTime = System.currentTimeMillis() + breathRemainingTime;
        status = TimeoutTimer.Status.running;
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

        mp.stop();
        mp = MediaPlayer.create(BreathActivity.this, R.raw.relaxing_music);

        radius = circle.getSavedRadius();

        animation = new CircleAngleAnimation(circle, radius);
        animation.setDuration(150);
        circle.swapColor(breath.isInhaling()); //?????????????????????????????????????????????????????????????????
        circle.startAnimation(animation);

        breathRemainingTime = 3000;
        status = TimeoutTimer.Status.ready;
    }

    private void pauseAnimation(){
        animation = new CircleAngleAnimation(circle, circle.getRadius());
        animation.setDuration(1);
        circle.startAnimation(animation);
    }

    private void updateRemainingTime() {
        long currentTime = System.currentTimeMillis();
        breathRemainingTime = finishTime - currentTime;
    }

    public void endTimer() {
        status = TimeoutTimer.Status.stop;
        breathRemainingTime = 0;
    }

    private void beginSelected() {

        isFirstInhale = false;

        breathsSpinner.setEnabled(false);

        helpMessage.setText(BREATH_IN_MESSAGE);
        begin.setText(IN);

        begin.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    //Starting breaths again.
                    if(begin.getText() == "Good Job"){
                        begin.setText(IN);
                        breathsSpinner.setEnabled(false);
                        helpMessage.setText(BREATH_IN_MESSAGE);
                    }


                    startBreath();
                    startForceChange();


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(forceChangeThread.isAlive()) {
                        forceChangeThread.interrupt();
                    }

                    pauseAnimation();

                    startAsyncHandler();

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
