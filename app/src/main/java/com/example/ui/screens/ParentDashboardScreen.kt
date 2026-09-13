package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel

@Composable
fun ParentDashboardScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val progress by viewModel.uiState.collectAsStateWithLifecycle()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E5F5))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(40.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Parent Dashboard", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6A1B9A))
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        DashboardCard("Total Stars ⭐", "${progress.stars}")
        DashboardCard("Letters Learned 🔤", "${progress.lettersLearned}")
        DashboardCard("Numbers Learned 🔢", "${progress.numbersLearned}")
        DashboardCard("Games Completed 🎮", "${progress.gamesCompleted}")
        
        val accuracy = if (progress.totalQuestions > 0) {
            (progress.correctAnswers.toFloat() / progress.totalQuestions * 100).toInt()
        } else 0
        DashboardCard("Quiz Accuracy 🎯", "$accuracy%")
    }
}

@Composable
fun DashboardCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color(0xFF424242))
            Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        }
    }
}
