package com.example.chefsprivatemenu

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the dashboard buttons
        val addDishButton = findViewById<Button>(R.id.addDishButton)
        val manageMenuButton = findViewById<Button>(R.id.manageMenuButton)
        val viewMenuButton = findViewById<Button>(R.id.viewMenuButton)

        // Add Dish button
        addDishButton.setOnClickListener {
            Toast.makeText(this, "Add Dish selected", Toast.LENGTH_SHORT).show()
        }

        // Manage Menu button
        manageMenuButton.setOnClickListener {
            Toast.makeText(this, "Manage Menu selected", Toast.LENGTH_SHORT).show()
        }

        // View Menu button
        viewMenuButton.setOnClickListener {
            Toast.makeText(this, "View Menu selected", Toast.LENGTH_SHORT).show()
        }
    }
}