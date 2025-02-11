package com.example.journeytracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private var distanceInKm = 500
    private var isKm = true
    private var progress = 0
    private var totalStops = 5
    private var currentStop = 1
    private lateinit var stopList: List<String>

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewStops = findViewById<RecyclerView>(R.id.recyclerViewStops)
        val btnToggleDistance = findViewById<Button>(R.id.btnToggleDistance)
        val btnNextStop = findViewById<Button>(R.id.btnNextStop)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvJourneyDetails = findViewById<TextView>(R.id.tvJourneyDetails)
        val tvDistance = findViewById<TextView>(R.id.tvDistance)
        progressBar.progress = progress

        btnToggleDistance.setOnClickListener {
            if (this.isKm) {
                val distanceInMiles = distanceInKm * 0.621
                tvDistance.text = "Distance: %.2f miles".format(distanceInMiles)
                btnToggleDistance.text = "Convert to KMs"
            } else {
                tvDistance.text = "Distance: $distanceInKm km"
                btnToggleDistance.text = "Convert to Miles"
            }
            isKm = !isKm
        }

        stopList = loadStopsFromFile()
        recyclerViewStops.layoutManager = LinearLayoutManager(this)
        val adapter = StopsAdapter(stopList, currentStop)
        recyclerViewStops.adapter = adapter

        btnNextStop.setOnClickListener {
            if (currentStop < totalStops) {
                currentStop++
                progress += (100 / totalStops)
                progressBar.progress = progress
            }

            if (distanceInKm > 0) {
                distanceInKm -= 100
            }
            tvDistance.text = "Distance: $distanceInKm km"



            recyclerViewStops.adapter = StopsAdapter(stopList, currentStop)
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadStopsFromFile(): List<String> {
        val stopList = mutableListOf<String>()
        val inputStream = resources.openRawResource(R.raw.stops)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.forEachLine { stopList.add(it) }
        reader.close()
        return stopList
    }
}
