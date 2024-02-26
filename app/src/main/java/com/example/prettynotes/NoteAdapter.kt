package com.example.prettynotes

import android.accounts.AuthenticatorDescription
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.prettynotes.databinding.NotesItemBinding

class NoteAdapter(private val notes:List<NoteItem>, private val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteClick(noteId:String)
        fun onUpdateClick(noteId:String, title:String, description: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note=notes[position]
        holder.bind(note)
        holder.binding.titleCard.setOnClickListener {
            itemClickListener.onUpdateClick(note.noteId, note.title, note.description)
        }
        holder.binding.deletebtn.setOnClickListener {
            itemClickListener.onDeleteClick(note.noteId)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(val binding: NotesItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItem) {
            binding.titleCard.text=note.title
            binding.descriptionCard.text=note.description
        }

    }
}