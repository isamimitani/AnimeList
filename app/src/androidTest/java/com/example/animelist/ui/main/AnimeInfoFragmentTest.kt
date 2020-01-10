package com.example.animelist.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.animelist.R
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class AnimeInfoFragmentTest {

    @Test
    fun testAnimeInfoFragment() {

        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)
        // Create a graphical FragmentScenario for Main Screen
        val scenario = launchFragmentInContainer<AnimeInfoFragment>()
        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(ViewMatchers.withId(R.id.progress_circular)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )

        //Todo: change to wait for RecyclerView to appear
        Thread.sleep(7000)
        // Tests filter function
        onView(ViewMatchers.withId(R.id.editText)).perform(typeText("chihaya"), click())
        onView(ViewMatchers.withText("Chihayafuru")).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(ViewMatchers.withText("Chihayafuru 2")).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(ViewMatchers.withText("Chihayafuru 3")).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )

        // Verify that performing a click prompts the correct Navigation action
        onView(ViewMatchers.withId(R.id.anime_list)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(ViewMatchers.withId(R.id.anime_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        val action =
            AnimeInfoFragmentDirections.actionAnimeInfoFragmentToAnimeDetailFragment("12991")
        verify(mockNavController).navigate(action)
    }

}