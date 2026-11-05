package com.example.chefsprivatemenu

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addDishButton = findViewById<Button>(R.id.addDishButton)
        val manageMenuButton = findViewById<Button>(R.id.manageMenuButton)
        val viewMenuButton = findViewById<Button>(R.id.viewMenuButton)

        addDishButton.setOnClickListener {
            val intent = android.content.Intent(this, AddDishActivity::class.java)
            startActivity(intent)
        }

        manageMenuButton.setOnClickListener {
            Toast.makeText(this, "Manage Menu selected", Toast.LENGTH_SHORT).show()
        }

        viewMenuButton.setOnClickListener {
            Toast.makeText(this, "View Menu selected", Toast.LENGTH_SHORT).show()
        }
    }

    // Refresh dashboard statistics whenever the screen becomes active
    override fun onResume() {
        super.onResume()
        updateDashboardStats()
    }

    // Calculate and display menu statistics
    private fun updateDashboardStats() {

        val menuItems = MenuStorage.getAll(this)

        val totalDishes = findViewById<android.widget.TextView>(R.id.totalDishes)
        val starterCount = findViewById<android.widget.TextView>(R.id.starterCount)
        val mainCount = findViewById<android.widget.TextView>(R.id.mainCount)
        val dessertCount = findViewById<android.widget.TextView>(R.id.dessertCount)
        val emptyAverage = findViewById<android.widget.TextView>(R.id.emptyAverage)

        totalDishes.text = menuItems.size.toString()

        starterCount.text = menuItems.count { it.course == "Starter" }.toString()

        mainCount.text = menuItems.count { it.course == "Main" }.toString()

        dessertCount.text = menuItems.count { it.course == "Dessert" }.toString()

        if (menuItems.isEmpty()) {
            emptyAverage.text = "No dishes added yet"
        } else {

            val averages = menuItems
                .groupBy { it.currency }
                .map { (currency, dishes) ->
                    val average = dishes.map { it.price }.average()
                    "$currency ${"%.2f".format(average)}"
                }

            emptyAverage.text =
                "Average prices: ${averages.joinToString(" | ")}"
        }
    }
}