package com.example.transactionalkeyvaluestore.screen.transactions

import com.example.transactionalkeyvaluestore.repo.TransactionRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


class TransactionViewModelTest {

    private var transactionRepository = TransactionRepositoryImpl()

    private lateinit var viewModel: TransactionViewModel

    @Before
    fun setup() {
        viewModel = TransactionViewModel(Dispatchers.Unconfined,transactionRepository )
    }

    @Test
    fun `given the viewmodel is intialised `() {
        assertNotNull(viewModel)
    }

    @Test
    fun `valid input should call the appropriate repository method`() = runTest {

        val input = "SET key1 value1"
        `when`(transactionRepository.set("key1", "value1")).thenReturn(Unit)

        viewModel.validateAndProcessInput(input)

        verify(transactionRepository).set("key1", "value1")
    }

    @Test
    fun `processSetOperation should call set method in repository`() = runTest {

        val tokens = listOf("SET", "key1", "value1")
        `when`(transactionRepository.set("key1", "value1")).thenReturn(Unit)

        viewModel.processSetOperation(tokens)

        verify(transactionRepository).set("key1", "value1")
    }

    @Test
    fun `processGetOperation should call get method in repository`() = runTest {

        val tokens = listOf("GET", "key1")
        `when`(transactionRepository.get("key1")).thenReturn("value1")

        viewModel.processGetOperation(tokens)

        verify(transactionRepository).get("key1")
    }

    @Test
    fun `processDeleteOperation should call delete method in repository`() = runBlocking {

        val tokens = listOf("DELETE", "key1")
        `when`(transactionRepository.delete("key1")).thenReturn(Unit)

        viewModel.processDeleteOperation(tokens)

        verify(transactionRepository).delete("key1")
    }

    @Test
    fun `processCountOperation should call count method in repository`() = runTest {

        val tokens = listOf("COUNT", "value1")
        `when`(transactionRepository.count("value1")).thenReturn(5)

        viewModel.processCountOperation(tokens)

        verify(transactionRepository).count("value1")
    }

    @Test
    fun `processBeginOperation should call begin method in repository`() = runTest {

        viewModel.processBeginOperation()

        verify(transactionRepository).begin()
    }

    @Test
    fun `commitOperation should call commit and removeLast methods in repository`() = runBlocking {

        `when`(transactionRepository.commit()).thenReturn("COMMIT")

        viewModel.commitOperation()


        verify(transactionRepository).commit()
        verify(transactionRepository).removeLast()
    }

    @Test
    fun `rollbackOperation should call rollback method in repository`(): Unit = runBlocking {
        `when`(transactionRepository.rollback()).thenReturn("ROLLBACK")

        viewModel.rollbackOperation()

        verify(transactionRepository).rollback()
    }
}
