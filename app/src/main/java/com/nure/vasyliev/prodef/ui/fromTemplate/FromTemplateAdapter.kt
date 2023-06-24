package com.nure.vasyliev.prodef.ui.fromTemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.RvItemTemplateBinding
import com.nure.vasyliev.prodef.model.template.Template

class FromTemplateAdapter(
    private val onItemClickListener: (Template) -> Unit,
    private val onItemEditClickListener: (Template) -> Unit
) : ListAdapter<Template, FromTemplateAdapter.ViewHolder>(TemplateDiffUtils()) {

    fun removeItem(position: Int) {
        val list = currentList.toMutableList()
        list.removeAt(position)
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            RvItemTemplateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickListener,
            onItemEditClickListener,
            parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val template = currentList[position]
        holder.bind(template)
    }

    inner class ViewHolder(
        private val binding: RvItemTemplateBinding,
        private val onItemClickListener: (Template) -> Unit,
        private val onItemEditClickListener: (Template) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(template: Template) {
            binding.tvTemplateName.text = template.taskName
            binding.tvTemplateDuration.text = context.getString(R.string.time_minutes, template.durationMins.toString())

            binding.ivEditTemplate.setOnClickListener {
                onItemEditClickListener.invoke(template)
            }
            binding.root.setOnClickListener {
                onItemClickListener.invoke(template)
            }
        }
    }
}

class TemplateDiffUtils : DiffUtil.ItemCallback<Template>() {

    override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean {
        return oldItem == newItem
    }
}