package com.nure.vasyliev.prodef.ui.sessions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.RvItemSessionBinding
import com.nure.vasyliev.prodef.model.pomodoro.Pomodoro
import com.nure.vasyliev.prodef.utils.ddMMyyyyHHmmFormatDate
import com.nure.vasyliev.prodef.utils.formatFromServer

class SessionRecyclerViewAdapter(
    private val list: MutableList<Pomodoro> = mutableListOf()
) : RecyclerView.Adapter<SessionRecyclerViewAdapter.ViewHolder>() {

    fun updateList(newList: List<Pomodoro>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            RvItemSessionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pomodoro = list[position]
        holder.bind(pomodoro)
    }

    inner class ViewHolder(
        private val binding: RvItemSessionBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pomodoro: Pomodoro) {
            binding.layoutPeriod.isVisible = !pomodoro.isValid
            binding.tvTaskName.text = pomodoro.task
            binding.tvDuration.text =
                context.getString(R.string.rv_item_duration, pomodoro.durationMins.toString())

            if (pomodoro.isValid) {
                binding.layoutTaskName.setBackgroundColor(
                    context.resources.getColor(
                        R.color.main_color,
                        context.theme
                    )
                )
            } else {
                val startDate = formatFromServer.parse(pomodoro.startTime) ?: ""
                val stopDate = formatFromServer.parse(pomodoro.stopTime) ?: ""

                val startTime = ddMMyyyyHHmmFormatDate.format(startDate)
                val stopTime = ddMMyyyyHHmmFormatDate.format(stopDate)

                binding.tvPeriod.text =
                    context.getString(R.string.rv_item_period, startTime, stopTime)

                if (pomodoro.finishedEarlier) {
                    binding.layoutTaskName.setBackgroundColor(
                        context.resources.getColor(
                            R.color.not_passed_session,
                            context.theme
                        )
                    )
                } else {
                    binding.layoutTaskName.setBackgroundColor(
                        context.resources.getColor(
                            R.color.passed_session,
                            context.theme
                        )
                    )
                }
            }
        }
    }
}