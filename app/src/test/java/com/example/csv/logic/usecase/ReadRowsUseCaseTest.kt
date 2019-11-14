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

class ReadRowsUseCaseTest {

    lateinit var useCase: ReadRowsUseCase
    lateinit var repository: IRepository
    lateinit var rows: List<Row>


    @Before
    fun before() {
        rows = emptyList()
        repository = mock {
            on { readRows() } doReturn rows
        }
        useCase = ReadRowsUseCase(repository)
    }

    @Test
    fun testEmptyRows() {
        val rowsList = useCase.execute()

        assertTrue(rowsList.isEmpty())

        verify(repository).readRows()
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun testNonEmptyRows() {
        rows = listOf(
            Row.from(
                listOf(
                    Cell.from("a"),
                    Cell.from("b"),
                    Cell.from("c")
                )
            )
        )
        val rowsList = useCase.execute()

        assertTrue(rowsList.isNotEmpty())
        assertEquals(1, rowsList.size)
        assertEquals(3, rowsList[0].cells.size)

        verify(repository).readRows()
        verifyNoMoreInteractions(repository)
    }
}