package com.example.chefsprivatemenu

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val course: String,
    val price: Double,
    val currency: String
)