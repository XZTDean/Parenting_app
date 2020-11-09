package ca.cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.TimeoutTimer;

/*
 *TimeoutTimerUI defines the look
 * of the timeout timer user interface.
 * The class also defines how the support timer
 * classes will interact with each other and the interface.
 */

public class TimeoutTimerUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static Context context;
    private TimeoutTimer timeoutTimer;

    private TextInputLayout customDurationLayout;
    private TextInputEditText customDuration;
    private Spinner duration;

    private Button startButton;
    private Button pauseButton;
    private Button resumeButton;
    private Button resetButton;

    private int chosenDuration;

    private Vibrator vibrator;
    private Ringtone ringtone;

    private FragmentManager manager = getSupportFragmentManager();
    private TimeoutFinishedDialog dialog;

    //Adapted from: https://stackoverflow.com/questions/18038399/how-to-check-if-activity-is-in-foreground-or-in-visible-background
    @Override
    protected void onResume() {
        super.onResume();
        TimeoutTimerUI.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TimeoutTimerUI.activityPaused();
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    startButton.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);
                    resetButton.setVisibility(View.GONE);
                    timeoutTimer = new TimeoutTimer(runnable, chosenDuration);

                    if(activityVisible) {
                        onFinish();
                    } else {
                        Intent intent = TimerService.makeIntent(TimeoutTimerUI.this);
                        startService(intent);
                    }

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timout_timer);

        duration = (Spinner) findViewById(R.id.durationSpinner);
        setSpinner();

        customDuration = (TextInputEditText) findViewById(R.id.customDurationInput);
        customDurationLayout = (TextInputLayout) findViewById(R.id.customDuration);

        duration.setOnItemSelectedListener(this);

        initializeButtons();

    }

    private void initializeButtons(){
        pauseButton = (Button) findViewById(R.id.pauseButton);
        resumeButton = (Button) findViewById(R.id.resumeButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        //Adapted from: https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);



        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeoutTimer != null) {
                    timeoutTimer.start();
                    startButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);
                    resetButton.setVisibility(View.VISIBLE);

                    pauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pauseSelected();
                        }
                    });

                    resetButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetSelected();
                        }
                    });

                    resumeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resumeSelected();
                        }
                    });
                }
            }
        });
    }

    private void onFinish(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(50000);
        }

        ringtone.play();
        finishNotification();
    }

    private void finishNotification(){

        dialog = new TimeoutFinishedDialog(
                TimeoutTimerUI.this,
                vibrator,
                ringtone);

        try {

            dialog.show(manager, "");

        } catch (IllegalStateException ISE){
            ringtone.stop();
            vibrator.cancel();
        }

    }

    private void pauseSelected(){

        pauseButton.setVisibility(View.GONE);
        resumeButton.setVisibility(View.VISIBLE);
        timeoutTimer.pause();

    }

    private void resetSelected(){

        if(timeoutTimer.getStatus() == TimeoutTimer.Status.ready){
            timeoutTimer.start();
        }
        else {
            timeoutTimer.reset();
            timeoutTimer.start();
        }
    }

    private void resumeSelected(){

        if(timeoutTimer.getStatus() != TimeoutTimer.Status.paused){
            timeoutTimer.pause();
        }
        resumeButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        timeoutTimer.resume();

    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.durationSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        customDuration.setVisibility(View.GONE);
        customDurationLayout.setVisibility(View.GONE);

        if(pos == 1) {
            chosenDuration = 1;
            timeoutTimer = new TimeoutTimer(runnable, chosenDuration);
        } else if (pos == 2) {
            chosenDuration = 2;
            timeoutTimer = new TimeoutTimer(runnable, chosenDuration);
        } else if (pos == 3) {
            chosenDuration = 3;
            timeoutTimer = new TimeoutTimer(runnable, chosenDuration);
        } else if (pos == 4) {
            chosenDuration = 5;
            timeoutTimer = new TimeoutTimer(runnable, chosenDuration);
        } else if (pos == 5) {
            chosenDuration = 10;
            timeoutTimer = new TimeoutTimer(runnable, chosenDuration);
        } else if (pos == 6) {
            customDuration.setVisibility(View.VISIBLE);
            customDurationLayout.setVisibility(View.VISIBLE);

            customDuration.addTextChangedListener(textWatcherDistance);

        }

    }

    TextWatcher textWatcherDistance = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = Objects.requireNonNull(customDuration.getText()).toString();

            if(timeoutTimer != null){
                return;
            }

            try {
                int inputInt = Integer.parseInt(input);
                if (inputInt >= 0) {
                    timeoutTimer = new TimeoutTimer(runnable, inputInt);
                }
            } catch (NumberFormatException nfe) {
                Context context = getApplicationContext();
                CharSequence text = "Invalid input. Please try again.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

        }
    };


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static Intent makeIntent(Context contextInput) {
        context = contextInput;
        return new Intent(context, TimeoutTimerUI.class);
    }

}
