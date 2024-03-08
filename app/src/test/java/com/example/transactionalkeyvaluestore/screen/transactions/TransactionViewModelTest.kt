package com.example.transactionalkeyvaluestore.screen.transactions

import com.example.transactionalkeyvaluestore.repo.TransactionRepository
import com.example.transactionalkeyvaluestore.repo.TransactionRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class TransactionViewModelTest {

    @Mock
    private lateinit var repo: TransactionRepository

    private lateinit var viewModel: TransactionViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this) // Ini
        viewModel = TransactionViewModel(Dispatchers.Unconfined, repo)
    }

    @Test
    fun `given the viewmodel is initialized, then assertNotNull`() {
        assertNotNull(viewModel)
    }

    @Test
    fun `valid input should call the appropriate repository method`() = runTest {

        val input = "SET key1 value1"
        doNothing().`when`(repo).set(anyString(), anyString())

        viewModel.validateAndProcessInput(input)

        verify(repo).set("key1", "value1")
    }

    @Test
    fun `processSetOperation should call set method in repository`() = runTest {

        val tokens = listOf("SET", "key1", "value1")

        doNothing().`when`(repo).set(anyString(), anyString())

        viewModel.processSetOperation(tokens)

        verify(repo).set("key1", "value1")
    }

    @Test
    fun `processGetOperation should call get method in repository`() = runTest {

        val tokens = listOf("GET", "key1")
        `when`(repo.get("key1")).thenReturn("value1")

        viewModel.processGetOperation(tokens)

        verify(repo).get("key1")
    }

    @Test
    fun `processDeleteOperation should call delete method in repository`() = runTest {

        val tokens = listOf("DELETE", "key1")

        doNothing().`when`(repo).delete(anyString())

        viewModel.processDeleteOperation(tokens)

        verify(repo).delete("key1")
    }

    @Test
    fun `processCountOperation should call count method in repository`() = runTest {

        val tokens = listOf("COUNT", "value1")
        `when`(repo.count("value1")).thenReturn(5)

        viewModel.processCountOperation(tokens)

        verify(repo).count("value1")
    }

    @Test
    fun `processBeginOperation should call begin method in repository`() = runTest {

        viewModel.processBeginOperation()

        verify(repo).begin()
    }

    @Test
    fun `commitOperation should call commit and removeLast methods in repository`() = runBlocking {

        `when`(repo.commit()).thenReturn("COMMIT")

        viewModel.commitOperation()

        verify(repo).commit()
        verify(repo).removeLast()
    }

    @Test
    fun `rollbackOperation should call rollback method in repository`(): Unit = runBlocking {
        `when`(repo.rollback()).thenReturn("ROLLBACK")

        viewModel.rollbackOperation()

        verify(repo).rollback()
    }
}
