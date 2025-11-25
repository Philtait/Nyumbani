package com.example.nyumbani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nyumbani.navigation.Screen
import com.example.nyumbani.ui.screens.auth.*
import com.example.nyumbani.ui.screens.owner.*
import com.example.nyumbani.ui.screens.student.*
import com.example.nyumbani.ui.theme.NyumbaniTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NyumbaniTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    // DEFAULT ANIMATIONS FOR ALL SCREENS
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    },
                    popEnterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                    },
                    popExitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                    }
                ) {
                    // Auth
                    composable(Screen.Splash.route) { SplashScreen(navController) }
                    composable(Screen.Login.route) { LoginScreen(navController) }
                    composable(Screen.SignUp.route) { SignUpScreen(navController) }
                    composable(Screen.EmailVerification.route) { EmailVerificationScreen(navController) }

                    // Owner
                    composable(Screen.OwnerHome.route) { OwnerHomeScreen(navController) }
                    composable(Screen.AddHostel.route) { AddHostelScreen(navController) }
                    composable(Screen.OwnerBookings.route) { OwnerBookingsScreen(navController) }
                    composable(Screen.OwnerProfile.route) { OwnerProfileScreen(navController) }

                    // Student
                    composable(Screen.StudentHome.route) { StudentHomeScreen(navController) }
                    composable(Screen.StudentProfile.route) { StudentProfileScreen(navController) }
                    composable(Screen.MyBookings.route) { MyBookingsScreen(navController) }

                    // Details & Booking
                    composable(
                        route = "hostel_details/{hostelId}",
                        arguments = listOf(navArgument("hostelId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val hostelId = backStackEntry.arguments?.getString("hostelId") ?: ""
                        HostelDetailsScreen(navController, hostelId)
                    }

                    composable(
                        route = "booking_screen/{hostelId}/{hostelName}/{price}/{ownerId}",
                        arguments = listOf(
                            navArgument("hostelId") { type = NavType.StringType },
                            navArgument("hostelName") { type = NavType.StringType },
                            navArgument("price") { type = NavType.FloatType },
                            navArgument("ownerId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val hostelId = backStackEntry.arguments?.getString("hostelId") ?: ""
                        val hostelName = backStackEntry.arguments?.getString("hostelName") ?: ""
                        val price = backStackEntry.arguments?.getFloat("price")?.toDouble() ?: 0.0
                        val ownerId = backStackEntry.arguments?.getString("ownerId") ?: ""
                        BookingScreen(navController, hostelId, hostelName, price, ownerId)
                    }
                }
            }
        }
    }
}