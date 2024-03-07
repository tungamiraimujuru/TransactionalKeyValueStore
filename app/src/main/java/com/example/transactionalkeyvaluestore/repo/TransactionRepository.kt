package com.example.transactionalkeyvaluestore.repo

interface TransactionRepository {

     fun set(key: String, value: String)
     fun get(key: String): String
     fun delete(key: String)
     fun count(value: String): Int
     fun removeLast()
     fun begin()
     fun commit() : String
     fun rollback() : String
}