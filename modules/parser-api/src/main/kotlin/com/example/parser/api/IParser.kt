package com.example.parser.api

import java.io.Reader

/**
 *  API class for parsers to comply
 */
abstract class IParser<OUT> {

    abstract fun parse(input: Reader): List<OUT>

}