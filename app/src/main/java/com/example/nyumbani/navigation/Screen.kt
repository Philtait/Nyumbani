package com.example.nyumbani.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object EmailVerification : Screen("email_verification")

    object StudentHome : Screen("student_home")
    object StudentProfile : Screen("student_profile")

    object OwnerHome : Screen("owner_home")
    object OwnerProfile : Screen("owner_profile") // <--- Added
    object AddHostel : Screen("add_hostel")
    object OwnerBookings : Screen("owner_bookings")

    object MyBookings : Screen("my_bookings")
    object HostelDetails : Screen("hostel_details")
}