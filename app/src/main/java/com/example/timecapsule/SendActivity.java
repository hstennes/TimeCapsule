package com.example.timecapsule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class SendActivity extends AppCompatActivity {

    private String message;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        getSupportActionBar().setTitle(getString(R.string.toolbar_send));

        EditText editPhone = findViewById(R.id.edit_phone);
        editPhone.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Intent intent = getIntent();
        message = intent.getStringExtra(getString(R.string.extra_message));
        if(intent.hasExtra(getString(R.string.extra_image))) {
            imageUri = Uri.parse(intent.getStringExtra(getString(R.string.extra_image)));
        }

        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendMessage()) startActivity(new Intent(SendActivity.this, DoneActivity.class));
            }
        });
        setupSpinner();
    }

    private void setupSpinner(){
        Spinner mSpinner = findViewById(R.id.spinner_timezone);
        String[] idArray = TimeZone.getAvailableIDs();
        ArrayAdapter<String> idAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                idArray);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(idAdapter);

        for(int i = 0; i < idAdapter.getCount(); i++) {
            if(idAdapter.getItem(i).equals(TimeZone.getDefault().getID())) {
                mSpinner.setSelection(i);
            }
        }
    }

    private boolean sendMessage(){
        String phone = ((EditText) findViewById(R.id.edit_phone)).getText().toString();
        if(phone.equals("")){
            Toast.makeText(this, getString(R.string.toast_no_phone), Toast.LENGTH_SHORT).show();
            return false;
        }

        TimePicker time = findViewById(R.id.picker_time);
        DatePicker date = findViewById(R.id.picker_date);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, time.getMinute());
        cal.set(Calendar.HOUR_OF_DAY, time.getHour());
        cal.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        cal.set(Calendar.MONTH, date.getMonth());
        cal.set(Calendar.YEAR, date.getYear());
        long millis = cal.getTimeInMillis();

        if(Calendar.getInstance().getTimeInMillis() - millis > 1) {
            Toast.makeText(this, getString(R.string.toast_invalid_time), Toast.LENGTH_SHORT).show();
            return false;
        }

        String timeZone = ((Spinner) findViewById(R.id.spinner_timezone)).getSelectedItem().toString();
        long offsetMillis = cal.getTimeInMillis() - TimeZone.getTimeZone(timeZone).getOffset(millis);
        new FutureMessage(message, imageUri, phone, offsetMillis).upload();
        return true;
    }
}
