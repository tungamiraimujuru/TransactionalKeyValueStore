package com.example.transactionalkeyvaluestore.screen.transactions.transactions_list

import org.junit.Assert.assertEquals
import org.junit.Test

class OperationTest {

    @Test
    fun `process SET operation`() {
        val operation = Operation("SET foo 123")
        assertEquals("SET", operation.operationString.substringBefore(" "))
        assertEquals("foo", operation.operationString.substringAfter("SET ").substringBefore(" "))
        assertEquals("123", operation.operationString.substringAfterLast(" "))
    }

    @Test
    fun `process GET operation`() {
        val operation = Operation("GET foo")
        assertEquals("GET", operation.operationString.substringBefore(" "))
        assertEquals("foo", operation.operationString.substringAfter("GET "))
    }

    @Test
    fun `process DELETE operation`() {
        val operation = Operation("DELETE foo")
        assertEquals("DELETE", operation.operationString.substringBefore(" "))
        assertEquals("foo", operation.operationString.substringAfter("DELETE "))
    }

    @Test
    fun `process COUNT operation`() {
        val operation = Operation("COUNT 123")
        assertEquals("COUNT", operation.operationString.substringBefore(" "))
        assertEquals("123", operation.operationString.substringAfter("COUNT "))
    }

    @Test
    fun `process BEGIN operation`() {
        val operation = Operation("BEGIN")
        assertEquals("BEGIN", operation.operationString)
    }

    @Test
    fun `process COMMIT operation`() {
        val operation = Operation("COMMIT")
        assertEquals("COMMIT", operation.operationString)
    }

    @Test
    fun `process ROLLBACK operation`() {
        val operation = Operation("ROLLBACK")
        assertEquals("ROLLBACK", operation.operationString)
    }
}