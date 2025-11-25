package com.example.nyumbani.ui.screens.owner

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.nyumbani.ui.viewmodel.ProfileViewModel
import com.example.nyumbani.utils.Resource

@Composable
fun OwnerProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadProfile() }

    val profileState by viewModel.profileState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val context = LocalContext.current

    var isEditing by remember { mutableStateOf(false) }
    var nameEdit by remember { mutableStateOf("") }
    var phoneEdit by remember { mutableStateOf("") }

    LaunchedEffect(updateState) {
        if (updateState is Resource.Success && updateState.data == true) {
            Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            isEditing = false
        }
    }

    Scaffold(
        topBar = {
            NyumbaniAppBar(
                title = "Owner Profile",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val state = profileState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Success -> {
                    val user = state.data!!

                    LaunchedEffect(user) {
                        if (nameEdit.isEmpty()) nameEdit = user.name
                        if (phoneEdit.isEmpty()) phoneEdit = user.phone
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar
                        Card(
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(8.dp),
                            modifier = Modifier.size(120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, null, modifier = Modifier.size(60.dp), tint = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (isEditing) {
                            OutlinedTextField(
                                value = nameEdit,
                                onValueChange = { nameEdit = it },
                                label = { Text("Full Name") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = phoneEdit,
                                onValueChange = { phoneEdit = it },
                                label = { Text("Phone Number") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = { isEditing = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) { Text("Cancel") }

                                Button(
                                    onClick = { viewModel.updateProfile(nameEdit, phoneEdit) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) { Text("Save") }
                            }

                        } else {
                            Text(text = user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            Text(text = user.email, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = user.phone, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

                            Spacer(modifier = Modifier.height(40.dp))

                            NyumbaniButton(text = "Edit Profile", onClick = { isEditing = true })

                            Spacer(modifier = Modifier.height(16.dp))

                            // Tenant Bookings Button
                            Button(
                                onClick = { navController.navigate(Screen.OwnerBookings.route) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text("View Tenant Bookings")
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            OutlinedButton(
                                onClick = {
                                    viewModel.logout()
                                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Logout")
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text("Error loading profile", modifier = Modifier.align(Alignment.Center))
                }
                // Added else branch to fix "exhaustive" error
                else -> {}
            }
        }
    }
}