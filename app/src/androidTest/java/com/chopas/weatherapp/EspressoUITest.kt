package com.chopas.weatherapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EspressoUITest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testSuggestionLayout() {
        // search suggestion layout should be hidden initially
        onView(withId(R.id.layout_search_suggestion)).check(matches(not(isDisplayed())))

        // when search view is focused, search suggestion layout should be visible
        onView(withId(R.id.city_search_view)).perform(click())
        onView(withId(R.id.layout_search_suggestion)).check(matches(isDisplayed()))

        // when user presses view outside the search view, search suggestion should be hidden again
        onView(withId(R.id.weather_icon_image_view)).perform(click())
        onView(withId(R.id.layout_search_suggestion)).check(matches(not(isDisplayed())))
    }
}