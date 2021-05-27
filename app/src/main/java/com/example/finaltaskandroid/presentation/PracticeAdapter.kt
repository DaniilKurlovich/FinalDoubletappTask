package com.example.finaltaskandroid.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finaltaskandroid.R
import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.presentation.AddEditPracticeFragment.Companion.PRACTICE_LEVELS
import com.example.finaltaskandroid.presentation.AddEditPracticeFragment.Companion.TYPE_PRACTICE


interface PracticeClickListener {
    fun onPracticeListener(data: Habit)
}

interface PracticeDoneClick {
    fun habitDone(habit: Habit)
}

class ViewHolder(itemView: View, val clickDone: PracticeDoneClick) : RecyclerView.ViewHolder(itemView) {

    private val namePractice: TextView = itemView.findViewById(R.id.name)
    private val descriptionPractice: TextView = itemView.findViewById(R.id.description)
    private val countRepeat: TextView = itemView.findViewById(R.id.count_repeat)
    private val type: TextView = itemView.findViewById(R.id.type_practice)
    private val level: TextView = itemView.findViewById(R.id.level_practice)
    private val btnDone: Button = itemView.findViewById(R.id.make_done)

    fun bind(user: Habit) {
        namePractice.text = user.title
        descriptionPractice.text = user.description
        countRepeat.text = itemView.context.getString(R.string.placeholder_cr, user.count, user.frequency)
        type.text = itemView.context.getString(R.string.type_text, TYPE_PRACTICE[user.type])
        level.text = itemView.context.getString(R.string.level_text, PRACTICE_LEVELS[user.priority])
        btnDone.setOnClickListener { clickDone.habitDone(user) }
    }

}

class PracticeAdapter(
    var practices: List<Habit> = listOf(),
    private val practiceClickListener: PracticeClickListener,
    private val practiceDone: PracticeDoneClick
): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.cards_practice, parent, false),
                          practiceDone)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            practiceClickListener.onPracticeListener(practices[position])
        }
        holder.bind(practices[position])
    }

    override fun getItemCount(): Int {
        return practices.size
    }

}
