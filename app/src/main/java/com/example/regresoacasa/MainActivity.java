package com.example.regresoacasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {

    Button aceptar;
    //Origen
    TextView latitudCasa;
    TextView longitudCasa;


    //Destino
    TextView latitudActual;
    TextView longitudActual;

    public static Intent intent = new Intent();

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            latitudActual.setText(String.valueOf(location.getLatitude()));
                            longitudActual.setText(String.valueOf(location.getLongitude()));



                        }else {Log.i("MiUbi", "Sin ubicaciòn, encienda sus datos y gps");}

                    }
                });


        aceptar = (Button)findViewById(R.id.btnAceptar);

        //Origen
        latitudCasa = (TextView)findViewById(R.id.txtLatitudCasa);
        longitudCasa = (TextView)findViewById(R.id.txtLongitudCasa);

        //Destino
        latitudActual = (TextView)findViewById(R.id.txtLatitudActual);
        longitudActual = (TextView)findViewById(R.id.txtLonfitudActual);



        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Origen
                intent.putExtra("LatitudCasa", latitudCasa.getText().toString());
                intent.putExtra("LongitudCasa",longitudCasa.getText().toString());

                //Destino
                intent.putExtra("latituActual",latitudActual.getText().toString());
                intent.putExtra("longitudActual",longitudActual.getText().toString());


                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });


    }

}