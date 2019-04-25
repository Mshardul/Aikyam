package com.example.aikyam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class contactUs extends AppCompatActivity {
    ProgressDialog prgDialog;
//    TextView errorMsg;
    EditText name;
    EditText email;
    EditText phone;
    EditText message;
    EditText subject;
//    Button feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name=(EditText)findViewById(R.id.nameF);
        email=(EditText)findViewById(R.id.emailF);
        phone=(EditText)findViewById(R.id.phoneF);
        message=(EditText)findViewById(R.id.nameF);
        subject=(EditText)findViewById(R.id.subjectF);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void sendFeedback(View view)
    {
        String nameT= name.getText().toString();
        String emailT= email.getText().toString();
        String phoneT= phone.getText().toString();
        String subjectT=subject.getText().toString();
        String messageT= message.getText().toString();
        String[] TO = {"saurabh.pahwa@iiitb.org"};
//        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setType("text/plain");

        emailIntent.setDataAndType(Uri.parse("mailto:saurabhphw@gmail.com"),"text/plain");

//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subjectT);
        emailIntent.putExtra(Intent.EXTRA_TEXT,"Name: "+nameT+"\nPhone: "+phoneT+"\nEmail: "+emailT+"\n Message:  "+messageT);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Toast.makeText(getApplicationContext(), "Thanks for your feedback. We will contact to you shortly. ", Toast.LENGTH_LONG).show();

            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    }


