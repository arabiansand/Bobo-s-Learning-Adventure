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

val letters = listOf(
    Pair("A", "Apple"), Pair("B", "Bear"), Pair("C", "Cat"),
    Pair("D", "Dog"), Pair("E", "Elephant"), Pair("F", "Fish")
)

@Composable
fun AlphabetScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var showQuiz by remember { mutableStateOf(false) }
    
    val currentLetter = letters[currentIndex]
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
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
            Text(text = "Alphabet", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF33691E))
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(40.dp))

        if (!showQuiz) {
            Text(
                text = currentLetter.first,
                fontSize = 120.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF5252)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "${currentLetter.first} is for ${currentLetter.second}!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { 
                    viewModel.speak("${currentLetter.first} is for ${currentLetter.second}!")
                    showQuiz = true
                },
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Play", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Text(
                text = "Which one starts with ${currentLetter.first}?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF512DA8),
                modifier = Modifier.padding(20.dp)
            )
            
            val options = letters.shuffled().take(3).toMutableList()
            if (!options.contains(currentLetter)) {
                options[0] = currentLetter
            }
            options.shuffle()
            
            options.forEach { option ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(80.dp)
                        .clickable {
                            if (option == currentLetter) {
                                viewModel.speak("Amazing! You got it!")
                                viewModel.addStars(10)
                                viewModel.markLetterLearned()
                                viewModel.recordAnswer(true)
                                if (currentIndex < letters.size - 1) {
                                    currentIndex++
                                    showQuiz = false
                                } else {
                                    onNavigateBack()
                                }
                            } else {
                                viewModel.speak("Almost! Let's try again!")
                                viewModel.recordAnswer(false)
                            }
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = option.second, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF37474F))
                    }
                }
            }
        }
    }
}
