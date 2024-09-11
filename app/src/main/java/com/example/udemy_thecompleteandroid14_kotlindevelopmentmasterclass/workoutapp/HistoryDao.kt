package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.workoutapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)
    
    @Query("SELECT * FROM `workouthistory`")
    fun fetchAllDates():Flow<List<HistoryEntity>>
    
}