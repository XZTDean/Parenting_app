package ca.cmpt276.project.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.TimeoutTimer;

/**
 * TimerService is class that keep track with the timer,
 * so that if you exit the timer page, the timer will also
 * make notification when time is up.
 */
public class TimerService extends Service {



    public static Intent makeIntent(Context c) {
        Intent intent = new Intent(c, TimerService.class);
        return intent;
    }

    private TimeoutTimer timer;
    private final String CHANNEL_ID = "TIMER";

    @Override
    public void onCreate() {

        createNotificationChannel();
        sendNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    private void timeout() {

        sendNotification();
        stopSelf();
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

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not supported");
    }
}
