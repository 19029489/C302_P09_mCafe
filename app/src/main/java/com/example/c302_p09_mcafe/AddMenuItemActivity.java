package com.example.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddMenuItemActivity extends AppCompatActivity {

    EditText etItemName, etPrice;
    Button btnAdd;

    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        etItemName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.btnAdd);

        client = new AsyncHttpClient();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loginId = prefs.getString("loginId", "");
        String apikey = prefs.getString("apikey", "");

        // TODO: if loginId and apikey is empty, go back to LoginActivity

        if (loginId.equalsIgnoreCase("") || apikey.equalsIgnoreCase("")) {
            //redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        Intent i = getIntent();
        String categoryId = i.getStringExtra("categoryId");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etItemName.getText().toString().equalsIgnoreCase("") || etPrice.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddMenuItemActivity.this, "Please fill in all the blanks.", Toast.LENGTH_SHORT).show();
                } else {

                    RequestParams params = new RequestParams();
                    params.add("loginId", loginId);
                    params.add("apikey", apikey);
                    params.add("categoryId", categoryId);
                    params.add("description", etItemName.getText().toString());
                    params.add("price", etPrice.getText().toString());

                    //for real devices, use the current location's ip address
                    client.post("http://10.0.2.2/C302_P09_mCafe/addMenuItem.php", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            Toast.makeText(AddMenuItemActivity.this, "Menu item added successfully", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(AddMenuItemActivity.this, DisplayMenuItemsActivity.class);
                            i.putExtra("categoryId", categoryId);
                            startActivity(i);

                        }//end onSuccess
                    });

                }
            }
        });
    }
}