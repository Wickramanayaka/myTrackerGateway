package com.proactiveants.mytrackergateway

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    private val SPLASH_DELAY : Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val qrCode = intent.getStringExtra("qrCode")
        val textViewQR: TextView = findViewById(R.id.textViewQRCode)
        textViewQR.text = qrCode

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }
}