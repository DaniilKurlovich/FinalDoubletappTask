package com.example.finaltaskandroid.presentation.viewmodels

import androidx.lifecycle.*
import com.example.finaltaskandroid.data.db.PracticeEntity
import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.data.repositories.HabitRepository
import com.example.finaltaskandroid.domain.ToastNotificationUseCase
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class ViewModelFactory(private val repo: HabitRepository?, private val notificationUseCase: ToastNotificationUseCase?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PracticesListViewModel::class.java)) {
            val key = "PracticesListViewModel"
            if (hashMapViewModel.containsKey(key)) {
                return getViewModel(key) as T
            } else {
                addViewModel(key, PracticesListViewModel(repo!!, notificationUseCase!!))
                return getViewModel(key) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        val hashMapViewModel = HashMap<String, ViewModel>()
        fun addViewModel(key: String, viewModel: ViewModel) {
            hashMapViewModel.put(key, viewModel)
        }

        fun getViewModel(key: String): ViewModel? {
            return hashMapViewModel[key]
        }
    }
}

class PracticesListViewModel(val repo: HabitRepository, private val notificationUseCase: ToastNotificationUseCase) : ViewModel(), CoroutineScope {
    companion object {
        const val NO_FILTER = "No filter"
        const val ALPHABET_FILTER = "Alphabetical name"

        val filters = mutableListOf(NO_FILTER, ALPHABET_FILTER)
    }

    private val job: Job = SupervisorJob()
    private var filter: String = filters[0]

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    val practiceList: MutableLiveData<List<Habit>> = MutableLiveData()
    val toastNotification: MutableLiveData<String> = MutableLiveData();

    init {
        repo.practicesStorage.observeForever {
            updateHabits(it)
        }
    }

    fun updateHabits(update: List<Habit>) {
        when (filter) {
            NO_FILTER -> practiceList.postValue(update)
            ALPHABET_FILTER -> getSortedNameListByName(update)
        }
    }

    fun setFilter(name: String) {
        when (name) {
            NO_FILTER -> filter = NO_FILTER
            ALPHABET_FILTER -> filter = ALPHABET_FILTER
            else -> throw Exception("Filter with name $name does not implemented.")
        }
        repo.notifyList()
    }

    fun habitDone(habit: Habit) {
        launch {
            toastNotification.postValue(notificationUseCase.makeDoneHabit(habit))
        }
    }

    fun getSortedNameListByName(listPractices: List<Habit>) {
        val test = listPractices.sortedBy { habit -> habit.title }
        practiceList.postValue(test)
    }


    fun findPracticeByName(name: String) {
        if (name == "") {
            repo.notifyList()
            return
        }

        repo.getPracticeByName(name)
    }

}
