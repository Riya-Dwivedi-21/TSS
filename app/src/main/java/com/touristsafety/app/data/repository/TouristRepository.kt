// Temporarily disabled for compatibility
/*
package com.touristsafety.app.data.repository

import android.content.Context
import com.touristsafety.app.data.database.TouristDatabase
import com.touristsafety.app.data.mapper.TouristMapper
import com.touristsafety.app.data.model.Tourist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TouristRepository {
    suspend fun registerTourist(tourist: Tourist): Result<String>
    suspend fun getTouristById(id: String): Tourist?
    suspend fun updateTourist(tourist: Tourist): Result<Boolean>
    suspend fun getAllTourists(): Flow<List<Tourist>>
    suspend fun getActiveTourists(): Flow<List<Tourist>>
    suspend fun deleteTourist(id: String): Result<Boolean>
    suspend fun getTouristByPhoneNumber(phoneNumber: String): Tourist?
    suspend fun getTouristByVerificationNumber(verificationNumber: String): Tourist?
    suspend fun updateTouristStatus(id: String, isActive: Boolean): Result<Boolean>
    suspend fun updateSafetyScore(id: String, safetyScore: Int): Result<Boolean>
    suspend fun getTouristCount(): Int
    suspend fun getActiveTouristCount(): Int
}

class TouristRepositoryImpl(private val context: Context) : TouristRepository {
    
    private val database = TouristDatabase.getDatabase(context)
    private val touristDao = database.touristDao()
    
    override suspend fun registerTourist(tourist: Tourist): Result<String> {
        return try {
            val id = "TOURIST_${System.currentTimeMillis()}"
            val newTourist = tourist.copy(id = id)
            val entity = TouristMapper.toEntity(newTourist)
            val result = touristDao.insertTourist(entity)
            if (result > 0) {
                Result.success(id)
            } else {
                Result.failure(Exception("Failed to insert tourist"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTouristById(id: String): Tourist? {
        return try {
            val entity = touristDao.getTouristById(id)
            entity?.let { TouristMapper.toModel(it) }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun updateTourist(tourist: Tourist): Result<Boolean> {
        return try {
            val entity = TouristMapper.toEntity(tourist)
            val result = touristDao.updateTourist(entity)
            Result.success(result > 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllTourists(): Flow<List<Tourist>> {
        return touristDao.getAllTourists().map { entities ->
            TouristMapper.toModelList(entities)
        }
    }
    
    override suspend fun getActiveTourists(): Flow<List<Tourist>> {
        return touristDao.getActiveTourists().map { entities ->
            TouristMapper.toModelList(entities)
        }
    }
    
    override suspend fun deleteTourist(id: String): Result<Boolean> {
        return try {
            val result = touristDao.deleteTouristById(id)
            Result.success(result > 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTouristByPhoneNumber(phoneNumber: String): Tourist? {
        return try {
            val entity = touristDao.getTouristByPhoneNumber(phoneNumber)
            entity?.let { TouristMapper.toModel(it) }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getTouristByVerificationNumber(verificationNumber: String): Tourist? {
        return try {
            val entity = touristDao.getTouristByVerificationNumber(verificationNumber)
            entity?.let { TouristMapper.toModel(it) }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun updateTouristStatus(id: String, isActive: Boolean): Result<Boolean> {
        return try {
            val result = touristDao.updateTouristStatus(id, isActive)
            Result.success(result > 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateSafetyScore(id: String, safetyScore: Int): Result<Boolean> {
        return try {
            val result = touristDao.updateSafetyScore(id, safetyScore, java.util.Date())
            Result.success(result > 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTouristCount(): Int {
        return try {
            touristDao.getTouristCount()
        } catch (e: Exception) {
            0
        }
    }
    
    override suspend fun getActiveTouristCount(): Int {
        return try {
            touristDao.getActiveTouristCount()
        } catch (e: Exception) {
            0
        }
    }
}
*/
