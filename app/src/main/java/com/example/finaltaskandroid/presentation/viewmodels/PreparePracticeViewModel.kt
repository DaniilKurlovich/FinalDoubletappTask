package com.example.finaltaskandroid.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finaltaskandroid.data.db.PracticeEntity
import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.data.repositories.HabitRepository
import com.example.finaltaskandroid.presentation.AddEditPracticeFragment.Companion.PRACTICE_LEVELS
import com.example.finaltaskandroid.presentation.AddEditPracticeFragment.Companion.TYPE_PRACTICE
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class PreparePracticeViewModel(val repo: HabitRepository) : ViewModel(), CoroutineScope {

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    val practiceReady: MutableLiveData<Boolean> = MutableLiveData()

    private var statusReady: Boolean = false
    private var uniqId: String? = null
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var type: String
    private lateinit var level: String

    private var count: Int = -1
    private var period: Int = -1

    fun setName(name: String) {
        this.name = name
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setType(type: String) {
        this.type = type
    }

    fun setLevel(level: String) {
        this.level = level
    }

    fun setCount(count: Int?) {
        if (count != null) {
            this.count = count
        }
    }

    fun setPeriod(period: Int?) {
        if (period != null) {
            this.period = period
        }
    }

    fun setUniqId(uniqId: String) {
        this.uniqId = uniqId
    }

    fun applyPractice() {
        this.statusReady =
            (this.period != -1) and (this.count != -1) and (this.name != "") and (this.description != "") and (this.type != "") and (this.level != "")
        if (this.statusReady) {

            val practice = Habit(
                this.name,
                this.description,
                PRACTICE_LEVELS.indexOf(this.level),
                TYPE_PRACTICE.indexOf(this.type),
                this.count,
                this.period,
                uniqId
            )

            launch {
                if (practice.uid == null) {
                    repo.insert(practice)
                } else {
                    repo.update(practice)
                }
            }

            practiceReady.postValue(true)
        }
    }

}