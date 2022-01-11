package com.example.thitit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class Timkiem extends AppCompatActivity {
    EditText nhapCity;
    ImageButton search;
    ListView dsthanhpho;
    AdapterCity adapterCity;
    ArrayList<Thanhpho> mangthanhpho;
    ArrayList<Thanhpho> mangtp;
    private SQLiteDatabase db;
    boolean isLongClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);
        run();
        initData();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                reloadData();
            }
        });

        dsthanhpho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isLongClick == false) {
                    goToDetail(position);
                }
                isLongClick = false;
            }
        });

        dsthanhpho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick = true;
                deleteItem(position);
                reloadData();
                return false;
            }
        });

        loadData();
    }
    public void GetCityData(String data) {
        String url = "https://api.weatherbit.io/v2.0/current?&city="+data+"&key=9beb0a4cb1d1467f96a241ba4d0750a8";
        RequestQueue requestQueue = Volley.newRequestQueue(Timkiem.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject jsonObjectData = jsonArray.getJSONObject(0);
                            String temp = jsonObjectData.getString("app_temp");
                            Double a = Double.valueOf(temp);
                            String Temp = String.valueOf(a.intValue());
                            String city_name = jsonObjectData.getString("city_name");
                            String country_code = jsonObjectData.getString("country_code");
                            mangthanhpho.add(new Thanhpho(city_name,country_code,Temp));
                            adapterCity.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    private void run() {
        nhapCity = (EditText) findViewById(R.id.nhapCity);
        search = (ImageButton) findViewById(R.id.search);
        dsthanhpho = (ListView) findViewById(R.id.dsthanhpho);
        mangthanhpho = new ArrayList<Thanhpho>();
        mangtp = new ArrayList<Thanhpho>();
        adapterCity = new AdapterCity(Timkiem.this,mangthanhpho);
        dsthanhpho.setAdapter(adapterCity);
    }

    private void initData() {
        db = openOrCreateDatabase("city.db",MODE_PRIVATE,null);
        String sql = "CREATE TABLE IF NOT EXISTS tbcity (id integer primary key autoincrement, city text)";
        db.execSQL(sql);
    }

    private void insertData() {
        String thanhpho = nhapCity.getText().toString();
        String sql = "INSERT INTO tbcity (city) VALUES ('"+thanhpho+"')";
        db.execSQL(sql);
        GetCityData(thanhpho);
    }

    private void loadData() {
        mangthanhpho.clear();
        mangtp.clear();
        String sql = "SELECT * FROM tbcity";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String city = cursor.getString(1);
            Thanhpho tp = new Thanhpho(id,city);
            mangtp.add(tp);
            GetCityData(city);
            cursor.moveToNext();
        }
    }
    private void reloadData() {
        mangtp.clear();
        String sql = "SELECT * FROM tbcity";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String city = cursor.getString(1);
            Thanhpho tp = new Thanhpho(id,city);
            mangtp.add(tp);
            cursor.moveToNext();
        }
    }
    private void goToDetail(int position) {
        String thanhpho = mangthanhpho.get(position).city;
        Intent intent = new Intent(this, Manhinh1.class);
        intent.putExtra("name", thanhpho);
        startActivity(intent);
    }

    private void deleteItem(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá thành phố")
                .setMessage("Bạn muốn xóa thành phố khỏi danh sách yêu thích?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(position);
                        mangthanhpho.remove(position);
                        adapterCity.notifyDataSetChanged();
                    }
                }).setNegativeButton("Không" ,null)
                .show();
    }

    private void deleteData(int position) {
        int id = mangtp.get(position).id;
        String sql = "DELETE FROM tbcity WHERE id = " + id ;
        db.execSQL(sql);
    }
}
