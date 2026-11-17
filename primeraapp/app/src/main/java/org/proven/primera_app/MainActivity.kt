package org.proven.primera_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

// Elige a qué Activity controlador ir
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.bPlay3).setOnClickListener {
            startActivity(Intent(this, Activity3Cubos::class.java))
        }
        findViewById<Button>(R.id.bPlay6).setOnClickListener {
            startActivity(Intent(this, Activity6Cubos::class.java))
        }
        findViewById<Button>(R.id.bPlayIncremental).setOnClickListener {
            startActivity(Intent(this, MainComposeActivity::class.java))
        }
    }
}
