package com.mindtree.osmosis.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mindtree.osmosis.Home;
import com.mindtree.osmosis.R;
import com.mindtree.osmosis.VendorMaps;
import com.mindtree.osmosis.util.FOCVolleyQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeContent extends Fragment {

    private ProgressBar progressBar;
    private RelativeLayout vendorContainer;
    private TextView vendor1, vendor2, vendor1Email, vendor2Email;
    private int MY_SOCKET_TIMEOUT_MS = 5000;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        vendorContainer = (RelativeLayout) view.findViewById(R.id.vendor_container);
        vendor1 = (TextView) view.findViewById(R.id.vendor1_name);
        vendor2 = (TextView) view.findViewById(R.id.vendor2_name);
        vendor1Email = (TextView) view.findViewById(R.id.vendor1_email);
        vendor2Email = (TextView) view.findViewById(R.id.vendor2_email);
        vendorContainer = (RelativeLayout) view.findViewById(R.id.vendor_container);
        cardView = (CardView) view.findViewById(R.id.vendor1);
        getVendors();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VendorMaps.class));
            }
        });
        return view;
    }

    private void getVendors() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);

        String url = getString(R.string.listVendor);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                vendorContainer.setVisibility(View.VISIBLE);
                try {
                    if (response.length() > 0) {
                        parseJSON(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json; charset=utf-8");
                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        FOCVolleyQueue.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void parseJSON(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            JSONObject object = response.getJSONObject(i);
            String name = object.getString("name");
            String email = object.getString("emailAddress");
            populateUI(name, email, i);
        }
    }

    private void populateUI(String name, String email, int i) {
        if (i == 0) {
            vendor1.setText(name);
            vendor1Email.setText(email);
        } else {
            vendor2.setText(name);
            vendor2Email.setText(email);
        }
    }
}
