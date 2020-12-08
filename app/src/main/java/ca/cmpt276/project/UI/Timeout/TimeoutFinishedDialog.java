package ca.cmpt276.project.UI.Timeout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import ca.cmpt276.project.R;

/**
 * Dialog for notifying when timer hsa completed!
 */
public class TimeoutFinishedDialog extends AppCompatDialogFragment {

    private Context context;
    private Vibrator vibrator;
    private Ringtone ringtone;

    public TimeoutFinishedDialog(Context contextInput,
                                 Vibrator vibrator,
                                 Ringtone ringtone){
        super();
        this.context = contextInput;
        this.vibrator =  vibrator;
        this.ringtone = ringtone;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.activity_timout_finished_dialog, null);


        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vibrator.cancel();
                ringtone.stop();
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}