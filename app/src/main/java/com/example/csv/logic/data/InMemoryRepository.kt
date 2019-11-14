package com.example.csv.logic.data

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Simple in-memory implementation of repository
 */
class InMemoryRepository : IRepository {

    private val readWriteLock = ReentrantReadWriteLock()
    private var headers = Row.from(emptyList())
    private var rows = emptyList<Row>()

    override fun isEmpty(): Boolean {
        return readWriteLock.read {
            rows.isEmpty()
        }
    }

    override fun clearRows() {
        readWriteLock.write {
            rows = emptyList<Row>()
        }
    }

    override fun readRows(): List<Row> {
        return readWriteLock.read {
            rows
        }
    }

    override fun readHeaders(): Row {
        return readWriteLock.read {
            headers
        }
    }

    override fun saveHeaders(row: Row) {
        return readWriteLock.write {
            headers = row
        }
    }

    override fun saveRows(newRows: List<Row>) {
        return readWriteLock.write {
            rows = newRows
        }
    }

    override fun addRow(row: Row) {
        return readWriteLock.write {
            rows = rows + row
        }
    }

}