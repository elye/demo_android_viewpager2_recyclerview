package com.elyeproj.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
    }

    fun mainActivity(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun secondActivity(view: View) {
        startActivity(Intent(this, SecondActivity::class.java))
    }
}
