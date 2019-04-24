package com.example.aikyam;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class Signup extends AppCompatActivity {
    private static final String TAG = "Signup";

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText nameT;
    EditText usernameT;
    EditText locationT;
    EditText emailT;
    EditText phoneT;
    EditText passwordT;
    EditText reEnterPassword;
    Button signupButton;
    TextView loginLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.register_error);
        nameT=(EditText)findViewById(R.id.name);
        usernameT=(EditText)findViewById(R.id.username);
        locationT=(EditText)findViewById(R.id.location);
        emailT=(EditText)findViewById(R.id.email);
        phoneT=(EditText)findViewById(R.id.phone);
        passwordT=(EditText)findViewById(R.id.password);
        reEnterPassword=(EditText)findViewById(R.id.reEnterPassword);
        signupButton=(Button)findViewById(R.id.btn_signup);
        loginLink=(TextView)findViewById(R.id.link_login);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        Toolbar toolbar = findViewById(R.id.toolbar);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatetoLoginActivity();
            }
        });


    }

    public void registerUser(View view){

        String name = nameT.getText().toString();
        String username=usernameT.getText().toString();
        String location = locationT.getText().toString();
        String email = emailT.getText().toString();
        String phone = phoneT.getText().toString();
        String password = passwordT.getText().toString();
        String rePass=reEnterPassword.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton1 = (RadioButton) findViewById(R.id.donor);
        radioButton2 = (RadioButton) findViewById(R.id.receiver);
        String type="";
        if(radioButton1.isChecked())
            type=String.valueOf(1);
        else if(radioButton2.isChecked())
            type=String.valueOf(2);


        // Instantiate Http Request Param Object
       // RequestParams params = new RequestParams();
        JSONObject params = new JSONObject();


        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(name) && Utility.isNotNull(username) && Utility.isNotNull(location) && Utility.isNotNull(email) && Utility.isNotNull(phone) && Utility.isNotNull(password)){
            // When Email entered is Valid
            if(Utility.validate(email) && rePass.equals(password)){
                try {
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
                    // Invoke RESTful Web Service with Http parameters
                    invokeWS(params, type);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
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
     */
    public void invokeWS(JSONObject params,String type){
        // Show Progress Dialog
        String url="";
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        if(type.equals("1"))
            url="http://172.16.130.228:8080/proj/user/regD";
        else if(type.equals("2"))
            url="http://172.16.130.228:8080/proj/user/regC";
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
                            // Set Default Values for Edit View controls
                            setDefaultValues();
                            // Display successfully registered message using Toast
                            Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                            navigatetoLoginActivity();
                        }
                        // Else display error message

                    /*} catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

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
                    // When Http response code other than 400, 500
                    else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {

        }

}

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        nameT.setText("");
        usernameT.setText("");
        emailT.setText("");
        locationT.setText("");
        passwordT.setText("");
        phoneT.setText("");
    }
}


