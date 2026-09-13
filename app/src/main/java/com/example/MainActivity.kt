package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.ProgressRepository
import com.example.ui.AlphabetRoute
import com.example.ui.CharacterSelectionRoute
import com.example.ui.MainMenuRoute
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.NumbersRoute
import com.example.ui.ParentDashboardRoute
import com.example.ui.PiAuthRoute
import com.example.ui.TracingRoute
import com.example.ui.screens.AlphabetScreen
import com.example.ui.screens.CharacterSelectionScreen
import com.example.ui.screens.MainMenuScreen
import com.example.ui.screens.NumbersScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.TracingScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "bobo-database"
        ).build()
        
        val repository = ProgressRepository(database.progressDao())
        
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(application, repository)
                )
                BoboApp(viewModel)
            }
        }
    }
}

@Composable
fun BoboApp(viewModel: MainViewModel) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CharacterSelectionRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<CharacterSelectionRoute> {
                CharacterSelectionScreen(
                    viewModel = viewModel,
                    onNavigateNext = {
                        navController.navigate(MainMenuRoute) {
                            popUpTo(CharacterSelectionRoute) { inclusive = true }
                        }
                    }
                )
            }
            
            composable<PiAuthRoute> {
                com.example.ui.screens.PiAuthScreen(
                    onAuthComplete = { token, username -> 
                        viewModel.handlePiAuthSuccess(token, username) {
                            navController.navigate(CharacterSelectionRoute) {
                                popUpTo(PiAuthRoute) { inclusive = true }
                            }
                        }
                    },
                    onAuthError = { error ->
                        viewModel.handlePiAuthError(error)
                    },
                    onNavigateBack = {
                        viewModel.setAuthenticating(false)
                        navController.popBackStack()
                    }
                )
            }
            
            composable<MainMenuRoute> {
                MainMenuScreen(
                    viewModel = viewModel,
                    onNavigateToAlphabet = { navController.navigate(AlphabetRoute) },
                    onNavigateToNumbers = { navController.navigate(NumbersRoute) },
                    onNavigateToTracing = { navController.navigate(TracingRoute) },
                    onNavigateToParent = { navController.navigate(ParentDashboardRoute) },
                    onNavigateToPiAuth = { navController.navigate(PiAuthRoute) }
                )
            }
            
            composable<AlphabetRoute> {
                AlphabetScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable<NumbersRoute> {
                NumbersScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable<TracingRoute> {
                TracingScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable<ParentDashboardRoute> {
                ParentDashboardScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
