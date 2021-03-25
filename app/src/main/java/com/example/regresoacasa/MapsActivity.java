package com.example.regresoacasa;

import androidx.fragment.app.FragmentActivity;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //Origen
    double  latitudCasa= Double.parseDouble(MainActivity.intent.getExtras().getString("LatitudCasa"));
    double  longitudCasa = Double.parseDouble(MainActivity.intent.getExtras().getString("LongitudCasa"));

    //Destino
    double latitudActual = Double.parseDouble(MainActivity.intent.getExtras().getString("latituActual"));
    double longitudActual = Double.parseDouble(MainActivity.intent.getExtras().getString("longitudActual"));

    //Key
    String key = "AIzaSyDZx8ah9aVIZ57h7k1-V2DnivCSKNkLtXA";

    JSONObject jso;



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ubicacion = new LatLng(latitudActual, longitudActual);
        LatLng destino = new LatLng(latitudCasa, longitudCasa);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicacion actual"));
        mMap.addMarker(new MarkerOptions().position(destino).title("Ubicacion actual"));



        CameraPosition cameraPosition = new CameraPosition.Builder().target(ubicacion).zoom(14).bearing(30).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latitudActual+","+longitudActual+"&destination="+latitudCasa+","+longitudCasa+"&key="+key+"";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    jso = new JSONObject(response);
                    trasaruta(jso);
                    Log.i("JsonRuta:", ""+response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }

    private void trasaruta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){

                jLegs = ((JSONObject)jRoutes.get(i)).getJSONArray("legs");

                for (int j=0; j<jLegs.length(); j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k=0; k<jSteps.length(); k++){
                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");

                        Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.GREEN).width(5));
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
