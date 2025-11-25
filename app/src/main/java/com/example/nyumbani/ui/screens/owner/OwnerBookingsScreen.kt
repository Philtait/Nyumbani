package com.example.nyumbani.ui.screens.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nyumbani.ui.components.NyumbaniAppBar
import com.example.nyumbani.ui.theme.GoldPrimary
import com.example.nyumbani.ui.viewmodel.BookingViewModel
import com.example.nyumbani.utils.Resource

@Composable
fun OwnerBookingsScreen(
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadOwnerBookings() }
    val bookingsState by viewModel.myBookings.collectAsState()

    Scaffold(
        topBar = { NyumbaniAppBar(title = "Tenant Bookings", onBackClick = { navController.popBackStack() }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val state = bookingsState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Success -> {
                    val list = state.data ?: emptyList()
                    if (list.isEmpty()) {
                        Text(
                            "No bookings received yet.",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(list) { booking ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        // Header
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text("Property", fontSize = 12.sp, color = Color.Gray)
                                                Text(booking.hostelName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            }
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text("Price", fontSize = 12.sp, color = Color.Gray)
                                                Text("KES ${booking.totalPrice}", color = GoldPrimary, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        Divider(modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(), color = Color.LightGray.copy(alpha = 0.3f))

                                        // Details
                                        Text("Tenant: ${booking.studentName}", fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(4.dp))

                                        if (booking.checkInDate.isNotEmpty()) {
                                            Text("Period: ${booking.checkInDate} - ${booking.checkOutDate}", fontSize = 14.sp, color = Color.Gray)
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Actions / Status
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            // Status Chip
                                            Text(
                                                text = booking.status.uppercase(),
                                                fontWeight = FontWeight.Bold,
                                                color = when(booking.status) {
                                                    "Confirmed" -> Color(0xFF4CAF50) // Green
                                                    "Cancelled" -> Color(0xFFE53935) // Red
                                                    else -> GoldPrimary
                                                },
                                                fontSize = 14.sp
                                            )

                                            // Buttons (Only show if Pending)
                                            if (booking.status == "Pending") {
                                                Row {
                                                    OutlinedButton(
                                                        onClick = { viewModel.updateStatus(booking.bookingId, "Cancelled") },
                                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                                        modifier = Modifier.height(36.dp)
                                                    ) {
                                                        Text("Reject", fontSize = 12.sp)
                                                    }

                                                    Spacer(modifier = Modifier.width(8.dp))

                                                    Button(
                                                        onClick = { viewModel.updateStatus(booking.bookingId, "Confirmed") },
                                                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                                        modifier = Modifier.height(36.dp)
                                                    ) {
                                                        Text("Confirm", fontSize = 12.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}