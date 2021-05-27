package com.example.finaltaskandroid.data.di

import android.content.Context
import androidx.room.Room
import com.example.finaltaskandroid.data.db.PracticeDao
import com.example.finaltaskandroid.data.db.PracticeDatabase
import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.data.models.HabitUID
import com.example.finaltaskandroid.data.network.*
import com.example.finaltaskandroid.data.repositories.HabitRepository
import com.example.finaltaskandroid.domain.ToastNotificationUseCase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DependencyFactory {

    @Volatile
    var retrofit: Retrofit? = null
    @Volatile
    var habitRepo: HabitRepository? = null

    @Provides
    @Singleton
    fun provideDatabase(context: Context): PracticeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PracticeDatabase::class.java,
            "practice_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHabitRepo(context: Context): HabitRepository {
        if (habitRepo == null) {
            synchronized(this) {
                if (habitRepo == null) {
                    habitRepo = HabitRepository(provideHabitDaoService(context), provideHabitService())
                }
            }
        }
        return habitRepo!!
    }

    fun provideToastNotificationUseCase(context: Context): ToastNotificationUseCase {
        return ToastNotificationUseCase(provideHabitRepo(context))
    }

    private fun provideHabitService(): HabitService {
        return provideRetrofit().create(HabitService::class.java)
    }

    private fun provideHabitDaoService(context: Context): PracticeDao {
        return provideDatabase(context).practiceDao()
    }

    private fun provideRetrofit(): Retrofit {
        if (retrofit == null) {
            synchronized(this) {
                if (retrofit == null) {
                    val client = OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor()).build()
                    val gson = GsonBuilder().registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
                        .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
                        .registerTypeAdapter(HabitUID::class.java, HabitUidDeserializer())
                        .registerTypeAdapter(HabitUID::class.java, HabitJsonSerializer())
                        .create()
                    retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                }
            }
        }
        return retrofit as Retrofit
    }

    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
    }
}