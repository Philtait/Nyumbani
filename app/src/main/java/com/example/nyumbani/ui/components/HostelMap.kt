package com.example.nyumbani.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.preference.PreferenceManager

@Composable
fun HostelMap(
    latitude: Double,
    longitude: Double,
    title: String
) {
    // Render the Map
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // 1. Initialize Map Configuration (Required by Osmdroid)
            Configuration.getInstance().load(
                context,
                PreferenceManager.getDefaultSharedPreferences(context)
            )

            // 2. Create and Config MapView
            MapView(context).apply {
                setMultiTouchControls(true)

                // Set Zoom and Center
                val mapController = controller
                mapController.setZoom(15.0)
                val startPoint = GeoPoint(latitude, longitude)
                mapController.setCenter(startPoint)

                // Add Red Marker
                val marker = Marker(this)
                marker.position = startPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = title
                overlays.add(marker)
            }
        }
    )
}