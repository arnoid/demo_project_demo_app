package com.example.csv.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.csv.logic.data.Cell
import com.example.csv.logic.data.Row
import com.example.csv.logic.di.DaggerMainComponent
import com.example.csv.logic.di.ParserModule
import com.example.csv.logic.di.RepositoryModule
import com.example.csv.logic.di.UseCaseModule
import com.example.csv.logic.usecase.LoadAndSaveRecordsUseCase
import com.example.csv.logic.usecase.ReadHeadersUseCase
import com.example.csv.logic.usecase.ReadRowsUseCase
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @UseExperimental(ObsoleteCoroutinesApi::class)

    lateinit var viewModel: MainViewModel

    lateinit var useCaseModule: UseCaseModule
    lateinit var repositoryModule: RepositoryModule
    lateinit var parserModule: ParserModule

    lateinit var loadAndSaveRecordsUseCase: LoadAndSaveRecordsUseCase
    lateinit var readHeadersUseCase: ReadHeadersUseCase
    lateinit var readRowsUseCase: ReadRowsUseCase

    @Before
    fun before() {
        viewModel = MainViewModel()

        loadAndSaveRecordsUseCase = mock()
        readHeadersUseCase = mock()
        readRowsUseCase = mock()

        useCaseModule = mock {
            on { provideLoadAndSaveRecordsUseCase(any(), any()) } doReturn loadAndSaveRecordsUseCase
            on { provideReadHeadersUseCase(any()) } doReturn readHeadersUseCase
            on { provideReadRowsUseCase(any()) } doReturn readRowsUseCase
        }

        repositoryModule = mock {
            on { provideRepository() } doReturn mock()
        }
        parserModule = mock {
            on { provideParser() } doReturn mock()
        }

        val mainComponent = DaggerMainComponent.builder()
            .useCaseModule(useCaseModule)
            .repositoryModule(repositoryModule)
            .parserModule(parserModule)
            .build()

        viewModel = mainComponent.inject(viewModel)

    }

    @Test
    fun testRetrieveRows() {
        val headers = Row.from(mutableListOf(Cell.from("")))
        val rows = mutableListOf(Row.from(mutableListOf(Cell.from(""))))
        whenever(readHeadersUseCase.execute()).doReturn(headers)
        whenever(readRowsUseCase.execute()).doReturn(rows)

        val viewStateTestObserver = viewModel.viewStateLiveData().test()
        val headersTestObserver = viewModel.headersLiveData().test()
        val rowsTestObserver = viewModel.rowsLiveData().test()

        runBlocking {
            viewModel.retrieveRows(Dispatchers.Unconfined)
        }

        viewStateTestObserver
            .assertHistorySize(2)
            .valueHistory().let { history ->
                assertEquals(ViewState.Loading, history[0])
                assertEquals(ViewState.Ready, history[1])
            }

        headersTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertValue(headers)

        rowsTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertValue(rows)

        verify(loadAndSaveRecordsUseCase).execute()
        verify(readHeadersUseCase).execute()
        verify(readRowsUseCase).execute()
    }

    @Test
    fun testRetrieveRowsAndLoadAndSaveRecordsUseCaseThrowsException() {
        val exception = RuntimeException("dfsfsd")
        whenever(loadAndSaveRecordsUseCase.execute()).doThrow(exception)

        val viewStateTestObserver = viewModel.viewStateLiveData().test()
        val headersTestObserver = viewModel.headersLiveData().test()
        val rowsTestObserver = viewModel.rowsLiveData().test()

        runBlocking {
            viewModel.retrieveRows(Dispatchers.Unconfined)
        }

        viewStateTestObserver
            .assertHistorySize(2)
            .valueHistory().let { history ->
                assertEquals(ViewState.Loading, history[0])
                assertTrue((history[1] as ViewState.Error).exception === exception)
            }

        headersTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertHistorySize(0)

        rowsTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertHistorySize(0)

        verify(loadAndSaveRecordsUseCase).execute()
        verifyZeroInteractions(readHeadersUseCase)
        verifyZeroInteractions(readRowsUseCase)
    }


    @Test
    fun testRetrieveRowsAndReadHeadersUseCaseThrowsException() {
        val exception = RuntimeException("dfsfsd")
        whenever(readHeadersUseCase.execute()).doThrow(exception)

        val viewStateTestObserver = viewModel.viewStateLiveData().test()
        val headersTestObserver = viewModel.headersLiveData().test()
        val rowsTestObserver = viewModel.rowsLiveData().test()

        runBlocking {
            viewModel.retrieveRows(Dispatchers.Unconfined)
        }

        viewStateTestObserver
            .assertHistorySize(2)
            .valueHistory().let { history ->
                assertEquals(ViewState.Loading, history[0])
                assertTrue((history[1] as ViewState.Error).exception === exception)
            }

        headersTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertHistorySize(0)

        rowsTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertHistorySize(0)

        verify(loadAndSaveRecordsUseCase).execute()
        verify(readHeadersUseCase).execute()
        verifyZeroInteractions(readRowsUseCase)
    }

    @Test
    fun testRetrieveRowsAndReadRowsUseCaseThrowsException() {
        val headers = Row.from(mutableListOf(Cell.from("")))
        whenever(readHeadersUseCase.execute()).doReturn(headers)

        val exception = RuntimeException("dfsfsd")
        whenever(readRowsUseCase.execute()).doThrow(exception)

        val viewStateTestObserver = viewModel.viewStateLiveData().test()
        val headersTestObserver = viewModel.headersLiveData().test()
        val rowsTestObserver = viewModel.rowsLiveData().test()

        runBlocking {
            viewModel.retrieveRows(Dispatchers.Unconfined)
        }

        viewStateTestObserver
            .assertHistorySize(2)
            .valueHistory().let { history ->
                assertEquals(ViewState.Loading, history[0])
                assertTrue((history[1] as ViewState.Error).exception === exception)
            }

        headersTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertValue(headers)

        rowsTestObserver
            .awaitNextValue(1, TimeUnit.SECONDS)
            .assertHistorySize(0)

        verify(loadAndSaveRecordsUseCase).execute()
        verify(readHeadersUseCase).execute()
        verify(readRowsUseCase).execute()
    }
}