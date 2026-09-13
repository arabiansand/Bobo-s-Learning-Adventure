package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel

@Composable
fun NumbersScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    var currentNumber by remember { mutableIntStateOf(1) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
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
            Text(text = "Numbers", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "$currentNumber",
            fontSize = 150.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFF9800)
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Show apples
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until currentNumber) {
                Text("🍎", fontSize = 48.sp, modifier = Modifier.padding(4.dp))
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { 
                viewModel.speak("$currentNumber apples!")
                viewModel.markNumberLearned()
                viewModel.addStars(5)
                if (currentNumber < 20) {
                    currentNumber++
                } else {
                    onNavigateBack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(if (currentNumber < 20) "Next Number" else "Finish", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}
