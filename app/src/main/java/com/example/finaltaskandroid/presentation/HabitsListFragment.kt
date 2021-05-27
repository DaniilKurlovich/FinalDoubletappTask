package com.example.finaltaskandroid.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finaltaskandroid.R
import com.example.finaltaskandroid.data.di.FinalTaskApplication
import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.presentation.AddEditPracticeFragment.Companion.TYPE_PRACTICE
import com.example.finaltaskandroid.presentation.viewmodels.PracticesListViewModel
import com.example.finaltaskandroid.presentation.viewmodels.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HabitsListFragment: Fragment(), PracticeClickListener, PracticeDoneClick {
    private lateinit var recyclerView: RecyclerView
    private lateinit var practicesListViewModel: PracticesListViewModel
    private lateinit var listAdapter: PracticeAdapter
    private lateinit var typePractice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = (requireActivity().application as FinalTaskApplication).dependencyFactory.provideHabitRepo(requireContext())
        val notificationUseCase = (requireActivity().application as FinalTaskApplication).dependencyFactory.provideToastNotificationUseCase(requireContext())
        practicesListViewModel = ViewModelProvider(this, ViewModelFactory(repo, notificationUseCase)).get(PracticesListViewModel::class.java)
        typePractice = arguments?.getString(keyTypePractice)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.practices_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.recycle_view_list)
        listAdapter = PracticeAdapter(practiceClickListener = this, practiceDone = this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = listAdapter

        val showFilters: FloatingActionButton = view.findViewById(R.id.leftButton)
        showFilters.setOnClickListener {
            showFiltersFragment(view)
        }

        val addButton: FloatingActionButton = view.findViewById(R.id.addPracticeButton)
        addButton.setOnClickListener {
            onClick(view)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        practicesListViewModel.toastNotification.observe(requireActivity(), {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        practicesListViewModel.practiceList.observe(requireActivity(), {
            val newPracticesList = it.filter { p -> TYPE_PRACTICE.get(p.type) == typePractice }
            listAdapter.practices = newPracticesList
            listAdapter.notifyDataSetChanged()
        })
    }

    override fun onPracticeListener(data: Habit) {
        val dialog = AddEditPracticeFragment.newInstance(data)
        dialog.show(childFragmentManager, "dialog")
    }

    fun onClick(v: View?) {
        val dialog = AddEditPracticeFragment()
        dialog.show(childFragmentManager, "dialog")
    }

    fun showFiltersFragment(v: View?) {
        val dialog = BottomSheetFragment.newInstance()
        dialog.show(childFragmentManager, BottomSheetFragment.TAG)
    }

    companion object {
        const val keyTypePractice: String = "type_practice"

        @JvmStatic
        fun newInstance(typePractice: String) =
            HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putString(keyTypePractice, typePractice)
                }
            }
    }

    override fun habitDone(habit: Habit) {
        practicesListViewModel.habitDone(habit)
    }
}