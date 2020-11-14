package ca.cmpt276.project.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;

import ca.cmpt276.project.R;

public class ChildrenPhotoActivity extends AppCompatActivity {

    static public int GALLERY_PHOTO = 0;
    static public int NEW_PHOTO = 1;


    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildrenPhotoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_photo);

        Button galleryUploadButton = findViewById(R.id.galleryUploadButton);
        galleryUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Uploading photo from gallery
                // Adapted from: https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        1);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                changePhoto(bitmap, GALLERY_PHOTO);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void changePhoto(Bitmap bitmap, int uploadType){
        if(uploadType == GALLERY_PHOTO){
            ImageView imageView = (ImageView) findViewById(R.id.galleryPhoto);
            imageView.setImageBitmap(bitmap);
        } else {

        }
    }
}
