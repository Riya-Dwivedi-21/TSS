package com.touristsafety.app.data.repository

import com.touristsafety.app.data.model.Tourist
import kotlinx.coroutines.flow.Flow

interface TouristRepository {
    suspend fun registerTourist(tourist: Tourist): Result<String>
    suspend fun getTouristById(id: String): Tourist?
    suspend fun updateTourist(tourist: Tourist): Result<Boolean>
    suspend fun getAllTourists(): Flow<List<Tourist>>
    suspend fun deleteTourist(id: String): Result<Boolean>
}

class TouristRepositoryImpl : TouristRepository {
    
    // Using in-memory storage for now
    private val tourists = mutableMapOf<String, Tourist>()
    
    override suspend fun registerTourist(tourist: Tourist): Result<String> {
        return try {
            val id = "TOURIST_${System.currentTimeMillis()}"
            val newTourist = tourist.copy(id = id)
            tourists[id] = newTourist
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTouristById(id: String): Tourist? {
        return tourists[id]
    }
    
    override suspend fun updateTourist(tourist: Tourist): Result<Boolean> {
        return try {
            tourists[tourist.id] = tourist
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllTourists(): Flow<List<Tourist>> {
        return kotlinx.coroutines.flow.flow {
            emit(tourists.values.toList())
        }
    }
    
    override suspend fun deleteTourist(id: String): Result<Boolean> {
        return try {
            tourists.remove(id)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
