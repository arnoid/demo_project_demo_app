package com.example.parser.csv

import com.example.parser.api.Parser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CsvParserTest {

    lateinit var csvParser: Parser<List<String>>

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
        val parsedValues = csvParser.parse(CELL_MULTIQUTE_ESCAPED.reader())
        assertEquals(1, parsedValues.size)
        assertEquals(1, parsedValues[0].size)
        assertEquals(CELL_MULTIQUTE, parsedValues[0][0])
    }

    companion object {
        const val CELL_1 = "1"
        const val CELL_2 = "2"
        const val CELL_3 = "3 \n 4"
        const val CELL_3_ESCAPED = "\"$CELL_3\""

        const val CELL_MULTILINE = "d\nf23f\n\n\n23423r23tf 2 34r2f 2f 2\n\n23r2gf2\n\n23rf\n23r2gfg24g24g 24f234f3f"
        const val CELL_MULTILINE_ESCAPED = "\"$CELL_MULTILINE\""

        const val CELL_MULTIQUTE = "\"  \n\"\"  dfadfafasf \""

        const val CELL_MULTIQUTE_ESCAPED = "\"\"\"  \n\"\"\"\"  dfadfafasf \"\"\""
    }
}