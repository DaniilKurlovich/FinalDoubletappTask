package com.example.finaltaskandroid.domain

import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.data.repositories.HabitRepository
import com.example.finaltaskandroid.presentation.AddEditPracticeFragment.Companion.TYPE_PRACTICE
import java.lang.Exception

class ToastNotificationUseCase(private val habitRepository: HabitRepository) {
    suspend fun makeDoneHabit(habit: Habit): String {
        val diff = habit.frequency - habit.count - 1
        habit.count += 1
        habitRepository.update(habit)
        if (diff < 0) {
            when (TYPE_PRACTICE[habit.type]) {
                "Good" -> return GOOD_MORE
                "Bad" -> return BAD_STOP
            }
        } else {
            when (TYPE_PRACTICE[habit.type]) {
                "Good" -> return GOOD_REPEAT.format(diff)
                "Bad" -> return BAD_REPEAT.format(diff)
            }
        }
        throw Exception("Invalid type practice!")
    }

    companion object {
        const val BAD_REPEAT = "Можете выполнить еще столько-то раз %d"
        const val BAD_STOP = "Хватит это делать"
        const val GOOD_REPEAT = "Стоит выполнить ещё %d раз"
        const val GOOD_MORE = "You'are breathtaking!"
    }
}