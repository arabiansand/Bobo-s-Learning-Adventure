package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
fun MainMenuScreen(
    viewModel: MainViewModel,
    onNavigateToAlphabet: () -> Unit,
    onNavigateToNumbers: () -> Unit,
    onNavigateToTracing: () -> Unit,
    onNavigateToParent: () -> Unit,
    onNavigateToPiAuth: () -> Unit = {}
) {
    val progress by viewModel.uiState.collectAsStateWithLifecycle()
    val isAuthenticating by viewModel.isAuthenticating.collectAsStateWithLifecycle()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToParent) {
                Icon(Icons.Default.Menu, contentDescription = "Parent Dashboard", tint = Color(0xFF00796B))
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Stars", tint = Color(0xFFFFC107), modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${progress.stars}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Let's Play!",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFD32F2F)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        MenuButton("🔤 Alphabet Adventure", Color(0xFF4CAF50), onNavigateToAlphabet)
        Spacer(modifier = Modifier.height(16.dp))
        MenuButton("🔢 Number Adventure", Color(0xFF2196F3), onNavigateToNumbers)
        Spacer(modifier = Modifier.height(16.dp))
        MenuButton("✏️ Letter Tracing", Color(0xFF9C27B0), onNavigateToTracing)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isAuthenticating) {
            CircularProgressIndicator(color = Color(0xFF5E35B1))
        } else {
            MenuButton("🟣 Sign In with Pi", Color(0xFF5E35B1)) {
                viewModel.setAuthenticating(true)
                onNavigateToPiAuth()
            }
        }
    }
}

@Composable
fun MenuButton(title: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
