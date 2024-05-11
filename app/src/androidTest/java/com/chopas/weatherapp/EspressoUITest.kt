package com.chopas.weatherapp

import android.view.View
import android.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
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
        onView(withId(R.id.layout_search_suggestion)).check(matches(not(isDisplayed())))

        onView(withId(R.id.city_search_view)).perform(click())
        onView(withId(R.id.layout_search_suggestion)).check(matches(isDisplayed()))

        onView(withId(R.id.weather_icon_image_view)).perform(click())
        onView(withId(R.id.layout_search_suggestion)).check(matches(not(isDisplayed())))
    }
}