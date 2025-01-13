package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import com.example.mentoriasapp.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
        }

        binding.textView4.setOnClickListener {
            startActivity(Intent(this@IntroActivity, RegisterActivity::class.java))
        }
    }
}