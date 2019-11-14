package com.example.csv.logic.data

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class InMemoryRepository : IRepository {

    private val readWriteLock = ReentrantReadWriteLock()
    private val headers = Row.from(mutableListOf())
    private val rows = mutableListOf<Row>()

    override fun isEmpty(): Boolean {
        return readWriteLock.read {
            rows.isEmpty()
        }
    }

    override fun clearRows() {
        readWriteLock.write {
            rows.clear()
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
            headers.clear()
            headers.addAll(row)
        }
    }

    override fun saveRows(newRows: List<Row>) {
        return readWriteLock.write {
            rows.clear()
            rows.addAll(newRows)
        }
    }

    override fun addRow(row: Row) {
        return readWriteLock.write {
            rows.add(row)
        }

    }

}