package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgressRepository(private val progressDao: ProgressDao) {
    val progress: Flow<ProgressEntity> = progressDao.getProgress().map { 
        it ?: ProgressEntity() 
    }

    suspend fun initDefaultIfNeeded() {
        // Just insert default if none exists, but Room replaces on conflict
        progressDao.insertProgress(ProgressEntity())
    }
    
    suspend fun updateProgress(progressEntity: ProgressEntity) {
        progressDao.insertProgress(progressEntity)
    }
}
