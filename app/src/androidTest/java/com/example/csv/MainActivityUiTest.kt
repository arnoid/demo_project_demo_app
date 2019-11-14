package com.example.csv

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.csv.logic.data.Cell
import com.example.csv.logic.data.Row
import com.example.csv.logic.di.MainComponent
import com.example.csv.ui.main.MainViewModel
import com.example.csv.ui.main.ViewState
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {

    private lateinit var mainComponent: MainComponent
    private lateinit var mainViewModel: MainViewModel

    private var viewStateLiveData = MutableLiveData<ViewState>()
    private var rowsLiveData = MutableLiveData<List<Row>>()
    private var headersLiveData = MutableLiveData<Row>()


    //TODO: test is not working on Android9
    //https://stackoverflow.com/questions/58061693/espresso-generating-filenotfoundexception-when-used-with-dagger
    @get:Rule
    var activityTestRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()

            before()
        }
    }

    fun before() {
        mainViewModel = mock {
            on { retrieveRows() } doAnswer {}
            on { viewStateLiveData() } doReturn viewStateLiveData
            on { rowsLiveData() } doReturn rowsLiveData
            on { headersLiveData() } doReturn headersLiveData
        }
        mainComponent = mock {
            on { inject(any()) } doReturn mainViewModel
        }

        CsvApplication.mainComponent = mainComponent
    }

    @Test
    fun testViewStateLoading() {
        viewStateLiveData.postValue(ViewState.Loading)
        onView(withId(R.id.v_loading_indicator)).check(matches(isDisplayed()))
        onView(withId(R.id.rclr_records)).check(matches(not(isDisplayed())))
        onView(withId(R.id.v_empty_state)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testViewStateReady() {
        viewStateLiveData.postValue(ViewState.Ready)
        onView(withId(R.id.v_loading_indicator)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rclr_records)).check(matches(isDisplayed()))
        onView(withId(R.id.v_empty_state)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testViewStateError() {
        viewStateLiveData.postValue(ViewState.Error(Exception("Error")))
        onView(withId(R.id.v_loading_indicator)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rclr_records)).check(matches(not(isDisplayed())))
        onView(withId(R.id.v_empty_state)).check(matches(isDisplayed()))
    }

    @Test
    fun testHeadersDisplay() {
        viewStateLiveData.postValue(ViewState.Ready)

        val headers = Row.from(mutableListOf(Cell.from("C1"), Cell.from("C2"), Cell.from("C3"), Cell.from("C4")))

        headersLiveData.postValue(headers)

        onView(withText(headers.cells[0].data)).check(matches(isDisplayed()))
        onView(withText(headers.cells[1].data)).check(matches(isDisplayed()))
        onView(withText(headers.cells[2].data)).check(matches(isDisplayed()))
        onView(withText(headers.cells[3].data)).check(matches(isDisplayed()))
    }

    @Test
    fun testRowsDisplay() {
        viewStateLiveData.postValue(ViewState.Ready)

        val inputDate1 = "1111-11-11T00:00:00"
        val inputDate2 = "1111-11-12T00:00:00"
        val outputDate1 = "11/11/1111"
        val outputDate2 = "12/11/1111"

        val row1 = Row.from(mutableListOf(Cell.from("C1"), Cell.from("C2"), Cell.from("C3"), Cell.from(inputDate1)))
        val row2 = Row.from(mutableListOf(Cell.from("A1"), Cell.from("A2"), Cell.from("A3"), Cell.from(inputDate2)))

        rowsLiveData.postValue(mutableListOf(row1, row2))

        onView(withText(row1.cells[0].data)).check(matches(isDisplayed()))
        onView(withText(row1.cells[1].data)).check(matches(isDisplayed()))
        onView(withText(row1.cells[2].data)).check(matches(isDisplayed()))
        onView(withText(outputDate1)).check(matches(isDisplayed()))

        onView(withText(row2.cells[0].data)).check(matches(isDisplayed()))
        onView(withText(row2.cells[1].data)).check(matches(isDisplayed()))
        onView(withText(row2.cells[2].data)).check(matches(isDisplayed()))
        onView(withText(outputDate2)).check(matches(isDisplayed()))
    }

    @Test
    fun testRowsDisplayDateIsInvalid() {
        viewStateLiveData.postValue(ViewState.Ready)

        val inputDate1 = "1111-11-"

        val row1 = Row.from(mutableListOf(Cell.from("C1"), Cell.from("C2"), Cell.from("C3"), Cell.from(inputDate1)))

        rowsLiveData.postValue(mutableListOf(row1))

        onView(withText(row1.cells[0].data)).check(matches(isDisplayed()))
        onView(withText(row1.cells[1].data)).check(matches(isDisplayed()))
        onView(withText(row1.cells[2].data)).check(matches(isDisplayed()))
        onView(withText("UNKNOWN")).check(matches(isDisplayed()))

    }

    @Test
    fun testRowsDisplayIncorrectNumberOfCells() {
        viewStateLiveData.postValue(ViewState.Ready)

        val row1 = Row.from(mutableListOf(Cell.from("C1")))

        rowsLiveData.postValue(mutableListOf(row1))

        row1.cells.add(Cell.from("C2"))
        row1.cells.add(Cell.from("C3"))
        row1.cells.add(Cell.from("C4"))
        row1.cells.add(Cell.from("C5"))
        row1.cells.add(Cell.from("C6"))

        rowsLiveData.postValue(mutableListOf(row1))

        onView(withText(row1.cells[0].data)).check(matches(isDisplayed()))
        onView(withText(row1.cells[1].data)).check(matches(isDisplayed()))
        onView(withText(row1.cells[2].data)).check(matches(isDisplayed()))
        try {
            //this is date field and it will not be parsed
            onView(withText(row1.cells[3].data)).check(matches(isDisplayed()))
            fail("This view should not be in the view hierarchy")
        } catch (e: Exception) {
        }
        try {
            onView(withText(row1.cells[4].data)).check(matches(isDisplayed()))
            fail("This view should not be in the view hierarchy")
        } catch (e: Exception) {
        }

        try {
            onView(withText(row1.cells[5].data)).check(matches(isDisplayed()))
            fail("This view should not be in the view hierarchy")
        } catch (e: Exception) {
        }

    }

}
