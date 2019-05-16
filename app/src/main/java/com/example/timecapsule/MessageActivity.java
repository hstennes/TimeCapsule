package com.example.timecapsule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MessageActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;
    private Button buttonImage;
    private ImageView imageView;
    private Uri imageUri;

    private boolean showingImage;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        showingImage = false;
        getSupportActionBar().setTitle(getString(R.string.toolbar_message));
        imageView = findViewById(R.id.image);

        Button buttonNext = findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonPressed();
            }
        });

        buttonImage = findViewById(R.id.button_image);
        buttonImage.setText(R.string.button_add_image);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingImage) {
                    imageView.setVisibility(View.GONE);
                    buttonImage.setText(R.string.button_add_image);
                    showingImage = false;
                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    showingImage = true;
                }
            }
        });
    }

    public void nextButtonPressed(){
        EditText editMessage = findViewById(R.id.edit_message);
        String message = editMessage.getText().toString();

        if(message.equals("")) {
            Toast.makeText(this, getString(R.string.toast_no_message), Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(MessageActivity.this, SendActivity.class);
            intent.putExtra(getString(R.string.extra_message), editMessage.getText().toString());
            if(imageUri != null) intent.putExtra(getString(R.string.extra_image), imageUri.toString());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE);
            buttonImage.setText(getString(R.string.button_remove_image));
        }
    }
}
