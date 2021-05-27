package com.example.finaltaskandroid.data.network

import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.data.models.HabitDone
import com.example.finaltaskandroid.data.models.HabitUID
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.*

interface HabitService {
    @GET("habit")
    suspend fun getPractices(): List<Habit>

    @PUT("habit")
    fun addHabit(@Body habit: Habit): Call<HabitUID>

    @DELETE("habit")
    suspend fun deleteHabit(@Body habit: HabitUID): Flow<Call<String>>

    @POST("habit")
    suspend fun postDoneHabit(@Body habit: HabitDone): Flow<Call<String>>
}