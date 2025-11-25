# 🏠 Nyumbani - Smart Student Accommodation Finder

> **Final Year Android Project**  
> **Built with:** Kotlin, Jetpack Compose, Firebase & MVVM Architecture.

## 📄 Abstract
**Nyumbani** is a full-stack mobile application designed to solve the housing crisis for university students. In many university towns, students struggle to find safe, affordable, and verified housing, while landlords lack efficient ways to manage tenants.

Nyumbani bridges this gap by providing a digital marketplace where **Owners** can list properties and **Students** can view details, verify locations via interactive maps, and book accommodation securely.

---

## ✨ Key Features

### 🎓 For Students
*   **Secure Authentication:** Sign up and verify identity via Email.
*   **Smart Search:** Filter hostels by name or location (e.g., "Juja", "Madaraka").
*   **Rich Details:** View pricing, description, and amenities (WiFi, Water, Security).
*   **Interactive Mapping:** View exact hostel locations using **OpenStreetMap** (Osmdroid).
*   **Booking System:** Select Check-in/Check-out dates and secure a room.
*   **Booking History:** Track booking status (Pending, Confirmed, Rejected).

### 🏘️ For Property Owners
*   **Property Management:** Add new hostels with details, pricing, and amenities.
*   **Image Handling:** Display property photos via URL links.
*   **Booking Ledger:** View incoming booking requests from students.
*   **Approval Workflow:** Accept or Reject bookings with real-time status updates for students.
*   **Dashboard:** Manage profile and view listed properties.

---

## 🛠️ Technology Stack

The app is built using modern Android development standards:

*   **Language:** [Kotlin](https://kotlinlang.org/) (100%)
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetbrains/compose) (Material Design 3)
*   **Architecture:** MVVM (Model-View-ViewModel) with Repository Pattern.
*   **Dependency Injection:** [Hilt](https://dagger.dev/hilt/).
*   **Backend:** 
    *   **Firebase Authentication:** Email/Password Login & Verification.
    *   **Cloud Firestore:** NoSQL Database for Users, Hostels, and Bookings.
*   **Mapping:** [Osmdroid](https://github.com/osmdroid/osmdroid) (OpenStreetMap implementation).
*   **Image Loading:** [Coil](https://coil-kt.github.io/coil/).
*   **Navigation:** Jetpack Navigation Component.
*   **Asynchronous Programming:** Kotlin Coroutines & StateFlow.

---

## 📱 Screenshots

| Student Home | Hostel Details | Booking Screen |
|:![Nyum![Booking Screen](https://github.com/user-attachments/assets/41818c14-9480-4bc8-89e2-d360bb64dd64)
bani-Home](https://github.com/user-attachments/assets/81ca2cea-08ae-4c17-8b96-8db574de39ca)
---:|:![HostelDetails](https://github.com/user-attachments/assets/e3afb62c-0244-466c-8780-e6862838f61f)


| Owner Dashboard | Add Property | Tenant Bookings |
|:---:|:---:|:---:|
| <img src="https://via.placeholder.com/250x500?text=Owner+Home" width="200"> | <img src="https://via.placeholder.com/250x500?text=Add+Hostel" width="200"> | <img src="https://via.placeholder.com/250x500?text=Management" width="200"> |

*(Note: Screenshots can be added by dragging images into the GitHub editor).*

---

## 🚀 Installation & Setup

To run this project locally:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/philtait/Nyumbani.git
    ```
2.  **Open in Android Studio:**
    *   File -> Open -> Select the cloned folder.
3.  **Firebase Configuration:**
    *   The `google-services.json` file is included for academic grading purposes.
    *   Ensure your internet connection is active.
4.  **Build & Run:**
    *   Sync Gradle.
    *   Select an Emulator (API 26+) or Physical Device.
    *   Click **Run**.

---

## 🏗️ Architecture

The app follows the **Clean Architecture** principles using **MVVM**:

*   **UI Layer (Compose):** Displays data and captures user events.
*   **ViewModel:** Holds UI state (`StateFlow`) and handles business logic.
*   **Repository:** Mediates between the ViewModel and Data Sources.
*   **Data Source:** Firebase (Remote) and SharedPreferences (Local).

---

## 👤 Author

**Philip Tait**  
Final Year Project - 2025
