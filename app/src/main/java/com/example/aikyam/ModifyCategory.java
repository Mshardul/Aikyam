package com.example.aikyam;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import org.json.JSONArray;


import android.widget.SpinnerAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import android.content.DialogInterface;


import android.view.View.OnClickListener;
import java.util.*;
import android.widget.ArrayAdapter;

public class ModifyCategory extends AppCompatActivity {


    ProgressDialog prgDialog;
    ArrayList<String> category_list;


    AlertDialog.Builder builder;
    EditText et;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_category);

//        errorMsg = (TextView)findViewById(R.id.register_error);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        builder = new AlertDialog.Builder(this);
        et=(EditText)findViewById(R.id.addNew);

        //        anonymous=(CheckBox)findViewById(R.id.anonymous);
        LinearLayout parent = (LinearLayout)findViewById(R.id.dynamic);
        category_list=new ArrayList<>();
        category_list.add("Books");
        category_list.add("Clothes");
        category_list.add("Toys");
//        LinearLayout l1 = new LinearLayout(this);
//        parent.addView(l1);
        for(int i=0;i<category_list.size();i++)
        {


            CheckBox cb = new CheckBox(this);
            cb.setText(category_list.get(i));
            cb.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT

            ));
            cb.setId(200+i);

            parent.addView(cb);




        }

    }

    public void performModification(View view){

        JSONArray categories;
        ArrayList<String> cat_list=new ArrayList<>();
        String add=et.getText().toString();

        JSONObject j = new JSONObject();
        try {



            CheckBox c;
            for (int i = 0; i < category_list.size(); i++) {
                c = (CheckBox) findViewById(200+i);
                if (c.isChecked()) {
                    cat_list.add(category_list.get(i));

                }

                categories=new JSONArray(cat_list);
                j.put("delete", categories);
                j.put("add", add);



            }
            String json= j.toString();
            if(Utility.isNotNull(json)){
//                invokeW(j);
                builder.setTitle("Alert");
                builder.setMessage("Json:"+j).setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();

                            }


                        });
                AlertDialog alert = builder.create();

                alert.show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
            }
        }
        catch(JSONException e) {

            e.printStackTrace();

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
                        Toast.makeText(getApplicationContext(), "Event Modified Successfully:)", Toast.LENGTH_LONG).show();
                        navigateToHomeActivity();
                    }

                }


                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // Hide Progress Dialog
                    prgDialog.hide();
                    // When Http response code is '400'
                    if(statusCode == 400){
                        Toast.makeText(getApplicationContext(), "Error modifying event!!", Toast.LENGTH_LONG).show();
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




    public ArrayList<String> invokeWS(RequestParams params){
        // Show Progress Dialog

        final ArrayList<String> venue_names=new ArrayList<>();


        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("https://www.google.com",params,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    String str=new String(response);
                    JSONObject jsnobject = new JSONObject(str);
                    JSONArray jsonArray = jsnobject.getJSONArray("venues");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject ob = jsonArray.getJSONObject(i);

                        venue_names.add(ob.getString("venue"));
                    }
                } catch (JSONException e) {

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

        return venue_names;

    }

    public void navigateToHomeActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),Donor_Home.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}
