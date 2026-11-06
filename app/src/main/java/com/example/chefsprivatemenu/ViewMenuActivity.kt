package com.example.chefsprivatemenu

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewMenuActivity : AppCompatActivity() {

    private lateinit var startersContainer: LinearLayout
    private lateinit var mainCoursesContainer: LinearLayout
    private lateinit var dessertsContainer: LinearLayout

    private lateinit var emptyStarters: TextView
    private lateinit var emptyMainCourses: TextView
    private lateinit var emptyDesserts: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_menu)

        // Connect XML elements
        startersContainer = findViewById(R.id.startersContainer)
        mainCoursesContainer = findViewById(R.id.mainCoursesContainer)
        dessertsContainer = findViewById(R.id.dessertsContainer)

        emptyStarters = findViewById(R.id.emptyStarters)
        emptyMainCourses = findViewById(R.id.emptyMainCourses)
        emptyDesserts = findViewById(R.id.emptyDesserts)

        val manageDishesButton =
            findViewById<Button>(R.id.manageDishesButton)

        val backToDashboardButton =
            findViewById<Button>(R.id.backToDashboardButton)

        // Open Manage Menu
        manageDishesButton.setOnClickListener {
            val intent = android.content.Intent(
                this,
                ManageMenuActivity::class.java
            )
            startActivity(intent)
        }

        // Return to Dashboard
        backToDashboardButton.setOnClickListener {
            finish()
        }

        displayMenu()
    }

    // Display all dishes in their correct sections
    private fun displayMenu() {

        val menuItems = MenuStorage.getAll(this)

        startersContainer.removeAllViews()
        mainCoursesContainer.removeAllViews()
        dessertsContainer.removeAllViews()

        val starters = menuItems.filter {
            it.course == "Starter"
        }

        val mainCourses = menuItems.filter {
            it.course == "Main"
        }

        val desserts = menuItems.filter {
            it.course == "Dessert"
        }

        // Display Starters
        if (starters.isEmpty()) {
            emptyStarters.visibility = View.VISIBLE
        } else {
            emptyStarters.visibility = View.GONE

            starters.forEach {
                addDishCard(startersContainer, it)
            }
        }

        // Display Main Courses
        if (mainCourses.isEmpty()) {
            emptyMainCourses.visibility = View.VISIBLE
        } else {
            emptyMainCourses.visibility = View.GONE

            mainCourses.forEach {
                addDishCard(mainCoursesContainer, it)
            }
        }

        // Display Desserts
        if (desserts.isEmpty()) {
            emptyDesserts.visibility = View.VISIBLE
        } else {
            emptyDesserts.visibility = View.GONE

            desserts.forEach {
                addDishCard(dessertsContainer, it)
            }
        }
    }

    // Create a card for each dish
    private fun addDishCard(
        container: LinearLayout,
        dish: MenuItem
    ) {

        val card = LinearLayout(this)

        card.orientation = LinearLayout.VERTICAL
        card.setPadding(40, 32, 40, 32)

        val background = GradientDrawable()

        background.setColor(Color.rgb(21, 21, 21))
        background.cornerRadius = 16f
        background.setStroke(
            2,
            Color.rgb(212, 183, 106)
        )

        card.background = background

        // Dish name
        val nameText = TextView(this)

        nameText.text = dish.name
        nameText.textSize = 20f
        nameText.setTextColor(Color.WHITE)

        // Dish description
        val descriptionText = TextView(this)

        descriptionText.text = dish.description
        descriptionText.textSize = 14f
        descriptionText.setTextColor(Color.LTGRAY)

        // Dish price
        val priceText = TextView(this)

        priceText.text =
            "${dish.currency} ${"%.2f".format(dish.price)}"

        priceText.textSize = 16f
        priceText.setTextColor(
            Color.rgb(212, 183, 106)
        )

        // Add details to card
        card.addView(nameText)
        card.addView(descriptionText)
        card.addView(priceText)

        // Add spacing between cards
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        cardParams.setMargins(0, 0, 0, 24)

        container.addView(card, cardParams)
    }

    // Refresh menu when returning to this screen
    override fun onResume() {
        super.onResume()

        if (::startersContainer.isInitialized) {
            displayMenu()
        }
    }
}