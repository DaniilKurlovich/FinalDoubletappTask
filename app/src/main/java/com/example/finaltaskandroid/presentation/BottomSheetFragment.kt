package com.example.finaltaskandroid.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.finaltaskandroid.R
import com.example.finaltaskandroid.data.di.FinalTaskApplication
import com.example.finaltaskandroid.presentation.viewmodels.PracticesListViewModel
import com.example.finaltaskandroid.presentation.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_edit.*


class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var spinner: Spinner
    private lateinit var practicesListViewModel: PracticesListViewModel
    private lateinit var findPracticeInput: EditText

    inner class SpinnerContext : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            practicesListViewModel.setFilter(PracticesListViewModel.filters[position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // SPINNER
        spinner = requireView().findViewById(R.id.priority_practice)
        val spinnerAdapter = view.let {
            ArrayAdapter(
                it.context,
                android.R.layout.simple_spinner_dropdown_item,
                PracticesListViewModel.filters
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
        priority_practice.adapter = spinnerAdapter
        priority_practice.onItemSelectedListener = SpinnerContext()

        val notificationUseCase = (requireActivity().application as FinalTaskApplication).dependencyFactory.provideToastNotificationUseCase(requireContext())
        val repo = (requireActivity().application as FinalTaskApplication).dependencyFactory.provideHabitRepo(requireContext())

        practicesListViewModel = ViewModelProvider(this, ViewModelFactory(repo, notificationUseCase)).get(PracticesListViewModel::class.java)
        practicesListViewModel.findPracticeByName("")   // hack! чтобы скидывать поиск

        findPracticeInput = requireView().findViewById(R.id.find_text_name)
        findPracticeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                practicesListViewModel.findPracticeByName(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): BottomSheetFragment {
            return BottomSheetFragment()
        }
    }
}