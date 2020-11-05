package ca.cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.TimeoutTimer;

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

                    onFinish();

                }
            });
        }
    };

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dialog);
        editor.putString("dialog", json);
        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("dialog", null);
        Type type = new TypeToken<ArrayList<Child>>() {}.getType();
        dialog = gson.fromJson(json, type);

        if(dialog == null){
            dialog = new TimeoutFinishedDialog(
                    TimeoutTimerUI.this,
                    vibrator,
                    ringtone);
        }

    }

    /*@Override
    public void onBackPressed() {
        SavePreferences();
        super.onBackPressed();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timout_timer);

        duration = (Spinner) findViewById(R.id.durationSpinner);
        setSpinner();

        customDuration = (TextInputEditText) findViewById(R.id.customDurationInput);
        customDurationLayout = (TextInputLayout) findViewById(R.id.customDuration);

        duration.setOnItemSelectedListener(this);

        customDuration.setVisibility(View.GONE);

        pauseButton = (Button) findViewById(R.id.pauseButton);
        resumeButton = (Button) findViewById(R.id.resumeButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        pauseButton.setVisibility(View.GONE);
        resumeButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);

        /*dialog = new TimeoutFinishedDialog(
                TimeoutTimerUI.this,
                vibrator,
                ringtone);*/

        loadData();

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

    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void finishNotification(){

        System.out.println("made it");

        dialog.show(manager, "");
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);


/*
        // Sets an ID for the notification, so it can be updated.
        int notifyID = 1;
        String CHANNEL_ID = "timeOutChannel";// The id of the channel.
        CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(TimeoutTimerUI.this)
                .setContentTitle("New Message")
                .setContentText("You've received new messages")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setChannelId(CHANNEL_ID)
                .build();

        // Builds your notification
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this)

                .setContentTitle("Timeout is over")
                .setContentText("Press OK to continue")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);*/

        // Creates the intent needed to show the notification
        /*Intent notificationIntent = new Intent(this, TimeoutTimerUI.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());*/
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
            try {
                int inputInt = Integer.parseInt(input);
                if (inputInt >= 0) {
                    timeoutTimer = new TimeoutTimer(runnable, inputInt);
                }
            } catch (NumberFormatException nfe) {
                return;
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
