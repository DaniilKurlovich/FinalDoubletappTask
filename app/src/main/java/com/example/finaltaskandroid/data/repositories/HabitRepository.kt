package com.example.finaltaskandroid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.finaltaskandroid.data.db.PracticeDao
import com.example.finaltaskandroid.data.db.PracticeEntity
import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.data.models.HabitUID
import com.example.finaltaskandroid.data.network.HabitService
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import kotlin.coroutines.CoroutineContext


class HabitRepository(val room: PracticeDao, val api: HabitService) : CoroutineScope {

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }


    val practicesStorage: MutableLiveData<List<Habit>> = MutableLiveData()

    init {
        GlobalScope.launch(Dispatchers.Main) {
            room.getAll().asLiveData().observeOnce {
                it.map { h ->
                    api.addHabit(h.practice)
                }
            }
            practicesStorage.postValue(api.getPractices())
        }
    }

    suspend fun insert(habit: Habit) {
        habit.uid = null
        val habitUID: HabitUID = api.addHabit(habit).awaitResponse().body()!!
        habit.uid = habitUID.uid
        room.insert(PracticeEntity(habit))
        GlobalScope.launch(Dispatchers.Main) {
            room.getAll().asLiveData().observeForever {
                practicesStorage.postValue(castToArrayList(it))
            }
        }
    }

    fun castToArrayList(listPractices: List<PracticeEntity>?): List<Habit> {
        val practices = mutableListOf<Habit>()
        listPractices?.map { practiceEntity -> practices.add(practiceEntity.practice) }
        return practices
    }

    fun notifyList() {
        GlobalScope.launch(Dispatchers.Main) {
            room.getAll().asLiveData().observeOnce {
                practicesStorage.postValue(castToArrayList(it))
            }
        }
    }

    suspend fun update(habit: Habit) {
        room.update(
            habit.title,
            habit.description,
            habit.priority,
            habit.type,
            habit.count,
            habit.frequency,
            habit.uid!!
        )
        api.addHabit(habit).awaitResponse()
        GlobalScope.launch(Dispatchers.Main) {
            room.getAll().asLiveData().observeForever {
                val new = mutableListOf<Habit>()
                it.map { practiceEntity -> new.add(practiceEntity.practice) }
                practicesStorage.postValue(new)
            }
        }
    }

    fun getPracticeByName(name: String) {
        GlobalScope.launch(Dispatchers.Main) {
            room.getPracticeByName(name).asLiveData().observeOnce {
                practicesStorage.postValue(castToArrayList(it))
            }
        }
    }

    fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {

            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }

        })
    }
}