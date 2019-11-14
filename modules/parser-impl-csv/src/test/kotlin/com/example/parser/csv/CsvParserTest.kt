package com.example.parser.csv

import com.example.parser.api.IParser
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.InputStream
import java.io.InputStreamReader

class CsvParserTest {

    lateinit var csvParser: IParser<List<String>>

    @Before
    internal fun before() {
        csvParser = CsvParser()
    }

    @Test
    fun testSingleRow() {
        val parsedValues = csvParser.parse("$CELL_1,$CELL_2,$CELL_3_ESCAPED".reader())

        assertEquals(1, parsedValues.size)
        val row = parsedValues[0]
        assertEquals(3, row.size)
        assertEquals(CELL_1, row[0])
        assertEquals(CELL_2, row[1])
        assertEquals(CELL_3, row[2])
    }

    @Test
    fun testMultiRow() {
        val input = "$CELL_1,$CELL_2,$CELL_3_ESCAPED\n" +
                "$CELL_2,$CELL_3_ESCAPED,$CELL_1\n" +
                "$CELL_3_ESCAPED,$CELL_1,$CELL_2"
        val parsedValues = csvParser.parse(input.reader())

        assertEquals(3, parsedValues.size)

        assertEquals(3, parsedValues[0].size)
        assertEquals(CELL_1, parsedValues[0][0])
        assertEquals(CELL_2, parsedValues[0][1])
        assertEquals(CELL_3, parsedValues[0][2])

        assertEquals(3, parsedValues[1].size)
        assertEquals(CELL_2, parsedValues[1][0])
        assertEquals(CELL_3, parsedValues[1][1])
        assertEquals(CELL_1, parsedValues[1][2])

        assertEquals(3, parsedValues[2].size)
        assertEquals(CELL_3, parsedValues[2][0])
        assertEquals(CELL_1, parsedValues[2][1])
        assertEquals(CELL_2, parsedValues[2][2])
    }

    @Test
    fun testMultilineCell() {
        val parsedValues = csvParser.parse(CELL_MULTILINE_ESCAPED.reader())
        assertEquals(1, parsedValues.size)
        assertEquals(1, parsedValues[0].size)
        assertEquals(CELL_MULTILINE, parsedValues[0][0])
    }

    @Test
    fun testMultiquoteCell() {
        val parsedValues = csvParser.parse(CELL_MULTIQUOTE_ESCAPED.reader())
        assertEquals(1, parsedValues.size)
        assertEquals(1, parsedValues[0].size)
        assertEquals(CELL_MULTIQUOTE, parsedValues[0][0])
    }

    @Test
    fun testEmpty() {
        val parsedValues = csvParser.parse("".reader())
        assertTrue(parsedValues.isEmpty())
    }

    @Test
    fun testExceptionOnRead() {
        var parsedValues: List<List<String>>? = null
        try {
            /**
             * We can do this with mocks, but for single use, I think it would be easier to do this anonymous implementation
             */
            parsedValues = csvParser.parse(
                object : InputStreamReader(
                    object : InputStream() {
                        override fun read(): Int {
                            throw RuntimeException("")
                        }
                    }
                ) {}
            )
            fail("Should propagate exception")
        } catch (e: RuntimeException) {
            //it is ok
        }

        assertNull(parsedValues)
    }

    companion object {
        const val CELL_1 = "1"
        const val CELL_2 = "2"
        const val CELL_3 = "3 \n 4"
        const val CELL_3_ESCAPED = "\"$CELL_3\""

        const val CELL_MULTILINE = "d\nf23f\n\n\n23423r23tf 2 34r2f 2f 2\n\n23r2gf2\n\n23rf\n23r2gfg24g24g 24f234f3f"
        const val CELL_MULTILINE_ESCAPED = "\"$CELL_MULTILINE\""

        const val CELL_MULTIQUOTE = "\"  \n\"\"  dfadfafasf \""

        const val CELL_MULTIQUOTE_ESCAPED = "\"\"\"  \n\"\"\"\"  dfadfafasf \"\"\""
    }
}