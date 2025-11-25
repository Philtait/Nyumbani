package com.example.nyumbani.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nyumbani.navigation.Screen
import com.example.nyumbani.ui.theme.GoldPrimary
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(2000) // Wait 2 seconds

        if (auth.currentUser != null) {
            // User is logged in -> Go to Student Home
            navController.navigate(Screen.StudentHome.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            // User is NOT logged in -> Go to Login
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    // UI Layout
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Nyumbani",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = GoldPrimary)
        }
    }
}