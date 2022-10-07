package com.example.newdraganddroppractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout

class PracticeActivity : AppCompatActivity() {
    private lateinit var linearLayout:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        linearLayout = findViewById(R.id.linearLayout)

        Log.v("PracticeActivity", "Count - ${linearLayout.childCount}")
    }
}