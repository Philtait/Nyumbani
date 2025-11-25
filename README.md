# 🏠 Nyumbani

> **Unit:** Mobile Application Development  
> **Author:** Philip Tait (2025)

## 📄 Overview
**Nyumbani** is a native Android application developed using **Kotlin** and **Jetpack Compose**. It serves as a digital marketplace connecting university students with verified hostel owners.

The app solves the student housing crisis by offering a secure platform for browsing listings, verifying locations via interactive maps, and managing digital bookings.

## 🛠️ Tech Stack
*   **Architecture:** MVVM (Clean Architecture)
*   **UI:** Jetpack Compose (Material 3)
*   **Backend:** Firebase (Auth & Firestore)
*   **DI:** Hilt
*   **Maps:** Osmdroid (OpenStreetMap)

## 📱 Screenshots

| Student Home | Hostel Details | Booking |
|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/81ca2cea-08ae-4c17-8b96-8db574de39ca" width="200"> | <img src="https://github.com/user-attachments/assets/e3afb62c-0244-466c-8780-e6862838f61f" width="200"> | <img src="https://github.com/user-attachments/assets/ef1240c8-0f3c-45e3-b2f6-61774370bf38" width="200"> |

| Owner Dashboard | Add Property | Tenant Bookings |
|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/21e25bbe-975a-4480-805c-3d979e13d381" width="200"> | <img src="https://github.com/user-attachments/assets/c823dc79-d30d-41be-ac5c-e4ab66e0f727" width="200"> | <img src="https://github.com/user-attachments/assets/c02a0dbe-08a2-495e-ba6b-011a838a0e3d" width="200"> |

## ✨ Key Features
*   **Role-Based Access:** Distinct dashboards for Students and Property Owners.
*   **Smart Booking:** Date selection, booking tracking, and approval workflows.
*   **Geolocation:** Integrated maps to view hostel proximity.
*   **Property Management:** Owners can list properties, amenities, and manage tenant requests.

## 🚀 Setup
1.  **Clone:** `git clone https://github.com/philtait/Nyumbani.git`
2.  **Configure:** The `google-services.json` is included.
3.  **Run:** Open in Android Studio, sync Gradle, and run on an Emulator/Device.
