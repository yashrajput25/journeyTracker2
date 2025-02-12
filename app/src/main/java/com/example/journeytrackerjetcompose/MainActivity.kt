package com.example.journeytrackerjetcompose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journeytrackerjetcompose.ui.theme.JourneyTrackerJetComposeTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JourneyTrackerApp()
        }
    }
}

@Composable
fun JourneyTrackerApp(){
    val context = LocalContext.current

    val (stopList , distanceList ) = loadStopsFromFile(context)

    var stops by remember { mutableStateOf(listOf(stopList.firstOrNull() ?: "Unknown")) } // Start with first stop
    var currentStopIndex by remember { mutableIntStateOf(0) }
    var progress by remember { mutableStateOf(0f) }
    var distanceInKm by remember { mutableDoubleStateOf(distanceList.firstOrNull()?.toDouble() ?: 0.0) }
    var isKm by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
        ){
            Text(
                text = "JourneyTracker",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom =  5.dp)
        ){
            Text(text = "Distance to the next stop: ${if (isKm)"$distanceInKm km" else "${distanceInKm * 0.621} miles"}",
                style = MaterialTheme.typography.headlineSmall )
        }

        Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(progress = { progress.toFloat() }, modifier = Modifier.fillMaxWidth())



        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp)
        ){
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(stops) { stop ->
                    Text(text = stop, style = MaterialTheme.typography.headlineSmall)
                    HorizontalDivider()
                }
            }
        }



        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = {isKm =! isKm}) {

                Text(if (isKm) "Convert to Miles" else "Convert to Kms")
            }


        Button(
            onClick = {
                if (currentStopIndex < stopList.size - 1) {
                    currentStopIndex++
                    stops = stops + stopList[currentStopIndex]
                    progress = (currentStopIndex.toFloat() / (stopList.size - 1))

                    // Update distance from list
                    distanceInKm = distanceList.getOrElse(currentStopIndex) { 0 }.toDouble()
                }
            },
            enabled = currentStopIndex < stopList.size - 1
        ) {
            Text("Next Stop Reached")
        }

    }
}

@Composable
fun loadStopsFromFile(context: Context): Pair<List<String>, List<Int>> {
    val stopList = mutableListOf<String>()
    val distanceList = mutableListOf<Int>()

    try {
        val inputStream = context.resources.openRawResource(R.raw.stops)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size == 2) {
                stopList.add(parts[0].trim())
                distanceList.add(parts[1].trim().toInt())
            }
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return Pair(stopList, distanceList) // Return both lists
}

