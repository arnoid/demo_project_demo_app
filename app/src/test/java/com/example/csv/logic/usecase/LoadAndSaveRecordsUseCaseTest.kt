package com.example.csv.logic.usecase

import android.content.Context
import com.example.csv.logic.data.Cell
import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.Row
import com.example.parser.api.IParser
import com.nhaarman.mockitokotlin2.*
import com.nhaarman.mockitokotlin2.internal.createInstance
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.InputStreamReader

class LoadAndSaveRecordsUseCaseTest {

    lateinit var useCase: LoadAndSaveRecordsUseCase
    lateinit var repository: IRepository
    lateinit var context: Context
    lateinit var parser: IParser<List<String>>
    lateinit var inputReader: InputStreamReader

    var isRepositoryEmpty = false

    var header0 = Cell.from("H0")
    var header1 = Cell.from("H1")
    var header2 = Cell.from("H2")

    var cell0 = Cell.from("C0")
    var cell1 = Cell.from("C1")
    var cell2 = Cell.from("C2")

    var headersRaw = listOf(header0.data, header1.data, header2.data)
    var rowRaw = listOf(cell0.data, cell1.data, cell2.data)

    @Before
    fun before() {
        inputReader = mock()
        parser = mock {
            on { parse(anyOrNull()) } doReturn listOf(headersRaw, rowRaw, rowRaw)
        }
        context = mock()
        repository = mock {
            on { isEmpty() } doAnswer { isRepositoryEmpty }
        }
        useCase = LoadAndSaveRecordsUseCase(context, parser, repository, { inputReader })
    }

    @Test
    fun testNormalRunAndRepositoryIsEmpty() {
        isRepositoryEmpty = true
        useCase.execute()

        verify(inputReader).close()
        verifyNoMoreInteractions(inputReader)

        verify(parser).parse(eq(inputReader))
        verifyNoMoreInteractions(parser)

        verify(repository).isEmpty()
        verify(repository).clearRows()
        argumentCaptor<Row>().apply {
            verify(repository).saveHeaders(capture())

            assertEquals(header0, allValues[0].cells[0])
            assertEquals(header1, allValues[0].cells[1])
            assertEquals(header2, allValues[0].cells[2])
        }

        verify(repository, times(2)).addRow(any())

        argumentCaptor<Row>().apply {
            verify(repository, times(2)).addRow(capture())

            assertEquals(cell0, allValues[0].cells[0])
            assertEquals(cell1, allValues[0].cells[1])
            assertEquals(cell2, allValues[0].cells[2])

            assertEquals(cell0, allValues[1].cells[0])
            assertEquals(cell1, allValues[1].cells[1])
            assertEquals(cell2, allValues[1].cells[2])
        }

        verifyNoMoreInteractions(repository)
    }

    @Test
    fun testNormalRunAndRepositoryIsNotEmpty() {
        isRepositoryEmpty = false
        useCase.execute()

        verifyZeroInteractions(inputReader)
        verifyZeroInteractions(parser)

        verify(repository).isEmpty()
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun testNormalRunAndRepositoryIsNotEmptyAndForced() {
        isRepositoryEmpty = false
        useCase.execute(true)

        verify(inputReader).close()
        verifyNoMoreInteractions(inputReader)

        verify(parser).parse(eq(inputReader))
        verifyNoMoreInteractions(parser)

        verify(repository).isEmpty()
        verify(repository).clearRows()
        argumentCaptor<Row>().apply {
            verify(repository).saveHeaders(capture())

            assertEquals(header0, allValues[0].cells[0])
            assertEquals(header1, allValues[0].cells[1])
            assertEquals(header2, allValues[0].cells[2])
        }

        verify(repository, times(2)).addRow(any())

        argumentCaptor<Row>().apply {
            verify(repository, times(2)).addRow(capture())

            assertEquals(cell0, allValues[0].cells[0])
            assertEquals(cell1, allValues[0].cells[1])
            assertEquals(cell2, allValues[0].cells[2])

            assertEquals(cell0, allValues[1].cells[0])
            assertEquals(cell1, allValues[1].cells[1])
            assertEquals(cell2, allValues[1].cells[2])
        }

        verifyNoMoreInteractions(repository)
    }

    inline fun <reified T : Any> any() = Mockito.any(T::class.java) ?: createInstance<T>()

}