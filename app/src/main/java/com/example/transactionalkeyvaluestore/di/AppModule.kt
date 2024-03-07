package com.example.transactionalkeyvaluestore.di

import com.example.transactionalkeyvaluestore.repo.TransactionRepository
import com.example.transactionalkeyvaluestore.repo.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("io")
    fun provideIoContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    fun providesTransactionRepository(): TransactionRepository = TransactionRepositoryImpl()
}