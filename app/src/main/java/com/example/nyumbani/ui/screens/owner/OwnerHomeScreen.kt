package com.example.nyumbani.ui.screens.owner

import androidx.compose.foundation.ExperimentalFoundationApi // <--- Added Import
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
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
import com.example.nyumbani.ui.theme.GoldPrimary
import com.example.nyumbani.ui.viewmodel.HostelViewModel
import com.example.nyumbani.utils.Resource

// Added ExperimentalFoundationApi to the OptIn list to fix the error
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OwnerHomeScreen(
    navController: NavController,
    viewModel: HostelViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadOwnerHostels() }

    val hostelState by viewModel.hostelListState.collectAsState()

    Scaffold(
        topBar = {
            NyumbaniAppBar(
                title = "My Properties",
                action = {
                    IconButton(onClick = { navController.navigate(Screen.OwnerProfile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddHostel.route) },
                containerColor = GoldPrimary,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Property")
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
                    val hostels = state.data ?: emptyList()
                    if (hostels.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No properties listed yet.",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Tap the + button to add one.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(hostels, key = { it.hostelId }) { hostel -> // Added key for animation
                                Box(modifier = Modifier.animateItemPlacement()) {
                                    HostelCard(hostel = hostel) {
                                        // Optional: Click to Edit in future
                                    }

                                    // Stylish Delete Button
                                    IconButton(
                                        onClick = { viewModel.deleteHostel(hostel.hostelId) },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                            .background(
                                                color = Color.White.copy(alpha = 0.9f),
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> Text("Error loading data", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}