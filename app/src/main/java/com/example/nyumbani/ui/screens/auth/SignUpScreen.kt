package com.example.nyumbani.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // <--- THIS IMPORT WAS MISSING
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nyumbani.navigation.Screen
import com.example.nyumbani.ui.components.NyumbaniButton
import com.example.nyumbani.ui.theme.GoldPrimary
import com.example.nyumbani.ui.viewmodel.AuthViewModel
import com.example.nyumbani.utils.Resource

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Student") }

    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (val state = authState) {
            is Resource.Success -> {
                Toast.makeText(context, "Account Created! Please verify email.", Toast.LENGTH_LONG).show()
                navController.navigate(Screen.EmailVerification.route) {
                    popUpTo(Screen.SignUp.route) { inclusive = true }
                }
                viewModel.resetState()
            }
            is Resource.Error -> {
                if (!state.message.isNullOrEmpty()) Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("Create Account", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Text("Join Nyumbani today", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            Spacer(modifier = Modifier.height(32.dp))

            // Role Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RoleButton(
                    role = "Student",
                    isSelected = selectedRole == "Student",
                    modifier = Modifier.weight(1f)
                ) { selectedRole = "Student" }

                RoleButton(
                    role = "Owner",
                    isSelected = selectedRole == "Owner",
                    modifier = Modifier.weight(1f)
                ) { selectedRole = "Owner" }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)

            Spacer(modifier = Modifier.height(32.dp))

            NyumbaniButton(
                text = "Sign Up",
                onClick = { viewModel.signUp(name, email, password, phone, selectedRole) },
                isLoading = authState is Resource.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text("Already have an account? Login", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun RoleButton(role: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(45.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) GoldPrimary else Color.LightGray.copy(alpha = 0.5f),
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(role)
    }
}