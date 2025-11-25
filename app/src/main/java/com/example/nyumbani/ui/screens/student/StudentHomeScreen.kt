package com.example.nyumbani.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nyumbani.navigation.Screen
import com.example.nyumbani.ui.components.HostelCard
import com.example.nyumbani.ui.components.NyumbaniAppBar
import com.example.nyumbani.ui.viewmodel.HostelViewModel
import com.example.nyumbani.utils.Resource

@Composable
fun StudentHomeScreen(
    navController: NavController,
    viewModel: HostelViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadAllHostels() }

    val hostelState by viewModel.hostelListState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                NyumbaniAppBar(
                    title = "Nyumbani",
                    action = {
                        IconButton(onClick = { navController.navigate(Screen.StudentProfile.route) }) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                )
                // Search Bar integrated into header
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search hostels or location...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val state = hostelState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Success -> {
                    val allHostels = state.data ?: emptyList()
                    val filteredHostels = allHostels.filter {
                        it.name.contains(searchText, ignoreCase = true) ||
                                it.address.contains(searchText, ignoreCase = true)
                    }

                    if (filteredHostels.isEmpty()) {
                        Text("No hostels found.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(filteredHostels) { hostel ->
                                HostelCard(hostel = hostel) {
                                    navController.navigate("hostel_details/${hostel.hostelId}")
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> Text("Error loading data", color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}