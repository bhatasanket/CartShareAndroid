package com.example.it354f701.CartShare;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartList extends Activity {

    private Spinner cartspinner;
    private ListView cartlist;
    private Button buttonAddToList;
    private EditText editTextAddToList;
    private LinearLayout addLayout;

    private String userId;
    private ArrayList<String> groupList;
    private String groupSelected;
    private ArrayList<String> itemList;
    private JSONArray itemObjectList;
    private EditText editTextAddNewList;
    private Button buttonAddNewList;
    private Button ButtonDeleteList;
    private JSONArray groupObjectList;
    private int groupSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userId = null;
            } else {
                userId = extras.getString("userId");
            }
        } else {
            userId = (String) savedInstanceState.getSerializable("userId");
        }

        cartspinner = findViewById(R.id.cartspinner);
        cartlist = findViewById(R.id.cartlist);
        buttonAddToList = findViewById(R.id.buttonAddToList);
        editTextAddToList = findViewById(R.id.editTextAddToList);
        addLayout = findViewById(R.id.addLayout);
        editTextAddNewList = findViewById(R.id.editTextAddNewList);
        buttonAddNewList = findViewById(R.id.buttonAddNewList);
        ButtonDeleteList = findViewById(R.id.ButtonDeleteList);

        getGrouList();

        cartspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    groupSelectedPosition = position;
                    groupSelected = groupList.get(position);

                    getCartList();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.shoppinglist/";

                JSONObject js = new JSONObject();
                try {
                    js.put("groupname", groupSelected);
                    js.put("listitems", editTextAddToList.getText().toString());
                    js.put("id", 1);
                    editTextAddNewList.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, url, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(CartList.this, "Added...", Toast.LENGTH_LONG).show();
                                getCartList();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.d(Tag, "Error: " + error.getMessage());
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
                            Toast.makeText(CartList.this, "Added ", Toast.LENGTH_SHORT).show();
                            getCartList();
                        }
                    }
                }) {

                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
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
                AppController.getInstance(CartList.this).addToRequestQueue(jsonObjReq);


            }
        });
        buttonAddNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.associations/";

                JSONObject js = new JSONObject();
                try {
                    String groupname = editTextAddNewList.getText().toString();
                    if (groupname.equals(""))
                        groupname = "List " + DateFormat.getDateInstance().format(new Date());
                    js.put("email", userId);
                    js.put("groupname", groupname);
                        js.put("id", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, url, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(CartList.this, "Added...", Toast.LENGTH_LONG).show();
                                getGrouList();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.d(Tag, "Error: " + error.getMessage());
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
                            Toast.makeText(CartList.this, "Added ", Toast.LENGTH_SHORT).show();
                            getCartList();
                        }
                    }
                }) {

                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
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
                AppController.getInstance(CartList.this).addToRequestQueue(jsonObjReq);


            }
        });
        ButtonDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removelist();
            }
        });
    }

    private void removefromcart(int position) {
        try {
            int removeId = (int) itemObjectList.getJSONObject(position).get("id");

            final String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.shoppinglist/" + removeId;

            StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Toast.makeText(CartList.this, "deleted", Toast.LENGTH_LONG).show();
                            getCartList();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error.
                            Toast.makeText(CartList.this, "not deleted", Toast.LENGTH_LONG).show();

                        }
                    }
            );

            AppController.getInstance(this).addToRequestQueue(dr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void removelist() {
        try {
            int id = (int) ((JSONObject) (groupObjectList.get(groupSelectedPosition - 1))).get("id");
            final String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.associations/" + id;
            StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Toast.makeText(CartList.this, "deleted", Toast.LENGTH_LONG).show();
                            getGrouList();

//                            getCartList();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error.
                            Toast.makeText(CartList.this, "not deleted", Toast.LENGTH_LONG).show();

                        }
                    }
            );

            AppController.getInstance(this).addToRequestQueue(dr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getCartList() {
        final String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.shoppinglist/groupname/" + groupSelected.replace(" ","%20");
        JsonArrayRequest getRequest2 = new JsonArrayRequest(Request.Method.GET, url.replace(" ","%20"), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        itemList = new ArrayList<>();
                        itemObjectList = response;

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                itemList.add((String) jsonObject.get("listitems"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                CartList.this,
                                R.layout.da_item,
                                itemList);
//                        cartlist.setAdapter(adapter);
                        cartlist.setAdapter(new MyListAdapter(CartList.this, R.layout.da_item, itemList));
                        cartlist.setVisibility(View.VISIBLE);
                        addLayout.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.TextOR)).setVisibility(View.GONE);
                        (findViewById(R.id.layoutAddToGroup)).setVisibility(View.GONE);
                        findViewById(R.id.ButtonDeleteList).setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("tag", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "No User found ... ", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

        };

        AppController.getInstance(this).addToRequestQueue(getRequest2);

    }

    private void getGrouList() {
        final String url = "http://10.112.4.10:8080/CartShareServ1/webresources/com.associations/email/" + userId;
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        groupObjectList = response;
                        groupList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                groupList.add((String) jsonObject.get("groupname"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        groupList.add(0, "Select already created list...");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CartList.this,
                                android.R.layout.simple_spinner_item, groupList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cartspinner.setAdapter(dataAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("tag", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "No User found ... ", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

        };

        AppController.getInstance(this).addToRequestQueue(getRequest);
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;

        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.item = (TextView) convertView.findViewById(R.id.itemText);
                viewHolder.item.setText(getItem(position).toString());
                viewHolder.itemButton = (Button) convertView.findViewById(R.id.itemButton);
//                viewHolder.itemButton.setTag();
                viewHolder.itemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(CartList.this, "Button was clicked", Toast.LENGTH_SHORT).show();
//                        itemList.remove(position);
//                        getCartList();
                        removefromcart(position);
                    }

                });
                convertView.setTag(viewHolder);
            } else {
                mainHolder = (ViewHolder) convertView.getTag();
                String abcd = getItem(position);
                mainHolder.item.setText(getItem(position).toString());
            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView item;
        Button itemButton;
    }

}
