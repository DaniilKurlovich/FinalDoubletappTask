package com.example.finaltaskandroid.data.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finaltaskandroid.data.models.Habit


@Entity(tableName = "practices")
data class PracticeEntity(
    @Embedded var practice: Habit
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
