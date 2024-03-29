package com.example.parser.csv

import com.example.parser.api.IParser
import java.io.Reader

/**
 *  CSV implementation of the parser.
 *
 *  This parser can be used in DSV mode if you specify DSV_MODE delimiter in constructor.
 */
class CsvParser(
    private val cellDelimiter: Char = CSV_MODE
) : IParser<List<String>>() {

    override fun parse(input: Reader): List<List<String>> {
        val result = mutableListOf<List<String>>()

        var endOfFile = false

        FileLoop@ while (true) {

            val row = mutableListOf<String>()
            val cellBuilder = StringBuilder()

            var isInContainer = false
            var lastChar = ' '//anything but " (double quotes) char

            RowLoop@ while (true) {
                val next = input.read()
                val nextChar = next.toChar()

                if (next == EOF) {
                    endOfFile = true
                }

                when {
                    !isInContainer && (nextChar == DELIMITER_ROW || next == EOF) -> {
                        //flush cell
                        row.add(cellBuilder.toString())

                        break@RowLoop
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

            result.add(row)

            if (endOfFile) {
                break@FileLoop
            }

        }

        return result
    }

    companion object {
        const val QUOTE = '"'
        const val DELIMITER_ROW = '\n'
        const val EOF = -1

        const val CSV_MODE = ','
        const val DSV_MODE = ';'
    }

}