package com.example.githubclientkmm.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubclientkmm.Greeting
import android.widget.TextView
import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.network.APIService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            AppRepository().signIn("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")
        }


        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()
    }
}
