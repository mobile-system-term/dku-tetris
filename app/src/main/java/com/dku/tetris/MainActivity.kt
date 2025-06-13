package com.dku.tetris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var hal: HalController
    private lateinit var dipSwitchValueText: TextView
    private lateinit var readDipSwitchBtn: Button
    private lateinit var buzzerOnBtn: Button
    private lateinit var buzzerOffBtn: Button
    private lateinit var writeLcdBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hal = HalController()

        dipSwitchValueText = findViewById(R.id.dipSwitchValue)
        readDipSwitchBtn = findViewById(R.id.readDipSwitchBtn)
        buzzerOnBtn = findViewById(R.id.buzzerOnBtn)
        buzzerOffBtn = findViewById(R.id.buzzerOffBtn)
        writeLcdBtn = findViewById(R.id.writeLcdBtn)

        readDipSwitchBtn.setOnClickListener {
            val value = hal.readDipSwitch()
            dipSwitchValueText.text = "Dip Switch: $value"
        }

        buzzerOnBtn.setOnClickListener {
            hal.buzzerOn()
        }

        buzzerOffBtn.setOnClickListener {
            hal.buzzerOff()
        }

        writeLcdBtn.setOnClickListener {
            hal.writeTextLcd("Hello Hanback")
        }
    }
}
