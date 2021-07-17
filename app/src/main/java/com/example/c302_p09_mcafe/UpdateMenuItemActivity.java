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

import cz.msebera.android.httpclient.Header;

public class UpdateMenuItemActivity extends AppCompatActivity {

    EditText etEditItemName, etEditPrice;
    Button btnEdit, btnDelete;

    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu_item);

        etEditItemName = findViewById(R.id.etItemNameEdit);
        etEditPrice = findViewById(R.id.etPriceEdit);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        client = new AsyncHttpClient();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loginId = prefs.getString("loginId", "");
        String apikey = prefs.getString("apikey", "");

        if (loginId.equalsIgnoreCase("") || apikey.equalsIgnoreCase("")) {
            //redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        Intent i = getIntent();
        String categoryId = i.getStringExtra("categoryId");
        String itemId = i.getStringExtra("itemId");
        String description = i.getStringExtra("description");
        Double price = i.getDoubleExtra("price", 0.0);

        Log.i("price", "" + price);

        etEditItemName.setText(description);
        etEditPrice.setText(price.toString());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.add("loginId", loginId);
                params.add("apikey", apikey);
                params.add("categoryId", categoryId);
                params.add("itemId", itemId);
                params.add("description", etEditItemName.getText().toString());
                params.add("price", etEditPrice.getText().toString());

                //for real devices, use the current location's ip address
                client.post("http://10.0.2.2/C302_P09_mCafe/updateMenuItemById.php", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        Toast.makeText(UpdateMenuItemActivity.this, "Menu item edited successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateMenuItemActivity.this, DisplayMenuItemsActivity.class);
                        i.putExtra("categoryId", categoryId);
                        startActivity(i);
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestParams params = new RequestParams();
                params.add("loginId", loginId);
                params.add("apikey", apikey);
                params.add("itemId", itemId);

                //for real devices, use the current location's ip address
                client.post("http://10.0.2.2/C302_P09_mCafe/deleteMenuItemById.php", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        Toast.makeText(UpdateMenuItemActivity.this, "Menu item deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateMenuItemActivity.this, DisplayMenuItemsActivity.class);
                        i.putExtra("categoryId", categoryId);
                        startActivity(i);
                    }
                });

            }
        });

    }
}