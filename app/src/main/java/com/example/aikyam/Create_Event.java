package com.example.aikyam;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Button;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import android.content.DialogInterface;
//import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


import android.view.View.OnClickListener;
import java.util.*;
import android.widget.ArrayAdapter;

public class Create_Event extends AppCompatActivity {
    Calendar myCalendar;
    EditText select_date;
    DatePickerDialog.OnDateSetListener date;
//    Spinner spinner;
    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText desc;
    Button createEvent;
    AlertDialog.Builder builder;
    MultiSelectionSpinner spinner;
    ArrayList<String> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__event);
        myCalendar = Calendar.getInstance();
        errorMsg = (TextView)findViewById(R.id.register_error);
        desc=(EditText)findViewById(R.id.desc);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        createEvent=(Button)findViewById(R.id.create_event);
        builder = new AlertDialog.Builder(this);
        options=new ArrayList<>();

        select_date= (EditText) findViewById(R.id.date);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = (MultiSelectionSpinner) findViewById(R.id.venue_select);

        options.add("Ngo1");
        options.add("Ngo2");
        options.add("Ngo3");
        spinner.setItems(options);

        JSONObject par=new JSONObject();
        try {
            par.put("", "");
            //options= invokeWS(params);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        // When Name Edit View, Email Edit View and Password Edit View have values other than Null




//
//
//// use default spinner item to show options in spinner
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner.setAdapter(adapter);

        date= new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        select_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Create_Event.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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

    public void createEvent(View view) {

        ArrayList<String> venue_list;
        venue_list = spinner.getSelectedItemsAsList();
        JSONArray jsArray = new JSONArray(venue_list);
        String s = spinner.getSelectedItemsAsString();

//        Log.e("getSelected", s);

        String date = select_date.getText().toString();
        String description = desc.getText().toString();

        builder.setTitle("Alert");
        builder.setMessage("venue:" + s + "\ndate:" + date + "\ndescription:" + description).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }


                });
        AlertDialog alert = builder.create();



       JSONObject params = new JSONObject();



        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(s) && Utility.isNotNull(date) && Utility.isNotNull(description) ){
            // When Email entered is Valid

                try {
                    // Put Http parameter name with value of Name Edit View control
                    params.put("venue", jsArray);
                    // Put Http parameter name with value of Username Edit View control
                    params.put("date",date );
                    // Put Http parameter name with value of Password Edit View control
                    params.put("description", description);

                    invokeW(params);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            alert.show();


        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

   public void invokeW(JSONObject params){
        // Show Progress Dialog
        String url="";
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        url="http://192.168.43.46:8080/proj/user/regD";
        try {
            StringEntity entity = new StringEntity(params.toString());
            client.post(this, url, entity, "application/json",new AsyncHttpResponseHandler() {
                // When the response returned by REST has Http response code '200'
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // Hide Progress Dialog
                    prgDialog.hide();
                    // try {
                    // JSON Object
//                        String str=new String(response);
//                        JSONObject obj = new JSONObject(str);
                    // When the JSON response has status boolean value assigned with true
                    if(statusCode==200){

                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Event created Successfully!!", Toast.LENGTH_LONG).show();
                        navigatetoHomeActivity();
                    }

                }


                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // Hide Progress Dialog
                    prgDialog.hide();
                    // When Http response code is '400'
                    if(statusCode == 400){
                        Toast.makeText(getApplicationContext(), "Error Creating Event!!", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 400, 500
                    else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }




    public ArrayList<String> invokeWS(JSONObject params){
        // Show Progress Dialog

        final ArrayList<String> venue_names=new ArrayList<>();


        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            StringEntity entity = new StringEntity(params.toString());
            client.post(this, "https://www.google.com", entity, "application/json", new AsyncHttpResponseHandler() {
                // When the response returned by REST has Http response code '200'
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // Hide Progress Dialog
                    prgDialog.hide();
                    try {
                        // JSON Object
                        String str = new String(response);
                        JSONObject jsnobject = new JSONObject(str);
                        JSONArray jsonArray = jsnobject.getJSONArray("venues");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ob = jsonArray.getJSONObject(i);

                            venue_names.add(ob.getString("venue"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // Hide Progress Dialog
                    prgDialog.hide();
                    // When Http response code is '404'
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404, 500
                    else {
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
        catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
        return venue_names;
    }

    private void updateLabel(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        select_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void navigatetoHomeActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),Admin_Home.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }


}
