package com.example.transactionalkeyvaluestore.repo

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TransactionRepositoryImplTest {

    private lateinit var transactionRepository: TransactionRepositoryImpl

    @Before
    fun setUp() {
        transactionRepository = TransactionRepositoryImpl()
    }

    @Test
    fun `set and get operations should work correctly`() {
        transactionRepository.set("key1", "value1")
        assertEquals("value1", transactionRepository.get("key1"))
    }

    @Test
    fun `delete operation should remove the key`() {
        transactionRepository.set("key1", "value1")
        transactionRepository.delete("key1")
        assertEquals("key not set", transactionRepository.get("key1"))
    }

    @Test
    fun `count operation should return the correct count`() {
        transactionRepository.set("key1", "value1")
        transactionRepository.set("key2", "value1")
        transactionRepository.set("key3", "value2")
        assertEquals(2, transactionRepository.count("value1"))
    }

    @Test
    fun `begin and commit operations should work correctly`() {
        transactionRepository.begin()
        transactionRepository.set("key1", "value1")
        transactionRepository.commit()
        assertEquals("value1", transactionRepository.get("key1"))
    }

    @Test
    fun `rollback operation should remove all transactions`() {
        transactionRepository.begin()
        transactionRepository.set("key1", "value1")
        transactionRepository.rollback()
        assertEquals("key not set", transactionRepository.get("key1"))
    }

    @Test
    fun `removeLast operation should remove the last transaction`() {
        transactionRepository.begin()
        transactionRepository.removeLast()
        assertTrue(transactionRepository.transactionStack.isEmpty())
    }
}