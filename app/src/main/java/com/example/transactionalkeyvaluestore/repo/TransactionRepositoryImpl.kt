package com.example.transactionalkeyvaluestore.repo

import com.example.transactionalkeyvaluestore.screen.transactions.TransactionViewModel

class TransactionRepositoryImpl : TransactionRepository {

    private val transactionStore = mutableMapOf<String, String>()
    val transactionStack = mutableListOf<MutableMap<String, String>>()

    override fun set(key: String, value: String) {
        if (transactionStack.isNotEmpty()) {
            transactionStack.last()[key] = value
        } else {
            transactionStore[key] = value
        }
    }

    override fun get(key: String): String {
        val value = transactionStack.lastOrNull()?.get(key) ?: transactionStore[key]
        return if (value != null) {
            "$value"
        } else {
            "key not set"
        }
    }

    override fun delete(key: String) {
        if (transactionStack.isNotEmpty()) {
            transactionStack.last().remove(key)
        } else {
            transactionStore.remove(key)
        }
    }

    override fun count(value: String): Int {
        return (transactionStack.lastOrNull()?.values?.count { it == value }
            ?: 0) + transactionStore.values.count { it == value }
    }

    override fun removeLast() {
        if (transactionStack.isNotEmpty()) {
            transactionStack.removeLast()
        } else {
            println("Currently there are no active transactions")
        }
    }

    override fun begin() {
        transactionStack.add(mutableMapOf())
    }

    override fun commit(): String {
        return if (transactionStack.isNotEmpty()) {
            val activeTransaction = transactionStack.last()
            for ((key, value) in activeTransaction) {
                transactionStore[key] = value
                transactionStack.dropLast(1)
            }
            "$RIGHT_ANGLE_BRACKET  $COMMIT"
        } else {
            ("no transaction")
        }
    }

    override fun rollback() : String {
        return if (transactionStack.isNotEmpty()) {
            transactionStack.last().clear()
            ("${TransactionViewModel.RIGHT_ANGLE_BRACKET}  ${TransactionViewModel.ROLLBACK}")
        } else {
            ("no transaction")
        }
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