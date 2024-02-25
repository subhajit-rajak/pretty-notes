package com.example.prettynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prettynotes.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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