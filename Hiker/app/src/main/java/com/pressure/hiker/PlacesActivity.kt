package com.pressure.hiker

import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_places.*
import java.util.ArrayList

class PlacesActivity : AppCompatActivity() {

    companion object
    {
        val list = mutableListOf<String>()
        val locations = mutableListOf<LatLng>()
    }
    private val adapter by lazy { ArrayAdapter(this, android.R.layout.simple_list_item_1, list) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, MapsActivity::class.java)
            intent .putExtra("position",position)
            startActivity(intent)

        }
        btnAddPlace.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
