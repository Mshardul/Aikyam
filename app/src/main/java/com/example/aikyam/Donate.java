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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import android.content.DialogInterface;


import android.view.View.OnClickListener;
import java.util.*;
import android.widget.ArrayAdapter;

public class Donate extends AppCompatActivity {


    ProgressDialog prgDialog;
    ArrayList<String> category_list;
    CheckBox anonymous;

    LinearLayout parent;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

//        errorMsg = (TextView)findViewById(R.id.register_error);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        builder = new AlertDialog.Builder(this);

        anonymous=(CheckBox)findViewById(R.id.anonymous);
        parent = (LinearLayout)findViewById(R.id.dynamic_content);
        category_list=new ArrayList<>();
//         category_list.add("Books");
//         category_list.add("Clothes");
//         category_list.add("Toys");
        JSONObject par=new JSONObject();
        try {
            par.put("", "");
            category_list= invokeWS(par);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
//        LinearLayout l1 = new LinearLayout(this);
//        parent.addView(l1);


    }

    public void performDonation(View view){

        JSONArray categories;
        JSONArray quantity;
        ArrayList<String> cat_list=new ArrayList<>();
        ArrayList<String> quan_list=new ArrayList<>();

        JSONObject j = new JSONObject();
        String checked="";
        try {

            j.put("donorId",String.valueOf(1));
            CheckBox c;
            EditText e;
            for (int i = 0; i < category_list.size(); i++) {
                c = (CheckBox) findViewById(100+i);
                if (c.isChecked()) {
                    e = (EditText) findViewById(i);
                    cat_list.add(category_list.get(i));
                    quan_list.add(e.getText().toString());

                }

                categories=new JSONArray(cat_list);
                quantity=new JSONArray(quan_list);
                j.put("i_name", categories);
                j.put("qty",quantity);

                if(anonymous.isChecked())
                    checked=String.valueOf(1);
                else
                    checked=String.valueOf(0);
                j.put("isAnon",checked);
            }
            String json= j.toString();
            if(Utility.isNotNull(json)){
                invokeW(j);
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
        url="http://172.16.130.228:8080/proj/user/donate";
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
                        Toast.makeText(getApplicationContext(), "Thanks for the Donation.Our representative will contact you shortly :)", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Error Creating Donation!!", Toast.LENGTH_LONG).show();
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
            client.post(this, "http://172.16.130.228:8080/proj/item/getAll", entity, "application/json", new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
//                    String str = new String(response);
//                    JSONArray jsonArray = new JSONArray(str);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject ob = response.getJSONObject(i);

                        venue_names.add(ob.getString("i_name"));
                    }
                    for(int i=0;i<category_list.size();i++)
                    {
                        LinearLayout ll = new LinearLayout(getApplicationContext());
                        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ll.setOrientation(LinearLayout.HORIZONTAL);

                        CheckBox cb = new CheckBox(getApplicationContext());
                        cb.setText(category_list.get(i));
                        cb.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                1.0f
                        ));
                        cb.setId(100+i);

                        parent.addView(ll);
                        ll.addView(cb);
                        EditText et = new EditText(getApplicationContext());
                        et.setHint("Quantity");
                        et.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                1.0f
                        ));
                        et.setId(i);
                        ll.addView(et);



                    }
                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }


            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers,  Throwable e, JSONArray errorResponse) {
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
        catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
        return venue_names;

    }

    public void navigateToHomeActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),Donor_Home.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}
