package com.example.prettynotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.prettynotes.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    // need to comment this out if signing out doesn't work and still stays on MainActivity
    override fun onStart() {
        super.onStart()

        //check if user is already logged in
        val currentUser=auth.currentUser
        if(currentUser!=null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this, gso)

        //initialize firebase auth
        auth=FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.login.setOnClickListener {
            val email=binding.emailLogin.text.toString()
            val password=binding.password.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all credentials", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task->
                        if(task.isSuccessful) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                                Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.google.setOnClickListener() {
            val signInClient=googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result->
        if(result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful) {
                val account:GoogleSignInAccount?=task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}