package com.example.aikyam;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class modifyEvent extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    public static String e_id="";
    public static String date="";
    public static String venue="";
    public static String desc="";
    TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.events_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        description=(TextView)findViewById(R.id.desc);
        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.drawableArray.length; i++) {
            String s="a";
            String d="2018/03/15";
            String e_id=String.valueOf(i);
            String des="jfgbxmnbxvckvbdjxchnvb dshgfsdkhgfdkj hdshgdf<sljgfhclk hfdgfskhjdkghf haudkfjhsdfskjgh hdksjfhksdjghk";
            data.add(new DataModel(
                    s,des,d,MyData.drawableArray[i],e_id
            ));
        }

//        removedItems = new ArrayList<Integer>();

        adapter = new AdapterModify(data);
        recyclerView.setAdapter(adapter);


    }

    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent loginIntent = new Intent(v.getContext(),modifyEventInfo.class);
//            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.putExtra("e_id",e_id);
            loginIntent.putExtra("cname",venue);
//            loginIntent.putExtra("desc",desc);
//            loginIntent.putExtra("date",date);

            v.getContext().startActivity(loginIntent);
        }


    }

    public void invokeWS(JSONObject params){
        // Show Progress Dialog




//        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            StringEntity entity = new StringEntity(params.toString());
            client.post(this, "https://www.google.com", entity, "application/json", new AsyncHttpResponseHandler() {
                // When the response returned by REST has Http response code '200'
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // Hide Progress Dialog
//                prgDialog.hide();
                    try {
                        // JSON Object
                        String str=new String(response);
                        JSONObject jsnobject = new JSONObject(str);
                        JSONArray jsonArray = jsnobject.getJSONArray("events");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ob = jsonArray.getJSONObject(i);
                            e_id=ob.getString("e_id");
                            venue=ob.getString("c_name");
                            date=ob.getString("date");
                            desc=ob.getString("desc");
                            data.add(new DataModel(
                                    venue,desc,date,MyData.drawableArray[i],e_id
                            ));

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
//                prgDialog.hide();
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


    }


}