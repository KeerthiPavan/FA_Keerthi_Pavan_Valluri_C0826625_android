package com.example.fa_keerthi_pavan_valluri_c0826625_android;

import static android.location.LocationManager.GPS_PROVIDER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DBHelper myDB;

    boolean permissionGranted;
    GoogleMap mGoogleMap;
    LocationManager loc_man;
    LatLng long_and_lat;
    LocationListener loc_listener;
    Geocoder geo;
    TextView markerText;
    Button favBtn;
    boolean updateButton;
    int updatePosition;
    boolean fromFavorites;
    public static ArrayList<String> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favBtn = (Button) findViewById(R.id.favPlaces);
        updateButton = MainActivity2.updateButton;
        updatePosition = MainActivity2.updatePosition;
        fromFavorites = MainActivity2.fromFavorites;
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFavPlaces();
            }
        });

        markerText = findViewById(R.id.txtmarkerAddress);

        boolean per = isPermissionGranted();

        if(updateButton){
            favBtn.setText("Update Button");
        }else{
            favBtn.setText("Go to Favorite Places");
        }

    }


    private void openFavPlaces() {
        if(updateButton){
            Toast.makeText(this,"Updated Favorite Place", Toast.LENGTH_SHORT).show();
            updateButton = false;
            favBtn.setText("Go to Favorite Places");
        }else{
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        }

    }

    private boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isPermissionGranted() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                supportMapFragment.getMapAsync(MainActivity.this);
                Toast.makeText(getApplicationContext(), "Location Permission Granted", Toast.LENGTH_SHORT).show();
                permissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent myIntent = new Intent();
                myIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                myIntent.setData(uri);
                startActivity(myIntent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest,
                                                           PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if(!updateButton && !fromFavorites){
            loc_man = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            loc_man.requestLocationUpdates(GPS_PROVIDER, 1000, 10, loc_listener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    long_and_lat = new LatLng(location.getLatitude(), location.getLongitude());
                    Toast.makeText(getApplicationContext(), long_and_lat.toString(), Toast.LENGTH_LONG).show();
                    mGoogleMap.clear();
                    mGoogleMap.addMarker(new MarkerOptions().position(long_and_lat).title("Your are Here"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(long_and_lat.latitude, long_and_lat.longitude)).zoom(16).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    markerText.setText("Your are Here");

//                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(long_and_lat.latitude, long_and_lat.longitude, 1);
//                    if (addresses.size() > 0) {
//                        sharingAddress = addresses.get(0).getAddressLine(0);
//                        sharingCoordintes = "Latitude: " + long_and_lat.latitude + " -- Longitude: " + long_and_lat.longitude;
//                    } else {
//
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {

                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    Toast.makeText(getApplicationContext(), "Please Enable Location", Toast.LENGTH_LONG).show();
                    Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(viewIntent);
                }
            });

            geo = new Geocoder(MainActivity.this, Locale.getDefault());

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mGoogleMap.clear();
                    try {
                        if (geo == null){
                            geo = new Geocoder(MainActivity.this, Locale.getDefault());
                        }
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);

                        if (address.size() > 0) {
                            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Address:" + address.get(0).getAddressLine(0)));
                            markerText.setText(address.get(0).getAddressLine(0));
                            String favPlace =  address.get(0).getAddressLine(0).replace("[","");
                            Log.d("favPlace",favPlace);
                            if(updateButton){
                                arrayList.remove(updatePosition);
                                arrayList.add(updatePosition,favPlace);
                            }else{
                                Toast.makeText(MainActivity.this, "Added to Favorite Places", Toast.LENGTH_SHORT).show();
                                arrayList.add(favPlace);
                            }
                        }
                    }
                    catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(MainActivity.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });


//        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                mGoogleMap.clear();
//                try {
//                    if (geo == null){
//                        geo = new Geocoder(MainActivity.this, Locale.getDefault());
//                    }
//                    List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                    if (address.size() > 0) {
//                        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Address:" + address.get(0).getAddressLine(0)));
//                    }
//                }
//                catch (IOException ex) {
//                    if (ex != null)
//                        Toast.makeText(MainActivity.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });

            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    markerText.setText(marker.getTitle().toString() + " Lat:" + marker.getPosition().latitude + " Long:" + marker.getPosition().longitude);
                    return false;
                }
            });

        }else{
            geo = new Geocoder(MainActivity.this, Locale.getDefault());
            Geocoder coder = new Geocoder(this);
            List<Address> address;


            try {
                address = coder.getFromLocationName(arrayList.get(updatePosition), 5);
                if (address == null) {

                }
                Address location = address.get(0);
                long_and_lat = new LatLng(location.getLatitude(), location.getLongitude());
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(long_and_lat).title(arrayList.get(updatePosition)));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(long_and_lat.latitude, long_and_lat.longitude)).zoom(16).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                markerText.setText(arrayList.get(updatePosition));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mGoogleMap.clear();
                    try {
                        if (geo == null){
                            geo = new Geocoder(MainActivity.this, Locale.getDefault());
                        }
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);

                        if (address.size() > 0) {
                            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Address:" + address.get(0).getAddressLine(0)));
                            markerText.setText(address.get(0).getAddressLine(0));
                            String favPlace =  address.get(0).getAddressLine(0).replace("[","");
                            Log.d("favPlace",favPlace);
                            if(updateButton){
                                arrayList.remove(updatePosition);
                                arrayList.add(updatePosition,favPlace);
                            }else if(!fromFavorites){
                                Toast.makeText(MainActivity.this, "Added to Favorite Places", Toast.LENGTH_SHORT).show();
                                arrayList.add(favPlace);
                            }
                        }
                    }
                    catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(MainActivity.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }
}