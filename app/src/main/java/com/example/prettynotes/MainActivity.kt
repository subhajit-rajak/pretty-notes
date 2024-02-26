package com.example.prettynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prettynotes.databinding.ActivityMainBinding
import com.example.prettynotes.databinding.UpdateNoteBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.noteRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUser = auth.currentUser
        currentUser?.let { user->
            val noteReference = databaseReference.child("users")
                .child(user.uid)
                .child("notes")
            noteReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<NoteItem>()
                    for (noteSnapShot in snapshot.children) {
                        val note = noteSnapShot.getValue(NoteItem::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                    }
                    noteList.reverse()
                    val adapter = NoteAdapter(noteList, this@MainActivity)
                    recyclerView.adapter=adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        binding.logout.setOnClickListener {
            // for sign-out
            // option 1
            FirebaseAuth.getInstance().signOut()
            // option 2
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            val go= GoogleSignIn.getClient(this, gso)
            go.signOut()
            // both options were needed to sign-out properly
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.addbtn.setOnClickListener {
            startActivity(Intent(this, AddNote::class.java))
        }
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user->
            val noteReference = databaseReference.child("users")
                .child(user.uid)
                .child("notes")
                .child(noteId)
                .removeValue()
        }
    }

    override fun onUpdateClick(noteId: String, currentTitle: String, currentDescription: String) {
        val updateBinding = UpdateNoteBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(updateBinding.root)
            .setTitle("Update your note")
            .setPositiveButton("Done") { dialog,_->
                val newTitle = updateBinding.updateTitle.text.toString()
                val newDescription = updateBinding.updateDescription.text.toString()
                updateNoteDatabase(noteId, newTitle, newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") {dialog,_->
                dialog.dismiss()
            }
            .create()
        updateBinding.updateTitle.setText(currentTitle)
        updateBinding.updateDescription.setText(currentDescription)
        dialog.show()
    }

    private fun updateNoteDatabase(noteId: String, newTitle: String, newDescription: String) {
        val currentUser=auth.currentUser
        currentUser?.let {user ->
            val noteReference = databaseReference.child("users")
                .child(user.uid)
                .child("notes")
            val updateNote = NoteItem(newTitle, newDescription, noteId)
            noteReference.child(noteId)
                .setValue(updateNote)
                .addOnCompleteListener { task->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}