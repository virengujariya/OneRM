package me.fitbod.repetition.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.fitbod.repetition.databinding.ItemRecordBinding

class RecordsAdapter(private val itemClickListener: (exerciseName: String, oneRm: Int) -> Unit) :
    ListAdapter<RecordViewData, RecordsAdapter.ItemHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }

    class ItemHolder(private val binding: ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecordViewData, itemClickListener: (exerciseName: String, oneRm: Int) -> Unit) {
            binding.textViewExercise.text = item.exerciseName
            binding.textViewWeight.text = "${item.oneRm}"
            binding.root.setOnClickListener { itemClickListener.invoke(item.exerciseName, item.oneRm) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RecordViewData>() {
        override fun areItemsTheSame(oldItem: RecordViewData, newItem: RecordViewData): Boolean {
            return oldItem.exerciseName == newItem.exerciseName
        }

        override fun areContentsTheSame(oldItem: RecordViewData, newItem: RecordViewData): Boolean {
            return oldItem == newItem
        }
    }
}