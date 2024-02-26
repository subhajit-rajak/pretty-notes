package com.example.prettynotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prettynotes.databinding.NotesItemBinding

class NoteAdapter(private val notes:List<NoteItem>):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note=notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(private val binding: NotesItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItem) {
            binding.titleCard.text=note.title
            binding.descriptionCard.text=note.description
        }

    }
}