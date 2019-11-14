package com.example.csv.logic.usecase

import com.example.csv.logic.data.Cell
import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.Row
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReadHeadersUseCaseTest {

    lateinit var useCase: ReadHeadersUseCase
    lateinit var repository: IRepository
    lateinit var headers: Row


    @Before
    fun before() {
        headers = Row(mutableListOf())
        repository = mock {
            on { readHeaders() } doReturn headers
        }
        useCase = ReadHeadersUseCase(repository)
    }

    @Test
    fun testEmptyHeaders() {
        val headers = useCase.execute()

        assertTrue(headers.cells.isEmpty())

        verify(repository).readHeaders()
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun testNonEmptyHeaders() {
        headers.cells.addAll(
            mutableListOf(
                Cell.from("a"),
                Cell.from("b"),
                Cell.from("c")
            )
        )
        val headers = useCase.execute()

        assertTrue(headers.cells.isNotEmpty())
        assertEquals(3, headers.cells.size)

        verify(repository).readHeaders()
        verifyNoMoreInteractions(repository)
    }
}