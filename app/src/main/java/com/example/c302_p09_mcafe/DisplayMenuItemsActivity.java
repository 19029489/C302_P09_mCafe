package com.example.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DisplayMenuItemsActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayAdapter<MenuCategoryItem> aa;
    private ArrayList<MenuCategoryItem> al;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_items);


        lv = (ListView) findViewById(R.id.lvMenuItem);
        al = new ArrayList<MenuCategoryItem>();
        aa = new ArrayAdapter<MenuCategoryItem>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

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

        RequestParams params = new RequestParams();
        params.add("loginId", loginId);
        params.add("apikey", apikey);
        params.add("categoryId", categoryId);

        //for real devices, use the current location's ip address
        client.post("http://10.0.2.2/C302_P09_mCafe/getMenuItemsByCategory.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.i("JSON Results: ", response.toString());

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObj = response.getJSONObject(i);

                        String itemId = jsonObj.getString("menu_item_id");
                        String categoryId = jsonObj.getString("menu_item_category_id");
                        String description = jsonObj.getString("menu_item_description");
                        String price = jsonObj.getString("menu_item_unit_price");

                        MenuCategoryItem menuItem = new MenuCategoryItem(itemId, categoryId, description, Double.parseDouble(price));
                        al.add(menuItem);
                    }

                    aa.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuCategoryItem selected = al.get(position);

                Intent i = new Intent(DisplayMenuItemsActivity.this, UpdateMenuItemActivity.class);
                i.putExtra("categoryId", "" + selected.getCategoryId());
                i.putExtra("itemId", selected.getId());
                i.putExtra("description", selected.getDescription());
                i.putExtra("price", selected.getUnitPrice());
                startActivity(i);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_addmenuitem){

            Intent i = getIntent();
            String categoryId = i.getStringExtra("categoryId");

            Intent o = new Intent(DisplayMenuItemsActivity.this, AddMenuItemActivity.class);
            o.putExtra("categoryId", categoryId);
            startActivity(o);

            return true;

        } else if (id == R.id.menu_logout) {
            // TODO: Clear SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            // TODO: Redirect back to login screen
            Intent i = new Intent(DisplayMenuItemsActivity.this, LoginActivity.class);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
