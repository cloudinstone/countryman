package com.github.countryman.broker

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import rikka.shizuku.Shizuku

class BrokerInitActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildContent())

        Shizuku.addBinderReceivedListenerSticky {
            maybeRequestPermission()
        }
        Shizuku.addRequestPermissionResultListener { _, grantResult ->
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Countryman Broker is ready. You can return to Countryman.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Countryman Broker needs Shizuku permission to write carrier config.", Toast.LENGTH_LONG).show()
            }
        }

        maybeRequestPermission()
    }

    private fun maybeRequestPermission() {
        if (Shizuku.getBinder() == null) {
            Toast.makeText(this, "Start Shizuku first, then reopen Countryman Broker.", Toast.LENGTH_LONG).show()
            return
        }
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Countryman Broker is ready. You can return to Countryman.", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Shizuku.requestPermission(1001)
        }
    }

    private fun buildContent(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            val padding = (24 * resources.displayMetrics.density).toInt()
            setPadding(padding, padding, padding, padding)
            addView(TextView(context).apply {
                text = "Countryman Broker"
                textSize = 24f
            })
            addView(TextView(context).apply {
                text = "Android 16 writes run through this helper so Countryman can stay on screen."
                textSize = 16f
                setPadding(0, padding / 2, 0, 0)
            })
            addView(TextView(context).apply {
                text = "Open this once, grant Shizuku permission, then return to Countryman."
                textSize = 15f
                setPadding(0, padding / 3, 0, 0)
            })
        }
    }
}
