package com.example.prettynotes

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.renderscript.ScriptGroup.Binding
import com.example.prettynotes.databinding.ActivityWelcomeScreenBinding

class WelcomeScreen : AppCompatActivity() {
    private val binding: ActivityWelcomeScreenBinding by lazy {
        ActivityWelcomeScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Login::class.java))
            finish()
        }, 3000)

        val logoText = binding.logoText;
        val paint =logoText.paint;
        val width = paint.measureText(logoText.text.toString())

        logoText.paint.shader = LinearGradient (
            0f, 0f, width, logoText.textSize, intArrayOf(
                Color.parseColor("#8a3ab9"),
                Color.parseColor("#bc2a8d"),
                Color.parseColor("#fccc63"),
                Color.parseColor("#fbad50"),
            ), null, Shader.TileMode.CLAMP
        )
    }
}