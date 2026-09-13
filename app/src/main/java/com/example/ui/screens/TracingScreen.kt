package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel

@Composable
fun TracingScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(40.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Tracing", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "A",
            fontSize = 240.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFBDBDBD)
        )
        Text(
            text = "Start here \u2192", // right arrow
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF5252)
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { 
                viewModel.speak("Beautiful A!")
                viewModel.addStars(10)
                onNavigateBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
        ) {
            Text("Done", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
