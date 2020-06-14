package com.pressure.hiker

import android.Manifest
import android.content.Context
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

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback ,GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener{
    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.let {
            p0.remove()
            return true
        }
        return false
    }
    private lateinit var mMap: GoogleMap
    private val  locManager by lazy {
        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private lateinit var locListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun showMap(location : LatLng, title : String)
    {
        mMap.addMarker(MarkerOptions().position(location).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,13f))

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val position = intent.getIntExtra("position",-1)
        if(position == -1)
        {
            mMap.setOnMapLongClickListener(this)
            mMap.setOnMarkerClickListener(this)
            locListener = object :LocationListener
            {
                override fun onLocationChanged(p0: Location?) {
                       // p0?.let { showMap(LatLng(p0.latitude,p0.longitude),"userLocation") }
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
                    showMap(LatLng(it.latitude,it.longitude),"userLocation")
                    Log.d("checking","passing")
                }
            }
        }
        else
        {
            showMap(PlacesActivity.locations[position],PlacesActivity.list[position])
        }


    }
    override fun onMapLongClick(p0: LatLng?) {

        p0?.let {

            var address =""
            try {
                val coder = Geocoder(this, Locale.getDefault())

                val list = coder.getFromLocation(p0.latitude,p0.longitude,1)[0]
                if(!list.subThoroughfare.isNullOrEmpty())
                    address = address.plus(list.subThoroughfare)
                if(!list.thoroughfare.isNullOrEmpty())
                    address = address.plus(list.thoroughfare)

                address = address.plus(list.subLocality)


                Toast.makeText(this,address,Toast.LENGTH_LONG).show()

                mMap.addMarker(MarkerOptions().position(p0))

                PlacesActivity.list.add(address)
                PlacesActivity.locations.add(p0)

            }
            catch (e:Exception)
            {

            }

        }

    }

}
