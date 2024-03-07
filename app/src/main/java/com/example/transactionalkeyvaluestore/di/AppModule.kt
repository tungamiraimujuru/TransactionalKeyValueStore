package com.example.transactionalkeyvaluestore.di

import com.example.transactionalkeyvaluestore.di.dispatchers.DispatcherProvider
import com.example.transactionalkeyvaluestore.repo.TransactionRepository
import com.example.transactionalkeyvaluestore.repo.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun dispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Provides
    @Named("io")
    fun provideIoContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    fun providesTransactionRepository(): TransactionRepository = TransactionRepositoryImpl()
}