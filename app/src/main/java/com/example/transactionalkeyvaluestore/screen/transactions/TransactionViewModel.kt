package com.example.transactionalkeyvaluestore.screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transactionalkeyvaluestore.repo.TransactionRepository
import com.example.transactionalkeyvaluestore.repo.TransactionRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class TransactionViewModel @Inject constructor(
    @Named("io")
    private val ioContext: CoroutineContext,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _clearInput = MutableSharedFlow<Boolean>()
    val clearInput = _clearInput.asSharedFlow()

    private val _transaction = MutableSharedFlow<String>()
    val transaction = _transaction.asSharedFlow()

    private fun isInputValid(input: String): Boolean {
        return if (input.isNotEmpty()) {
            verifyOperations(input)
        } else {
            false
        }
    }

    fun validateAndProcessInput(input: String) {
        viewModelScope.launch(ioContext) {
            if (!isInputValid(input)) {
                _transaction.emit("invalid command")
            } else {
                processOperations(input)
            }
        }
    }

    private fun verifyOperations(input: String): Boolean {
        val operation = input.split(" ")
        return when (operation[0]) {
            SET, GET, DELETE, COUNT, BEGIN, COMMIT, ROLLBACK -> {
                true
            }

            else -> {
                false
            }
        }
    }

    fun processOperations(input: String) {

        val operationTokens = input.split(" ")
        val operationType = operationTokens[0]

        when (operationType) {
            SET -> processSetOperation(operationTokens)
            GET -> processGetOperation(operationTokens)
            DELETE -> processDeleteOperation(operationTokens)
            COUNT -> processCountOperation(operationTokens)
            BEGIN -> processBeginOperation()
            COMMIT -> {
                processCommitOperation()
            }

            ROLLBACK -> processRollbackOperation()
            else -> {
                // No action for unrecognized operation
            }
        }
    }

    fun processSetOperation(tokens: List<String>) {
        viewModelScope.launch(ioContext) {
            tokens.takeIf { it.size == 3 }?.let { (_, key, value) ->
                transactionRepository.set(key, value)
                _transaction.emit("$RIGHT_ANGLE_BRACKET  $SET $key $value")
            } ?:  _transaction.emit("Error: Insufficient arguments for set operation")
            clearInputField()
        }
    }

    fun processGetOperation(tokens: List<String>) {
        viewModelScope.launch(ioContext) {
            tokens.takeIf { it.size == 2 }?.let { (_, key) ->
                _transaction.emit("${TransactionRepositoryImpl.RIGHT_ANGLE_BRACKET}  $GET $key ")
                _transaction.emit(transactionRepository.get(key))
            }
            clearInputField()
        }
    }

    fun processDeleteOperation(tokens: List<String>) {
        viewModelScope.launch(ioContext) {
            tokens.takeIf { it.size == 2 }?.let { (_, key) ->
                _transaction.emit("$RIGHT_ANGLE_BRACKET  $DELETE $key")
                transactionRepository.delete(key)
            }
            clearInputField()
        }
    }

    fun processCountOperation(tokens: List<String>) {
        viewModelScope.launch(ioContext) {
            tokens.takeIf { it.size == 2 }?.let { (_, value) ->
                _transaction.emit("$RIGHT_ANGLE_BRACKET  $COUNT $value")
                _transaction.emit(transactionRepository.count(value).toString())
            }
            clearInputField()
        }
    }

    fun processBeginOperation() {
        viewModelScope.launch(ioContext) {
            _transaction.emit("$RIGHT_ANGLE_BRACKET  $BEGIN")
            transactionRepository.begin()
            clearInputField()
        }
    }

    private fun processCommitOperation() {
        commitOperation()
    }

    private fun processRollbackOperation() {
        rollbackOperation()
    }

    fun commitOperation() {
        viewModelScope.launch(ioContext) {
            _transaction.emit(transactionRepository.commit())
            transactionRepository.removeLast()
            clearInputField()
        }
    }

    fun rollbackOperation() {
        viewModelScope.launch(ioContext) {

            _transaction.emit(transactionRepository.rollback())

            clearInputField()
        }
    }

    private suspend fun clearInputField() {
        _clearInput.emit(true)
    }

    companion object {
        const val SET = "SET"
        const val GET = "GET"
        const val DELETE = "DELETE"
        const val COUNT = "COUNT"
        const val BEGIN = "BEGIN"
        const val COMMIT = "COMMIT"
        const val ROLLBACK = "ROLLBACK"
        const val RIGHT_ANGLE_BRACKET = ">"
    }
}