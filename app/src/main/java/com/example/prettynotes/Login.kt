package com.example.prettynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prettynotes.databinding.ActivityLoginBinding
import com.example.prettynotes.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
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
    }
}