package com.dku.tetris

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.dku.tetris.utils.DB


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var playButton: Button
    private lateinit var exitButton: Button

    companion object {
        init {
            System.loadLibrary("tetris")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DB.createDatabaseIfNotExists(applicationContext)
        setContentView(R.layout.activity_main)

        playButton = findViewById(R.id.playButton)
        exitButton = findViewById(R.id.exitButton)

        playButton.setOnClickListener(this)
        exitButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.playButton -> {
                startActivity(Intent(applicationContext, GameActivity::class.java))
            }
            R.id.exitButton -> {
                finish()
            }
        }
    }
}

