package com.example.chefsprivatemenu

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object MenuStorage {

    private const val PREFS_NAME = "chefs_private_menu"
    private const val KEY_MENU_ITEMS = "menu_items"

    // Retrieve all saved dishes
    fun getAll(context: Context): MutableList<MenuItem> {
        val preferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val json = preferences.getString(KEY_MENU_ITEMS, "[]") ?: "[]"
        val array = JSONArray(json)

        val items = mutableListOf<MenuItem>()

        for (i in 0 until array.length()) {
            val dish = array.getJSONObject(i)

            items.add(
                MenuItem(
                    id = dish.getString("id"),
                    name = dish.getString("name"),
                    description = dish.getString("description"),
                    course = dish.getString("course"),
                    price = dish.getDouble("price"),
                    currency = dish.getString("currency")
                )
            )
        }

        return items
    }

    // Add a new dish
    fun add(context: Context, item: MenuItem) {
        val items = getAll(context)
        items.add(item)
        saveAll(context, items)
    }

    // Save the complete dish list
    private fun saveAll(context: Context, items: List<MenuItem>) {
        val array = JSONArray()

        items.forEach { item ->
            val dish = JSONObject()

            dish.put("id", item.id)
            dish.put("name", item.name)
            dish.put("description", item.description)
            dish.put("course", item.course)
            dish.put("price", item.price)
            dish.put("currency", item.currency)

            array.put(dish)
        }

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_MENU_ITEMS, array.toString())
            .apply()
    }
}

