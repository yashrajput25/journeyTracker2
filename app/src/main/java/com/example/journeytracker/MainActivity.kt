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

    private lateinit var allStops: List<String>
    private lateinit var allDistances: List<Int>
    private val visibleStops = mutableListOf<String>()
    private var currentStopIndex = 0
    private var progress = 0
    private var isKm = true
    private lateinit var adapter: StopsAdapter

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewStops = findViewById<RecyclerView>(R.id.recyclerViewStops)
        val btnToggleDistance = findViewById<Button>(R.id.btnToggleDistance)
        val btnNextStop = findViewById<Button>(R.id.btnNextStop)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvDistance = findViewById<TextView>(R.id.tvDistance)

        val (stops, distances) = loadStopsFromFile()
        allStops = stops
        allDistances = distances

        if (allStops.isNotEmpty()) {
            visibleStops.add(allStops[0])

            if (allDistances.isNotEmpty()) {
                tvDistance.text = "Distance to the next stop: ${allDistances[0]} km"
            } else {
                tvDistance.text = "Distance: Unknown"
            }
        }

        recyclerViewStops.layoutManager = LinearLayoutManager(this)
        adapter = StopsAdapter(visibleStops)
        recyclerViewStops.adapter = adapter
        progressBar.progress = progress

        btnToggleDistance.setOnClickListener {
            val distance = allDistances.getOrElse(currentStopIndex) { 0 }
            val displayDistance = if (isKm) "$distance km" else "${"%.2f".format(distance * 0.621)} miles"
            tvDistance.text = "Distance to the next stop: $displayDistance"
            isKm = !isKm
        }

        btnNextStop.setOnClickListener {
            if (currentStopIndex < allStops.size - 1) {
                currentStopIndex++
                progress = ((currentStopIndex.toFloat() / (allStops.size - 1)) * 100).toInt()
                progressBar.progress = progress


                val distance = allDistances.getOrElse(currentStopIndex) { 0 }
                tvDistance.text = "Distance to the next stop: $distance km"

                visibleStops.add(allStops[currentStopIndex])
                adapter.notifyItemInserted(visibleStops.size - 1)
            }

            if (currentStopIndex == allStops.size - 1) {
                btnNextStop.isEnabled = false
                btnNextStop.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                progressBar.progress = 100
                tvDistance.text = "You have arrived!"
            }
        }
    }


    private fun loadStopsFromFile(): Pair<List<String>, List<Int>> {
        val stopList = mutableListOf<String>()
        val distanceList = mutableListOf<Int>()

        val inputStream = resources.openRawResource(R.raw.stops)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size == 2) {
                stopList.add(parts[0].trim())
                distanceList.add(parts[1].trim().toInt())
            }
        }
        reader.close()

        return Pair(stopList, distanceList)
    }
}


