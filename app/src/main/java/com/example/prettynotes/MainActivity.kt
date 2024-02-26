package com.example.prettynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prettynotes.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
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
                    val adapter = NoteAdapter(noteList)
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
}