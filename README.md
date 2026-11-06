Chef’s Private Menu – Android Application

Project Description

Chef’s Private Menu is an Android application designed to help a restaurant owner manage their menu. Users can add, edit, delete, search, filter and view dishes according to their course.

The original application was developed in React Native using Expo Snack in Part 2. For Part 3, the application was rebuilt and refactored in Android Studio using Kotlin and XML.

Change Log: Changes Since Part 2

1. Dashboard

 Added a total dish count.
 Added average dish price calculations grouped by currency.
 Retained the course statistics for Starters, Mains and Desserts.
 Updated the dashboard to navigate to the different application screens.

2. Add and Edit Dish

 Retained the ability to add new dishes.
 Implemented the ability to edit existing dishes.
 Added validation to ensure required fields are completed.
 Added validation to ensure prices are valid numbers greater than zero.
 Added support for decimal prices using commas or full stops.
 Retained currency selection for Rands, Dollars and Pounds.

3. Manage Menu

 Retained the ability to search for dishes by name.
 Added a course filter to filter dishes by Starter, Main or Dessert.
 Added a Clear Filters button to reset the search and course filter.
 Added confirmation before deleting a dish.
 Retained the ability to edit and delete existing dishes.

4. View Menu

 Implemented a dedicated screen for displaying menu items.
 Organised dishes into Starter, Main and Dessert sections.
 Added empty-state messages when a course has no dishes.
 Added navigation between View Menu, Manage Menu and the Dashboard.

5. Data Storage

 Implemented local data storage using SharedPreferences and JSON.
 Menu items are saved locally and remain available after closing and reopening the application.
 Added functionality to update and delete saved menu items.

Refactoring Changes

The original React Native application was refactored into a native Android application.

The following changes were made:

 Replaced React Native and JavaScript with Kotlin.
 Replaced React Native components with native Android XML layouts.
 Separated the application into individual Activities instead of managing all screens within one React Native component.
 Created a MenuItem data class to represent menu items.
 Created a MenuStorage class to manage saving, retrieving, updating and deleting menu data.
 Separated the application’s screen layouts into individual XML files.
 Updated the AndroidManifest.xml to register the application’s Activities.
 Implemented navigation between the different Activities.
 Replaced temporary React state with persistent local storage.
 Updated the project configuration and dependencies for Android Studio.

Technologies Used

 Android Studio
 Kotlin
 XML
 SharedPreferences
 JSON
 GitHub





Video recording:
