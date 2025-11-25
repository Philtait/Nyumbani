package com.example.nyumbani.ui.screens.student

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
import com.example.nyumbani.navigation.Screen
import com.example.nyumbani.ui.components.NyumbaniAppBar
import com.example.nyumbani.ui.theme.GoldPrimary
import com.example.nyumbani.ui.viewmodel.BookingViewModel
import com.example.nyumbani.utils.Resource

@Composable
fun MyBookingsScreen(
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadMyBookings() }
    val bookingsState by viewModel.myBookings.collectAsState()

    Scaffold(
        topBar = { NyumbaniAppBar(title = "My Bookings", onBackClick = { navController.navigate(Screen.StudentHome.route) }) }
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
                        Text("No bookings yet.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(list) { booking ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = booking.hostelName, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                                        // --- SHOWING DATES HERE ---
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Dates: ${booking.checkInDate} - ${booking.checkOutDate}",
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Price: KES ${booking.totalPrice}", color = GoldPrimary, fontWeight = FontWeight.Medium)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Status: ", fontSize = 14.sp)
                                            Text(
                                                text = booking.status,
                                                color = if (booking.status == "Confirmed") GoldPrimary else Color.Red,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Only show Cancel if not confirmed
                                        if (booking.status == "Pending") {
                                            OutlinedButton(
                                                onClick = { viewModel.cancelBooking(booking.bookingId) },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                                modifier = Modifier.align(Alignment.End)
                                            ) {
                                                Text("Cancel Booking")
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