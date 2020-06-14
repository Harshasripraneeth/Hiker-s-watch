package com.pressure.hiker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private val  locManager by lazy {
        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private lateinit var locListener: LocationListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locListener = object :LocationListener
        {
            override fun onLocationChanged(p0: Location?) {
                p0?.let { updateInfo(it) }

            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onProviderDisabled(p0: String?) {

            }

        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),123
            )
        }
        else
        {
           Log.d("checking","yes")
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0.0f, locListener)
            locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                updateInfo(it)
               Log.d("checking","passing")
            }
        }

        btnShowPlaces.setOnClickListener {
            val intent = Intent(this,PlacesActivity::class.java)
            startActivity(intent)
        }
    }


    fun updateInfo(location : Location)
    {
        tvLatitude.text = "latitude is ${location.latitude}"
        tvLongitude.text = "longitude is ${location.longitude}"
        tvAltitude.text = "altitude is ${location.altitude}"
        tvAccuracy.text = "accuracy is ${location.accuracy}"
        try{
            val geocoder = Geocoder(this, Locale.getDefault())
            val list = geocoder.getFromLocation(location.latitude,location.longitude,1)[0]

            tvaddress.text = list.getAddressLine(list.maxAddressLineIndex)
        }
        catch (e : Exception)
        {

        }

    }
}
