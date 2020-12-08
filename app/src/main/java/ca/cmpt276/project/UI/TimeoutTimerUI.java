package ca.cmpt276.project.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private TimeoutTimer timeoutTimer = null;
    private int maxDuration;
    private int speedOptionSelected;        // 1 = 25%, 2 = 50%, 3 = 75%, 4 = 100%, 5 = 200%, 6 = 300%, 7 = 400%

    private TextView textTimeSpeed;
    private TextInputLayout customDurationLayout;
    private TextInputEditText customDuration;
    private Spinner duration;

    private Button startButton;
    private Button pauseButton;
    private Button resumeButton;
    private Button resetButton;

    private GifImageView relaxingBG;

    private ProgressBar progressBar;

    private MenuItem speedTimerMenu;

    private int chosenDuration;
    private double speed = 1;

    private final String CHANNEL_ID = "TIMER";
    private Vibrator vibrator;
    private Ringtone ringtone;

    private final FragmentManager manager = getSupportFragmentManager();

    private static final String SHARED_PREF = "sharedPrefs";
    private static final String MAX_DURATION = "maxDuration";
    private static final String SPEED = "speed";
    private static final String SPEED_OPTION = "SpeedOption";
    private static final String CHOSEN_DURATION = "chosenDuration";

    //Adapted from: https://stackoverflow.com/questions/18038399/how-to-check-if-activity-is-in-foreground-or-in-visible-background
    @Override
    protected void onResume() {
        super.onResume();
        activityVisible = true;
        obj = this;
        loadData();
        updateData();
        if(timeoutTimer != null){
            if(timeoutTimer.getSpeed() == 0.25) {
                textTimeSpeed.setText(R.string.time_25);
            }
            else if(timeoutTimer.getSpeed() == 0.5){
                textTimeSpeed.setText(R.string.time_50);
            }
            else if(timeoutTimer.getSpeed() == 0.75){
                textTimeSpeed.setText(R.string.time_75);
            }
            else if(timeoutTimer.getSpeed() == 1){
                textTimeSpeed.setText(R.string.time_100);
            }
            else if(timeoutTimer.getSpeed() == 2){
                textTimeSpeed.setText(R.string.time_200);
            }
            else if(timeoutTimer.getSpeed() == 3){
                textTimeSpeed.setText(R.string.time_300);
            }
            else if(timeoutTimer.getSpeed() == 4){
                textTimeSpeed.setText(R.string.time_400);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        obj.saveData();
        activityVisible = false;
        obj = null;
    }



    public static boolean isActivityVisible() {
        return activityVisible;
    }

    private static boolean activityVisible = false;
    private static TimeoutTimerUI obj = null; // will set to null when exit activity

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if(activityVisible) {
                    obj.setButtonInReady();
                    obj.onFinish();
                } else {
                    sendNotification();
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
        setupViews();
        setupGifBG();
        createNotificationChannel();
        initializeButtons();
        restoreTimer();

        // if needed to add functionality of saving state when app is closed in future iterations
        /*loadData();
        updateData();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timer_toolbar, menu);
        speedTimerMenu = menu.findItem(R.id.speed_icon);
        speedTimerMenu.setVisible(false);
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
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.timer_speed_menu);
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
                speedOptionSelected = 1;
                textTimeSpeed.setText(R.string.time_25);
                saveData();
                break;
            case R.id.speed_50:
                speed = 0.5;
                speedOptionSelected = 2;
                textTimeSpeed.setText(R.string.time_50);
                saveData();
                break;
            case R.id.speed_75:
                speed = 0.75;
                speedOptionSelected = 3;
                textTimeSpeed.setText(R.string.time_75);
                saveData();
                break;
            case R.id.speed_100:
                speed = 1;
                speedOptionSelected = 4;
                textTimeSpeed.setText(R.string.time_100);
                saveData();
                break;
            case R.id.speed_200:
                speed = 2;
                speedOptionSelected = 5;
                textTimeSpeed.setText(R.string.time_200);
                saveData();
                break;
            case R.id.speed_300:
                speed = 3;
                speedOptionSelected = 6;
                textTimeSpeed.setText(R.string.time_300);
                saveData();
                break;
            case R.id.speed_400:
                speed = 4;
                speedOptionSelected = 7;
                textTimeSpeed.setText(R.string.time_400);
                saveData();
                break;
            default:
                return false;
        }
        timeoutTimer.setSpeed(speed);
        return true;
    }

    private void setupViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress_circular_timer);
        textTimeSpeed = (TextView) findViewById(R.id.textTimeSpeed);
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
        speedTimerMenu.setVisible(true);
    }

    private void displayTime(int min, int sec) {
        TextView minBox = findViewById(R.id.minute_text);
        TextView secBox = findViewById(R.id.second_text);
        minBox.setText(String.valueOf(min));
        secBox.setText(String.format("%02d", sec));
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
        speedOptionSelected = 1;
        maxDuration = 100;
        textTimeSpeed.setText(R.string.time_100);
        speed = 1;
        saveData();
        speedTimerMenu.setVisible(false);
        timeoutTimer = null;
        progressBar.setMax(100);
        progressBar.setProgress(0);
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
                        Thread.sleep((long)(1000/speed));
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
        speedTimerMenu.setVisible(true);
        timeoutTimer = TimeoutTimer.getNewInstance(runnable, chosenDuration);
        timeoutTimer.start();
        customDuration.setVisibility(View.INVISIBLE);
        customDurationLayout.setVisibility(View.INVISIBLE);
        updateProgressBar();
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
                updateProgressBar();
                displayChosen();
                break;
            case 2:
                chosenDuration = 2;
                updateProgressBar();
                displayChosen();
                break;
            case 3:
                chosenDuration = 3;
                updateProgressBar();
                displayChosen();
                break;
            case 4:
                chosenDuration = 5;
                updateProgressBar();
                displayChosen();
                break;
            case 5:
                chosenDuration = 10;
                updateProgressBar();
                displayChosen();
                break;
            case 6:
                customDuration.setVisibility(View.VISIBLE);
                customDurationLayout.setVisibility(View.VISIBLE);
                customDuration.addTextChangedListener(textWatcherDistance);
                break;
        }

    }

    private void updateProgressBar() {
        progressBar.setMax(chosenDuration*60);
        progressBar.setProgress(chosenDuration*60);
        saveData();
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
        return new Intent(contextInput, TimeoutTimerUI.class);
    }

    public void saveData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(MAX_DURATION, chosenDuration*60);
        editor.putFloat(SPEED, (float)speed);
        editor.putInt(SPEED_OPTION, speedOptionSelected);
        editor.putInt(CHOSEN_DURATION, chosenDuration);

        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        maxDuration = sharedPreferences.getInt(MAX_DURATION, 100);
        speed = (double) sharedPreferences.getFloat(SPEED, 1);
        speedOptionSelected = sharedPreferences.getInt(SPEED_OPTION, 4);
        chosenDuration = sharedPreferences.getInt(CHOSEN_DURATION, 1);
    }

    public void updateData(){
        progressBar.setMax(maxDuration);
        switch (speedOptionSelected){
            case 1:
                textTimeSpeed.setText(R.string.time_25);
            case 2:
                textTimeSpeed.setText(R.string.time_50);
            case 3:
                textTimeSpeed.setText(R.string.time_75);
            case 4:
                textTimeSpeed.setText(R.string.time_100);
            case 5:
                textTimeSpeed.setText(R.string.time_200);
            case 6:
                textTimeSpeed.setText(R.string.time_300);
            case 7:
                textTimeSpeed.setText(R.string.time_400);
            default:
                textTimeSpeed.setText(R.string.time_100);
        }
    }
}
