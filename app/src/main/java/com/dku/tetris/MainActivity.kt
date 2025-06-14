package com.dku.tetris

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.dku.tetris.utils.DB


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnPlayNew: Button
    private lateinit var btnExitApp: Button

    companion object {
        init {
            System.loadLibrary("tetris")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DB.createDatabaseIfNotExists(applicationContext)
        setContentView(R.layout.activity_main)


        btnPlayNew.setOnClickListener(this)
        btnExitApp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}

