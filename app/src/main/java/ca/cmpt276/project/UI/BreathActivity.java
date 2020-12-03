package ca.cmpt276.project.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Breath;
import ca.cmpt276.project.model.BreathManager;
import ca.cmpt276.project.model.ChildManager;
import ca.cmpt276.project.model.TimeoutTimer;

public class BreathActivity extends AppCompatActivity {

    //private BreathManager breathManager = BreathManager.g;

    OnCustomEventListener mListener;

    private Thread thread;

    private String IN = "In";
    private String OUT = "Out";

    private long breathRemainingTime = 3000;

    private final Runnable changeBreathRunnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(breath.isInhaling()){
                        begin.setText(OUT);
                    } else {
                        begin.setText(IN);
                    }
                    reset();
                    breath.changeBreath();
                    breath.updateBreathLeft();

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




        begin = (Button) findViewById(R.id.begin);
        begin.setText("begin");

        breathsSpinner = (Spinner) findViewById(R.id.breathsSpinner);
        setSpinner();


        begin.setOnClickListener(v -> beginSelected());

    }

    private void startBreath() {
        thread = new Thread(changeBreathRunnable){
            @Override

            public void run() {
                try {
                    Thread.sleep(breathRemainingTime);
                } catch (InterruptedException e) {
                    return;
                }
                super.run();
            }
        };
        thread.start();
    }

    public void reset() {
        breathRemainingTime = 3000;
    }

    public interface OnCustomEventListener {
        void onEvent();
    }

    public void setCustomEventListener(OnCustomEventListener eventListener) {
        mListener = eventListener;

    }

    private void beginSelected() {
        if(breath.isInhaling()){
            begin.setText(OUT);
        } else {
            begin.setText(IN);
        }

        begin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    startBreath();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    reset();
                }

                return true;
            }
        });


    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        switch (pos) {
            case 0:
                breath.setBreathNum(1);
                break;
            case 1:
                breath.setBreathNum(2);
                break;
            case 2:
                breath.setBreathNum(3);
                break;
            case 3:
                breath.setBreathNum(4);
                break;
            case 4:
                breath.setBreathNum(5);
                break;
            case 5:
                breath.setBreathNum(6);
                break;
            case 6:
                breath.setBreathNum(7);
                break;
            case 7:
                breath.setBreathNum(8);
                break;
            case 8:
                breath.setBreathNum(9);
                break;
            case 9:
                breath.setBreathNum(10);
                break;
        }

    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.breathsSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breathsSpinner.setAdapter(adapter);
    }
}
