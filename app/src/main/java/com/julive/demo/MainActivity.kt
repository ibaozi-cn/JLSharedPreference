package com.julive.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.julive.mmkv.JLSharedPreference

class MainActivity : AppCompatActivity() {

    private val jlSharedPreference by lazy {
        JLSharedPreference.defaultSp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootDir = JLSharedPreference.initWithContext(this)
        println("mmkv root: $rootDir")

    }
}