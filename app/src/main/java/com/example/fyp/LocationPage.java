package com.example.fyp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationPage extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    Location currentLocation;
    GoogleApiClient client;
    LatLng latLngCurrent;
    String apikey = "AIzaSyAGKWrIDiHlJ6tHSme5gP8K9mE6oCusaFc";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //obtain support fragment and notify map when ready to use
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
       // getLocation();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
       UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setMyLocationButtonEnabled(true);

        LatLng sydney = new LatLng(-34,151);
        map.addMarker(new MarkerOptions().position(sydney).title("Sydney Location"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        getLocation();

        client = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        client.connect();

    }

    public void getLocation() {

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            map.setMyLocationEnabled(true);
            locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

          //  fusedLocationClient = new FusedLocationProviderClient(getApplicationContext());
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    double lat = locationResult.getLastLocation().getLatitude();
                    double lng = locationResult.getLastLocation().getLongitude();
                    LatLng currentlocation = new LatLng(lat, lng);
                    displayLocation(currentlocation);
                }
            }, null);
        }
    }

    private void displayLocation(LatLng latlng) {
        if (map != null){
            Geocoder coder = new Geocoder( this );
            try {
                List<Address> locations = coder.getFromLocation(latlng.latitude,
                        latlng.longitude, 1);
                if (locations != null) {
                    String add1 = locations.get(0).getAddressLine(0);
                    map.addMarker(new MarkerOptions().position(latlng).title("Current location").snippet(add1));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
    }
        hospitalPointer(latlng);
        pharmacyPointer(latlng);
        GPPointer(latlng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: //the code defined in the requestConnection
                    // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                }
        }

}

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    //show hospitals within 10km
    public void hospitalPointer(LatLng latLng){
        StringBuilder builder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        builder.append("location="+latLng.latitude+","+latLng.longitude);
        builder.append("&radius="+10000);
        builder.append("&keyword="+"hospital");
        builder.append("&key=").append(apikey);
        String url = builder.toString();

        Object[] object = new Object[2];
        object[0] = map;
        object[1]=url;

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        getNearbyPlaces.execute(object);
       // map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

    }

     public void pharmacyPointer(LatLng latLng) {
        StringBuilder builder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        builder.append("location=" + latLng.latitude + "," + latLng.longitude);
        builder.append("&radius=" + 10000);
        builder.append("&keyword=" + "pharmacy");
        builder.append("&key=").append(apikey);
        String url = builder.toString();
        Object[] object = new Object[2];
        object[0] = map;
        object[1] = url;

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        getNearbyPlaces.execute(object);
    }

        public void GPPointer (LatLng latLng){
            StringBuilder builder1 = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            builder1.append("location=" + latLng.latitude + "," + latLng.longitude);
            builder1.append("&radius=" + 10000);
            builder1.append("&keyword=" + "GP");
            builder1.append("&key=").append(apikey);
            String url1 = builder1.toString();
            Object[] objects = new Object[2];
            objects[0] = map;
            objects[1] = url1;

            GetNearbyPlaces getNearbyPlace = new GetNearbyPlaces();
            getNearbyPlace.execute(objects);
        }



    }


