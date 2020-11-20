package ca.cmpt276.project.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;

/*
 * ChildrenPhotoActivity provides the user interface
 * in which pictures can be added to each Child. Users
 * may upload such photos from their gallery, create a
 * new photo, or use the default photo.
 */
public class ChildrenPhotoActivity extends AppCompatActivity {

    static final int REQUEST_GALLERY_IMAGE = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button continueButton;
    private ImageView imageView;

    private Bitmap childPhoto;
    private static Child child;

    public static Intent makeIntent(Context context, Child childInput) {
        child = childInput;
        return new Intent(context, ChildrenPhotoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_photo);

        imageView = (ImageView) findViewById(R.id.displayPhoto);

        child.setPhoto(childPhoto);

        setToolbar();
        setButtons();

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setButtons(){
        continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        Button galleryUploadButton = (Button) findViewById(R.id.galleryUploadButton);
        galleryUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Uploading photo from gallery
                // Adapted from: https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        REQUEST_GALLERY_IMAGE);
            }
        });

        Button newPhotoButton = (Button) findViewById(R.id.newPhotoButton);
        newPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Taking a new photo
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Context context = getApplicationContext();
                    CharSequence text = "Camera is currently unavailable.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        Button defaultPhotoButton = (Button) findViewById(R.id.defaultPhotoButton);
        defaultPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("UseCompatLoadingForDrawables")
            public void onClick(View v) {
                Drawable defaultImage = getResources().getDrawable(R.drawable.default_photo_jerry);
                imageView.setImageDrawable(defaultImage);
                childPhoto = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                child.setPhoto(childPhoto);
                continueButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //
        if(requestCode==REQUEST_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(imageBitmap);
                childPhoto = imageBitmap;
                child.setPhoto(childPhoto);
                continueButton.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode==REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            //assert extras != null;
            if(extras !=  null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                childPhoto = imageBitmap;
                child.setPhoto(childPhoto);
                continueButton.setVisibility(View.VISIBLE);
            }
        }

    }

}
