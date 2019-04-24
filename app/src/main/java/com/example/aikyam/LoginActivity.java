package com.example.aikyam;


import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 *
 * Login Activity Class
 *
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;
    public static final String MyPREFERENCES = "Bin" ;
    public static final String Username = "username";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.login_error);
        // Find Email Edit View control by ID
        emailET = (EditText)findViewById(R.id.email);
        // Find Password Edit View control by ID
        pwdET = (EditText)findViewById(R.id.password);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);


        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
       // Toolbar toolbar = findViewById(R.id.toolbar);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        //setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void loginUser(View view){
        // Get Email Edit View Value
        String email = emailET.getText().toString();
        // Get Password Edit View Value
        String password = pwdET.getText().toString();
        // Instantiate Http Request Param Object
//        RequestParams params = new RequestParams();
        JSONObject params = new JSONObject();
        //int selectedId = radioGroup.getCheckedRadioButtonId();
        //Toast.makeText(getApplicationContext(), String.valueOf(selectedId), Toast.LENGTH_LONG).show();
        // find the radiobutton by returned id
        radioButton1 = (RadioButton) findViewById(R.id.donor);
        radioButton2 = (RadioButton) findViewById(R.id.receiver);
        String type="";
        if(radioButton1.isChecked())
            type=String.valueOf(1);
        else if(radioButton2.isChecked())
        type=String.valueOf(2);
        // When Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(email) && Utility.isNotNull(password)){
            // When Email entered is Valid

                try {
                // Put Http parameter username with value of Email Edit View control
                params.put("username", email);
                // Put Http parameter password with value of Password Edit Value control
                params.put("password", password);
                // Invoke RESTful Web Service with Http parameters
                invokeWS(params,type);
            }
                catch (JSONException e) {
                    e.printStackTrace();
                }


        } else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(JSONObject params, final String type){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String url="";

        if(type.equals("1"))
            url="http://172.16.130.228:8080/proj/user/loginD";
        else if(type.equals("2"))
            url="http://172.16.130.228:8080/proj/user/loginC";
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
        try {
            StringEntity entity = new StringEntity(params.toString());
            client.post(this, url, entity, "application/json",new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // Hide Progress Dialog
                prgDialog.hide();

                    // JSON Object
                    //String str=new String(response);
                    //JSONObject obj = new JSONObject(str);
                    // When the JSON response has status boolean value assigned with true
                    if(statusCode== 200){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(Username, emailET.getText().toString());

                        editor.commit();
                        // Navigate to Home screen
                        if(type.equals("1"))
                            navigateToDonorHomeActivity();
                        else if(type.equals("2"))
                            navigateToReceiverHomeActivity();
                    }
                    // Else display error message
                    /*else{
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }*/
                }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '400'
                if(statusCode == 400){
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
        }); }
        catch(Exception e)
        {

        }
    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigateToDonorHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),Donor_Home.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    public void navigateToDonorHomeActivity(View view){
        Intent homeIntent = new Intent(getApplicationContext(),Donor_Home.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    public void navigateToReceiverHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),Home.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void navigateToRegisterActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),Signup.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

}