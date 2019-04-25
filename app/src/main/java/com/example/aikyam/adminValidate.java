package com.example.aikyam;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class adminValidate extends AppCompatActivity {
    private String m_Text = "";
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_validate);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login");



// Set up the input
         input = new EditText(this);
         input.setHint("Enter Secret Key");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                loginAdmin();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                navigateToMain();
            }
        });

        builder.show();
    }
    public void loginAdmin(){

        JSONObject params = new JSONObject();

        if(Utility.isNotNull(m_Text)){

            try {
                // Put Http parameter username with value of Email Edit View control
                params.put("password", m_Text);

                invokeWS(params);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }


        } else{
            Toast.makeText(getApplicationContext(), "Please fill the key, don't leave field blank", Toast.LENGTH_LONG).show();
            navigateToMain();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(JSONObject params){


        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String url="";


        try {
            StringEntity entity = new StringEntity(params.toString());
            client.post(this, url, entity, "application/json",new AsyncHttpResponseHandler() {
                // When the response returned by REST has Http response code '200'
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // Hide Progress Dialog

                    // JSON Object
                    //String str=new String(response);
                    //JSONObject obj = new JSONObject(str);
                    // When the JSON response has status boolean value assigned with true
                    if(statusCode== 200){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        navigateToAdmin();
                    }

                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

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
            e.printStackTrace();
        }
    }




    public void navigateToAdmin(){
        Intent homeIntent = new Intent(getApplicationContext(),Admin_Home.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    public void navigateToMain(){
        Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


}
