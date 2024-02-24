package com.example.prettynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prettynotes.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize firebase auth
        auth=FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.register.setOnClickListener {

            // get credentials from user inputs
            val email=binding.email.text.toString()
            val username=binding.usernameSignUp.text.toString()
            val password=binding.passwordSignUp.text.toString()
            val confirmPassword=binding.confirmPassword.text.toString()

            //check if any credentials is given by user
            if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Fill all credentials", Toast.LENGTH_SHORT).show()
            } else if(password!=confirmPassword) {
                Toast.makeText(this, "Confirmed password is not same", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {task->
                        if(task.isSuccessful) {
                            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Login::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}