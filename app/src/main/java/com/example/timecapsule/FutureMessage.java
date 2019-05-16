package com.example.timecapsule;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class FutureMessage {

    private String message;
    private Uri imageUri;
    private String phone;
    private long millis;

    public FutureMessage(String message, Uri imageUri, String phone, long millis){
        this.message = message;
        this.imageUri = imageUri;
        this.phone = phone;
        this.millis = millis;
    }

    public void upload(){
        DatabaseReference dbRef = FirebaseDatabase
                .getInstance()
                .getReference(getDateString())
                .push();
        dbRef.child("message").setValue(message);
        dbRef.child("phone").setValue(phone);

        if(imageUri != null){
            dbRef.child("image").setValue(true);
            StorageReference storageRef = FirebaseStorage
                    .getInstance()
                    .getReference(getDateString())
                    .child(dbRef.getKey());
            storageRef.putFile(imageUri);
        }
        else dbRef.child("image").setValue(false);
    }

    private String getDateString(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.MINUTE) + "-" +
                cal.get(Calendar.HOUR_OF_DAY) + "-" +
                cal.get(Calendar.DAY_OF_MONTH) + "-" +
                cal.get(Calendar.MONTH) + "-" +
                cal.get(Calendar.YEAR);
    }

}
