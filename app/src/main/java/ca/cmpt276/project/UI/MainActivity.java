package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.CoinFlip;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Button configureChildren = findViewById(R.id.configureChildren);
        configureChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildManager.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });*/

        Button coinFlip = findViewById(R.id.coinFlip);
        coinFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlipCoinScreen.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        /*Button timeoutTimer = findViewById(R.id.timeoutTimer);
        timeoutTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildManager.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
*/

    }
}