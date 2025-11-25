package com.example.nyumbani.ui.screens.owner

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nyumbani.ui.components.NyumbaniAppBar
import com.example.nyumbani.ui.components.NyumbaniButton
import com.example.nyumbani.ui.theme.GoldPrimary
import com.example.nyumbani.ui.viewmodel.HostelViewModel
import com.example.nyumbani.utils.Resource

@Composable
fun AddHostelScreen(
    navController: NavController,
    viewModel: HostelViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    val availableFeatures = listOf("WiFi", "Water", "Security", "Electricity", "Tiles", "Hot Shower")
    val selectedFeatures = remember { mutableStateListOf<String>() }

    val context = LocalContext.current
    val operationState by viewModel.operationState.collectAsState()

    LaunchedEffect(operationState) {
        if (operationState is Resource.Success && operationState.data == true) {
            Toast.makeText(context, "Hostel Published Successfully!", Toast.LENGTH_SHORT).show()
            viewModel.resetOperationState()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = { NyumbaniAppBar(title = "Add New Hostel", onBackClick = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Property Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Hostel Name") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Location (e.g. Madaraka)") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (KES/Month)") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3, shape = MaterialTheme.shapes.medium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Image URL") }, placeholder = { Text("https://...") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)

            Spacer(modifier = Modifier.height(24.dp))
            Text("Amenities", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            // Styled Checkbox List
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    availableFeatures.forEach { feature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedFeatures.contains(feature)) selectedFeatures.remove(feature)
                                    else selectedFeatures.add(feature)
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedFeatures.contains(feature),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) selectedFeatures.add(feature) else selectedFeatures.remove(feature)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
                            )
                            Text(text = feature, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            NyumbaniButton(
                text = "Publish Property",
                onClick = {
                    if (name.isNotEmpty() && price.isNotEmpty()) {
                        viewModel.addHostel(name, address, price, description, imageUrl, selectedFeatures.toList())
                    } else {
                        Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                isLoading = operationState is Resource.Loading
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}