package com.example.prettynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prettynotes.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNote : AppCompatActivity() {
    private val binding: ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference=FirebaseDatabase.getInstance().reference
        auth= FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.savebtn.setOnClickListener {
            val title = binding.noteTitle.text.toString()
            val description = binding.noteDescription.text.toString()

            if(title.isEmpty() && description.isEmpty()) {
                Toast.makeText(this, "Write something...", Toast.LENGTH_SHORT).show()
            } else {
                val currentUser =auth.currentUser
                currentUser?.let { user->
                    // generate a unique key for the note
                    val noteKey = databaseReference.child("users")
                        .child(user.uid)
                        .child("notes")
                        .push()
                        .key
                    val noteItem = NoteItem(title, description ,noteKey?:"")

                    if(noteKey!=null) {
                        databaseReference.child("users")
                            .child(user.uid)
                            .child("notes")
                            .child(noteKey)
                            .setValue(noteItem)
                            .addOnCompleteListener { task->
                                if(task.isSuccessful) {
                                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }

}