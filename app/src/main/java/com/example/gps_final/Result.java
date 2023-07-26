package com.example.gps_final;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private LocationManager locationManager;
    private LocationListener locationListener;
    TextView resultado, welcome;
    ImageView imagen_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        welcome = findViewById(R.id.welcome);
        resultado = findViewById(R.id.Result_viaje);
        imagen_result = findViewById(R.id.img_result);

        String userName = getIntent().getStringExtra("user_name");
        if (userName != null && !userName.isEmpty()) {
            String welcomeMessage = "¡Hola, " + userName + "!";
            welcome.setText(welcomeMessage);
        } else {
            welcome.setText("Hola, desconocido");
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Se verifica si la locación está dentro de los parámetros de Panamá
                boolean insidePanama = isInsidePanama(latitude, longitude);
                if (insidePanama) {
                    resultado.setText("Estás en Panamá");
                    imagen_result.setImageResource(R.drawable.panmap);
                } else {
                    resultado.setText(" Estás viajando " +
                            "\nfuera de Panamá");
                    imagen_result.setImageResource(R.drawable.flying);
                    activateAirplaneMode();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}


        };
    }

    private void activateAirplaneMode() {
        boolean isAirplaneModeOn = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        if (!isAirplaneModeOn) {
            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verifica los permisos de GPS y los pide si no se han concedido.
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Registra los datos de locación
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Locaciones no registradas se actualizan para ahorrar batería.
        locationManager.removeUpdates(locationListener);
    }

    private boolean isInsidePanama(double latitude, double longitude) {
        double panamaMinLatitude = 7.033;
        double panamaMaxLatitude = 9.867;
        double panamaMinLongitude = -83.051;
        double panamaMaxLongitude = -77.142;
        return (latitude >= panamaMinLatitude && latitude <= panamaMaxLatitude &&
                longitude >= panamaMinLongitude && longitude <= panamaMaxLongitude);
    }
}
