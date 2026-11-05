package com.example.chefsprivatemenu

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity

class ManageMenuActivity : AppCompatActivity() {

    private lateinit var menuItemsContainer: LinearLayout
    private lateinit var resultCount: TextView
    private lateinit var emptyMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_menu)

        menuItemsContainer = findViewById(R.id.menuItemsContainer)
        resultCount = findViewById(R.id.resultCount)
        emptyMessage = findViewById(R.id.emptyMessage)

        val backButton = findViewById<Button>(R.id.backButton)
        val searchInput = findViewById<android.widget.EditText>(R.id.searchInput)

        backButton.setOnClickListener {
            finish()
        }

        searchInput.addTextChangedListener(object : android.text.TextWatcher {

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
                displayMenuItems(s.toString())
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        displayMenuItems()
    }

    private fun displayMenuItems(searchText: String = "") {

        val menuItems = MenuStorage.getAll(this).filter {
            it.name.contains(searchText, ignoreCase = true)
        }

        menuItemsContainer.removeAllViews()

        resultCount.text = "${menuItems.size} dishes found"

        if (menuItems.isEmpty()) {
            emptyMessage.visibility = View.VISIBLE
            return
        }

        emptyMessage.visibility = View.GONE

        for (dish in menuItems) {

            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(40, 32, 40, 32)

            val background = GradientDrawable()
            background.setColor(Color.rgb(21, 21, 21))
            background.cornerRadius = 16f
            background.setStroke(2, Color.rgb(212, 183, 106))

            card.background = background

            val nameText = TextView(this)
            nameText.text = dish.name
            nameText.textSize = 20f
            nameText.setTextColor(Color.WHITE)

            val descriptionText = TextView(this)
            descriptionText.text = dish.description
            descriptionText.textSize = 14f
            descriptionText.setTextColor(Color.LTGRAY)

            val courseText = TextView(this)
            courseText.text = "Course: ${dish.course}"
            courseText.textSize = 14f
            courseText.setTextColor(Color.rgb(212, 183, 106))

            val priceText = TextView(this)
            priceText.text = "${dish.currency} ${"%.2f".format(dish.price)}"
            priceText.textSize = 16f
            priceText.setTextColor(Color.WHITE)

            card.addView(nameText)
            card.addView(descriptionText)
            card.addView(courseText)
            card.addView(priceText)

            val cardParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            cardParams.setMargins(0, 0, 0, 24)

            menuItemsContainer.addView(card, cardParams)
        }
    }

    override fun onResume() {
        super.onResume()

        if (::menuItemsContainer.isInitialized) {
            displayMenuItems()
        }
    }
}