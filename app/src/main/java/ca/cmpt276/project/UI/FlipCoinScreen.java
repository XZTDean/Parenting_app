package ca.cmpt276.project.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.TextView;

import java.sql.BatchUpdateException;

import ca.cmpt276.project.R;

public class FlipCoinScreen extends Activity {

    private ImageView image1;
    private ImageView image2;

    private boolean isFirstImage = true;

    public static Intent makeLaunchIntent(Context context) {
        Intent intent = new Intent(context, FlipCoinScreen.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_screen);

        // setup pop up screen to choose Heads or tails
        //popUpScreen();


        // flip animation
        /*image1 = (ImageView) findViewById(R.id.ImageView01);
        image2 = (ImageView) findViewById(R.id.ImageView02);
        image2.setVisibility(View.GONE);

        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isFirstImage) {
                    applyRotation(0, 90);
                    isFirstImage = !isFirstImage;

                } else {
                    applyRotation(0, 90);
                    isFirstImage = !isFirstImage;
                }
            }
        });*/
    }


    private void applyRotation(float start, float end) {

        // Find the center of image
        final float centerX = image1.getWidth() / 2.0f;
        final float centerY = image1.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(isFirstImage, image1, image2));

        if (isFirstImage)
        {
            image1.startAnimation(rotation);
        } else {
            image2.startAnimation(rotation);
        }

    }

    private void popUpScreen() {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custom_pop_up);

        TextView closeButton;
        closeButton = (TextView) myDialog.findViewById(R.id.textX);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        Button heads;
        heads = (Button) myDialog.findViewById(R.id.buttonHeads);
        heads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chose heads
            }
        });

        Button tails;
        tails = (Button) myDialog.findViewById(R.id.buttonTails);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}