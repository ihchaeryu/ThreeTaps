package com.example.threetaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactDetailActivity extends AppCompatActivity {

    // creating variables for our image view and text view and string.
    private String contactName, contactNumber;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // on below line we are getting string data which
        // we passed in our adapter class with intent.
        contactName = getIntent().getStringExtra("name");
        contactNumber = getIntent().getStringExtra("contact");

        // initializing our views.
        // bind each view with id
        nameTV = findViewById(R.id.TVName);
        contactIV = findViewById(R.id.IVAccount);
        contactTV = findViewById(R.id.TVPhone);
        callIV = findViewById(R.id.IVCall);
        // set each text view
        nameTV.setText(contactName);
        contactTV.setText(contactNumber);

        // on below line adding click listener for our calling image view.
        callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to make a call.
                makeCall(contactNumber);
            }
        });

    }

    private void makeCall(String contactNumber) {
        // this method is called for making a call.
        // on below line we are calling an intent to make a call.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // on below line we are setting data to it.
        callIntent.setData(Uri.parse("tel:" + contactNumber));
        // on below line we are checking if the calling permissions are granted not.
        if (ActivityCompat.checkSelfPermission(ContactDetailActivity.this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // at last we are starting activity.
        startActivity(callIntent);
    }

}