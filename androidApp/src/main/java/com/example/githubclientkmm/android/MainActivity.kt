package com.example.githubclientkmm.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.githubclientkmm.DI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv: TextView = findViewById(R.id.text_view)
        GlobalScope.launch {
            try {
                DI.appRepo.signIn("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")
                Log.d("AAA", "onCreate: aaaaa")

            } catch (e: RuntimeException) {
                Log.e("AAA", "onCreate: ${e}")
                Log.e("AAA", "onCreate: ${e.cause?.message}")
            }
        }

        tv.text = "aaaaaaaaaaaaaa"
    }
}
