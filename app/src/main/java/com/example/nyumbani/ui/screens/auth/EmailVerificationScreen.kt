package com.example.nyumbani.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun EmailVerificationScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val verificationState by viewModel.verificationState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(verificationState) {
        if (verificationState is Resource.Success && verificationState.data == true) {
            Toast.makeText(context, "Email Verified! Logging in...", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Login.route) { popUpTo(0) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = GoldPrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Verify your Email",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We have sent a verification link to your email address. Please click the link and then press the button below.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            NyumbaniButton(
                text = "I have verified my email",
                onClick = { viewModel.checkEmailVerification() },
                isLoading = verificationState is Resource.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    viewModel.resendEmail()
                    Toast.makeText(context, "Verification email resent!", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("Resend Email", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            ) {
                Text("Back to Login", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}