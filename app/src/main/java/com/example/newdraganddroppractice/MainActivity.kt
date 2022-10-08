package com.example.newdraganddroppractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var imageQuestion:Button
    private lateinit var degreeQuestion:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageQuestion = findViewById(R.id.imageQuestion)
        degreeQuestion = findViewById(R.id.degreeQuestion)

        imageQuestion.setOnClickListener(this)
        degreeQuestion.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.imageQuestion -> {
                val intent = Intent(this, ImageQuestionsActivity::class.java)
                startActivity(intent)
            }

            R.id.degreeQuestion -> {
                val intent = Intent(this, DegreeQuestionActivity::class.java)
                startActivity(intent)
            }
        }
    }
}