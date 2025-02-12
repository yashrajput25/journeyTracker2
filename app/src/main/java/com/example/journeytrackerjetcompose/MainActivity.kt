package com.example.journeytrackerjetcompose

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journeytrackerjetcompose.ui.theme.JourneyTrackerJetComposeTheme
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

    var stops by remember { mutableStateOf(listOf("New York")) }
    val allStops = listOf("New York", "London", "Dubai", "Delhi", "Sydney")
    var currentStopIndex by remember { mutableIntStateOf(0) }
    var progress by remember { mutableIntStateOf(0) }
    var distanceInKm by remember { mutableDoubleStateOf(500.0) }
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
                text = "JourneyTracker", style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom =  5.dp)
        ){
            Text(text = "Distance : ${if (isKm)"$distanceInKm km" else "${distanceInKm * 0.621} miles"}",
                style = MaterialTheme.typography.headlineSmall )
        }

        Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(progress = { progress/100f }, modifier = Modifier.fillMaxWidth())



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
                if (currentStopIndex < allStops.size - 1) {
                    currentStopIndex++
                    stops = stops + allStops[currentStopIndex] // Add next stop dynamically
                    progress = (currentStopIndex.toFloat() / (allStops.size - 1) * 100).toInt()
                    distanceInKm -= distanceInKm / (allStops.size - currentStopIndex + 1)
                }
            },
            enabled = currentStopIndex < allStops.size - 1
        ) {
            Text("Next Stop Reached")
        }

    }
}
