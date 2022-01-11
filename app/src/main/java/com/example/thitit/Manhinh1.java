package com.example.thitit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Manhinh1 extends AppCompatActivity {
    TextView city, country,currenttime,temp,description,sunrise,sunset,uv,feellike,humidity,cloud,wind,visibility,pressure,aqi,co,o3,so2,no2;
    ImageView imgicon,goback;
    RecyclerView dshours, dsdaily;
    AdapterHour adapterHour;
    AdapterDaily adapterDaily;
    ArrayList<Dubaotheogio> dubaotheogioList;
    ArrayList<Dubaotheongay> dubaotheongayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinh1);
        run();
        String name = getIntent().getStringExtra("name");
        GetDataCurrent(name);
        GetHoursData(name);
        GetDailysData(name);
        GetAQIData(name);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void GetDataCurrent(String data) {
        String url ="https://api.weatherbit.io/v2.0/current?&city="+data+"&key=9beb0a4cb1d1467f96a241ba4d0750a8";
        RequestQueue requestQueue = Volley.newRequestQueue(Manhinh1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            JSONObject jsonObjectData = jsonArrayData.getJSONObject(0);

                            String city_name = jsonObjectData.getString("city_name");
                            city.setText(city_name);

                            String country_code = jsonObjectData.getString("country_code");
                            country.setText(country_code);

                            String ts = jsonObjectData.getString("ts");
                            long l = Long.valueOf(ts);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            String Ts = simpleDateFormat.format(date);
                            currenttime.setText(Ts);

                            String TEMP = jsonObjectData.getString("temp");
                            Double a = Double.valueOf(TEMP);
                            String Temp = String.valueOf(a.intValue());
                            temp.setText(Temp + "  ̊C");

                            JSONObject jsonObjectWeather = jsonObjectData.getJSONObject("weather");
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.get().load("https://www.weatherbit.io/static/img/icons/"+icon+".png").into(imgicon);
                            String Description = jsonObjectWeather.getString("description");
                            description.setText(Description);

                            String Sunrise = jsonObjectData.getString("sunrise");
                            String Sunset = jsonObjectData.getString("sunset");
                            sunrise.setText(Sunrise);
                            sunset.setText(Sunset);

                            String app_temp = jsonObjectData.getString("app_temp");
                            Double b = Double.valueOf(app_temp);
                            String App_temp = String.valueOf(b.intValue());
                            feellike.setText(App_temp +" ̊C");

                            String rh = jsonObjectData.getString("rh");
                            humidity.setText(rh + " %");

                            String UV = jsonObjectData.getString("uv");
                            Double c = Double.valueOf(UV);
                            String Uv = String.valueOf(c.intValue());
                            uv.setText(Uv);

                            String clouds = jsonObjectData.getString("clouds");
                            cloud.setText(clouds + " %");

                            String win_spd = jsonObjectData.getString("wind_spd");
                            wind.setText(win_spd + " m/s");

                            String vis = jsonObjectData.getString("vis");
                            visibility.setText(vis + " km");

                            String pres = jsonObjectData.getString("pres");
                            pressure.setText(pres + " mb");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    public void GetHoursData(String data) {
        String url ="https://api.weatherbit.io/v2.0/forecast/hourly?city="+data+"&key=9beb0a4cb1d1467f96a241ba4d0750a8&hours=9";
        RequestQueue requestQueue = Volley.newRequestQueue(Manhinh1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for(int i = 0; i<jsonArrayData.length() ; i++) {
                                JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);

                                String ts = jsonObjectData.getString("ts");
                                long l = Long.valueOf(ts);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                String Ts = simpleDateFormat.format(date);

                                String TEMP = jsonObjectData.getString("temp");
                                Double a = Double.valueOf(TEMP);
                                String Temp = String.valueOf(a.intValue());

                                String wind_spd = jsonObjectData.getString("wind_spd");
                                JSONObject jsonObjectWeather = jsonObjectData.getJSONObject("weather");
                                String icon = jsonObjectWeather.getString("icon");
                                dubaotheogioList.add(new Dubaotheogio(Ts,Temp,icon,wind_spd));
                            }
                            adapterHour.notifyDataSetChanged();
                        } catch (Exception e) {
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
    public void GetDailysData(String data) {
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?city="+data+"&key=9beb0a4cb1d1467f96a241ba4d0750a8&days=7";
        RequestQueue requestQueue = Volley.newRequestQueue(Manhinh1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for(int i = 0; i<jsonArrayData.length() ; i++) {
                                JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                                String ts = jsonObjectData.getString("ts");
                                 long l = Long.valueOf(ts);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM");
                                String Dt = simpleDateFormat.format(date);

                                String max_temp = jsonObjectData.getString("max_temp");
                                String min_temp = jsonObjectData.getString("min_temp");
                                Double a = Double.valueOf(max_temp);
                                String Max_temp = String.valueOf(a.intValue());
                                Double b = Double.valueOf(min_temp);
                                String Min_temp = String.valueOf(b.intValue());

                                String wind_spd = jsonObjectData.getString("wind_spd");

                                JSONObject jsonObjectWeather = jsonObjectData.getJSONObject("weather");
                                String icon = jsonObjectWeather.getString("icon");
                                dubaotheongayList.add(new Dubaotheongay(Dt,icon,Max_temp,Min_temp,wind_spd));
                            }
                            adapterDaily.notifyDataSetChanged();
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
    public void GetAQIData(String data) {
        String url = "https://api.weatherbit.io/v2.0/current/airquality?&city="+data+"&key=9beb0a4cb1d1467f96a241ba4d0750a8";
        RequestQueue requestQueue = Volley.newRequestQueue(Manhinh1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            JSONObject jsonObjectData = jsonArrayData.getJSONObject(0);
                            String AQI = jsonObjectData.getString("aqi");
                            Double a = Double.valueOf(AQI);
                            String Aqi = String.valueOf(a.intValue());
                            aqi.setText(Aqi);

                            String CO = jsonObjectData.getString("co");
                            Double b = Double.valueOf(CO);
                            String Co = String.valueOf(b.intValue());
                            co.setText(Co + " µg/m³");

                            String O3 = jsonObjectData.getString("o3");
                            Double c = Double.valueOf(O3);
                            String oxi = String.valueOf(c.intValue());
                            o3.setText(oxi + " µg/m³");

                            String NO2 = jsonObjectData.getString("no2");
                            Double d = Double.valueOf(NO2);
                            String No2 = String.valueOf(d.intValue());
                            no2.setText(No2 + " µg/m³");

                            String SO2 = jsonObjectData.getString("so2");
                            Double e = Double.valueOf(SO2);
                            String So2 = String.valueOf(e.intValue());
                            so2.setText(So2 + " µg/m³");

                        } catch (Exception e) {
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
        goback = (ImageView) findViewById(R.id.goback);
        city = (TextView) findViewById(R.id.city);
        country = (TextView) findViewById(R.id.country);
        currenttime = (TextView) findViewById(R.id.currenttime);
        temp = (TextView) findViewById(R.id.temp);
        description = (TextView) findViewById(R.id.description);
        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        uv = (TextView) findViewById(R.id.uv);
        feellike = (TextView) findViewById(R.id.feellike);
        humidity = (TextView) findViewById(R.id.humidity);
        cloud = (TextView) findViewById(R.id.cloud);
        wind = (TextView) findViewById(R.id.wind);
        visibility = (TextView) findViewById(R.id.visibility);
        pressure = (TextView) findViewById(R.id.pressure);
        aqi = (TextView) findViewById(R.id.aqi);
        co = (TextView) findViewById(R.id.co);
        o3 = (TextView) findViewById(R.id.o3);
        no2 = (TextView) findViewById(R.id.no2);
        so2 = (TextView) findViewById(R.id.so2);
        imgicon = (ImageView) findViewById(R.id.icon);
        dshours = (RecyclerView) findViewById(R.id.dscacgio);
        dsdaily = (RecyclerView) findViewById(R.id.dscacngay);
        dubaotheogioList = new ArrayList<>();
        adapterHour = new AdapterHour(this,dubaotheogioList);
        dshours.setAdapter(adapterHour);
        dubaotheongayList = new ArrayList<>();
        adapterDaily = new AdapterDaily(this,dubaotheongayList);
        dsdaily.setAdapter(adapterDaily);
    }
}