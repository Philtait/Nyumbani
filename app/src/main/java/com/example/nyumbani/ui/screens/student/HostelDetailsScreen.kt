package com.example.nyumbani.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nyumbani.ui.components.HostelMap
import com.example.nyumbani.ui.components.NyumbaniAppBar
import com.example.nyumbani.ui.components.NyumbaniButton
import com.example.nyumbani.ui.theme.GoldPrimary
import com.example.nyumbani.ui.viewmodel.HostelViewModel

@Composable
fun HostelDetailsScreen(
    navController: NavController,
    hostelId: String,
    viewModel: HostelViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadAllHostels() }

    val hostelList by viewModel.hostelListState.collectAsState()
    val hostel = hostelList.data?.find { it.hostelId == hostelId }

    if (hostel == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = GoldPrimary)
        }
        return
    }

    Scaffold(
        topBar = {
            NyumbaniAppBar(
                title = "Hostel Details",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            // Container for the button with shadow/elevation
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    NyumbaniButton(
                        text = "Book Now - KES ${hostel.price}",
                        onClick = {
                            navController.navigate("booking_screen/${hostel.hostelId}/${hostel.name}/${hostel.price}/${hostel.ownerId}")
                        }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = if (hostel.imageUrls.isNotEmpty()) hostel.imageUrls[0] else "",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = hostel.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = hostel.address,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Description", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hostel.description,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (hostel.features.isNotEmpty()) {
                    Text("Amenities", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(8.dp))

                    hostel.features.forEach { feature ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
                            Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = feature, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text("Location", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    val lat = if (hostel.latitude != 0.0) hostel.latitude else -1.2921
                    val lng = if (hostel.longitude != 0.0) hostel.longitude else 36.8219
                    HostelMap(latitude = lat, longitude = lng, title = hostel.name)
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}