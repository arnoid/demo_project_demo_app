package com.example.parser.csv

import com.example.parser.api.Parser
import java.io.Reader

/**
 *  CSV implementation of the parser.
 *
 *  This parser can be used in DSV mode if you specify DSV_MODE delimiter in constructor.
 */
class CsvParser(
    private val cellDelimiter: Char = CSV_MODE
) : Parser<List<String>>() {

    override fun parse(input: Reader): List<List<String>> {
        val result = mutableListOf<List<String>>()
        val bufferedInput = input.buffered()

        while (true) {
            val row = parseRow(bufferedInput)

            if (row.isEmpty()) {
                //no new lines available
                break
            } else {
                result.add(row)
            }

        }

        return result
    }

    private fun parseRow(input: Reader): List<String> {
        val row = mutableListOf<String>()

        val cellBuilder = StringBuilder()

        var isInContainer = false
        var lastChar = ' '//anything but " (double quotes) char

        loop@ while (true) {
            val next = input.read()
            val nextChar = next.toChar()

            when {
                !isInContainer && (nextChar == DELIMITER_ROW || next == EOF) -> {
                    //flush cell
                    if (cellBuilder.isNotEmpty()) {
                        row.add(cellBuilder.toString())
                    }
                    break@loop
                }
                nextChar == cellDelimiter -> {
                    row.add(cellBuilder.toString())
                    cellBuilder.clear()
                }
                !isInContainer && nextChar == QUOTE && lastChar == QUOTE -> {
                    //mimic for the double quote lookup
                    //according to RFC4180 double quotes are allowed in quoted section ONLY
                    isInContainer = true
                    cellBuilder.append(QUOTE)
                }
                nextChar == QUOTE && isInContainer -> {
                    isInContainer = false
                }
                nextChar == QUOTE -> {
                    isInContainer = true
                }
                else -> {
                    cellBuilder.append(nextChar)
                }
            }

            lastChar = nextChar
        }

        return row
    }

    companion object {
        const val QUOTE = '"'
        const val DELIMITER_ROW = '\n'
        const val EOF = -1

        const val CSV_MODE = ','
        const val DSV_MODE = ';'
    }

}