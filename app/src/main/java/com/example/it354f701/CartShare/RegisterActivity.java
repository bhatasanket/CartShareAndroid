package com.example.it354f701.CartShare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "debug";
    private static final String TYPE_UTF8_CHARSET = "charset=UTF-8";
    Button register;
    EditText firstName, lastName, password, repassword, email;
    AccountBean accountBean = new AccountBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.regFirstName);
        lastName = findViewById(R.id.regLastName);
        password = findViewById(R.id.regPassword);
        repassword = findViewById(R.id.regRePassword);
        email = findViewById(R.id.regEmail);
        register = (Button) findViewById(R.id.regButton);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

//        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        accountBean.setFirstName(firstName.getText().toString());
        accountBean.setLastName(lastName.getText().toString());
        accountBean.setPassword(password.getText().toString());
        accountBean.setEmail(email.getText().toString());
        if (accountBean.getFirstName().equals("")
                || accountBean.getLastName().equals("")
                || accountBean.getPassword().equals("")
                || accountBean.getEmail().equals("")
                ) {
            Toast.makeText(this, "Please enter all th information", Toast.LENGTH_LONG).show();
        } else if (!accountBean.getPassword().equals(repassword.getText().toString())) {
            Toast.makeText(this, "Passwords Do Not Match", Toast.LENGTH_LONG).show();
        } else if (accountBean.getFirstName().length() < 2
                || accountBean.getFirstName().length() > 25
                || accountBean.getLastName().length() < 2
                || accountBean.getLastName().length() > 25
                ) {
            Toast.makeText(this, "First & Last name should be >2 and <25 chars in length", Toast.LENGTH_LONG).show();
        } else {
//            RequestQueue requestQueue = Volley.newRequestQueue();
            String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.logininfo";
//            String url = "http://10.0.2.2:8080/LoginREST/webresources/src.logininfo";
//            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            // response
//                            Log.d("Response", response);
//                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivity(intent);
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // error
//                            Log.d("Error.Response", error.getMessage());
//                        }
//                    }
//            ) {
//
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("userid", accountBean.getUserId());
//                    params.put("firstName", accountBean.getFirstName());
//                    params.put("lastName", accountBean.getLastName());
//                    params.put("password", accountBean.getPassword());
//                    params.put("email", accountBean.getEmail());
//                    params.put("secQs", accountBean.getSecurityQS());
//                    params.put("secAns", accountBean.getSecurityAns());
//                    return params;
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//
//                    return super.getBody();
//                }
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Content-Type", "application/json");
//                    return params;
//                }
//            };
//            AppController.getInstance(this).addToRequestQueue(postRequest);

//method json--------------------------------------------
            JSONObject js = new JSONObject();
            JSONObject jsonobject_one = new JSONObject();
            try {
                jsonobject_one.put("email", accountBean.getEmail());
                jsonobject_one.put("firstName", accountBean.getFirstName());
                jsonobject_one.put("lastName", accountBean.getLastName());
                jsonobject_one.put("password", accountBean.getPassword());

                js.put("logininfo", jsonobject_one.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, url, jsonobject_one,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            JSONObject obj = new JSONObject(res);
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (error.networkResponse.statusCode == 204) {
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                }
            }) {

                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                    if (response.data == null || response.data.length == 0) {
                        return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                    } else {
                        return super.parseNetworkResponse(response);
                    }
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance(this).addToRequestQueue(jsonObjReq);
        }
    }
}