package com.example.nyumbani.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nyumbani.model.Hostel
import com.example.nyumbani.ui.theme.GoldPrimary

@Composable
fun HostelCard(
    hostel: Hostel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Image (Loads from URL)
            AsyncImage(
                model = if (hostel.imageUrls.isNotEmpty()) hostel.imageUrls[0] else "https://via.placeholder.com/300",
                contentDescription = "Hostel Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Nice big image
                contentScale = ContentScale.Crop
            )

            // Details
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = hostel.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = hostel.address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "KES ${hostel.price}/month",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldPrimary
                )
            }
        }
    }
}