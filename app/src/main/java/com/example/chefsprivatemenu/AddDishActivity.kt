package com.example.chefsprivatemenu

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class AddDishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dish)

        // Connect the XML elements
        val dishNameInput = findViewById<EditText>(R.id.dishNameInput)
        val descriptionInput = findViewById<EditText>(R.id.descriptionInput)
        val priceInput = findViewById<EditText>(R.id.priceInput)

        val courseSpinner = findViewById<Spinner>(R.id.courseSpinner)
        val currencySpinner = findViewById<Spinner>(R.id.currencySpinner)

        val saveDishButton = findViewById<Button>(R.id.saveDishButton)
        val cancelDishButton = findViewById<Button>(R.id.cancelDishButton)

        // Course options
        val courses = arrayOf(
            "Select Course",
            "Starter",
            "Main",
            "Dessert"
        )

        val courseAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            courses
        )

        courseAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        courseSpinner.adapter = courseAdapter

        // Currency options
        val currencies = arrayOf(
            "Rands (R)",
            "Dollars ($)",
            "Pounds (£)"
        )

        val currencyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            currencies
        )

        currencyAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        currencySpinner.adapter = currencyAdapter

        // Check whether an existing dish is being edited
        val dishId = intent.getStringExtra("dish_id")

        val existingDish = MenuStorage.getAll(this).find {
            it.id == dishId
        }

        // Fill in the existing dish details when editing
        existingDish?.let { dish ->

            dishNameInput.setText(dish.name)
            descriptionInput.setText(dish.description)
            priceInput.setText(dish.price.toString())

            courseSpinner.setSelection(
                courses.indexOf(dish.course)
            )

            val currencyPosition = when (dish.currency) {
                "$" -> 1
                "£" -> 2
                else -> 0
            }

            currencySpinner.setSelection(currencyPosition)

            saveDishButton.text = "Update Dish"
        }

        // Save or update the dish
        saveDishButton.setOnClickListener {

            val dishName = dishNameInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val priceText = priceInput.text.toString().trim()

            val price = priceText
                .replace(",", ".")
                .toDoubleOrNull()

            when {

                dishName.isEmpty() -> {
                    dishNameInput.error = "Enter a dish name"
                }

                description.isEmpty() -> {
                    descriptionInput.error = "Enter a description"
                }

                courseSpinner.selectedItemPosition == 0 -> {
                    Toast.makeText(
                        this,
                        "Please select a course",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                price == null || price <= 0 -> {
                    priceInput.error =
                        "Enter a valid price greater than zero"
                }

                else -> {

                    val selectedCurrency = when (
                        currencySpinner.selectedItemPosition
                    ) {
                        0 -> "R"
                        1 -> "$"
                        2 -> "£"
                        else -> "R"
                    }

                    val dish = MenuItem(
                        id = existingDish?.id
                            ?: UUID.randomUUID().toString(),

                        name = dishName,
                        description = description,

                        course = courseSpinner.selectedItem.toString(),

                        price = price,
                        currency = selectedCurrency
                    )

                    if (existingDish == null) {

                        MenuStorage.add(this, dish)

                        Toast.makeText(
                            this,
                            "Dish saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        MenuStorage.update(this, dish)

                        Toast.makeText(
                            this,
                            "Dish updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                }
            }
        }

        // Cancel and return to the previous screen
        cancelDishButton.setOnClickListener {
            finish()
        }
    }
}