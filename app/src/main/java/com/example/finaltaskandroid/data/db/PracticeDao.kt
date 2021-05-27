package com.example.finaltaskandroid.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface PracticeDao {
    @Query("select * from practices")
    fun getAll(): Flow<List<PracticeEntity>>

    @Query("select * from practices where title like :find_name || '%'")
    fun getPracticeByName(find_name: String): Flow<List<PracticeEntity>?>

    @Query("select * from practices where uid = :other_id")
    fun getPracticeById(other_id: String): Flow<PracticeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(practice: PracticeEntity)

    @Query("UPDATE practices SET title = :title, description = :description, priority = :priority, type = :type, count = :count, frequency = :frequency WHERE uid = :uid")
    fun update(title: String, description: String, priority: Int, type: Int, count: Int, frequency: Int,
               uid: String)

    @Query("DELETE FROM practices")
    fun nukeTable()
}