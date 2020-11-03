package com.regula.documentreader


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.regula.documentreader.ui.main.ScanFragment

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ScanFragment.newInstance())
                .commitNow()
        }
    }
}