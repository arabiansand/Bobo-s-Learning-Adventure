package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BackendApiService
import com.example.data.ProgressEntity
import com.example.data.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Locale

class MainViewModel(
    private val application: Application,
    private val repository: ProgressRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<ProgressEntity> = repository.progress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProgressEntity()
        )
        
    private val _isAuthenticating = MutableStateFlow(false)
    val isAuthenticating: StateFlow<Boolean> = _isAuthenticating.asStateFlow()
    
    fun setAuthenticating(isAuthenticating: Boolean) {
        _isAuthenticating.value = isAuthenticating
    }

    private var tts: TextToSpeech? = null
    
    // MOCK BACKEND RETROFIT
    private val backendApi: BackendApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.minepi.com/") 
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(BackendApiService::class.java)
    }
    
    init {
        viewModelScope.launch {
            repository.initDefaultIfNeeded()
        }
        
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    fun handlePiAuthSuccess(accessToken: String, username: String, onAuthVerified: () -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("PiAuth", "Sending token to backend for validation: $accessToken")
                // Simulating Backend Validation locally since we don't have a Node backend:
                val response = backendApi.validatePiToken("Bearer $accessToken")
                
                if (response.username != null) {
                    Toast.makeText(application, "Authenticated as ${response.username} via Pi!", Toast.LENGTH_LONG).show()
                    speak("Welcome, ${response.username}!")
                    onAuthVerified()
                } else {
                    Toast.makeText(application, "Validation failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("PiAuth", "Failed to validate token on backend", e)
                Toast.makeText(application, "Failed to authenticate on backend", Toast.LENGTH_SHORT).show()
            } finally {
                setAuthenticating(false)
            }
        }
    }

    fun handlePiAuthError(error: String) {
        setAuthenticating(false)
        Log.e("PiAuth", "Auth error: $error")
        Toast.makeText(application, "Pi Auth Error: $error", Toast.LENGTH_SHORT).show()
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun selectCharacter(id: String) {
        val current = uiState.value.copy(selectedCharacterId = id)
        viewModelScope.launch {
            repository.updateProgress(current)
        }
        speak("Yay! Let's play together!")
    }

    fun addStars(amount: Int) {
        val current = uiState.value
        val newProgress = current.copy(
            stars = current.stars + amount,
            gamesCompleted = current.gamesCompleted + 1
        )
        viewModelScope.launch {
            repository.updateProgress(newProgress)
        }
    }

    fun recordAnswer(correct: Boolean) {
        val current = uiState.value
        val newProgress = current.copy(
            correctAnswers = current.correctAnswers + if (correct) 1 else 0,
            totalQuestions = current.totalQuestions + 1
        )
        viewModelScope.launch {
            repository.updateProgress(newProgress)
        }
    }
    
    fun markLetterLearned() {
        val current = uiState.value
        val newProgress = current.copy(lettersLearned = current.lettersLearned + 1)
        viewModelScope.launch { repository.updateProgress(newProgress) }
    }
    
    fun markNumberLearned() {
        val current = uiState.value
        val newProgress = current.copy(numbersLearned = current.numbersLearned + 1)
        viewModelScope.launch { repository.updateProgress(newProgress) }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
