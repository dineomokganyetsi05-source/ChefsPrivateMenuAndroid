package com.example.chefsprivatemenu

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ManageMenuActivity : AppCompatActivity() {

    private lateinit var menuItemsContainer: LinearLayout
    private lateinit var resultCount: TextView
    private lateinit var emptyMessage: TextView
    private lateinit var searchInput: EditText
    private lateinit var courseFilterSpinner: Spinner

    private val courses = arrayOf(
        "All",
        "Starter",
        "Main",
        "Dessert"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_menu)

        // Connect XML elements
        menuItemsContainer = findViewById(R.id.menuItemsContainer)
        resultCount = findViewById(R.id.resultCount)
        emptyMessage = findViewById(R.id.emptyMessage)

        searchInput = findViewById(R.id.searchInput)
        courseFilterSpinner = findViewById(R.id.courseFilterSpinner)

        val backButton = findViewById<Button>(R.id.backButton)
        val clearFiltersButton =
            findViewById<Button>(R.id.clearFiltersButton)

        // Set up course filter
        val courseAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            courses
        )

        courseAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        courseFilterSpinner.adapter = courseAdapter

        // Listen for course filter changes
        courseFilterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    displayMenuItems(
                        searchInput.text.toString(),
                        courses[position]
                    )
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {}
            }

        // Listen for search changes
        searchInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                displayMenuItems(
                    s.toString(),
                    courseFilterSpinner.selectedItem?.toString() ?: "All"
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Clear search and course filters
        clearFiltersButton.setOnClickListener {

            searchInput.text.clear()

            courseFilterSpinner.setSelection(0)

            displayMenuItems("", "All")

            Toast.makeText(
                this,
                "Filters cleared",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return to Dashboard
        backButton.setOnClickListener {
            finish()
        }

        displayMenuItems()
    }

    // Display and filter menu dishes
    private fun displayMenuItems(
        searchText: String = "",
        selectedCourse: String = "All"
    ) {

        val allDishes = MenuStorage.getAll(this)

        val menuItems = allDishes.filter { dish ->

            val matchesSearch = dish.name.contains(
                searchText,
                ignoreCase = true
            )

            val matchesCourse =
                selectedCourse == "All" ||
                        dish.course == selectedCourse

            matchesSearch && matchesCourse
        }

        menuItemsContainer.removeAllViews()

        resultCount.text = if (menuItems.size == 1) {
            "1 dish found"
        } else {
            "${menuItems.size} dishes found"
        }

        // Show an appropriate empty message
        if (menuItems.isEmpty()) {

            emptyMessage.visibility = View.VISIBLE

            emptyMessage.text = if (allDishes.isEmpty()) {
                "No dishes added yet."
            } else {
                "No dishes match your search or filter."
            }

            return
        }

        emptyMessage.visibility = View.GONE

        // Create a card for every dish
        for (dish in menuItems) {

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

            // Dish course
            val courseText = TextView(this)

            courseText.text = "Course: ${dish.course}"
            courseText.textSize = 14f
            courseText.setTextColor(
                Color.rgb(212, 183, 106)
            )

            // Dish price
            val priceText = TextView(this)

            priceText.text =
                "${dish.currency} ${"%.2f".format(dish.price)}"

            priceText.textSize = 16f
            priceText.setTextColor(Color.WHITE)

            // Edit button
            val editButton = Button(this)

            editButton.text = "Edit Dish"

            editButton.setOnClickListener {

                val intent = android.content.Intent(
                    this,
                    AddDishActivity::class.java
                )

                intent.putExtra("dish_id", dish.id)

                startActivity(intent)
            }

            // Delete button
            val deleteButton = Button(this)

            deleteButton.text = "Delete Dish"

            deleteButton.setOnClickListener {

                AlertDialog.Builder(this)
                    .setTitle("Delete Dish")
                    .setMessage(
                        "Are you sure you want to delete ${dish.name}?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        MenuStorage.delete(this, dish.id)

                        Toast.makeText(
                            this,
                            "Dish deleted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        displayMenuItems(
                            searchInput.text.toString(),
                            courseFilterSpinner.selectedItem?.toString()
                                ?: "All"
                        )
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            // Add everything to the dish card
            card.addView(nameText)
            card.addView(descriptionText)
            card.addView(courseText)
            card.addView(priceText)
            card.addView(editButton)
            card.addView(deleteButton)

            // Add spacing between cards
            val cardParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            cardParams.setMargins(0, 0, 0, 24)

            menuItemsContainer.addView(card, cardParams)
        }
    }

    // Refresh menu when returning to this screen
    override fun onResume() {
        super.onResume()

        if (::menuItemsContainer.isInitialized) {

            displayMenuItems(
                searchInput.text.toString(),
                courseFilterSpinner.selectedItem?.toString() ?: "All"
            )
        }
    }
}
