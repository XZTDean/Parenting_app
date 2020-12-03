package ca.cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class BreathActivity extends AppCompatActivity {

    //private BreathManager breathManager = BreathManager.g;

    OnCustomEventListener mListener;



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

    public interface OnCustomEventListener {
        void onEvent();
    }

    public void setCustomEventListener(OnCustomEventListener eventListener) {
        mListener = eventListener;

    }

    private void beginSelected() {
        begin.setText("In");
        breath.startBreath();
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
