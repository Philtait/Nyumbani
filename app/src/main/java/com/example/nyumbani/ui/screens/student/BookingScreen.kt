package com.example.nyumbani.ui.screens.student

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nyumbani.navigation.Screen
import com.example.nyumbani.ui.components.NyumbaniAppBar
import com.example.nyumbani.ui.components.NyumbaniButton
import com.example.nyumbani.ui.theme.GoldPrimary
import com.example.nyumbani.ui.viewmodel.BookingViewModel
import com.example.nyumbani.utils.Resource
import java.util.Calendar

@Composable
fun BookingScreen(
    navController: NavController,
    hostelId: String,
    hostelName: String,
    price: Double,
    ownerId: String,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val bookingState by viewModel.bookingState.collectAsState()

    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val checkInPicker = DatePickerDialog(context, { _, y, m, d -> checkInDate = "$d/${m + 1}/$y" }, year, month, day)
    val checkOutPicker = DatePickerDialog(context, { _, y, m, d -> checkOutDate = "$d/${m + 1}/$y" }, year, month, day)

    LaunchedEffect(bookingState) {
        if (bookingState is Resource.Success && bookingState.data == true) {
            Toast.makeText(context, "Booking Successful!", Toast.LENGTH_LONG).show()
            viewModel.resetState()
            navController.navigate(Screen.MyBookings.route) { popUpTo(Screen.StudentHome.route) }
        }
    }

    Scaffold(
        topBar = { NyumbaniAppBar(title = "Confirm Booking", onBackClick = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Hostel", fontSize = 14.sp, color = Color.Gray)
                    Text(hostelName, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Price per Month", fontSize = 14.sp, color = Color.Gray)
                    Text("KES $price", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = GoldPrimary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Check In
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = checkInDate,
                    onValueChange = {},
                    label = { Text("Check-in Date") },
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.CalendarToday, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Box(modifier = Modifier.matchParentSize().clickable { checkInPicker.show() })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Check Out
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = checkOutDate,
                    onValueChange = {},
                    label = { Text("Check-out Date") },
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.CalendarToday, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Box(modifier = Modifier.matchParentSize().clickable { checkOutPicker.show() })
            }

            Spacer(modifier = Modifier.weight(1f))

            NyumbaniButton(
                text = "Confirm & Book",
                onClick = {
                    if (checkInDate.isNotEmpty() && checkOutDate.isNotEmpty()) {
                        // --- PASSING DATES TO VIEWMODEL HERE ---
                        viewModel.createBooking(hostelId, hostelName, price, ownerId, checkInDate, checkOutDate)
                    } else {
                        Toast.makeText(context, "Please select check-in and check-out dates", Toast.LENGTH_SHORT).show()
                    }
                },
                isLoading = bookingState is Resource.Loading
            )
        }
    }
}