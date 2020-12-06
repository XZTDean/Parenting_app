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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class TimeoutTimerUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PopupMenu.OnMenuItemClickListener{

    private static Context context;
    private TimeoutTimer timeoutTimer = null;

    private TextInputLayout customDurationLayout;
    private TextInputEditText customDuration;
    private Spinner duration;

    private Button startButton;
    private Button pauseButton;
    private Button resumeButton;
    private Button resetButton;

    private GifImageView relaxingBG;

    private ProgressBar progressBar;

    private int chosenDuration;
    private double speed = 1;

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

        setupToolBar();
        setupProgressBar();
        setupGifBG();
        createNotificationChannel();
        initializeButtons();
        restoreTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timer_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.speed_icon) {
            View icon = findViewById(R.id.speed_icon);
            showPopup(icon);
            return true;
        } else {
            return false;
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.timer_speed_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (timeoutTimer == null) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.speed_25:
                speed = 0.25;
                break;
            case R.id.speed_50:
                speed = 0.5;
                break;
            case R.id.speed_75:
                speed = 0.75;
                break;
            case R.id.speed_100:
                speed = 1;
                break;
            case R.id.speed_200:
                speed = 2;
                break;
            case R.id.speed_300:
                speed = 3;
                break;
            case R.id.speed_400:
                speed = 4;
                break;
            default:
                return false;
        }
        timeoutTimer.setSpeed(speed);
        return true;
    }

    private void setupProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progress_circular_timer);
        progressBar.setProgress(100);
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void displayTimeDisplay() {
        View view = findViewById(R.id.remaining_time_display);
        view.setVisibility(View.VISIBLE);
    }

    private void hideTimeDisplay() {
        View view = findViewById(R.id.remaining_time_display);
        view.setVisibility(View.INVISIBLE);
    }

    private void resumeTimerDisplay() {
        if (timeoutTimer != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        return;
                    }
                    runOnUiThread(() -> {
                        displayTimeDisplay();
                        if (timeoutTimer.getStatus() == TimeoutTimer.Status.running) {
                            startCountDown();
                        }
                    });
                }
            }.start();
        }
    }

    private void displayTime() {
        TextView minBox = findViewById(R.id.minute_text);
        TextView secBox = findViewById(R.id.second_text);
        long time = timeoutTimer.getRemainingTime();
        String minText = String.valueOf(time / 60);
        String secText = String.format("%02d", time % 60);
        minBox.setText(minText);
        secBox.setText(secText);
        progressBar.setProgress((int)time);
    }

    private void displayTime(int min, int sec) {
        TextView minBox = findViewById(R.id.minute_text);
        TextView secBox = findViewById(R.id.second_text);
        minBox.setText(String.valueOf(min));
        secBox.setText(String.format("%02d", sec));
        progressBar.setProgress(100);
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

        startButton.setOnClickListener(v -> startSelected());

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
        timeoutTimer = null;
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
                return;
            }
            resumeTimerDisplay();
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

    private void startCountDown() {
        new Thread() {
            @Override
            public void run() {
                while (activityVisible && timeoutTimer != null
                        && timeoutTimer.getStatus() == TimeoutTimer.Status.running) {
                    runOnUiThread(() -> displayTime());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }.start();
    }

    private void setButtonInRunning() {
        startGifBG();
        setVisibilityDropDownAsInvisible();
        startButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        resumeButton.setVisibility(View.INVISIBLE);
        startCountDown();
    }

    private void setButtonInPause() {
        stopGifBG();
        setVisibilityDropDownAsInvisible();
        startButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        resumeButton.setVisibility(View.VISIBLE);
        displayTime();
    }

    private void setButtonInReady() {
        stopGifBG();
        setVisibilityDropDownAsVisible();
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.INVISIBLE);
    }

    private void startSelected() {
        timeoutTimer = TimeoutTimer.getNewInstance(runnable, chosenDuration);
        timeoutTimer.start();
        customDuration.setVisibility(View.INVISIBLE);
        customDurationLayout.setVisibility(View.INVISIBLE);
        progressBar.setMax(chosenDuration*60);
        progressBar.setProgress(chosenDuration*60);
        displayTimeDisplay();
        setButtonInRunning();
    }

    private void pauseSelected(){
        timeoutTimer.pause();
        setButtonInPause();
    }

    private void resetSelected(){
        timeoutTimer.reset();
        timeoutTimer.start();
        setButtonInRunning();
    }

    private void resumeSelected(){
        timeoutTimer.resume();
        setButtonInRunning();
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

        switch (pos) {
            case 0:
                chosenDuration = 0;
                hideTimeDisplay();
                break;
            case 1:
                chosenDuration = 1;
                displayChosen();
                break;
            case 2:
                chosenDuration = 2;
                displayChosen();
                break;
            case 3:
                chosenDuration = 3;
                displayChosen();
                break;
            case 4:
                chosenDuration = 5;
                displayChosen();
                break;
            case 5:
                chosenDuration = 10;
                displayChosen();
                break;
            case 6:
                customDuration.setVisibility(View.VISIBLE);
                customDurationLayout.setVisibility(View.VISIBLE);
                customDuration.addTextChangedListener(textWatcherDistance);
                break;
        }

    }

    private void displayChosen() {
        displayTimeDisplay();
        displayTime(chosenDuration, 0);
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
                    displayChosen();
                } else {
                    hideTimeDisplay();
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
