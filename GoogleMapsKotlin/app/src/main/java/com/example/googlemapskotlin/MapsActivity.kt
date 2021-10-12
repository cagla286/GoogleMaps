package com.example.googlemapskotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Latitude -> Enlem
        //Longitude -> Boylam
        //39.9229566,32.8468007

        /*
        val ankara = LatLng(39.9229566,32.8468007)
        mMap.addMarker(MarkerOptions().position(ankara).title("Ankara Kızılay"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ankara,15f))
         */

        //casting
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
               // lokasyon,konum değişince yapılacak işlemler

                mMap.clear()
                val guncelKonum =  LatLng(location.latitude,location.longitude)
                mMap.addMarker(MarkerOptions().position(guncelKonum).title("Guncel Konum"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(guncelKonum,15f))




            }

        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // izin verilmemis
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)


        }else {
            //izin verilmis

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
            val sonBilinenKonum = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (sonBilinenKonum != null){
                val sonBilinenLatLng = LatLng(sonBilinenKonum.latitude,sonBilinenKonum.longitude)
                mMap.addMarker(MarkerOptions().position(sonBilinenLatLng).title("Son Bilinen Konumunuz"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonBilinenLatLng,15f))
                
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1) {
            if(grantResults.size > 0){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}