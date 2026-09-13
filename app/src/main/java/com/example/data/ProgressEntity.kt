package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey val id: Int = 1,
    var stars: Int = 0,
    var lettersLearned: Int = 0,
    var numbersLearned: Int = 0,
    var gamesCompleted: Int = 0,
    var correctAnswers: Int = 0,
    var totalQuestions: Int = 0,
    var timeSpentMinutes: Int = 0,
    var selectedCharacterId: String = "bobo"
)
