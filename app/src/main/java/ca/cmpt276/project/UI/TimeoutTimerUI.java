package ca.cmpt276.project.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.TimeoutTimer;
import pl.droidsonroids.gif.GifImageView;

/**
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

    private GifImageView relaxingBG;

    private int chosenDuration;

    private final String CHANNEL_ID = "TIMER";
    private Vibrator vibrator;
    private Ringtone ringtone;

    private final FragmentManager manager = getSupportFragmentManager();

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

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setButtonInReady();

                    if(activityVisible) {
                        onFinish();
                    } else {
                        sendNotification();
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

        setupGifBG();
        createNotificationChannel();
        initializeButtons();
        restoreTimer();
    }

    private void setupGifBG() {
        relaxingBG = (GifImageView) findViewById(R.id.gifTimeoutTimer);
    }

    private void startGifBG(){
        relaxingBG.setBackgroundResource(R.drawable.tom_gif_1);
        relaxingBG.setVisibility(View.VISIBLE);
    }

    private void stopGifBG(){
        relaxingBG.setVisibility(View.GONE);
    }

    private void initializeButtons(){
        startButton = (Button) findViewById(R.id.startButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        resumeButton = (Button) findViewById(R.id.resumeButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        //Adapted from: https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);

        startButton.setOnClickListener(v -> {
            timeoutTimer = TimeoutTimer.getNewInstance(runnable, chosenDuration);
            timeoutTimer.start();
            setButtonInRunning();
        });

        pauseButton.setOnClickListener(v -> pauseSelected());
        resetButton.setOnClickListener(v -> resetSelected());
        resumeButton.setOnClickListener(v -> resumeSelected());
    }

    private void setVisibilityDropDownAsVisible() {
        duration.setVisibility(View.VISIBLE);
    }

    private void setVisibilityDropDownAsInvisible(){
        duration.setVisibility(View.INVISIBLE);
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

        TimeoutFinishedDialog dialog = new TimeoutFinishedDialog(
                TimeoutTimerUI.this,
                vibrator,
                ringtone);

        try {

            dialog.show(manager, "");

        } catch (IllegalStateException ISE){
            ringtone.stop();
            vibrator.cancel();
        }

        TimeoutTimer.deleteInstance();
    }

    private void restoreTimer() {
        if (TimeoutTimer.hasInstance()) {
            timeoutTimer = TimeoutTimer.getExistInstance();
            setButtonByState();
            if (timeoutTimer.getStatus() == TimeoutTimer.Status.stop) {
                onFinish();
            }
        }
    }

    private void setButtonByState() {
        switch (timeoutTimer.getStatus()) {
            case running:
                setButtonInRunning();
                break;
            case paused:
                setButtonInPause();
                break;
            case stop:
            case ready:
                setButtonInReady();
        }
    }

    private void setButtonInRunning() {
        startButton.setVisibility(View.GONE);
        startGifBG();
        setVisibilityDropDownAsInvisible();
        pauseButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        resumeButton.setVisibility(View.GONE);
    }

    private void setButtonInPause() {
        stopGifBG();
        setVisibilityDropDownAsVisible();
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.VISIBLE);
        resumeButton.setVisibility(View.VISIBLE);
    }

    private void setButtonInReady() {
        setVisibilityDropDownAsVisible();
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
        resumeButton.setVisibility(View.GONE);
    }

    private void pauseSelected(){
        setButtonInPause();
        timeoutTimer.pause();

    }

    private void resetSelected(){
        setButtonInRunning();
        timeoutTimer.reset();
        timeoutTimer.start();
    }

    private void resumeSelected(){
        setButtonInRunning();
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
        } else if (pos == 2) {
            chosenDuration = 2;
        } else if (pos == 3) {
            chosenDuration = 3;
        } else if (pos == 4) {
            chosenDuration = 5;
        } else if (pos == 5) {
            chosenDuration = 10;
        } else if (pos == 6) {
            customDuration.setVisibility(View.VISIBLE);
            customDurationLayout.setVisibility(View.VISIBLE);

            customDuration.addTextChangedListener(textWatcherDistance);

        }

    }

    private final TextWatcher textWatcherDistance = new TextWatcher() {
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
                    chosenDuration = inputInt;
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.timer);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 500, 500, 500, 500, 500, 500, 500, 500});
            channel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + getPackageName() + "/" + R.raw.bell),
                    new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
            channel.setDescription(getString(R.string.timer_notification_desc));
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification() {
        Intent intent = new Intent(this, TimeoutTimerUI.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_clock)
                .setContentTitle(getString(R.string.timer))
                .setContentText(getString(R.string.timer_notification_text))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setVibrate(new long[]{0, 500, 500, 500, 500, 500, 500, 500, 500, 500})
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + getPackageName() + "/" + R.raw.bell), AudioManager.STREAM_ALARM)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    public static Intent makeIntent(Context contextInput) {
        context = contextInput;
        return new Intent(context, TimeoutTimerUI.class);
    }

}
