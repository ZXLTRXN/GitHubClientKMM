package com.example.githubclientkmm.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.githubclientkmm.Greeting
import android.widget.TextView
import com.example.githubclientkmm.DI
import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.KeyValueStorage
import com.example.githubclientkmm.data.network.APIService
import com.example.githubclientkmm.data.network.ktorHttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun greet(): String {
    return Greeting().greeting()
}

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
                Log.e("AAA", "onCreate: $e")
            }
        }

        tv.text = greet()
    }
}
