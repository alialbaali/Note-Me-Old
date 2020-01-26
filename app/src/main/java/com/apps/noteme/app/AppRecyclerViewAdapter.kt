package com.apps.noteme.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apps.noteme.database.Note
import com.apps.noteme.databinding.NoteItemBinding

class AppRecyclerViewAdapter(val clickListener: NoteListener) :
    ListAdapter<Note, AppRecyclerViewAdapter.AppViewHolder>(AppDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder.from(parent) // only refactoring has been done here
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener) // only refactoring has been done here
    }

    // binding variable and connect to note var in note_item.xml and use binding.note = note(the one already provided). Whenever text changes it updates automatically
    // private constructor so that it's can't be called from outside of the class
    class AppViewHolder private constructor(val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            val title = binding.titleEditText
            val content = binding.contentEditText
            if (title.text.isNullOrEmpty() && (!content.text.isNullOrEmpty())) {
                title.visibility = View.GONE
                content.visibility = View.VISIBLE
            } else if ((!title.text.isNullOrEmpty()) && content.text.isNullOrEmpty()) {
                title.visibility = View.VISIBLE
                content.visibility = View.GONE
            }
        }

        fun bind(note: Note, clickListener: NoteListener) {
            binding.note = note
            binding.clickListener = clickListener

        }


        // companion object similar to static variable in java
        companion object {
            fun from(parent: ViewGroup): AppViewHolder {
                val binding =
                    NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return AppViewHolder(binding)
            }
        }
    }

    // the diffUtil for updating the list with "submitList" function. it's more useful than "notifyDataSetChanged"
    // class for Difference Utility
    class AppDiffUtilCallback : DiffUtil.ItemCallback<Note>() {
        // it checks if the ids are the same
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        // it checks if the objects are the same
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}


// clickListener class and a onClick Method
class NoteListener(val clickListeneer: (id: Long) -> Unit) {
    fun onClick(note: Note) = clickListeneer(note.id)
}