package com.example.it354f701.CartShare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {

    //    RequestQueue queue = Volley.newRequestQueue(this);
    private Button login, register;
    int wrongCnt = 0;
    //    private EditText userName, userPassword;
    private LoginBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.LoginButton);
        register = findViewById(R.id.RegisterButton);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.LoginButton:
//                if (wrongCnt >= 3) {
//                    Toast.makeText(this, "Attempts exceeded", Toast.LENGTH_SHORT).show();
//
//                    break;
//                }
                user = new LoginBean();
                user.setUserName(((TextView) findViewById(R.id.userName)).getText().toString());
                user.setUserPassword(((TextView) findViewById(R.id.userPassword)).getText().toString());

                final String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.logininfo/" + user.getUserName();

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // display response
                                try {
                                    if (user.getUserPassword().equals(response.get("password"))) {

//                                        Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                                        Intent intent = new Intent(getApplicationContext(), CartList.class);
                                        intent.putExtra("userId", user.getUserName());
                                        startActivity(intent);
                                        Log.d("Response", response.toString());
                                    } else {
                                        wrongCnt += 1;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("tag", "Error: " + error.getMessage());
                                Toast.makeText(getApplicationContext(), "No User found ... ", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                AppController.getInstance(this).addToRequestQueue(getRequest);
                break;
            case R.id.RegisterButton:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

        }
    }
}
