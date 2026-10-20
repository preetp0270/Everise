package com.example.everise.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val MAIN = "main"
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val ADD_HABIT = "add_habit"
    const val INSIGHTS = "insights"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"

    const val HABIT_DETAIL = "habit_detail/{habitId}"
    fun habitDetail(habitId: String) = "habit_detail/$habitId"

    const val EDIT_HABIT = "edit_habit/{habitId}"
    fun editHabit(habitId: String) = "edit_habit/$habitId"
}
