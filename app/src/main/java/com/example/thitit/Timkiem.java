package com.example.thitit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.content.pm.PackageManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
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
import java.util.Date;


public class Timkiem extends AppCompatActivity {
    EditText nhapCity;
    ImageButton search;
    ListView dsthanhpho;
    AdapterCity adapterCity;
    ArrayList<Thanhpho> mangthanhpho;
    ArrayList<Thanhpho> mangtp;
    LocationManager locationManager;
    int PERMISSION_CODE = 1;
    private SQLiteDatabase db;
    boolean isLongClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);
        run();
        initData();
        locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
                    sendNotification();
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

    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(this, Timkiem.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(getNotificationId(), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, NotificationApplication.CHANNEL_ID)
                .setContentTitle("Th???i ti???t ng??y h??m nay")
                .setContentText("B???m ????? xem th??ng tin")
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.notifi_icon)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setSound(uri)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(getNotificationId(),notification);
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
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
                .setTitle("Xo?? th??nh ph???")
                .setMessage("B???n mu???n x??a th??nh ph??? kh???i danh s??ch y??u th??ch?")
                .setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(position);
                        mangthanhpho.remove(position);
                        adapterCity.notifyDataSetChanged();
                    }
                }).setNegativeButton("Kh??ng" ,null)
                .show();
    }

    private void deleteData(int position) {
        int id = mangtp.get(position).id;
        String sql = "DELETE FROM tbcity WHERE id = " + id ;
        db.execSQL(sql);
    }
}
