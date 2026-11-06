package com.example.chefsprivatemenu

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect Dashboard buttons
        val addDishButton = findViewById<Button>(R.id.addDishButton)
        val manageMenuButton = findViewById<Button>(R.id.manageMenuButton)
        val viewMenuButton = findViewById<Button>(R.id.viewMenuButton)

        // Open Add Dish screen
        addDishButton.setOnClickListener {
            val intent = android.content.Intent(
                this,
                AddDishActivity::class.java
            )

            startActivity(intent)
        }

        // Open Manage Menu screen
        manageMenuButton.setOnClickListener {
            val intent = android.content.Intent(
                this,
                ManageMenuActivity::class.java
            )

            startActivity(intent)
        }

        // Open View Menu screen
        viewMenuButton.setOnClickListener {
            val intent = android.content.Intent(
                this,
                ViewMenuActivity::class.java
            )

            startActivity(intent)
        }
    }

    // Refresh statistics when returning to Dashboard
    override fun onResume() {
        super.onResume()
        updateDashboardStats()
    }

    // Update Dashboard statistics
    private fun updateDashboardStats() {

        val menuItems = MenuStorage.getAll(this)

        val totalDishes = findViewById<TextView>(R.id.totalDishes)
        val starterCount = findViewById<TextView>(R.id.starterCount)
        val mainCount = findViewById<TextView>(R.id.mainCount)
        val dessertCount = findViewById<TextView>(R.id.dessertCount)
        val emptyAverage = findViewById<TextView>(R.id.emptyAverage)

        // Total number of dishes
        totalDishes.text = menuItems.size.toString()

        // Count dishes by course
        starterCount.text = menuItems.count {
            it.course == "Starter"
        }.toString()

        mainCount.text = menuItems.count {
            it.course == "Main"
        }.toString()

        dessertCount.text = menuItems.count {
            it.course == "Dessert"
        }.toString()

        // Calculate average prices by currency
        if (menuItems.isEmpty()) {

            emptyAverage.text = "No dishes added yet"

        } else {

            val averages = menuItems
                .groupBy { it.currency }
                .map { (currency, dishes) ->

                    val average = dishes
                        .map { it.price }
                        .average()

                    "$currency ${"%.2f".format(average)}"
                }

            emptyAverage.text =
                "Average prices: ${averages.joinToString(" | ")}"
        }
    }
}