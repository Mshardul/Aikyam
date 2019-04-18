package com.example.aikyam;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import android.widget.Spinner;
import android.app.AlertDialog;
import android.widget.SpinnerAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import android.content.DialogInterface;


import android.view.View.OnClickListener;
import java.util.*;
import android.widget.ArrayAdapter;

public class Donate extends AppCompatActivity {

    EditText quantity;

    Spinner spinner;
    ProgressDialog prgDialog;
    TextView errorMsg;
    CheckBox anonymous;
    Button donate;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__event);

//        errorMsg = (TextView)findViewById(R.id.register_error);
        quantity=(EditText)findViewById(R.id.quantity);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        donate=(Button)findViewById(R.id.donate);
        builder = new AlertDialog.Builder(this);

        anonymous= (CheckBox) findViewById(R.id.anonymous);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner= (Spinner) findViewById(R.id.category_select);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> options=new ArrayList<String>();

        options.add("Ngo 1");
        options.add("Ngo 2");
        options.add("Ngo 3");

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

               FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void performDonation(View view){

        String category = String.valueOf(spinner.getSelectedItem());
        String quantityT=quantity.getText().toString();
        String checked="";
        if(anonymous.isChecked())
            checked=String.valueOf(1);
        else
            checked=String.valueOf(2);
        builder.setTitle("Alert");
        builder.setMessage("Category:"+category+"\nQuantity:"+quantityT+"\nAnonymous:"+checked).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }


                });
        AlertDialog alert = builder.create();

        alert.show();
    }

    /*// Instantiate Http Request Param Object
    RequestParams params = new RequestParams();
    // When Name Edit View, Email Edit View and Password Edit View have values other than Null
    if(Utility.isNotNull(name) && Utility.isNotNull(username) && Utility.isNotNull(location) && Utility.isNotNull(email) && Utility.isNotNull(phone) && Utility.isNotNull(password)){
        // When Email entered is Valid
        if(Utility.validate(email) && rePass.equals(password)){
            // Put Http parameter name with value of Name Edit View control
            params.put("name", name);
            // Put Http parameter name with value of Username Edit View control
            params.put("username", username);
            // Put Http parameter name with value of Password Edit View control
            params.put("password", password);
            // Put Http parameter name with value of Location Edit View control
            params.put("location", location);
            // Put Http parameter username with value of Email Edit View control
            params.put("email", email);
            // Put Http parameter password with value of Password Edit View control
            params.put("phone", phone);
            params.put("type",type);
            // Invoke RESTful Web Service with Http parameters
            invokeWS(params);
        }
        // When Email is invalid
        else{
            if(!Utility.validate(email))
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Both the passwords must match", Toast.LENGTH_LONG).show();
        }
    }
    // When any of the Edit View control left blank
    else{
        Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
    }

}
/**
 * Method that performs RESTful webservice invocations
 *
 * @param params

public void invokeWS(RequestParams params){
    // Show Progress Dialog
    prgDialog.show();
    // Make RESTful webservice call using AsyncHttpClient object
    AsyncHttpClient client = new AsyncHttpClient();
    client.get("https://www.google.com",params,new AsyncHttpResponseHandler() {
        // When the response returned by REST has Http response code '200'
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            // Hide Progress Dialog
            prgDialog.hide();
            try {
                // JSON Object
                String str=new String(response);
                JSONObject obj = new JSONObject(str);
                // When the JSON response has status boolean value assigned with true
                if(obj.getBoolean("status")){
                    // Set Default Values for Edit View controls
                    setDefaultValues();
                    // Display successfully registered message using Toast
                    Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                }
                // Else display error message
                else{
                    errorMsg.setText(obj.getString("error_msg"));
                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }


        // When the response returned by REST has Http response code other than '200'
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            // Hide Progress Dialog
            prgDialog.hide();
            // When Http response code is '404'
            if(statusCode == 404){
                Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
            }
            // When Http response code is '500'
            else if(statusCode == 500){
                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
            }
            // When Http response code other than 404, 500
            else{
                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            }
        }
    });
}

*/



}
